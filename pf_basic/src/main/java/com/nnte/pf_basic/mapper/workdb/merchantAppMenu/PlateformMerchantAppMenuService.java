package com.nnte.pf_basic.mapper.workdb.merchantAppMenu;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantAppMenuService extends BaseService<PlateformMerchantAppMenuDao,PlateformMerchantAppMenu> {
    public PlateformMerchantAppMenuService(){
        super(PlateformMerchantAppMenuDao.class);
    }
}

