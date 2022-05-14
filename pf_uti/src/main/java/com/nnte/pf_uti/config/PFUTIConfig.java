package com.nnte.pf_uti.config;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppRegistry;
import org.springframework.stereotype.Component;

@Component
@BusiLogAttr(PFUTIConfig.loggerName)
public class PFUTIConfig extends BaseComponent implements ModuleInterface {
    public static final String loggerName = "PFUTInterface";
    public static final String jarName = "pf_uti.jar";

    public final static String Module_Code = "UTInterface"; //统一交易接口
    static {
        AppRegistry.setAppModuleName(Module_Code,"统一交易接口");
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
