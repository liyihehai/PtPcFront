package com.nnte.pf_merchant.config;

import com.nnte.basebusi.entity.AppRegistry;
import org.springframework.context.annotation.Configuration;

//模块系统角色定义
@Configuration
public class PFMerchantSysRole {
    //系统角色：平台商户管理员 -- 执行商户相关管理功能审核
    public static final String PLATEFORM_MERCAHNT_MANAGER = "PLATEFORM_MERCAHNT_MANAGER";
    //系统角色：平台商户业务员 -- 执行商户相关管理功能的操作
    public static final String PLATEFORM_MERCAHNT_WORKER = "PLATEFORM_MERCAHNT_WORKER";
    static {
        AppRegistry.setSysRoleName(PLATEFORM_MERCAHNT_MANAGER,"平台商户管理员");
        AppRegistry.setSysRoleName(PLATEFORM_MERCAHNT_WORKER,"平台商户业务员");
    }
}
