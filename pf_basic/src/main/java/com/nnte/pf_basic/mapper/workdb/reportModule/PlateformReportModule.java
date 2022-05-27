package com.nnte.pf_basic.mapper.workdb.reportModule;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-27 08:04:21>
 */
public class PlateformReportModule extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private String pmCode; //商户代码
    private String appCode; //应用代码
    private String appName; //应用名称
    private String moduleCode; //模块代码
    private String moduleName; //模块名称
    private String moduleVersion; //最新版本
    private Integer frameModule; //框架模块:1框架模块，非1商业模块
    private String reportTerminal; //报告终端
    private String reportIp; //报告IP
    private Date refreshTime; //刷新时间

    public PlateformReportModule(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getAppCode(){ return appCode;}
    public void setAppCode(String  appCode){ this.appCode = appCode;}
    public String  getAppName(){ return appName;}
    public void setAppName(String  appName){ this.appName = appName;}
    public String  getModuleCode(){ return moduleCode;}
    public void setModuleCode(String  moduleCode){ this.moduleCode = moduleCode;}
    public String  getModuleName(){ return moduleName;}
    public void setModuleName(String  moduleName){ this.moduleName = moduleName;}
    public String  getModuleVersion(){ return moduleVersion;}
    public void setModuleVersion(String  moduleVersion){ this.moduleVersion = moduleVersion;}
    public Integer  getFrameModule(){ return frameModule;}
    public void setFrameModule(Integer  frameModule){ this.frameModule = frameModule;}
    public String  getReportTerminal(){ return reportTerminal;}
    public void setReportTerminal(String  reportTerminal){ this.reportTerminal = reportTerminal;}
    public String  getReportIp(){ return reportIp;}
    public void setReportIp(String  reportIp){ this.reportIp = reportIp;}
    public Date  getRefreshTime(){ return refreshTime;}
    public void setRefreshTime(Date  refreshTime){ this.refreshTime = refreshTime;}
}
