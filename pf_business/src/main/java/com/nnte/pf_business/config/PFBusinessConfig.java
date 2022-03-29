package com.nnte.pf_business.config;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.framework.annotation.MybatisXmlMapper;
import org.springframework.stereotype.Component;

@Component
@BusiLogAttr(PFBusinessConfig.loggerName)
@MybatisXmlMapper("com.nnte.pf_business.mapper.workdb")
public class PFBusinessConfig extends BaseComponent implements ModuleInterface {
    public static final String loggerName = "PfBusiness";
    public static final String jarName = "pf_business.jar";

    public final static String MODULE_SYSTEM_SETTING = "systemSetting"; //系统设置
    static {
        AppRegistry.setAppModuleName(MODULE_SYSTEM_SETTING,"系统设置");
    }

    @Override
    public void initModule() {

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
