package com.nnte.pf_business.mapper.workdb.functions;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;

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
}

