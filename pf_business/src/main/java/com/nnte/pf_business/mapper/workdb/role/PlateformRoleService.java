package com.nnte.pf_business.mapper.workdb.role;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformRoleService extends BaseService<PlateformRoleDao,PlateformRole> {
    public PlateformRoleService(){
        super(PlateformRoleDao.class);
    }
}

