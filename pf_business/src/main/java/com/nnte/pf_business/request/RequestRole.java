package com.nnte.pf_business.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRole {
    private Integer id;
    @StringCheck(colName = "角色代码",maxLen = 20,nullValid = false)
    private String roleCode;
    @StringCheck(colName = "角色名称",maxLen = 50,nullValid = false)
    private String roleName;
    private String sysroleList;
    private String sysroleNameList;
    @IntegerCheck(colName = "角色状态",inVals = {0,1},nullValid = false)
    private Integer roleState;
    @IntegerCheck(colName = "操作类型",inVals = {1,2},nullValid = false)
    private Integer actionType;
    private String functions;   //角色功能序列
}
