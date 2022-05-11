package com.nnte.pf_pc_front.controller.api.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.AppendWhereLike;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicSysRole;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.roles.PlateformRoleComponent;
import com.nnte.pf_business.config.PFBusinessConfig;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestRole;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/sysset/roleSetting")
public class RoleSettingController extends BaseController {

    @Autowired
    private PlateformRoleComponent plateformRoleComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;

    /**
     * 获取角色设置列表
     */
    @ModuleEnter(path = "/sysset/roleSetList", name = "角色设置页面", desc = "平台系统角色设置，系统管理员功能",
            sysRole = AppBasicSysRole.SYS_MANAGER, roleRuler = "pf-roleset",
            moduleCode = PFBusinessConfig.MODULE_SYSTEM_SETTING)
    @RequestMapping(value = "/roleSetList", method = {RequestMethod.POST})
    @ResponseBody
    public Object roleSetList(HttpServletRequest request, @Nullable @RequestBody JsonNode data) {
        try {
            Map<String, Object> map = new HashedMap();
            setParamMapDataEnv(request, map);
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            List<AppendWhere> appendWhereList = new ArrayList<>();
            String roleCodeLike = StringUtils.defaultString((data.get("roleCode") != null) ? data.get("roleCode").textValue() : "");
            if (StringUtils.isNotEmpty(roleCodeLike))
                appendWhereList.add(new AppendWhereLike("t.role_code", roleCodeLike));
            String roleNameLike = StringUtils.defaultString((data.get("roleName") != null) ? data.get("roleName").textValue() : "");
            if (StringUtils.isNotEmpty(roleNameLike))
                appendWhereList.add(new AppendWhereLike("t.role_name", roleNameLike));
            String sysRoleSel = StringUtils.defaultString((data.get("sysRoleSel") != null) ? data.get("sysRoleSel").textValue() : "");
            if (StringUtils.isNotEmpty(sysRoleSel)){
                appendWhereList.add(new AppendWhereLike("t.sysrole_list", "["+sysRoleSel+"]"));
            }
            if (appendWhereList.size() > 0)
                map.put("appendWhereList", appendWhereList);
            Integer roleState = NumberDefUtil.defIntegerNull((data.get("roleState") != null &&
                    !data.get("roleState").textValue().equals("null")) ? data.get("roleState").textValue() : null);
            if (roleState != null)
                map.put("roleState", roleState);
            map.put("sort","create_time");
            map.put("dir","desc");
            map.put("roleList", plateformRoleComponent.queryRoleListPage(map, pageNo, pageSize));
            List<KeyValue> sysRoleList = AppRegistry.getSysRoleNameList();
            map.put("sysRoleList", sysRoleList);
            map.put("functionList", plateformFunctionComponent.queryPlateformFunctionList(1));
            return success("获取角色设置列表成功", map);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 保存角色设置
     */
    @RequestMapping(value = "/saveRoleModify", method = {RequestMethod.POST})
    @ResponseBody
    public Object saveRoleModify(@RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestRole rRole = JsonUtil.jsonToBean(json.toString(), RequestRole.class);
            if (rRole.getId() != null && rRole.getId() > 0)
                rRole.setActionType(2);
            else
                rRole.setActionType(1);
            BaseComponent.checkModelFields(rRole);
            PlateformRole role = new PlateformRole();
            BeanUtils.copyFromSrc(rRole, role);
            Map<String, Object> retMap = plateformRoleComponent.saveRoleModify(role, rRole.getActionType());
            if (BaseNnte.getRetSuc(retMap))
                return success("保存角色设置成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (BusiException be) {
            return onException(be);
        }
    }

    /**
     * 删除角色定义
     */
    @RequestMapping(value = "/deleteRoleByCode")
    @ResponseBody
    public Object deleteRoleByCode(@Nullable @RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestRole rRole = JsonUtil.jsonToBean(json.toString(), RequestRole.class);
            if (rRole == null || StringUtils.isEmpty(rRole.getRoleCode())) {
                return error("1002", "角色代码不合法");
            }
            Map<String, Object> retMap = plateformRoleComponent.deleteRoleByCode(rRole.getRoleCode());
            if (BaseNnte.getRetSuc(retMap))
                return success("删除角色成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 通过角色编码查询角色功能信息
     */
    @RequestMapping(value = "/queryRoleFunctions")
    @ResponseBody
    public Object queryRoleFunctions(@Nullable @RequestBody JsonNode json) {
        try {
            RequestRole rRole = JsonUtil.jsonToBean(json.toString(), RequestRole.class);
            PlateformRole pf = plateformRoleComponent.queryRoleByCode(rRole.getRoleCode());
            RequestRole retRole = plateformRoleComponent.queryRequestRoleByCode(pf);
            if (retRole == null) {
                return error("1002", "没找到指定角色信息");
            }
            List<RequestFunc> roleFunctions = plateformRoleComponent.getRoleFunctions(pf);
            return success("查询角色功能信息成功", roleFunctions);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 设置角色的功能
     */
    @RequestMapping(value = "/saveRoleFunctions")
    @ResponseBody
    public Object saveRoleFunctions(@RequestBody JsonNode json) {
        try {
            RequestRole rRole = JsonUtil.jsonToBean(json.toString(), RequestRole.class);
            if (rRole == null) {
                return error("1002", "角色代码不合法");
            }
            if (StringUtils.isEmpty(rRole.getRoleCode())) {
                return error("1002", "角色代码不合法");
            }
            Map<String, Object> retMap = plateformRoleComponent.saveRoleFunctions(rRole.getRoleCode(), rRole.getFunctions());
            if (BaseNnte.getRetSuc(retMap))
                return success("设置角色的功能成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }
}
