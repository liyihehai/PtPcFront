package com.nnte.pf_business.mapper.workdb.rolefunction;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformRoleFunctionService extends BaseService<PlateformRoleFunctionDao,PlateformRoleFunction> {
    public PlateformRoleFunctionService(){
        super(PlateformRoleFunctionDao.class);
    }
}

