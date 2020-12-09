package com.nnte.pf_business.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestApply {
    private Integer id;
    @StringCheck(colName = "商户名称",maxLen = 100,nullValid = false)
    private String pmName;
    @IntegerCheck(colName = "验证方式",inVals = {1,2},nullValid = false)
    private Integer confirmType;
    @StringCheck(colName = "电话号码",maxLen = 15)
    private String applyPhone;
    @StringCheck(colName = "邮箱号码",maxLen = 50)
    private String applyEmail;
    @IntegerCheck(colName = "公司或个人",inVals = {1,2},nullValid = false)
    private Integer pmCompanyPerson;
    @IntegerCheck(colName = "申请方式",inVals = {1,2,3,4},nullValid = false)
    private Integer applyWays;
    @StringCheck(colName = "申请人代码",maxLen = 20)
    private String applyerCode;
    @StringCheck(colName = "申请人姓名",maxLen = 50)
    private String applyerName;
    private String applyContent;
    @StringCheck(colName = "申请备注",maxLen = 200)
    private String applyMemo;
    @IntegerCheck(colName = "申请状态",inVals = {-1,0,1,2,3,4},nullValid = false)
    private Integer applyState;

    @StringCheck(colName = "短信验证码",maxLen = 6)
    private String smRandomCode;

    private String opeCode;
    private String opeName;
    private String pmCode;
    private String checkDesc;
    private String checkerCode;
    private String pmShortName;
    private String checkerName;
    private String createTimeRange;
    private String lockTimeRange;
    private String checkTimeRange;

    @IntegerCheck(colName = "操作类型",inVals = {1,2},nullValid = false)
    private Integer actionType;     //1：新增，2：编辑
}
