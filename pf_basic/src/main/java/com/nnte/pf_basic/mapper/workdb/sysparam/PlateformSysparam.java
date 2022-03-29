package com.nnte.pf_basic.mapper.workdb.sysparam;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2022-03-28 17:21:10>
 */
public class PlateformSysparam extends BaseModel {
    @DBPKColum private Integer id;
    private String paramType;
    private String paramName;
    private String paramKeyGroup;
    private String paramKey;
    private String value1;
    private String value2;
    private String valueText;
    private Integer paramState;
    private Date createTime;
    private String createOpeCode;
    private String createOpeName;

    public PlateformSysparam(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getParamType(){ return paramType;}
    public void setParamType(String  paramType){ this.paramType = paramType;}
    public String  getParamName(){ return paramName;}
    public void setParamName(String  paramName){ this.paramName = paramName;}
    public String  getParamKeyGroup(){ return paramKeyGroup;}
    public void setParamKeyGroup(String  paramKeyGroup){ this.paramKeyGroup = paramKeyGroup;}
    public String  getParamKey(){ return paramKey;}
    public void setParamKey(String  paramKey){ this.paramKey = paramKey;}
    public String  getValue1(){ return value1;}
    public void setValue1(String  value1){ this.value1 = value1;}
    public String  getValue2(){ return value2;}
    public void setValue2(String  value2){ this.value2 = value2;}
    public String  getValueText(){ return valueText;}
    public void setValueText(String  valueText){ this.valueText = valueText;}
    public Integer  getParamState(){ return paramState;}
    public void setParamState(Integer  paramState){ this.paramState = paramState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public String  getCreateOpeCode(){ return createOpeCode;}
    public void setCreateOpeCode(String  createOpeCode){ this.createOpeCode = createOpeCode;}
    public String  getCreateOpeName(){ return createOpeName;}
    public void setCreateOpeName(String  createOpeName){ this.createOpeName = createOpeName;}
}
