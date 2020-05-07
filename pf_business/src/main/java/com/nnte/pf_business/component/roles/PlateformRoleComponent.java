package com.nnte.pf_business.component.roles;

import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.mapper.workdb.role.PlateformRoleService;
import com.nnte.pf_business.request.RequestRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
/**
 * 平台角色组件
 * */
public class PlateformRoleComponent {
    @Autowired
    private PlateformRoleService plateformRoleService;
    /**
     * 查询用户角色列表
     * */
    public List<PlateformRole> queryRoleList(){
        PlateformRole dto = new PlateformRole();
        return plateformRoleService.findModelList(dto);
    }
    /**
     * 查询用户角色列表,处理用户角色的系统角色信息
     * */
    public List<RequestRole> queryRequestRoleList(){
        List<RequestRole> retList = new ArrayList<>();
        List<PlateformRole> list=queryRoleList();
        if (list!=null && list.size()>0){
            for(PlateformRole pfr:list){
                RequestRole rr = getRequestRoleByPR(pfr);
                retList.add(rr);
            }
        }
        return retList;
    }
    private RequestRole getRequestRoleByPR(PlateformRole pr){
        Map sysRoleMap=PfBusinessComponent.getSystemRoleMap();
        RequestRole rr = new RequestRole();
        BeanUtils.copyFromSrc(pr,rr);
        String sysrolelist = pr.getSysroleList();
        if (StringUtils.isNotEmpty(sysrolelist)){
            String[] roles=sysrolelist.split(",");
            StringBuffer nameBuf = new StringBuffer();
            for(String role:roles){
                if (role.length()>=3){
                    String nRole=role.substring(1,role.length()-1);
                    String roleName=StringUtils.defaultString(sysRoleMap.get(nRole));
                    if (StringUtils.isNotEmpty(roleName)){
                        if (nameBuf.length()>0)
                            nameBuf.append(",");
                        nameBuf.append(roleName);
                    }
                }
            }
            rr.setSysroleNameList(nameBuf.toString());
        }
        return rr;
    }
    /**
     * 通过编码查询特定的角色
     * */
    public PlateformRole queryRoleByCode(String code){
        PlateformRole dto = new PlateformRole();
        dto.setRoleCode(code);
        List<PlateformRole> list = plateformRoleService.findModelList(dto);
        if (list!=null && list.size()==1)
            return list.get(0);
        return null;
    }
    /**
     * 通过编码查询特定的角色，返回请求角色对象
     * */
    public RequestRole queryRequestRoleByCode(String code){
        PlateformRole pr=queryRoleByCode(code);
        if (pr!=null)
            return getRequestRoleByPR(pr);
        return null;
    }

    /**
     * 保存角色更改,包括新增及更改
     * */
    public Map<String,Object> saveRoleModify(PlateformRole role,int actionType) {
        Map<String, Object> retMap = BaseNnte.newMapRetObj();
        PlateformRole srcRole = queryRoleByCode(role.getRoleCode());
        if (actionType==1){
            //如果是新增操作
            if (srcRole!=null){
                BaseNnte.setRetFalse(retMap,1002,"角色编号已存在，不能新增");
                return retMap;
            }
            PlateformRole newRole = new PlateformRole();
            newRole.setRoleCode(role.getRoleCode());
            newRole.setRoleName(role.getRoleName());
            newRole.setRoleState(role.getRoleState());
            newRole.setCreateTime(new Date());
            newRole.setSysroleList(role.getSysroleList());
            int count = NumberUtil.getDefaultInteger(plateformRoleService.addModel(newRole));
            if (count!=1){
                BaseNnte.setRetFalse(retMap,1002,"新增角色操作失败");
                return retMap;
            }
            BaseNnte.setRetTrue(retMap,"新增角色操作成功");
        }else if(actionType==2){
            if (srcRole==null){
                BaseNnte.setRetFalse(retMap,1002,"编号["+role.getRoleCode()+"]角色不存在，不能更改");
                return retMap;
            }
            PlateformRole updateRole = new PlateformRole();
            updateRole.setId(srcRole.getId());
            updateRole.setRoleName(role.getRoleName());
            updateRole.setRoleState(role.getRoleState());
            updateRole.setSysroleList(role.getSysroleList());
            int count = NumberUtil.getDefaultInteger(plateformRoleService.updateModel(updateRole));
            if (count!=1){
                BaseNnte.setRetFalse(retMap,1002,"更改角色操作失败");
                return retMap;
            }
            BaseNnte.setRetTrue(retMap,"更改角色操作成功");
        }
        return retMap;
    }

    /**
     * 按编号删除角色
     * */
    public Map<String,Object> deleteRoleByCode(String roleCode){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        if (StringUtils.isEmpty(roleCode)){
            BaseNnte.setRetFalse(retMap,1002,"角色编号不合法");
            return retMap;
        }
        PlateformRole role = queryRoleByCode(roleCode);
        if (role==null){
            BaseNnte.setRetFalse(retMap,1002,"没找到指定编号的角色");
            return retMap;
        }
        Integer count=plateformRoleService.deleteModel(role.getId());
        if (count!=1){
            BaseNnte.setRetFalse(retMap,1002,"角色删除失败");
            return retMap;
        }
        retMap.put("roleCode",roleCode);
        BaseNnte.setRetTrue(retMap,"角色删除成功");
        return retMap;
    }
}
