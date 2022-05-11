package com.nnte.pf_merchant.mapper.workdb.merchantUtiAccount;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantUtiAccountService extends BaseService<PlateformMerchantUtiAccountDao,PlateformMerchantUtiAccount> {
    public PlateformMerchantUtiAccountService(){
        super(PlateformMerchantUtiAccountDao.class);
    }
}

