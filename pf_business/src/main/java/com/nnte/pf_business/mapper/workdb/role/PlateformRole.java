package com.nnte.pf_business.mapper.workdb.role;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2020-05-07 08:38:48>
 */
public class PlateformRole extends BaseModel {
    @DBPKColum private Long id;
    private String roleCode;
    private String roleName;
    private String sysroleList;
    private Integer roleState;
    private Date createTime;

    public PlateformRole(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getRoleCode(){ return roleCode;}
    public void setRoleCode(String  roleCode){ this.roleCode = roleCode;}
    public String  getRoleName(){ return roleName;}
    public void setRoleName(String  roleName){ this.roleName = roleName;}
    public String  getSysroleList(){ return sysroleList;}
    public void setSysroleList(String  sysroleList){ this.sysroleList = sysroleList;}
    public Integer  getRoleState(){ return roleState;}
    public void setRoleState(Integer  roleState){ this.roleState = roleState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
}
