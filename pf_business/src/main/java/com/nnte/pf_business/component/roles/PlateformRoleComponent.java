package com.nnte.pf_business.component.roles;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctionsService;
import com.nnte.pf_business.mapper.workdb.operole.PlateformOpeRoleService;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.mapper.workdb.role.PlateformRoleService;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
/**
 * 平台角色组件
 * */
@BusiLogAttr(value = "SystemManager")
public class PlateformRoleComponent extends BaseComponent {
    @Autowired
    private PlateformRoleService plateformRoleService;
    @Autowired
    private PlateformOpeRoleService plateformOpeRoleService;
    @Autowired
    private PlateformFunctionsService plateformFunctionsService;
    /**
     * 查询用户角色列表
     * */
    public List<PlateformRole> queryRoleList(){
        PlateformRole dto = new PlateformRole();
        dto.setRoleState(1);//只查询出有效的角色
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

    public PageData<RequestRole> queryRoleListPage(Map<String,Object> paramMap, Integer pageNo, Integer pageSize){
        PageData<RequestRole> retPd = new PageData<>();
        PageData<PlateformRole> pageData=plateformRoleService.getListPageData("findModelListByMap",paramMap,pageNo,pageSize);
        retPd.setSuccess(pageData.isSuccess());
        retPd.setTotal(pageData.getTotal());
        List<RequestRole> rList = new ArrayList<>();
        for(PlateformRole pfr:pageData.getData()){
            RequestRole rr = getRequestRoleByPR(pfr);
            rList.add(rr);
        }
        retPd.setData(rList);
        return retPd;
    }

    private RequestRole getRequestRoleByPR(PlateformRole pr){
        RequestRole rr = new RequestRole();
        BeanUtils.copyFromSrc(pr,rr);
        String sysrolelist = pr.getSysroleList();
        if (StringUtils.isNotEmpty(sysrolelist)){
            String[] roles=sysrolelist.split(",");
            StringBuffer nameBuf = new StringBuffer();
            for(String role:roles){
                if (role.length()>=3){
                    String nRole=role.substring(1,role.length()-1);
                    String roleName=StringUtils.defaultString(AppRegistry.getSysRoleName(nRole));
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
            return queryRequestRoleByCode(pr);
        return null;
    }
    public RequestRole queryRequestRoleByCode(PlateformRole pr){
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
        plateformOpeRoleService.deleteRoleListByRoleCode(role.getRoleCode());
        retMap.put("roleCode",roleCode);
        BaseNnte.setRetTrue(retMap,"角色删除成功");
        return retMap;
    }
    /**
     * 查询角色关联的功能列表
     * */
    public List<PlateformFunctions> findFunctionListOfRole(String roleCode){
        PlateformRole role = new PlateformRole();
        role.setRoleCode(roleCode);
        return plateformFunctionsService.findFunctionsByRoleCode(role);
    }
    /**
     * 查询角色关联的功能列表，含系统角色功能标志
     * */
    public List<RequestFunc> getRoleFunctions(PlateformRole pr){
        if (pr==null)
            return null;
        List<PlateformFunctions> pfList=plateformFunctionsService.findFunctionsByRoleCode(pr);
        Map<String,RequestFunc> map=new HashMap<>();
        for(PlateformFunctions func:pfList){
            RequestFunc rf = new RequestFunc();
            BeanUtils.copyFromSrc(func,rf);
            rf.setRoleFunction(1);
            rf.setSysRoleFunction(0);
            map.put(rf.getFunCode(),rf);
        }
        List<PlateformFunctions> sysPfList=findFunctionListOfSysRole(pr);
        for(PlateformFunctions func:sysPfList){
            RequestFunc rf=map.get(func.getFunCode());
            if (rf!=null){
                rf.setSysRoleFunction(1);
            }else{
                rf = new RequestFunc();
                BeanUtils.copyFromSrc(func,rf);
                rf.setRoleFunction(0);
                rf.setSysRoleFunction(1);
                map.put(rf.getFunCode(),rf);
            }
        }
        if (map.size()<=0)
            return null;
        List<RequestFunc> retList = new ArrayList<>();
        for(String key:map.keySet()){
            retList.add(map.get(key));
        }
        return retList;
    }
    /**
     * 设置角色功能
     * */
    public Map<String,Object> saveRoleFunctions(String roleCode,String functions){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        plateformFunctionsService.deleteFunctionsByRoleCode(roleCode);
        if (StringUtils.isNotEmpty(functions)){
            String[] fs = functions.split(",");
            StringBuilder fsBuilder = new StringBuilder();
            for(String fcode:fs){
                if (fsBuilder.length()>0){
                    fsBuilder.append(",");
                }
                fsBuilder.append("'").append(fcode).append("'");
            }
            plateformFunctionsService.insertFunctionsByRoleCode(roleCode,fsBuilder.toString());
        }
        BaseNnte.setRetTrue(retMap,"设置角色功能成功");
        return retMap;
    }
    /**
     * 查询角色关联的系统角色关联的功能列表
     * */
    public List<PlateformFunctions> findFunctionListOfSysRole(PlateformRole role){
        if (role==null || role.getSysroleList()==null || StringUtils.isEmpty(role.getSysroleList()))
            return null;
        String sysRoleCodeList=role.getSysroleList().replaceAll("\\[","'").replaceAll("\\]","'");
        return plateformFunctionsService.findFunctionsBySysRoleCode(sysRoleCodeList);
    }
}
