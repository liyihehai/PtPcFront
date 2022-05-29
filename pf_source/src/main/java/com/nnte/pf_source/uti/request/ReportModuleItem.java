package com.nnte.pf_source.uti.request;

public class ReportModuleItem {
    private String appCode; //应用代码
    private String moduleCode; //模块代码
    private String moduleName;  //模块名称
    private Integer frameModule; //框架模块:1框架模块，非1商业模块
    private String moduleVersion; //模块版本

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getFrameModule() {
        return frameModule;
    }

    public void setFrameModule(Integer frameModule) {
        this.frameModule = frameModule;
    }
}
