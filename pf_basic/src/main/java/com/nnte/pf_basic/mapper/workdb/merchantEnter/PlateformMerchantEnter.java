package com.nnte.pf_basic.mapper.workdb.merchantEnter;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-27 14:37:46>
 */
public class PlateformMerchantEnter extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private String funCode; //功能代码
    private String funPath; //功能路径
    private String funName; //功能名称
    private String appCode; //应用代码
    private String appName; //应用名称
    private String moduleCode; //模块代码
    private String moduleName; //模块名称
    private Date freshTime; //刷新时间

    public PlateformMerchantEnter(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getFunCode(){ return funCode;}
    public void setFunCode(String  funCode){ this.funCode = funCode;}
    public String  getFunPath(){ return funPath;}
    public void setFunPath(String  funPath){ this.funPath = funPath;}
    public String  getFunName(){ return funName;}
    public void setFunName(String  funName){ this.funName = funName;}
    public String  getAppCode(){ return appCode;}
    public void setAppCode(String  appCode){ this.appCode = appCode;}
    public String  getAppName(){ return appName;}
    public void setAppName(String  appName){ this.appName = appName;}
    public String  getModuleCode(){ return moduleCode;}
    public void setModuleCode(String  moduleCode){ this.moduleCode = moduleCode;}
    public String  getModuleName(){ return moduleName;}
    public void setModuleName(String  moduleName){ this.moduleName = moduleName;}
    public Date  getFreshTime(){ return freshTime;}
    public void setFreshTime(Date  freshTime){ this.freshTime = freshTime;}
}
