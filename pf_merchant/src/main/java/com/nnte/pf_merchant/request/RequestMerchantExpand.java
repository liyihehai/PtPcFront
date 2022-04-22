package com.nnte.pf_merchant.request;

import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequestMerchantExpand {
    @StringCheck(colName = "商户代码",maxLen = 10)
    private String pmCode;
    @StringCheck(colName = "行业分类",maxLen = 20)
    private String pmBusiType;
    @StringCheck(colName = "国家地区",maxLen = 20)
    private String pmCountry;
    @StringCheck(colName = "省份",maxLen = 20)
    private String pmProvince;
    @StringCheck(colName = "城市",maxLen = 20)
    private String pmCity;
    @StringCheck(colName = "区县",maxLen = 20)
    private String pmArea;
    @StringCheck(colName = "省市县",maxLen = 100)
    private String pmPcazh;
    @StringCheck(colName = "地址",maxLen = 100)
    private String pmAddress;
    @StringCheck(colName = "邮编",maxLen = 20)
    private String pmZipcode;
    @StringCheck(colName = "地理坐标",maxLen = 50)
    private String pmCoordinate;
    private Double pmLongitude;
    private Double pmLatitude;
    @StringCheck(colName = "联系姓名",maxLen = 20)
    private String pmLinkName;
    @StringCheck(colName = "联系电话",maxLen = 50)
    private String pmLinkPhone;
    @StringCheck(colName = "客服电话",maxLen = 50)
    private String pmCsrPhone;
    @StringCheck(colName = "电子邮箱",maxLen = 50)
    private String pmEmail;
    private String pmIntroduce;
    private String pmLogo;
    private String pmPic1;
    private String pmPic2;
    private String pmPic3;
    @StringCheck(colName = "商户备注",maxLen = 100)
    private String pmRemark;
    @StringCheck(colName = "法人姓名",maxLen = 50)
    private String pmLegalName;
    @StringCheck(colName = "法人证件号",maxLen = 50)
    private String pmLegalIdNum;
    private String pmCertificatePic1;
    private String pmCertificatePic2;
    private String pmCertificatePic3;
    private String pmCertificatePic4;
    private Date createTime;
    private String createBy;
    private String updateBy;
    private Date updateTime;
}
