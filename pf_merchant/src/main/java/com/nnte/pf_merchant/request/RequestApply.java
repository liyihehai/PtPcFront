package com.nnte.pf_merchant.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    private String creatorCode;      //创建人代码
    private String creatorName;      //创建人姓名
    private Date createTime;

    @StringCheck(colName = "短信验证码",maxLen = 6)
    private String smRandomCode;

    private String opeCode;
    private String opeName;
    private String pmCode;
    private Integer checkResult;    //审核结果:1审核通过，0审核不通过
    private String checkDesc;
    private String checkerCode;
    private String pmShortName;
    private String checkerName;
    private String confirmCode;
    private String confirmName;
    private String createTimeRange;
    private String lockTimeRange;
    private String checkTimeRange;

    @IntegerCheck(colName = "操作类型",inVals = {1,2},nullValid = false)
    private Integer actionType;     //1：新增，2：编辑
}
