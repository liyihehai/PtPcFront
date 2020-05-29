package com.nnte.pf_pc_front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.nnte")
public class PcPlateformApplication
{
    /**
     * 定义应用代码及应用名称
     * */
    public final static String App_Code = "PF-PC-MANAGER";
    public final static String App_Name = "平台PC管理端应用";
    /**
     * 系统模块定义
     * 模块定义为可独立交付给客户的一组功能
     * */
    public final static String MODULE_SYSSETTING = "systemSetting"; //系统设置
    public final static String MODULE_MERCHANT_MANAGE = "merchantManage"; //商户管理
    private static Map<String,String> moduleMap = new HashMap<>();
    static {
        moduleMap.put(MODULE_SYSSETTING,"系统设置");
        moduleMap.put(MODULE_MERCHANT_MANAGE,"商户管理");
    }
    public static Map<String,String> getModuleMap(){
        return moduleMap;
    }
    //------------------------------------------------------------
    public static void main(String[] args)
    {
        SpringApplication.run(PcPlateformApplication.class, args);
    }
}
