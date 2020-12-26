package com.nnte.pf_pc_front;

import com.nnte.basebusi.annotation.AppInitInterface;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.base.JedisComponent;
import com.nnte.basebusi.base.WatchComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.*;
import com.nnte.framework.utils.BaiduMapUtil;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.mqcomp.EmailMQComponent;
import com.nnte.pf_business.component.mqcomp.SMMQComponent;
import com.nnte.pf_pc_front.config.AppRootConfig;
import com.nnte.pf_pc_front.config.WorkDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PcPlateformConfig extends BaseBusiComponent
        implements ApplicationRunner, AppInitInterface {

    @Autowired
    PfBusinessComponent pfBusinessComponent;
    @Autowired
    WatchComponent watchComponent;
    @Autowired
    private AppRootConfig appRootConfig;
    @Autowired
    private WorkDBConfig workDBConfig;
    @Autowired
    private DBSchemaPostgreSQL dbSchemaPostgreSQL;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AppRegistry.registry(PcPlateformApplication.App_Code,
                PcPlateformApplication.App_Name,this);
        appInit();
    }

    private void appInit() throws Exception{
        //--------------------------------------------------------------------------------------
        BaseNnte.outConsoleLog("执行后续初始化功能......日志设置");
        ConfigInterface.LoadConfigComponent(appRootConfig);
        BaiduMapUtil.setBaiduApiKey(appRootConfig.getBaiduApiKey());
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
        BaseBusiComponent.logInfo(this,"初始化工作数据库连接数据源......");
        BaseBusiComponent.createDataBaseSource(PfBusinessComponent.class,
                dbSchemaPostgreSQL, WorkDBConfig.DB_NAME,WorkDBConfig.MAPPER_PATH,
                true,workDBConfig);
        //--------装载系统模块入口--------------
        BaseBusiComponent.loadSystemFuntionEnters();
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
