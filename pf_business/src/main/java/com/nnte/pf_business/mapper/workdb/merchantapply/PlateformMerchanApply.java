package com.nnte.pf_business.mapper.workdb.merchantapply;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2020-06-20 09:27:44>
 */
public class PlateformMerchanApply extends BaseModel {
    @DBPKColum private Long id;
    private String pmName;
    private Integer confirmType;
    private String applyPhone;
    private String applyEmail;
    private Integer pmCompanyPerson;
    private Integer applyWays;
    private String applyerCode;
    private String applyerName;
    private String applyContent;
    private String applyMemo;
    private String creatorCode;
    private String creatorName;
    private Date createTime;
    private Integer applyState;
    private String confirmCode;
    private String confirmName;
    private Date confirmTime;
    private Integer smSendState;
    private String smRandomCode;
    private Date smSendTime;
    private Integer smConfirmState;
    private Integer emailSendState;
    private String emailRandomCode;
    private Date emailSendTime;
    private Integer emailConfirmState;
    private String opeCode;
    private String opeName;
    private Date lockTime;
    private Integer checkResult;
    private String pmCode;
    private String checkDesc;
    private String checkerCode;
    private String checkerName;
    private Date checkTime;

    public PlateformMerchanApply(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getPmName(){ return pmName;}
    public void setPmName(String  pmName){ this.pmName = pmName;}
    public Integer  getConfirmType(){ return confirmType;}
    public void setConfirmType(Integer  confirmType){ this.confirmType = confirmType;}
    public String  getApplyPhone(){ return applyPhone;}
    public void setApplyPhone(String  applyPhone){ this.applyPhone = applyPhone;}
    public String  getApplyEmail(){ return applyEmail;}
    public void setApplyEmail(String  applyEmail){ this.applyEmail = applyEmail;}
    public Integer  getPmCompanyPerson(){ return pmCompanyPerson;}
    public void setPmCompanyPerson(Integer  pmCompanyPerson){ this.pmCompanyPerson = pmCompanyPerson;}
    public Integer  getApplyWays(){ return applyWays;}
    public void setApplyWays(Integer  applyWays){ this.applyWays = applyWays;}
    public String  getApplyerCode(){ return applyerCode;}
    public void setApplyerCode(String  applyerCode){ this.applyerCode = applyerCode;}
    public String  getApplyerName(){ return applyerName;}
    public void setApplyerName(String  applyerName){ this.applyerName = applyerName;}
    public String  getApplyContent(){ return applyContent;}
    public void setApplyContent(String  applyContent){ this.applyContent = applyContent;}
    public String  getApplyMemo(){ return applyMemo;}
    public void setApplyMemo(String  applyMemo){ this.applyMemo = applyMemo;}
    public String  getCreatorCode(){ return creatorCode;}
    public void setCreatorCode(String  creatorCode){ this.creatorCode = creatorCode;}
    public String  getCreatorName(){ return creatorName;}
    public void setCreatorName(String  creatorName){ this.creatorName = creatorName;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public Integer  getApplyState(){ return applyState;}
    public void setApplyState(Integer  applyState){ this.applyState = applyState;}
    public String  getConfirmCode(){ return confirmCode;}
    public void setConfirmCode(String  confirmCode){ this.confirmCode = confirmCode;}
    public String  getConfirmName(){ return confirmName;}
    public void setConfirmName(String  confirmName){ this.confirmName = confirmName;}
    public Date  getConfirmTime(){ return confirmTime;}
    public void setConfirmTime(Date  confirmTime){ this.confirmTime = confirmTime;}
    public Integer  getSmSendState(){ return smSendState;}
    public void setSmSendState(Integer  smSendState){ this.smSendState = smSendState;}
    public String  getSmRandomCode(){ return smRandomCode;}
    public void setSmRandomCode(String  smRandomCode){ this.smRandomCode = smRandomCode;}
    public Date  getSmSendTime(){ return smSendTime;}
    public void setSmSendTime(Date  smSendTime){ this.smSendTime = smSendTime;}
    public Integer  getSmConfirmState(){ return smConfirmState;}
    public void setSmConfirmState(Integer  smConfirmState){ this.smConfirmState = smConfirmState;}
    public Integer  getEmailSendState(){ return emailSendState;}
    public void setEmailSendState(Integer  emailSendState){ this.emailSendState = emailSendState;}
    public String  getEmailRandomCode(){ return emailRandomCode;}
    public void setEmailRandomCode(String  emailRandomCode){ this.emailRandomCode = emailRandomCode;}
    public Date  getEmailSendTime(){ return emailSendTime;}
    public void setEmailSendTime(Date  emailSendTime){ this.emailSendTime = emailSendTime;}
    public Integer  getEmailConfirmState(){ return emailConfirmState;}
    public void setEmailConfirmState(Integer  emailConfirmState){ this.emailConfirmState = emailConfirmState;}
    public String  getOpeCode(){ return opeCode;}
    public void setOpeCode(String  opeCode){ this.opeCode = opeCode;}
    public String  getOpeName(){ return opeName;}
    public void setOpeName(String  opeName){ this.opeName = opeName;}
    public Date  getLockTime(){ return lockTime;}
    public void setLockTime(Date  lockTime){ this.lockTime = lockTime;}
    public Integer  getCheckResult(){ return checkResult;}
    public void setCheckResult(Integer  checkResult){ this.checkResult = checkResult;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getCheckDesc(){ return checkDesc;}
    public void setCheckDesc(String  checkDesc){ this.checkDesc = checkDesc;}
    public String  getCheckerCode(){ return checkerCode;}
    public void setCheckerCode(String  checkerCode){ this.checkerCode = checkerCode;}
    public String  getCheckerName(){ return checkerName;}
    public void setCheckerName(String  checkerName){ this.checkerName = checkerName;}
    public Date  getCheckTime(){ return checkTime;}
    public void setCheckTime(Date  checkTime){ this.checkTime = checkTime;}
}
