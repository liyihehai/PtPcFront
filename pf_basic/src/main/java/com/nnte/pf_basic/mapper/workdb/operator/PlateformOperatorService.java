package com.nnte.pf_basic.mapper.workdb.operator;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformOperatorService extends BaseService<PlateformOperatorDao,PlateformOperator> {
    public PlateformOperatorService(){
        super(PlateformOperatorDao.class);
    }
}

