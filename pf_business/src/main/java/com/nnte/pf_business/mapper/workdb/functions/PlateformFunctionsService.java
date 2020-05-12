package com.nnte.pf_business.mapper.workdb.functions;

import com.nnte.framework.base.BaseService;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlateformFunctionsService extends BaseService<PlateformFunctionsDao,PlateformFunctions> {
    public PlateformFunctionsService(){
        super(PlateformFunctionsDao.class);
    }
    /**
     * 查询所有有效的功能
     * */
    public List<PlateformFunctions> queryAllPlateformFunctions(PlateformFunctions param){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryAllPlateformFunctions",param);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员编码删除操作员功能
     * */
    public Integer deleteFunctionsByOpeCode(String opeCode){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().delete("deleteFunctionsByOpeCode",opeCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员编码批量增加功能
     * */
    public Integer insertFunctionsByOpeCode(String opeCode,String functions){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            Map<String,Object> map = new HashMap<>();
            map.put("opeCode",opeCode);
            map.put("functions",functions);
            return cds.getSqlSession().insert("insertFunctionsByOpeCode",map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按操作员编码查询关联的功能列表
     * */
    public List<PlateformFunctions> findFunctionsByOpeCode(PlateformOperator ope){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findFunctionsByOpeCode",ope);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按角色编码查询关联的功能列表
     * */
    public List<PlateformFunctions> findFunctionsByRoleCode(PlateformRole role){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findFunctionsByRoleCode",role);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按角色编码删除操作员功能
     * */
    public Integer deleteFunctionsByRoleCode(String roleCode){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().delete("deleteFunctionsByRoleCode",roleCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按角色编码批量增加功能
     * */
    public Integer insertFunctionsByRoleCode(String roleCode,String functions){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            Map<String,Object> map = new HashMap<>();
            map.put("roleCode",roleCode);
            map.put("functions",functions);
            return cds.getSqlSession().insert("insertFunctionsByRoleCode",map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按系统角色编码序列查询功能列表
     * */
    public List<PlateformFunctions> findFunctionsBySysRoleCode(String sysRoleCodeList){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findFunctionsBySysRoleCode",sysRoleCodeList);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员关联的角色查询功能列表
     * */
    public List<PlateformFunctions> findFunctionsByOpeRoles(PlateformOperator ope){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findFunctionsByOpeRoles",ope);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

