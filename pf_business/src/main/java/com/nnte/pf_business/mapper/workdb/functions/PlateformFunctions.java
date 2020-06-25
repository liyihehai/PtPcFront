package com.nnte.pf_business.mapper.workdb.functions;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2020-06-21 13:55:29>
 */
public class PlateformFunctions extends BaseModel {
    @DBPKColum private Long id;
    private String menuCode;
    private String funCode;
    private String funName;
    private String authCode;
    private String funParam;
    private Integer funState;
    private Date createTime;

    public PlateformFunctions(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getMenuCode(){ return menuCode;}
    public void setMenuCode(String  menuCode){ this.menuCode = menuCode;}
    public String  getFunCode(){ return funCode;}
    public void setFunCode(String  funCode){ this.funCode = funCode;}
    public String  getFunName(){ return funName;}
    public void setFunName(String  funName){ this.funName = funName;}
    public String  getAuthCode(){ return authCode;}
    public void setAuthCode(String  authCode){ this.authCode = authCode;}
    public String  getFunParam(){ return funParam;}
    public void setFunParam(String  funParam){ this.funParam = funParam;}
    public Integer  getFunState(){ return funState;}
    public void setFunState(Integer  funState){ this.funState = funState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
}
