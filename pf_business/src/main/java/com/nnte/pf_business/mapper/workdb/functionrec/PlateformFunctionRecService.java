package com.nnte.pf_business.mapper.workdb.functionrec;

import com.nnte.framework.base.BaseService;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlateformFunctionRecService extends BaseService<PlateformFunctionRecDao,PlateformFunctionRec> {
    public PlateformFunctionRecService(){
        super(PlateformFunctionRecDao.class);
    }
}

