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
