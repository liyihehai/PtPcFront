package com.nnte.pf_basic.mapper.workdb.merchantEnter;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantEnterService extends BaseService<PlateformMerchantEnterDao,PlateformMerchantEnter> {
    public PlateformMerchantEnterService(){
        super(PlateformMerchantEnterDao.class);
    }
}

