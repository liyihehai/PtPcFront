package com.nnte.pf_basic.mapper.workdb.utiAccount;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantUtiAccountService extends BaseService<PlateformMerchantUtiAccountDao,PlateformMerchantUtiAccount> {
    public PlateformMerchantUtiAccountService(){
        super(PlateformMerchantUtiAccountDao.class);
    }
}

