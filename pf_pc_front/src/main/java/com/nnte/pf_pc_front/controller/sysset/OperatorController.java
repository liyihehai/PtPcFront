package com.nnte.pf_pc_front.controller.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.component.roles.PlateformRoleComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestOpe;
import com.nnte.pf_business.request.RequestRole;
import com.nnte.pf_pc_front.PcPlateformApplication;
import com.nnte.pf_pc_front.config.AppModelConfig;
import com.nnte.pf_pc_front.config.SysRoleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/operator")
public class OperatorController extends BaseController {
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    @Autowired
    private PlateformRoleComponent plateformRoleComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;
    /**
     * 显示操作员设置页面
     * */
    @RequestMapping(value = "/operatorset")
    public ModelAndView operatorset(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map= BaseNnte.newMapRetObj();
        setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        RequestOpe opeRow = new RequestOpe();
        opeRow.setOpeCode("\"+rowItem.opeCode+\"");
        opeRow.setOpeName("\"+rowItem.opeName+\"");
        opeRow.setOpeTypeName("\"+rowItem.opeTypeName+\"");
        opeRow.setOpeMobile("\"+rowItem.opeMobile+\"");
        opeRow.setOpeStateName("\"+rowItem.opeStateName+\"");
        opeRow.setOpeState(0);//默认状态
        modelAndView.addObject("opeRow", opeRow);
        List<RequestRole> roleList=plateformRoleComponent.queryRequestRoleList();
        map.put("roleList",roleList);
        map.put("functionList",plateformFunctionComponent.queryPlateformFunctionList(null));
        modelAndView.setViewName("front/sysset/operator/operatorset");
        return modelAndView;
    }
    /**
     * 按条件刷新操作员列表
     * */
    @RequestMapping(value = "/refreshOpes")
    @ResponseBody
    public Map<String,Object> refreshOpes(HttpServletRequest request,@RequestBody JsonNode json){
        RequestOpe queryOpe = JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        Map<String,Object> ret=plateformOperatorComponent.queryRequestOpeList(queryOpe);
        setParamMapDataEnv(request,ret);
        String opeRows=applyOpeRows(request,ret);
        ret.put("opeRows",opeRows);
        return ret;
    }
    /**
     * 在服务器端渲染角色列表
     * */
    private String applyOpeRows(HttpServletRequest request,Map<String,Object> map){
        return FreeMarkertUtil.getFreemarkerFtl(request,request.getServletContext(),
                FreeMarkertUtil.pathType.cls,map,"/templates/front/sysset/operator/operatorrows.ftl");
    }
    /**
     * 通过编码查询特定操作员信息
     * */
    @RequestMapping(value = "/queryOperator")
    @ResponseBody
    public Map<String,Object> queryOperator(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=queryOperator(json);
        return ret;
    }
    //查询操作员信息
    private Map<String,Object> queryOperator(JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        if (rOpe==null || StringUtils.isEmpty(rOpe.getOpeCode())){
            BaseNnte.setRetFalse(ret,1002,"操作员编码错误");
            return ret;
        }
        RequestOpe retOpe=plateformOperatorComponent.queryRequestOperatorByCode(rOpe.getOpeCode());
        if (retOpe==null){
            BaseNnte.setRetFalse(ret,1002,"没找到指定操作员信息");
            return ret;
        }
        ret.put("operator",retOpe);
        BaseNnte.setRetTrue(ret,"查找操作员成功");
        return ret;
    }
    /**
     * 通过操作员编码查询操作员用户角色信息
     * */
    @RequestMapping(value = "/queryOperatorRoles")
    @ResponseBody
    public Map<String,Object> queryOperatorRoles(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=queryOperator(json);
        if (BaseNnte.getRetSuc(ret)){
            RequestOpe retOpe=(RequestOpe)ret.get("operator");
            List<KeyValue> opeRoles=plateformOperatorComponent.findRoleListOfOpe(retOpe.getOpeCode());
            ret.put("opeRoles",opeRoles);
        }
        return ret;
    }
    /**
     * 保存操作员信息更改，含新增和更改
     * */
    @RequestMapping(value = "/saveOperatorModify")
    @ResponseBody
    public Map<String,Object> saveOperatorModify(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        try {
            BaseComponent.checkModelFields(rOpe);
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            return ret;
        }
        PlateformOperator operator = new PlateformOperator();
        BeanUtils.copyFromSrc(rOpe,operator);
        if (rOpe.getOpeType().equals(1)){
            //如果要将操作员设置为超级管理员，当前操作员必须也是超级管理员才行
            OperatorInfo oi=(OperatorInfo)request.getAttribute("OperatorInfo");
            PlateformOperator curOpe=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            if (curOpe==null || !curOpe.getOpeType().equals(1)){
                BaseNnte.setRetFalse(ret,1002,"当前操作员不是超级管理员，不能操作");
                return ret;
            }
        }
        return plateformOperatorComponent.saveOperatorModify(operator,rOpe.getActionType());
    }

