package com.nnte.pf_basic.mapper.workdb.merchant_expand;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantExpandService extends BaseService<PlateformMerchantExpandDao,PlateformMerchantExpand> {
    public PlateformMerchantExpandService(){
        super(PlateformMerchantExpandDao.class);
    }
}

