package com.nnte_plateform.base;

import java.util.Date;

public class GroupMerchant{
    private String pmCode;
    private String pmName;
    private String pmShortName;
    private Integer pmCompanyPerson;
    private Integer pmState;
    private Date createTime;
    private String applyEmail;

    public String getPmCode() {
        return pmCode;
    }

    public void setPmCode(String pmCode) {
        this.pmCode = pmCode;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPmShortName() {
        return pmShortName;
    }

    public void setPmShortName(String pmShortName) {
        this.pmShortName = pmShortName;
    }

    public Integer getPmCompanyPerson() {
        return pmCompanyPerson;
    }

    public void setPmCompanyPerson(Integer pmCompanyPerson) {
        this.pmCompanyPerson = pmCompanyPerson;
    }

    public Integer getPmState() {
        return pmState;
    }

    public void setPmState(Integer pmState) {
        this.pmState = pmState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplyEmail() {
        return applyEmail;
    }

    public void setApplyEmail(String applyEmail) {
        this.applyEmail = applyEmail;
    }
}
