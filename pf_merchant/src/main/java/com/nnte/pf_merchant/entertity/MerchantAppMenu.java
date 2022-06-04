package com.nnte.pf_merchant.entertity;

import com.nnte.pf_basic.mapper.workdb.merchantAppMenu.PlateformMerchantAppMenu;
import lombok.Data;

@Data
public class MerchantAppMenu extends PlateformMerchantAppMenu {
    private String appName;
    private String pmName;
    private String pmShortName;

    private String[] createDateRange;
}
