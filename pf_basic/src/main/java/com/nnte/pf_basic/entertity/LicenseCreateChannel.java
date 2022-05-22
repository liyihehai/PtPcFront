package com.nnte.pf_basic.entertity;

import com.nnte.framework.utils.EnumJsonInterface;

public enum LicenseCreateChannel implements EnumJsonInterface {
    Plate(1),       //平台管理
    Merchant(2);    //商户自助

    private Integer value;
    LicenseCreateChannel(Integer val){
        value = val;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static Object getEnumFromInt(Integer iVal) {
        switch (iVal){
            case 1:return Plate;
            case 2:return Merchant;
        }
        return null;
    }
}
