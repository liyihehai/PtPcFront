package com.nnte.pf_business.mapper.workdb.role;

import com.nnte.framework.base.BaseService;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlateformRoleService extends BaseService<PlateformRoleDao,PlateformRole> {
    public PlateformRoleService(){
        super(PlateformRoleDao.class);
    }

    /**
     * 按操作员查询关联的角色列表
     * */
    public List<PlateformRole> findOpeRoleList(String opeCode){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findOpeRoleList",opeCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

