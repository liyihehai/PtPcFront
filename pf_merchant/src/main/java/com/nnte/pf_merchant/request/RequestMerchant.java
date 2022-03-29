package com.nnte.pf_merchant.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMerchant {
    private Integer id;
    @StringCheck(colName = "商户代码",maxLen = 20)
    private String pmCode;
    @StringCheck(colName = "商户名称",maxLen = 100,nullValid = false)
    private String pmName;
    @StringCheck(colName = "商户简称",maxLen = 50)
    private String pmShortName;
    @StringCheck(colName = "申请邮箱",maxLen = 50)
    private String applyEmail;
    @IntegerCheck(colName = "公司或个人",inVals = {1,2},nullValid = false)
    private Integer pmCompanyPerson;
    @IntegerCheck(colName = "商户状态",inVals = {-2,-1,0,1,2},nullValid = false)
    private Integer pmState;
    private String createTimeRange;
}
