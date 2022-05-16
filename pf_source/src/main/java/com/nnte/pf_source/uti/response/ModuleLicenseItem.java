package com.nnte.pf_source.uti.response;

public class ModuleLicenseItem {
    private String appCode;         //应用代码
    private String moduleCode;      //模块代码
    private String moduleLicense;

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

    public String getModuleLicense() {
        return moduleLicense;
    }

    public void setModuleLicense(String moduleLicense) {
        this.moduleLicense = moduleLicense;
    }
}
