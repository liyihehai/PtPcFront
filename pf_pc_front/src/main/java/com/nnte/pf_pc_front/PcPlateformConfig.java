package com.nnte.pf_pc_front;

import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.base.JedisComponent;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.base.DynamicDatabaseSourceHolder;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.framework.utils.BaiduMapUtil;
import com.nnte.pf_business.component.*;
import com.nnte.pf_business.component.mqcomp.EmailMQComponent;
import com.nnte.pf_business.component.mqcomp.SMMQComponent;
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
public class PcPlateformConfig extends BaseBusiComponent
        implements ApplicationRunner, ConfigInterface {
    //本地工作环境
    private String debug;
    private String localHostName;
    private String localHostAbstractName;
    private String staticRoot;
    private String uploadStaticRoot;
    //工作数据库连接配置
    private String workDBDriverClassName;
    private String workDBIp;
    private String workDBPort;
    private String workDBSchema;
    private String workDBUser;
    private String workDBPassword;
    //百度API-KEY
    private String baiduApiKey;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        appInit();
    }

    private static void appInit() throws Exception{

        BaseNnte.outConsoleLog("执行后续初始化功能......日志设置");
        BaseBusiComponent.loadComponentBusiLogAttr();
        //--------------------------------------------------------------------------------------
        PcPlateformConfig Config= SpringContextHolder.getBean("pcPlateformConfig");
        BaseBusiComponent.logInfo(Config,"初始化PcPlateformConfig组件......");
        BaiduMapUtil.setBaiduApiKey(Config.getBaiduApiKey());
        ConfigInterface.LoadConfigComponent(Config);
        DynamicDatabaseSourceHolder ddh= SpringContextHolder.getBean("dynamicDatabaseSourceHolder");
        BaseBusiComponent.logInfo(Config,"初始化DynamicDatabaseSourceHolder组件......"+(ddh==null?"null":"suc"));
        DynamicDatabaseSourceHolder.loadDBSchemaInterface();
        //--初始化Redis服务器-----
        JedisComponent jedis = SpringContextHolder.getBean("jedisComponent");
        jedis.setLogInterface(Config);
        jedis.initJedisCom();
        //-----------------------
        //--初始化文件服务器连接--

        //------------------------
        //--初始短信MQ-------------
        SMMQComponent smmq=SpringContextHolder.getBean("SMMQComponent");
        if (smmq!=null){
            try {
                smmq.initProducer();
                BaseBusiComponent.logInfo(Config,"SMMQComponent initProducer suc");
                smmq.initConsumer();
                BaseBusiComponent.logInfo(Config,"SMMQComponent initConsumer suc");
            }catch (BusiException be){
                BaseBusiComponent.logInfo(Config,"SMMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        //--初始邮件MQ-------------
        EmailMQComponent emailapplymq=SpringContextHolder.getBean("emailMQComponent");
        if (emailapplymq!=null){
            try {
                emailapplymq.initProducer();
                BaseBusiComponent.logInfo(Config,"EmailMQComponent initProducer suc");
                emailapplymq.initConsumer();
                BaseBusiComponent.logInfo(Config,"EmailMQComponent initConsumer suc");
            }catch (BusiException be){
                BaseBusiComponent.logInfo(Config,"EmailMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        BaseBusiComponent.logInfo(Config,"初始化工作数据库连接......");
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
        BaseBusiComponent.logInfo(Config,"初始化工作数据库连接数据源......");
        ddh.initDataBaseSource(DBSrcTranc.Work_DBSrc_Name,config,mappers, PfBusinessComponent.class,true);
        //-------------------------------------
        //--启动程序守护线程，注册组件（系统参数）
        PlateformSysParamComponent pfsp=SpringContextHolder.getBean("plateformSysParamComponent");
    //    PfBusinessComponent pfbc=SpringContextHolder.getBean("pfBusinessComponent");
        PlateformWatchComponent pfw= SpringContextHolder.getBean("plateformWatchComponent");
        if (pfw!=null){
            try{
                if (pfsp!=null){
                    pfw.registerWatchItem(pfsp,0);
      //              pfw.registerWatchItem(pfbc,1);
                }
            }catch (BusiException be){
                pfw.logException(be);
            }
            BaseBusiComponent.logInfo(Config,"PlateformWatchComponent......"+(pfw==null?"null":"suc"));
            pfw.startWatch();
        }
        //-------------------------------------
        //--------装载系统模块入口--------------
        BaseBusiComponent.loadSystemFuntionEnters(PfBusinessComponent.getSystemRoleMap());
        PfBusinessComponent pfbusiness= SpringContextHolder.getBean("pfBusinessComponent");
        if (pfbusiness!=null){
            List<MEnter> funcList=BaseBusiComponent.getSystemModuleEnters();
            pfbusiness.registerFunctions(PcPlateformApplication.App_Code,PcPlateformApplication.App_Name,
                    PcPlateformApplication.getModuleMap(),funcList);
        }
    }
}
