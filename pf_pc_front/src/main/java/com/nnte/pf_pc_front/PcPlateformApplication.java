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
    //------------------------------------------------------------
    public static void main(String[] args)
    {
        SpringApplication.run(PcPlateformApplication.class, args);
    }
}
