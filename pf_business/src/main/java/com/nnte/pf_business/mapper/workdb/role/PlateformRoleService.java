package com.nnte.pf_business.mapper.workdb.role;

import com.nnte.framework.base.BaseService;
import com.nnte.framework.entity.PageData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    public PageData<PlateformRole> findRoleListPage(Map<String,Object> paramMap,Integer pageNo,Integer pageSize){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return BaseService.pageInfoToData(getListPage(cds,"findRoleListByMap",paramMap,pageNo,pageSize));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

