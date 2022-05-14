package com.nnte.pf_pc_front;

import com.nnte.basebusi.annotation.AppInitInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.WatchComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.DBSchemaPostgreSQL;
import com.nnte.framework.base.DynamicDatabaseSourceHolder;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.framework.utils.BaiduMapUtil;
import com.nnte.pf_basic.component.CruxOpeMQComponent;
import com.nnte.pf_basic.component.JedisComponent;
import com.nnte.pf_basic.component.PFServiceCommonMQ;
import com.nnte.pf_basic.component.PlateformSysParamComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_merchant.component.mqcomp.EmailMQComponent;
import com.nnte.pf_merchant.component.mqcomp.SMMQComponent;
import com.nnte.pf_pc_front.config.AppDBSrcConfig;
import com.nnte.pf_pc_front.config.AppRootConfig;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PcPlateformConfig extends BaseComponent
        implements ApplicationRunner, AppInitInterface {

    @Autowired
    PfBusinessComponent pfBusinessComponent;
    @Autowired
    WatchComponent watchComponent;
    @Autowired
    private AppRootConfig appRootConfig;
    @Autowired
    private AppDBSrcConfig appDBSrcConfig;
    @Autowired
    private DBSchemaPostgreSQL dbSchemaPostgreSQL;
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AppRegistry.registry(AppBasicConfig.App_Code,
                AppBasicConfig.App_Name,this);
        appInit();
    }

    private void appInit() throws Exception{
        //--------------------------------------------------------------------------------------
        outLogInfo("执行后续初始化功能......日志设置");
        BaiduMapUtil.setBaiduApiKey(appRootConfig.getBaiduApiKey());
        //--------------------------------------------------------------------------------------
        DynamicDatabaseSourceHolder ddh= SpringContextHolder.getBean(DynamicDatabaseSourceHolder.class);
        outLogInfo("初始化DynamicDatabaseSourceHolder组件......"+(ddh==null?"null":"suc"));
        ddh.loadDBSchemaInterface();
        //--初始化Redis服务器-----
        JedisComponent jedis = SpringContextHolder.getBean(JedisComponent.class);
        jedis.initJedisCom();
        //-----------------------
        outLogInfo("初始化工作数据库连接数据源......");
        BaseComponent.createDataBaseSource(dbSchemaPostgreSQL, AppBasicConfig.DB_Name,
                true,appDBSrcConfig);
        //----加载所有系统参数------------------
        plateformSysParamComponent.initLoadAllSysParams();
        //--------装载系统模块入口--------------
        BaseComponent.loadSystemFuntionEnters();
        //-------------------------------------
        //--初始化MQ-------------
        CruxOpeMQComponent mqComponent = SpringContextHolder.getBean(CruxOpeMQComponent.class);
        mqComponent.initProducer();
        PFServiceCommonMQ pfServiceCommonMQ = SpringContextHolder.getBean(PFServiceCommonMQ.class);
        pfServiceCommonMQ.initProducer(false, ProducerAccessMode.Shared);

        //--初始短信MQ-------------
        SMMQComponent smmq= SpringContextHolder.getBean(SMMQComponent.class);
        if (smmq!=null){
            try {
                smmq.initProducer();
                outLogInfo("SMMQComponent initProducer suc");
                smmq.initConsumer();
                outLogInfo("SMMQComponent initConsumer suc");
            }catch (BusiException be){
                outLogInfo("SMMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        //--初始邮件MQ-------------
        EmailMQComponent emailapplymq=SpringContextHolder.getBean(EmailMQComponent.class);
        if (emailapplymq!=null){
            try {
                emailapplymq.initProducer();
                outLogInfo("EmailMQComponent initProducer suc");
                emailapplymq.initConsumer();
                outLogInfo("EmailMQComponent initConsumer suc");
            }catch (BusiException be){
                outLogInfo("EmailMQComponent init err:"+be.getMessage());
            }
        }
        //------------------------
        //--启动程序守护线程，注册组件（系统参数）
        watchComponent.startWatch();
        //-------------------------------------
    }

    @Override
    public void onRegisterFunctions(String appCode, String appName, Map<String, String> moduleMap, List<MEnter> functionModuleList) {
        pfBusinessComponent.registerFunctions(appCode,appName,moduleMap,functionModuleList);
    }
}
