package com.nnte.pf_merchant.entertity;

import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;
import lombok.Data;

@Data
public class AppLicenseItem extends PlateformAppLicense {
    private String pmName;      //商户名称
    private String pmShortName; //商户简称
    private String appName;     //应用名称
    private String moduleName;  //模块名称
    private String merchantTerminals;   //商户终端
}
