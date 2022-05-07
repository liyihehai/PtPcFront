package com.nnte.pf_basic.mapper.workdb.busiModule;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-07 11:43:58>
 */
public class PlateformBusiModule extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private String moduleCode; //模块代码
    private String moduleName; //模块名称
    private String moduleDesc; //模块说明
    private String currentVersion; //最新版本
    private Integer moduleStatus; //模块状态:0未上线，1已上线，2将作废，-1已作废
    private String createBy; //创建人
    private Date createDate; //创建时间
    private String updateBy; //更改人
    private Date updateDate; //更改时间

    public PlateformBusiModule(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getModuleCode(){ return moduleCode;}
    public void setModuleCode(String  moduleCode){ this.moduleCode = moduleCode;}
    public String  getModuleName(){ return moduleName;}
    public void setModuleName(String  moduleName){ this.moduleName = moduleName;}
    public String  getModuleDesc(){ return moduleDesc;}
    public void setModuleDesc(String  moduleDesc){ this.moduleDesc = moduleDesc;}
    public String  getCurrentVersion(){ return currentVersion;}
    public void setCurrentVersion(String  currentVersion){ this.currentVersion = currentVersion;}
    public Integer  getModuleStatus(){ return moduleStatus;}
    public void setModuleStatus(Integer  moduleStatus){ this.moduleStatus = moduleStatus;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public Date  getCreateDate(){ return createDate;}
    public void setCreateDate(Date  createDate){ this.createDate = createDate;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateDate(){ return updateDate;}
    public void setUpdateDate(Date  updateDate){ this.updateDate = updateDate;}
}
