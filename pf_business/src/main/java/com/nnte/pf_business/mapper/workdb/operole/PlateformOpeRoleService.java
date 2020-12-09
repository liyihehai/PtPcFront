package com.nnte.pf_business.mapper.workdb.operole;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlateformOpeRoleService extends BaseService<PlateformOpeRoleDao,PlateformOpeRole> {
    public PlateformOpeRoleService(){
        super(PlateformOpeRoleDao.class);
    }
    /**
     * 按操作员编码查询操作员用户角色
     * */
    public List<PlateformOpeRoleEx> findRoleListByOpeCode(PlateformOpeRole param){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findRoleListByOpeCode",param);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员编码删除操作员角色
     * */
    public Integer deleteRoleListByOpeCode(String opeCode){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().delete("deleteRoleListByOpeCode",opeCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员编码删除操作员角色
     * */
    public Integer deleteRoleListByRoleCode(String roleCode){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().delete("deleteRoleListByRoleCode",roleCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 按操作员编码批量增加角色
     * */
    public Integer insertRoleListByOpeCode(String opeCode,String userRoles){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            Map<String,Object> map = new HashMap<>();
            map.put("opeCode",opeCode);
            map.put("userRoles",userRoles);
            return cds.getSqlSession().insert("insertRoleListByOpeCode",map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

