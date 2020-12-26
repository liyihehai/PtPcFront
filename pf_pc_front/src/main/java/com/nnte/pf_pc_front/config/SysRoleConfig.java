package com.nnte.pf_pc_front.config;

import com.nnte.basebusi.entity.AppRegistry;
import org.springframework.stereotype.Component;

@Component
public class SysRoleConfig {
    /***
     * 系统角色定义
     * */
    public static final String SYS_MANAGER = "SYS_MANAGER";
    public static final String PLATEFORM_MERCAHNT_MANAGER = "PLATEFORM_MERCAHNT_MANAGER";
    public static final String PLATEFORM_MERCAHNT_WORKER = "PLATEFORM_MERCAHNT_WORKER";
    public static final String MERCHANT_OPERATOR = "MERCHANT_OPERATOR";
    public static final String MERCHANT_MANAGER = "MERCHANT_MANAGER";

    static {
        AppRegistry.setSysRoleName(SYS_MANAGER,"系统管理员");
        AppRegistry.setSysRoleName(PLATEFORM_MERCAHNT_MANAGER,"平台商户管理员");
        AppRegistry.setSysRoleName(PLATEFORM_MERCAHNT_WORKER,"平台商户业务员");
        AppRegistry.setSysRoleName(MERCHANT_OPERATOR,"商家业务操作员");
        AppRegistry.setSysRoleName(MERCHANT_MANAGER,"商家业务管理员");
    }
}
