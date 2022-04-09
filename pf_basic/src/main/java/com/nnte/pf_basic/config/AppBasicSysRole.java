package com.nnte.pf_basic.config;

import com.nnte.basebusi.entity.AppRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBasicSysRole {
    public static final String SYS_MANAGER = "SYS_MANAGER";
    static {
        AppRegistry.setSysRoleName(SYS_MANAGER,"系统管理员");
    }
}
