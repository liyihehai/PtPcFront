package com.nnte.pf_business.mapper.workdb.operator;

import com.nnte.framework.base.BaseService;
import com.nnte.framework.utils.LogUtil;
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
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryPlateformOperators",param);
        }catch (Exception e){
            LogUtil.logExp(e);
            return null;
        }
    }
}

