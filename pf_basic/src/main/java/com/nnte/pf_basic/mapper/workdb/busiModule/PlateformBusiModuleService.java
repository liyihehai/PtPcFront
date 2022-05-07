package com.nnte.pf_basic.mapper.workdb.busiModule;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformBusiModuleService extends BaseService<PlateformBusiModuleDao,PlateformBusiModule> {
    public PlateformBusiModuleService(){
        super(PlateformBusiModuleDao.class);
    }
}

