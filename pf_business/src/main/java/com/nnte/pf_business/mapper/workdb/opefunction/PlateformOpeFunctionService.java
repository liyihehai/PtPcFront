package com.nnte.pf_business.mapper.workdb.opefunction;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlateformOpeFunctionService extends BaseService<PlateformOpeFunctionDao,PlateformOpeFunction> {
    public PlateformOpeFunctionService(){
        super(PlateformOpeFunctionDao.class);
    }
}

