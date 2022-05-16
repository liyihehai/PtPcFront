package com.nnte.pf_basic.mapper.workdb.appLicense;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-16 15:02:47>
 */
public class PlateformAppLicense extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private String pmCode; //商户代码
    private String appCode; //应用代码
    private String moduleCode; //模块代码
    private String moduleVersion; //模块版本
    private Integer licenseState; //License状态:0编辑，1待执行，2执行中，3执行结束，4已终止，-1已删除
    private Integer copyCount; //拷贝数1<= x <=50
    private String terminals; //终端序列
    private Date stareDate; //开始日期
    private Date endDate; //结束日期
    private String orderNo; //订单号
    private Date exeDate; //执行日期
    private Long remainderDays; //剩余天数
    private Double licenseAmount; //License金额
    private Double exeAmount; //已执行金额
    private Double remainderAmount; //剩余金额
    private String createBy; //创建人
    private Date createTime; //创建时间
    private String updateBy; //更改人
    private Date updateDate; //更改时间

    public PlateformAppLicense(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getAppCode(){ return appCode;}
    public void setAppCode(String  appCode){ this.appCode = appCode;}
    public String  getModuleCode(){ return moduleCode;}
    public void setModuleCode(String  moduleCode){ this.moduleCode = moduleCode;}
    public String  getModuleVersion(){ return moduleVersion;}
    public void setModuleVersion(String  moduleVersion){ this.moduleVersion = moduleVersion;}
    public Integer  getLicenseState(){ return licenseState;}
    public void setLicenseState(Integer  licenseState){ this.licenseState = licenseState;}
    public Integer  getCopyCount(){ return copyCount;}
    public void setCopyCount(Integer  copyCount){ this.copyCount = copyCount;}
    public String  getTerminals(){ return terminals;}
    public void setTerminals(String  terminals){ this.terminals = terminals;}
    public Date  getStareDate(){ return stareDate;}
    public void setStareDate(Date  stareDate){ this.stareDate = stareDate;}
    public Date  getEndDate(){ return endDate;}
    public void setEndDate(Date  endDate){ this.endDate = endDate;}
    public String  getOrderNo(){ return orderNo;}
    public void setOrderNo(String  orderNo){ this.orderNo = orderNo;}
    public Date  getExeDate(){ return exeDate;}
    public void setExeDate(Date  exeDate){ this.exeDate = exeDate;}
    public Long  getRemainderDays(){ return remainderDays;}
    public void setRemainderDays(Long  remainderDays){ this.remainderDays = remainderDays;}
    public Double  getLicenseAmount(){ return licenseAmount;}
    public void setLicenseAmount(Double  licenseAmount){ this.licenseAmount = licenseAmount;}
    public Double  getExeAmount(){ return exeAmount;}
    public void setExeAmount(Double  exeAmount){ this.exeAmount = exeAmount;}
    public Double  getRemainderAmount(){ return remainderAmount;}
    public void setRemainderAmount(Double  remainderAmount){ this.remainderAmount = remainderAmount;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateDate(){ return updateDate;}
    public void setUpdateDate(Date  updateDate){ this.updateDate = updateDate;}
}
