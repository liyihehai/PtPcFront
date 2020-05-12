package com.nnte.pf_business.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestOpe {
    @IntegerCheck(colName = "操作员类型",inVals = {1,2,3},nullValid = false)
    private Integer opeType;
    private String opeTypeName;
    @StringCheck(colName = "操作员代码",maxLen = 20,nullValid = false)
    private String opeCode;
    @StringCheck(colName = "操作员名称",maxLen = 50,nullValid = false)
    private String opeName;
    @StringCheck(colName = "操作员手机",maxLen = 20,nullValid = false)
    private String opeMobile;
    @IntegerCheck(colName = "操作员状态",inVals = {0,1,2,3},nullValid = false)
    private Integer opeState;
    private String opeStateName;
    @IntegerCheck(colName = "操作类型",inVals = {1,2},nullValid = false)
    private Integer actionType;
    private String setAimPwd;   //设置目标密码
    private String userRoles;   //用户角色序列,","分割的角色Code
    private String functions;   //操作员功能序列,","分割的function id
}
