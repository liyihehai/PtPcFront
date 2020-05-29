package com.nnte.pf_business.mapper.workdb.merchantapply;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchanApplyService extends BaseService<PlateformMerchanApplyDao,PlateformMerchanApply> {
    public PlateformMerchanApplyService(){
        super(PlateformMerchanApplyDao.class);
    }
}

