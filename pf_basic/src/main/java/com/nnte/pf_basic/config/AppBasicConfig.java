package com.nnte.pf_basic.config;

import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.framework.annotation.MybatisXmlMapper;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@MybatisXmlMapper("com.nnte.pf_basic.mapper.workdb")
public class AppBasicConfig implements ModuleInterface {
    public final static String App_Code = "PF-PC-MANAGER";
    public final static String App_Name = "平台PC管理端应用";
    public final static String DB_Name = "PfWorkDB";
    public final static String JarLoggerName = "PfBasic";
    public final static String jarName = "pf_basic.jar";
    public final static String Module_Code = "systemBasicInfo"; //基础信息
    public final static String auto_task_operator = "3001";
    static {
        AppRegistry.setAppModuleName(Module_Code,"基础信息");
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
        return JarLoggerName;
    }
}