    /**
     * 删除操作员（逻辑删除）
     * */
    @RequestMapping(value = "/deleteOpeByCode")
    @ResponseBody
    public Map<String,Object> deleteOpeByCode(HttpServletRequest request, @RequestBody JsonNode json){
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        if (rOpe==null||StringUtils.isEmpty(rOpe.getOpeCode())){
            Map<String,Object> ret=BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        OperatorInfo oi=(OperatorInfo)request.getAttribute("OperatorInfo");
        PlateformOperator curOpe=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
        return plateformOperatorComponent.deleteOpeByCode(rOpe.getOpeCode(),curOpe);
    }
    /**
     * 设置操作员密码
     * */
    @RequestMapping(value = "/setPws")
    @ResponseBody
    public Map<String,Object> setPws(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        if (rOpe==null){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        if (StringUtils.isEmpty(rOpe.getOpeCode())){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        if (StringUtils.isEmpty(rOpe.getSetAimPwd())){
            BaseNnte.setRetFalse(ret,1002,"密码不能为空");
            return ret;
        }
        OperatorInfo oi=(OperatorInfo)request.getAttribute("OperatorInfo");
        PlateformOperator curOpe=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
        return plateformOperatorComponent.setPws(rOpe.getOpeCode(),rOpe.getSetAimPwd(),curOpe);
    }
    /**
     * 设置操作员的用户角色
     * */
    @RequestMapping(value = "/saveOperatorRoles")
    @ResponseBody
    public Map<String,Object> saveOperatorRoles(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        if (rOpe==null){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        if (StringUtils.isEmpty(rOpe.getOpeCode())){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        OperatorInfo oi=(OperatorInfo)request.getAttribute("OperatorInfo");
        PlateformOperator curOpe=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
        return plateformOperatorComponent.saveOperatorRoles(rOpe.getOpeCode(),rOpe.getUserRoles(),curOpe);
    }
    /**
     * 通过操作员编码查询操作员功能信息
     * */
    @RequestMapping(value = "/queryOperatorFunctions")
    @ResponseBody
    public Map<String,Object> queryOperatorFunctions(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=queryOperator(json);
        if (BaseNnte.getRetSuc(ret)){
            RequestOpe retOpe=(RequestOpe)ret.get("operator");
            PlateformOperator ope = new PlateformOperator();
            ope.setOpeCode(retOpe.getOpeCode());
            List<RequestFunc> opeFunctions=plateformOperatorComponent.getOpeFunctionsList(ope);
            ret.put("opeFunctions",opeFunctions);
        }
        return ret;
    }
    /**
     * 设置操作员的功能
     * */
    @RequestMapping(value = "/saveOperatorFunctions")
    @ResponseBody
    public Map<String,Object> saveOperatorFunctions(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestOpe rOpe= JsonUtil.jsonToBean(json.toString(),RequestOpe.class);
        if (rOpe==null){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        if (StringUtils.isEmpty(rOpe.getOpeCode())){
            BaseNnte.setRetFalse(ret,1002,"操作员代码不合法");
            return ret;
        }
        OperatorInfo oi=(OperatorInfo)request.getAttribute("OperatorInfo");
        PlateformOperator curOpe=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
        return plateformOperatorComponent.saveOperatorFunctions(rOpe.getOpeCode(),rOpe.getFunctions(),curOpe);
    }
}
