package com.nnte.pf_business.config;

import com.nnte.basebusi.entity.AppRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PFBusinessSysRole {
    /***
     * 系统角色定义
     * */
    public static final String SYS_MANAGER = "SYS_MANAGER";
    static {
        AppRegistry.setSysRoleName(SYS_MANAGER,"系统管理员");
    }
}
