package com.nnte.pf_merchant.mapper.workdb.merchant_expand;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/*
 * 自动代码 请勿更改 <2022-03-28 13:16:25>
 */
public class PlateformMerchantExpand extends BaseModel {
    @DBPKColum private String pmCode;
    private String pmBusiType;
    private String pmCountry;
    private String pmProvince;
    private String pmCity;
    private String pmArea;
    private String pmAddress;
    private String pmZipcode;
    private String pmCoordinate;
    private Double pmLongitude;
    private Double pmLatitude;
    private String pmLinkName;
    private String pmLinkPhone;
    private String pmCsrPhone;
    private String pmEmail;
    private String pmIntroduce;
    private String pmLogo;
    private String pmPic1;
    private String pmPic2;
    private String pmPic3;
    private String pmRemark;
    private String pmLegalName;
    private String pmLegalIdNum;
    private String pmCertificatePic1;
    private String pmCertificatePic2;
    private String pmCertificatePic3;
    private String pmCertificatePic4;
    private Date createTime;

    public PlateformMerchantExpand(){}

    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getPmBusiType(){ return pmBusiType;}
    public void setPmBusiType(String  pmBusiType){ this.pmBusiType = pmBusiType;}
    public String  getPmCountry(){ return pmCountry;}
    public void setPmCountry(String  pmCountry){ this.pmCountry = pmCountry;}
    public String  getPmProvince(){ return pmProvince;}
    public void setPmProvince(String  pmProvince){ this.pmProvince = pmProvince;}
    public String  getPmCity(){ return pmCity;}
    public void setPmCity(String  pmCity){ this.pmCity = pmCity;}
    public String  getPmArea(){ return pmArea;}
    public void setPmArea(String  pmArea){ this.pmArea = pmArea;}
    public String  getPmAddress(){ return pmAddress;}
    public void setPmAddress(String  pmAddress){ this.pmAddress = pmAddress;}
    public String  getPmZipcode(){ return pmZipcode;}
    public void setPmZipcode(String  pmZipcode){ this.pmZipcode = pmZipcode;}
    public String  getPmCoordinate(){ return pmCoordinate;}
    public void setPmCoordinate(String  pmCoordinate){ this.pmCoordinate = pmCoordinate;}
    public Double  getPmLongitude(){ return pmLongitude;}
    public void setPmLongitude(Double  pmLongitude){ this.pmLongitude = pmLongitude;}
    public Double  getPmLatitude(){ return pmLatitude;}
    public void setPmLatitude(Double  pmLatitude){ this.pmLatitude = pmLatitude;}
    public String  getPmLinkName(){ return pmLinkName;}
    public void setPmLinkName(String  pmLinkName){ this.pmLinkName = pmLinkName;}
    public String  getPmLinkPhone(){ return pmLinkPhone;}
    public void setPmLinkPhone(String  pmLinkPhone){ this.pmLinkPhone = pmLinkPhone;}
    public String  getPmCsrPhone(){ return pmCsrPhone;}
    public void setPmCsrPhone(String  pmCsrPhone){ this.pmCsrPhone = pmCsrPhone;}
    public String  getPmEmail(){ return pmEmail;}
    public void setPmEmail(String  pmEmail){ this.pmEmail = pmEmail;}
    public String  getPmIntroduce(){ return pmIntroduce;}
    public void setPmIntroduce(String  pmIntroduce){ this.pmIntroduce = pmIntroduce;}
    public String  getPmLogo(){ return pmLogo;}
    public void setPmLogo(String  pmLogo){ this.pmLogo = pmLogo;}
    public String  getPmPic1(){ return pmPic1;}
    public void setPmPic1(String  pmPic1){ this.pmPic1 = pmPic1;}
    public String  getPmPic2(){ return pmPic2;}
    public void setPmPic2(String  pmPic2){ this.pmPic2 = pmPic2;}
    public String  getPmPic3(){ return pmPic3;}
    public void setPmPic3(String  pmPic3){ this.pmPic3 = pmPic3;}
    public String  getPmRemark(){ return pmRemark;}
    public void setPmRemark(String  pmRemark){ this.pmRemark = pmRemark;}
    public String  getPmLegalName(){ return pmLegalName;}
    public void setPmLegalName(String  pmLegalName){ this.pmLegalName = pmLegalName;}
    public String  getPmLegalIdNum(){ return pmLegalIdNum;}
    public void setPmLegalIdNum(String  pmLegalIdNum){ this.pmLegalIdNum = pmLegalIdNum;}
    public String  getPmCertificatePic1(){ return pmCertificatePic1;}
    public void setPmCertificatePic1(String  pmCertificatePic1){ this.pmCertificatePic1 = pmCertificatePic1;}
    public String  getPmCertificatePic2(){ return pmCertificatePic2;}
    public void setPmCertificatePic2(String  pmCertificatePic2){ this.pmCertificatePic2 = pmCertificatePic2;}
    public String  getPmCertificatePic3(){ return pmCertificatePic3;}
    public void setPmCertificatePic3(String  pmCertificatePic3){ this.pmCertificatePic3 = pmCertificatePic3;}
    public String  getPmCertificatePic4(){ return pmCertificatePic4;}
    public void setPmCertificatePic4(String  pmCertificatePic4){ this.pmCertificatePic4 = pmCertificatePic4;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
}
