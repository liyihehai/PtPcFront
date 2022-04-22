package com.nnte.pf_pc_front.controller.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.roles.PlateformRoleComponent;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestRole;
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
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    /*
    @Autowired
    private PlateformRoleComponent plateformRoleComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;

    //显示用户角色设置页面
    @RequestMapping(value = "/roleset")
    public ModelAndView roleset(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map=refreshRoles(request);
        map.put("functionList",plateformFunctionComponent.queryPlateformFunctionList(null));
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/sysset/role/roleset");
        return modelAndView;
    }

    //通过编码查询特定角色信息
    @RequestMapping(value = "/queryRole")
    @ResponseBody
    public Map<String,Object> queryRole(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestRole rRole= JsonUtil.jsonToBean(json.toString(),RequestRole.class);
        if (rRole==null || StringUtils.isEmpty(rRole.getRoleCode())){
            BaseNnte.setRetFalse(ret,1002,"角色编码错误");
            return ret;
        }
        RequestRole retRole=plateformRoleComponent.queryRequestRoleByCode(rRole.getRoleCode());
        if (retRole==null){
            BaseNnte.setRetFalse(ret,1002,"没找到指定角色信息");
            return ret;
        }
        ret.put("role",retRole);
        BaseNnte.setRetTrue(ret,"查找角色信息成功");
        return ret;
    }

    //保存角色信息更改，含新增和更改
    @RequestMapping(value = "/saveRoleModify")
    @ResponseBody
    public Map<String,Object> saveRoleModify(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestRole rRole= JsonUtil.jsonToBean(json.toString(),RequestRole.class);
        try {
            BaseComponent.checkModelFields(rRole);
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            return ret;
        }
        PlateformRole role = new PlateformRole();
        BeanUtils.copyFromSrc(rRole,role);
        return plateformRoleComponent.saveRoleModify(role,rRole.getActionType());
    }

    //删除角色定义（物理删除）
    @RequestMapping(value = "/deleteRoleByCode")
    @ResponseBody
    public Map<String,Object> deleteRoleByCode(HttpServletRequest request, @RequestBody JsonNode json){
        RequestRole rRole= JsonUtil.jsonToBean(json.toString(),RequestRole.class);
        if (rRole==null||StringUtils.isEmpty(rRole.getRoleCode())){
            Map<String,Object> ret=BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(ret,1002,"角色代码不合法");
            return ret;
        }
        return plateformRoleComponent.deleteRoleByCode(rRole.getRoleCode());
    }

    //在服务器端渲染角色列表
    private String applyRoleRows(HttpServletRequest request,Map<String,Object> map){
        return FreeMarkertUtil.getFreemarkerFtl(request,request.getServletContext(),
                FreeMarkertUtil.pathType.cls,map,"/templates/front/sysset/role/rolerows.ftl");
    }
    //删除菜单定义（物理删除）
    @RequestMapping(value = "/refreshRoles")
    @ResponseBody
    public Map<String,Object> refreshRoles(HttpServletRequest request){
        Map<String,Object> map=BaseNnte.newMapRetObj();
        setParamMapDataEnv(request,map);
        List<RequestRole> roleList=plateformRoleComponent.queryRequestRoleList();
        map.put("roleList",roleList);
        List<KeyValue> sysRoleList= AppRegistry.getSysRoleNameList();
        map.put("sysRoleList",sysRoleList);
        String roleRows=applyRoleRows(request,map);
        map.put("roleRows", roleRows);
        BaseNnte.setRetTrue(map,"刷新角色列表成功");
        return map;
    }

    //通过角色编码查询角色功能信息
    @RequestMapping(value = "/queryRoleFunctions")
    @ResponseBody
    public Map<String,Object> queryRoleFunctions(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestRole rRole= JsonUtil.jsonToBean(json.toString(),RequestRole.class);
        PlateformRole pf=plateformRoleComponent.queryRoleByCode(rRole.getRoleCode());
        RequestRole retRole=plateformRoleComponent.queryRequestRoleByCode(pf);
        if (retRole==null){
            BaseNnte.setRetFalse(ret,1002,"没找到指定角色信息");
            return ret;
        }
        ret.put("role",retRole);
       // List<PlateformFunctions> roleFunctions=plateformRoleComponent.findFunctionListOfRole(retRole.getRoleCode());
        List<RequestFunc> roleFunctions=plateformRoleComponent.getRoleFunctions(pf);
        ret.put("roleFunctions",roleFunctions);
        BaseNnte.setRetTrue(ret,"查询角色功能信息成功");
        return ret;
    }

    //设置角色的功能
    @RequestMapping(value = "/saveRoleFunctions")
    @ResponseBody
    public Map<String,Object> saveRoleFunctions(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestRole rRole= JsonUtil.jsonToBean(json.toString(),RequestRole.class);
        if (rRole==null){
            BaseNnte.setRetFalse(ret,1002,"角色代码不合法");
            return ret;
        }
        if (StringUtils.isEmpty(rRole.getRoleCode())){
            BaseNnte.setRetFalse(ret,1002,"角色代码不合法");
            return ret;
        }
        return plateformRoleComponent.saveRoleFunctions(rRole.getRoleCode(),rRole.getFunctions());
    }
    */
}
