package com.nnte.pf_business.mapper.workdb.operator;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlateformOperatorService extends BaseService<PlateformOperatorDao,PlateformOperator> {
    public PlateformOperatorService(){
        super(PlateformOperatorDao.class);
    }

    /**
     * 按条件查询操作员
     * */
    public List<PlateformOperator> queryPlateformOperators(PlateformOperator param){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryPlateformOperators",param);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

