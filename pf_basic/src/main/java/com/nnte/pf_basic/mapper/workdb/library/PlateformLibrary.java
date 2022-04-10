package com.nnte.pf_basic.mapper.workdb.library;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-04-10 18:28:37>
 */
public class PlateformLibrary extends BaseModel {
    @DBPKColum private Integer id;
    private String libTypeCode;
    private String libTypeName;
    private String typeItemCode;
    private String typeItemName;
    private Integer itemSort;
    private Integer itemState;
    private String appCode;
    private String modelCode;
    private Integer canModify;
    private String remark;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;

    public PlateformLibrary(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getLibTypeCode(){ return libTypeCode;}
    public void setLibTypeCode(String  libTypeCode){ this.libTypeCode = libTypeCode;}
    public String  getLibTypeName(){ return libTypeName;}
    public void setLibTypeName(String  libTypeName){ this.libTypeName = libTypeName;}
    public String  getTypeItemCode(){ return typeItemCode;}
    public void setTypeItemCode(String  typeItemCode){ this.typeItemCode = typeItemCode;}
    public String  getTypeItemName(){ return typeItemName;}
    public void setTypeItemName(String  typeItemName){ this.typeItemName = typeItemName;}
    public Integer  getItemSort(){ return itemSort;}
    public void setItemSort(Integer  itemSort){ this.itemSort = itemSort;}
    public Integer  getItemState(){ return itemState;}
    public void setItemState(Integer  itemState){ this.itemState = itemState;}
    public String  getAppCode(){ return appCode;}
    public void setAppCode(String  appCode){ this.appCode = appCode;}
    public String  getModelCode(){ return modelCode;}
    public void setModelCode(String  modelCode){ this.modelCode = modelCode;}
    public Integer  getCanModify(){ return canModify;}
    public void setCanModify(Integer  canModify){ this.canModify = canModify;}
    public String  getRemark(){ return remark;}
    public void setRemark(String  remark){ this.remark = remark;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public Date  getCreateDate(){ return createDate;}
    public void setCreateDate(Date  createDate){ this.createDate = createDate;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateDate(){ return updateDate;}
    public void setUpdateDate(Date  updateDate){ this.updateDate = updateDate;}
}
