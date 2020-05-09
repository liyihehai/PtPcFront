package com.nnte.pf_business.mapper.workdb.operole;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;


/*
 * 自动代码 请勿更改 <2020-05-09 13:03:48>
 */
public class PlateformOpeRole extends BaseModel {
    @DBPKColum private Long id;
    private String opeCode;
    private String roleCode;

    public PlateformOpeRole(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getOpeCode(){ return opeCode;}
    public void setOpeCode(String  opeCode){ this.opeCode = opeCode;}
    public String  getRoleCode(){ return roleCode;}
    public void setRoleCode(String  roleCode){ this.roleCode = roleCode;}
}
