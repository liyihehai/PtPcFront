package com.nnte.pf_pc_front;

import com.nnte.framework.utils.FileUtil;
import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

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
        //---------
        String jarPath=FileUtil.toUNIXpath(System.getProperty("user.dir"));
        System.out.println("jarPath="+jarPath);
        String logHomeRoot = "/"+jarPath+"/logs/pf_pc_front/logs/";
        System.out.println("LogHomeRoot="+logHomeRoot);
        System.setProperty("LogHomeRoot", logHomeRoot);
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        /*
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "false");
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "WARN");
        System.setProperty(ClientLogger.CLIENT_LOG_ROOT, "/D:/logs/rocketMQ/");
        System.setProperty(ClientLogger.CLIENT_LOG_FILENAME, "message.log");
        */
        SpringApplication.run(PcPlateformApplication.class, args);
    }
}
