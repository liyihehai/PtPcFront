package com.nnte.pf_business.mapper.workdb.menus;

import com.nnte.framework.annotation.DBPKColum;
import com.nnte.framework.base.BaseModel;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2020-04-20 19:23:21>
 */
public class PlateformMenus extends BaseModel {
    @DBPKColum private Integer id;
    private String menuCode;
    private String menuName;
    private Integer menuClass;
    private String parentMenuCode;
    private Integer menuState;
    private Date createTime;

    public PlateformMenus(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getMenuCode(){ return menuCode;}
    public void setMenuCode(String  menuCode){ this.menuCode = menuCode;}
    public String  getMenuName(){ return menuName;}
    public void setMenuName(String  menuName){ this.menuName = menuName;}
    public Integer  getMenuClass(){ return menuClass;}
    public void setMenuClass(Integer  menuClass){ this.menuClass = menuClass;}
    public String  getParentMenuCode(){ return parentMenuCode;}
    public void setParentMenuCode(String  parentMenuCode){ this.parentMenuCode = parentMenuCode;}
    public Integer  getMenuState(){ return menuState;}
    public void setMenuState(Integer  menuState){ this.menuState = menuState;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
}
