package com.nnte.pf_basic.mapper.workdb.appLicense;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformAppLicenseService extends BaseService<PlateformAppLicenseDao,PlateformAppLicense> {
    public PlateformAppLicenseService(){
        super(PlateformAppLicenseDao.class);
    }
}

