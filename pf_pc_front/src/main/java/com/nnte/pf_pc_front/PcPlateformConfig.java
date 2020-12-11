package com.nnte.pf_pc_front;

import com.nnte.basebusi.annotation.AppInitInterface;
import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.base.JedisComponent;
import com.nnte.basebusi.base.WatchComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.*;
import com.nnte.framework.utils.BaiduMapUtil;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.mqcomp.EmailMQComponent;
import com.nnte.pf_business.component.mqcomp.SMMQComponent;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "pf.pc.front")
@PropertySource(value = "classpath:pf-pc-front-config.properties")

@Getter
@Setter
public class PcPlateformConfig extends BaseBusiComponent
        implements ApplicationRunner, ConfigInterface, AppInitInterface {
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

    @Autowired
    PfBusinessComponent pfBusinessComponent;
    @Autowired
    WatchComponent watchComponent;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AppRegistry.registry(PcPlateformApplication.App_Code,
                PcPlateformApplication.App_Name,this);
        AppRegistry.registryModel(PcPlateformApplication.MODULE_SYSSETTING,"系统设置");
        AppRegistry.registryModel(PcPlateformApplication.MODULE_MERCHANT_MANAGE,"商户管理");
        appInit();
    }

    private void appInit() throws Exception{
        //--------------------------------------------------------------------------------------
        BaseNnte.outConsoleLog("执行后续初始化功能......日志设置");
        BaiduMapUtil.setBaiduApiKey(getBaiduApiKey());
        ConfigInterface.LoadConfigComponent(this);
        //--------------------------------------------------------------------------------------
        DynamicDatabaseSourceHolder ddh= SpringContextHolder.getBean(DynamicDatabaseSourceHolder.class);
        BaseBusiComponent.logInfo(this,"初始化DynamicDatabaseSourceHolder组件......"+(ddh==null?"null":"suc"));
        DynamicDatabaseSourceHolder.loadDBSchemaInterface();
        //--初始化Redis服务器-----
        JedisComponent jedis = SpringContextHolder.getBean(JedisComponent.class);
        jedis.setLogInterface(this);
        jedis.initJedisCom();
        //-----------------------
        //--初始化文件服务器连接--

        //------------------------
        //--初始短信MQ-------------
        SMMQComponent smmq=SpringContextHolder.getBean(SMMQComponent.class);
        if (smmq!=null){
            try {
                smmq.initProducer();
                BaseBusiComponent.logInfo(this,"SMMQComponent initProducer suc");
                smmq.initConsumer();
                BaseBusiComponent.logInfo(this,"SMMQComponent initConsumer suc");
            }catch (BusiException be){
                BaseBusiComponent.logInfo(this,"SMMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        //--初始邮件MQ-------------
        EmailMQComponent emailapplymq=SpringContextHolder.getBean(EmailMQComponent.class);
        if (emailapplymq!=null){
            try {
                emailapplymq.initProducer();
                BaseBusiComponent.logInfo(this,"EmailMQComponent initProducer suc");
                emailapplymq.initConsumer();
                BaseBusiComponent.logInfo(this,"EmailMQComponent initConsumer suc");
            }catch (BusiException be){
                BaseBusiComponent.logInfo(this,"EmailMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        BaseBusiComponent.logInfo(this,"初始化工作数据库连接......");
        DBSchemaPostgreSQL dbschema= SpringContextHolder.getBean(DBSchemaPostgreSQL.class);
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(getWorkDBDriverClassName());
        config.setJdbcUrl(dbschema.makeJDBCUrl(getWorkDBIp(), NumberUtil.getDefaultLong(getWorkDBPort()),
                getWorkDBSchema()));
        config.setUsername(getWorkDBUser());
        config.setPassword(getWorkDBPassword());
        config.setMinimumIdle(0);
        config.setMaximumPoolSize(5);
        config.setIdleTimeout(1000*20);
        config.setConnectionTestQuery("SELECT 1");
        List<String> mappers=new ArrayList<>();
        mappers.add("com.nnte.pf_business.mapper.workdb");
        BaseBusiComponent.logInfo(this,"初始化工作数据库连接数据源......");
        ddh.initDataBaseSource(DBSrcTranc.Work_DBSrc_Name,config,mappers, PfBusinessComponent.class,true);
        //--------装载系统模块入口--------------
        BaseBusiComponent.loadSystemFuntionEnters(PfBusinessComponent.getSystemRoleMap());
        //-------------------------------------
        //--启动程序守护线程，注册组件（系统参数）
        watchComponent.startWatch();
        //-------------------------------------
    }

    @Override
    public void onRegisterFunctions(String appCode, String appName, Map<String, String> moduleMap, List<MEnter> functionModuleList) {
        pfBusinessComponent.registerFunctions(appCode,appName,moduleMap,functionModuleList);
    }
}
