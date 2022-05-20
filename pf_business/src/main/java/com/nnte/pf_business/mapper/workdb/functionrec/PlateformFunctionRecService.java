package com.nnte.pf_business.mapper.workdb.functionrec;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformFunctionRecService extends BaseService<PlateformFunctionRecDao,PlateformFunctionRec> {
    public PlateformFunctionRecService(){
        super(PlateformFunctionRecDao.class);
    }
}

