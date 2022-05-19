package com.nnte.pf_basic.mapper.workdb.operator;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-19 22:39:44>
 */
public class PlateformOperator extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private Integer opeType; //操作员类型:1超级管理员    2普通操作员 3:自动操作员
    private String opeCode; //操作员编号-唯一
    private String opeName; //操作员姓名
    private String opePassword; //操作员密码
    private String opeMobile; //手机号-唯一
    private Integer opeState; //0:未开通，1：有效，2：暂停，3：删除
    private Date createTime; //创建时间
    private String tmpKey; //用于登录时加密密码

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
