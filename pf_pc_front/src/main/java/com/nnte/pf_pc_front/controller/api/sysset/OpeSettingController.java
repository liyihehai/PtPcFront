package com.nnte.pf_pc_front.controller.api.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicSysRole;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.component.roles.PlateformRoleComponent;
import com.nnte.pf_business.config.PFBusinessConfig;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestOpe;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/sysset/operatorSetting")
public class OpeSettingController extends BaseController {
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    @Autowired
    private PlateformRoleComponent plateformRoleComponent;

    /**
     * 取得操作员设置页面列表
     */
    @ModuleEnter(path = "/sysset/operatorSetList", name = "操作员设置",
            desc = "平台系统操作员设置，系统管理员功能",
            sysRole = AppBasicSysRole.SYS_MANAGER,
            roleRuler = "pf-operatorset",
            moduleCode = PFBusinessConfig.MODULE_SYSTEM_SETTING)
    @RequestMapping(value = "/operatorSetList")
    @ResponseBody
    public Object operatorSetList(@Nullable @RequestBody JsonNode data) {
        try {
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            RequestOpe queryOpe = JsonUtil.jsonToBean(data.toString(), RequestOpe.class);
            //----------------------------------------------
            Map<String, Object> paramMap = new HashedMap();
            paramMap.put("sort", "create_time");
            paramMap.put("dir", "desc");
            //----------------------------------------------
            List<AppendWhere> appendWhereList = new ArrayList<>();
            AppendWhere whereState = new AppendWhere(AppendWhere.Type_Direct);
            whereState.setWhereTxt("t.ope_state!=3");
            appendWhereList.add(whereState);
            AppendWhere.addLikeStringToWhereList(queryOpe.getOpeCode(),"t.ope_code",appendWhereList);
            AppendWhere.addLikeStringToWhereList(queryOpe.getOpeName(),"t.ope_name",appendWhereList);
            AppendWhere.addLikeStringToWhereList(queryOpe.getOpeMobile(),"t.ope_mobile",appendWhereList);
            AppendWhere.addDataRangeToWhereList(queryOpe.getCreateTimeRange(),"t.create_time",appendWhereList);
            //-----------------------------------------------
            if (appendWhereList.size() > 0)
                paramMap.put("appendWhereList", appendWhereList);
            //-----------------------------------------------
            if (queryOpe.getOpeType() != null)
                paramMap.put("opeType", queryOpe.getOpeType());
            if (queryOpe.getOpeState() != null)
                paramMap.put("opeState", queryOpe.getOpeState());
            PageData<RequestOpe> pageData = plateformOperatorComponent.operatorSetList(paramMap, pageNo, pageSize);
            return success("取得操作员设置页面列表成功!", pageData);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 保存操作员信息更改，含新增和更改
     */
    @RequestMapping(value = "/saveOperatorModify")
    @ResponseBody
    public Object saveOperatorModify(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            BaseComponent.checkModelFields(rOpe);
            PlateformOperator operator = new PlateformOperator();
            BeanUtils.copyFromSrc(rOpe, operator);
            if (rOpe.getOpeType().equals(1)) {
                //如果要将操作员设置为超级管理员，当前操作员必须也是超级管理员才行
                OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
                PlateformOperator curOpe = plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
                if (curOpe == null || !curOpe.getOpeType().equals(1)) {
                    throw new BusiException(1002, "当前操作员不是超级管理员，不能操作");
                }
            }
            Map<String, Object> retMap = plateformOperatorComponent.saveOperatorModify(operator, rOpe.getActionType());
            if (BaseNnte.getRetSuc(retMap))
                return success("保存操作员设置成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 设置操作员密码
     */
    @RequestMapping(value = "/setOperatorPws")
    @ResponseBody
    public Object setOperatorPws(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            if (StringUtils.isEmpty(rOpe.getOpeCode())) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            if (StringUtils.isEmpty(rOpe.getSetAimPwd())) {
                throw new BusiException(1002, "密码不能为空");
            }
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformOperator curOpe = plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            Map<String, Object> retMap = plateformOperatorComponent.setPws(rOpe.getOpeCode(), rOpe.getSetAimPwd(), curOpe);
            if (BaseNnte.getRetSuc(retMap))
                return success("设置操作员密码成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 删除操作员（逻辑删除）
     */
    @RequestMapping(value = "/deleteOperatorByCode")
    @ResponseBody
    public Object deleteOperatorByCode(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null || StringUtils.isEmpty(rOpe.getOpeCode())) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformOperator curOpe = plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            Map<String, Object> retMap = plateformOperatorComponent.deleteOpeByCode(rOpe.getOpeCode(), curOpe);
            if (BaseNnte.getRetSuc(retMap))
                return success("删除操作员成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 通过操作员编码查询操作员用户角色信息
     */
    @RequestMapping(value = "/queryOperatorRoles")
    @ResponseBody
    public Object queryOperatorRoles(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null || StringUtils.isEmpty(rOpe.getOpeCode())) {
                throw new BusiException(1002, "操作员编码错误");
            }
            RequestOpe retOpe = plateformOperatorComponent.queryRequestOperatorByCode(rOpe.getOpeCode());
            if (retOpe == null) {
                throw new BusiException(1002, "没找到指定操作员信息");
            }
            List<PlateformRole> roles = plateformRoleComponent.queryRoleList();
            List<KeyValue> opeRoles = plateformOperatorComponent.findRoleListOfOpe(retOpe.getOpeCode());
            Map<String, Object> retMap = new HashedMap();
            retMap.put("roles", roles);
            retMap.put("opeRoles", opeRoles);
            return success("查询操作员用户角色成功", retMap);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 设置操作员的用户角色
     */
    @RequestMapping(value = "/saveOperatorRoles")
    @ResponseBody
    public Object saveOperatorRoles(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            if (StringUtils.isEmpty(rOpe.getOpeCode())) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformOperator curOpe = plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            Map<String, Object> retMap = plateformOperatorComponent.saveOperatorRoles(rOpe.getOpeCode(), rOpe.getUserRoles(), curOpe);
            if (BaseNnte.getRetSuc(retMap))
                return success("设置操作员的用户角色成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 通过操作员编码查询操作员功能信息
     */
    @RequestMapping(value = "/queryOperatorFunctions")
    @ResponseBody
    public Object queryOperatorFunctions(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null) throw new BusiException(1002, "操作员代码不合法");
            if (StringUtils.isEmpty(rOpe.getOpeCode()))throw new BusiException(1002, "操作员代码不合法");
            PlateformOperator ope = new PlateformOperator();
            ope.setOpeCode(rOpe.getOpeCode());
            List<RequestFunc> opeFunctions = plateformOperatorComponent.getOpeFunctionsList(ope);
            return success("通过操作员编码查询操作员功能信息成功", opeFunctions);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 设置操作员的功能
     */
    @RequestMapping(value = "/saveOperatorFunctions")
    @ResponseBody
    public Object saveOperatorFunctions(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestOpe rOpe = JsonUtil.jsonToBean(json.toString(), RequestOpe.class);
            if (rOpe == null) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            if (StringUtils.isEmpty(rOpe.getOpeCode())) {
                throw new BusiException(1002, "操作员代码不合法");
            }
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformOperator curOpe = plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            Map<String, Object> retMap =  plateformOperatorComponent.saveOperatorFunctions(rOpe.getOpeCode(), rOpe.getFunctions(), curOpe);
            if (BaseNnte.getRetSuc(retMap))
                return success("设置操作员的功能成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }
}
