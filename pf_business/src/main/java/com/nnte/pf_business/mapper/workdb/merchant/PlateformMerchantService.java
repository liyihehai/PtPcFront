package com.nnte.pf_business.mapper.workdb.merchant;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantService extends BaseService<PlateformMerchantDao,PlateformMerchant> {
    public PlateformMerchantService(){
        super(PlateformMerchantDao.class);
    }
}

