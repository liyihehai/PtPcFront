package com.nnte.pf_business.mapper.workdb.opefunction;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;


/*
 * 自动代码 请勿更改 <2020-05-11 10:21:36>
 */
public class PlateformOpeFunction extends BaseModel {
    @DBPKColum private Long id;
    private String opeCode;
    private String funCode;

    public PlateformOpeFunction(){}

    public Long  getId(){ return id;}
    public void setId(Long  id){ this.id = id;}
    public String  getOpeCode(){ return opeCode;}
    public void setOpeCode(String  opeCode){ this.opeCode = opeCode;}
    public String  getFunCode(){ return funCode;}
    public void setFunCode(String  funCode){ this.funCode = funCode;}
}
