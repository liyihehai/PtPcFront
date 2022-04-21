package com.nnte.pf_merchant.mapper.workdb.merchant;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-04-21 09:54:47>
 */
public class PlateformMerchant extends BaseModel {
    @DBPKColum private Integer id;
    private String pmCode;
    private String pmName;
    private String pmShortName;
    private Integer pmCompanyPerson;
    private String pmLogo;
    private Integer pmState;
    private Date createTime;
    private String applyEmail;
    private String createBy;
    private String updateBy;
    private Date updateTime;

    public PlateformMerchant(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getPmName(){ return pmName;}
    public void setPmName(String  pmName){ this.pmName = pmName;}
    public String  getPmShortName(){ return pmShortName;}
    public void setPmShortName(String  pmShortName){ this.pmShortName = pmShortName;}
    public Integer  getPmCompanyPerson(){ return pmCompanyPerson;}
    public void setPmCompanyPerson(Integer  pmCompanyPerson){ this.pmCompanyPerson = pmCompanyPerson;}
    public String  getPmLogo(){ return pmLogo;}
    public void setPmLogo(String  pmLogo){ this.pmLogo = pmLogo;}
    public Integer  getPmState(){ return pmState;}
    public void setPmState(Integer  pmState){ this.pmState = pmState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public String  getApplyEmail(){ return applyEmail;}
    public void setApplyEmail(String  applyEmail){ this.applyEmail = applyEmail;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateTime(){ return updateTime;}
    public void setUpdateTime(Date  updateTime){ this.updateTime = updateTime;}
}
