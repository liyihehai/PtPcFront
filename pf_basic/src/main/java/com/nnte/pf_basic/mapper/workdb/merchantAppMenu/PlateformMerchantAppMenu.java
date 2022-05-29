package com.nnte.pf_basic.mapper.workdb.merchantAppMenu;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-29 17:23:47>
 */
public class PlateformMerchantAppMenu extends BaseModel {
    @DBPKColum private Integer id; //主键ID
    private String appCode; //应用代码
    private Integer menuType; //菜单类型:1全局菜单，2：商户菜单
    private String pmCode; //商户代码:菜单类型为2商户菜单时有效
    private String menuContent; //菜单内容
    private Integer menuStatus; //模块状态:0编辑，1已确认，-1已作废
    private String createBy; //创建人
    private Date createDate; //创建时间
    private String updateBy; //更改人
    private Date updateDate; //更改时间

    public PlateformMerchantAppMenu(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getAppCode(){ return appCode;}
    public void setAppCode(String  appCode){ this.appCode = appCode;}
    public Integer  getMenuType(){ return menuType;}
    public void setMenuType(Integer  menuType){ this.menuType = menuType;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getMenuContent(){ return menuContent;}
    public void setMenuContent(String  menuContent){ this.menuContent = menuContent;}
    public Integer  getMenuStatus(){ return menuStatus;}
    public void setMenuStatus(Integer  menuStatus){ this.menuStatus = menuStatus;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public Date  getCreateDate(){ return createDate;}
    public void setCreateDate(Date  createDate){ this.createDate = createDate;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateDate(){ return updateDate;}
    public void setUpdateDate(Date  updateDate){ this.updateDate = updateDate;}
}
