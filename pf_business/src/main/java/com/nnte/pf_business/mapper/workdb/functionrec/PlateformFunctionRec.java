package com.nnte.pf_business.mapper.workdb.functionrec;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;


/*
 * 自动代码 请勿更改 <2020-05-10 15:47:28>
 */
public class PlateformFunctionRec extends BaseModel {
    @DBPKColum private Integer id;
    private String funPath;
    private String funName;
    private String sysRoleCode;
    private String sysRoleName;
    private String authCode;
    private String appCode;
    private String appName;
    private String moduleCode;
    private String moduleName;
    private String moduleVersion;

    public PlateformFunctionRec(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getFunPath(){ return funPath;}
    public void setFunPath(String  funPath){ this.funPath = funPath;}
    public String  getFunName(){ return funName;}
    public void setFunName(String  funName){ this.funName = funName;}
    public String  getSysRoleCode(){ return sysRoleCode;}
    public void setSysRoleCode(String  sysRoleCode){ this.sysRoleCode = sysRoleCode;}
    public String  getSysRoleName(){ return sysRoleName;}
    public void setSysRoleName(String  sysRoleName){ this.sysRoleName = sysRoleName;}
    public String  getAuthCode(){ return authCode;}
    public void setAuthCode(String  authCode){ this.authCode = authCode;}
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
}
