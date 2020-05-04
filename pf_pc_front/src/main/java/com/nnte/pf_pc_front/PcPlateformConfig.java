package com.nnte.pf_pc_front;

import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.base.DynamicDatabaseSourceHolder;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.PlateformSysParamComponent;
import com.nnte.pf_business.component.PlateformWatchComponent;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "pf.pc.front")
@PropertySource(value = "classpath:pf-pc-front-config.properties")

@Getter
@Setter
public class PcPlateformConfig implements ApplicationRunner, ConfigInterface {
    //本地工作环境
    private String debug;
    private String localHostName;
    private String localHostAbstractName;
    private String staticRoot;
    //工作数据库连接配置
    private String workDBDriverClassName;
    private String workDBIp;
    private String workDBPort;
    private String workDBSchema;
    private String workDBUser;
    private String workDBPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        appInit();
    }

    private static void appInit(){
        BaseNnte.outConsoleLog("执行后续初始化功能......");
        PcPlateformConfig Config= SpringContextHolder.getBean("pcPlateformConfig");
        BaseNnte.outConsoleLog("初始化KingReportConfig组件......");
        ConfigInterface.LoadConfigComponent(Config);
        DynamicDatabaseSourceHolder ddh= SpringContextHolder.getBean("dynamicDatabaseSourceHolder");
        BaseNnte.outConsoleLog("初始化DynamicDatabaseSourceHolder组件......"+(ddh==null?"null":"suc"));
        DynamicDatabaseSourceHolder.loadDBSchemaInterface();

        //--初始化文件服务器连接--

        //------------------------
        BaseNnte.outConsoleLog("初始化工作数据库连接......");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(Config.getWorkDBDriverClassName());
        config.setJdbcUrl("jdbc:mysql://"+Config.getWorkDBIp()+":"+Config.getWorkDBPort()+"/"+
                Config.getWorkDBSchema()+"?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        config.setUsername(Config.getWorkDBUser());
        config.setPassword(Config.getWorkDBPassword());
        config.setMinimumIdle(0);
        config.setMaximumPoolSize(5);
        config.setIdleTimeout(1000*20);
        config.setConnectionTestQuery("SELECT 1");

        List<String> mappers=new ArrayList<>();
        mappers.add("com.nnte.pf_business.mapper.workdb");
        BaseNnte.outConsoleLog("初始化工作数据库连接数据源......");
        ddh.initDataBaseSource(DBSrcTranc.Work_DBSrc_Name,config,mappers, PfBusinessComponent.class,true);

        //------------------------
        BaseBusiComponent.loadComponentBusiLogAttr();
        PlateformSysParamComponent pfsp=SpringContextHolder.getBean("plateformSysParamComponent");
        PlateformWatchComponent pfw= SpringContextHolder.getBean("plateformWatchComponent");
        if (pfw!=null){
            try{
                if (pfsp!=null){
                    pfw.registerWatchItem(pfsp,0);
                }
            }catch (BusiException be){
                pfw.logException(be);
            }
            BaseNnte.outConsoleLog("PlateformWatchComponent......"+(pfw==null?"null":"suc"));
            pfw.startWatch();
        }
        BaseBusiComponent.loadSystemFuntionEnters();
    }
}
