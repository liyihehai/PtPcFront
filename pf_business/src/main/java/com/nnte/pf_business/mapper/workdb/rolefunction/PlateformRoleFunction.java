package com.nnte.pf_business.mapper.workdb.rolefunction;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;


/*
 * 自动代码 请勿更改 <2020-05-11 11:05:21>
 */
public class PlateformRoleFunction extends BaseModel {
    @DBPKColum private Long id;
    private String roleCode;
    private String funCode;

    public PlateformRoleFunction(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getRoleCode(){ return roleCode;}
    public void setRoleCode(String  roleCode){ this.roleCode = roleCode;}
    public String  getFunCode(){ return funCode;}
    public void setFunCode(String  funCode){ this.funCode = funCode;}
}
