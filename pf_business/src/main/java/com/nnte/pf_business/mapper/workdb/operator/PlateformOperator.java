package com.nnte.pf_business.mapper.workdb.operator;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2020-04-22 11:07:53>
 */
public class PlateformOperator extends BaseModel {
    @DBPKColum private Integer id;
    private Integer opeType;
    private String opeCode;
    private String opeName;
    private String opePassword;
    private String opeMobile;
    private Integer opeState;
    private Date createTime;
    private String tmpKey;

    public PlateformOperator(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public Integer  getOpeType(){ return opeType;}
    public void setOpeType(Integer  opeType){ this.opeType = opeType;}
    public String  getOpeCode(){ return opeCode;}
    public void setOpeCode(String  opeCode){ this.opeCode = opeCode;}
    public String  getOpeName(){ return opeName;}
    public void setOpeName(String  opeName){ this.opeName = opeName;}
    public String  getOpePassword(){ return opePassword;}
    public void setOpePassword(String  opePassword){ this.opePassword = opePassword;}
    public String  getOpeMobile(){ return opeMobile;}
    public void setOpeMobile(String  opeMobile){ this.opeMobile = opeMobile;}
    public Integer  getOpeState(){ return opeState;}
    public void setOpeState(Integer  opeState){ this.opeState = opeState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public String  getTmpKey(){ return tmpKey;}
    public void setTmpKey(String  tmpKey){ this.tmpKey = tmpKey;}
}
