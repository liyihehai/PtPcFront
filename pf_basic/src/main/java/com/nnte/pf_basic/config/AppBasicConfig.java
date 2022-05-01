package com.nnte.pf_basic.config;

import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.annotation.RootConfigProperties;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.basebusi.entity.DBSrcConfig;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.MybatisXmlMapper;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.pf_basic.component.CruxOpeMQComponent;
import com.nnte.pf_basic.component.PFServiceCommonMQ;
import org.springframework.stereotype.Component;

@Component
@MybatisXmlMapper("com.nnte.pf_basic.mapper.workdb")
public class AppBasicConfig implements ModuleInterface {
    public final static String App_Code = "PF-PC-MANAGER";
    public final static String App_Name = "平台PC管理端应用";

    public static final String DB_Name = "PfWorkDB";
    public static final String JarLoggerName = "PfBasic";
    public static final String jarName = "pf_basic.jar";

    public final static String SYSTEM_BASIC_INFO = "systemBasicInfo"; //基础信息
    static {
        AppRegistry.setAppModuleName(SYSTEM_BASIC_INFO,"基础信息");
    }

    @Override
    public void initModule() {
        /*
        CruxOpeMQComponent mqComponent = SpringContextHolder.getBean(CruxOpeMQComponent.class);
        if (mqComponent!=null){
            try {
                mqComponent.initProducer();
            }catch (BusiException be){}
        }
        PFServiceCommonMQ pfServiceCommonMQ = SpringContextHolder.getBean(PFServiceCommonMQ.class);
        if (pfServiceCommonMQ!=null){
            try{
                pfServiceCommonMQ.initProducer();
                pfServiceCommonMQ.scanRegisterProcess();
            }catch (Exception e){
            }
        }
        */
    }

    @Override
    public String getModuleJarName() {
        return jarName;
    }

    @Override
    public String getModuleLoggerName() {
        return JarLoggerName;
    }
}
