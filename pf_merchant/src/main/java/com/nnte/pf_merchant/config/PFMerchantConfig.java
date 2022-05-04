package com.nnte.pf_merchant.config;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.MybatisXmlMapper;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.pf_merchant.component.mqcomp.EmailMQComponent;
import com.nnte.pf_merchant.component.mqcomp.SMMQComponent;
import org.springframework.stereotype.Component;

@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
@MybatisXmlMapper("com.nnte.pf_merchant.mapper.workdb")
public class PFMerchantConfig extends BaseComponent implements ModuleInterface {
    public static final String loggerName = "PfMerchant";
    public static final String jarName = "pf_merchant.jar";

    public final static String Module_Code = "merchantManage"; //商户管理
    static {
        AppRegistry.setAppModuleName(Module_Code,"商户管理");
    }

    @Override
    public void initModule() {
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
    }

    @Override
    public String getModuleJarName() {
        return jarName;
    }

    @Override
    public String getModuleLoggerName() {
        return loggerName;
    }
}
