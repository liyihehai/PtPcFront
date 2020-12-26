package com.nnte_plateform.component;

import com.nnte.framework.base.BaseNnte;

import java.util.Map;

public class MerchantClientComponent {
    /**
     * 校验商户合法性：本函数向平台服务器提交
     * */
    public Map<String,Object> VerifyMerchant(String merchantCode,String merchant){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        return ret;
    }
}
