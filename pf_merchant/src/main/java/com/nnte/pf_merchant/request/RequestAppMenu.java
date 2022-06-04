package com.nnte.pf_merchant.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Data;

@Data
public class RequestAppMenu {
    private Integer id; //主键ID
    @StringCheck(colName = "应用代码",maxLen = 50,nullValid = false)
    private String appCode;
    @IntegerCheck(colName = "收费类型",inVals = {1,2},nullValid = false)
    private Integer menuType; //菜单类型:1全局菜单，2：商户菜单
    private String pmCode; //商户代码:菜单类型为2商户菜单时有效
    @StringCheck(colName = "菜单内容",nullValid = false)
    private String menuContent; //菜单内容
    @IntegerCheck(colName = "模块状态",inVals = {0,1,-1},nullValid = true)
    private Integer menuStatus; //模块状态:0编辑，1已确认，-1已作废
}
