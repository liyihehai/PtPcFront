package com.nnte.pf_basic.mapper.workdb.reportModule;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformReportModuleService extends BaseService<PlateformReportModuleDao,PlateformReportModule> {
    public PlateformReportModuleService(){
        super(PlateformReportModuleDao.class);
    }
}

