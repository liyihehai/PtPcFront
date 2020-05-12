package com.nnte.pf_business.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestFunc {
    @StringCheck(colName = "菜单代码",maxLen = 10)
    private String menuCode;
    @StringCheck(colName = "功能代码",maxLen = 10)
    private String funCode;
    @StringCheck(colName = "功能名称",maxLen = 50)
    private String funName;
    @StringCheck(colName = "功能路径",maxLen = 200)
    private String funPath;
    @StringCheck(colName = "功能参数",maxLen = 200)
    private String funParam;
    @IntegerCheck(colName = "功能状态")
    private Integer funState;
    private Integer opeFunction;        //0或null表示非操作员功能，1表示操作员功能
    private Integer roleFunction;       //0或null表示非角色功能，1表示角色功能
    private Integer sysRoleFunction;    //0或null表示非系统角色功能，1表示系统角色功能
}
