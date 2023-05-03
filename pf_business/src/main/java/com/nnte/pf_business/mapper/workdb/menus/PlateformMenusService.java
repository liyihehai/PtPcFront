package com.nnte.pf_business.mapper.workdb.menus;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlateformMenusService extends BaseService<PlateformMenusDao,PlateformMenus> {
    public PlateformMenusService(){
        super(PlateformMenusDao.class);
    }
    /**
     * 按菜单等级查询所有菜单
     * */
    public List<PlateformMenus> queryPMenusOrderByClass(PlateformMenus paramMenu){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryPMenusOrderByClass",paramMenu);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }
}

