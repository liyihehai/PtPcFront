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
     * 查询平台所有菜单
     * */
    public List<PlateformMenus> queryAllPlateformMenus(PlateformMenus paramMenu){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryAllPlateformMenus",paramMenu);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

