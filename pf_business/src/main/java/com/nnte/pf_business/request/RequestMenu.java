package com.nnte.pf_business.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestMenu {
    @StringCheck(colName = "菜单代码",maxLen = 10)
    private String menuCode;
    @StringCheck(colName = "菜单名称",maxLen = 50,nullValid = false)
    private String menuName;
    @IntegerCheck(colName = "菜单等级")
    private Integer menuClass;
    @StringCheck(colName = "上级菜单代码",maxLen = 10)
    private String parentMenuCode;
    @IntegerCheck(colName = "菜单状态")
    private Integer menuState;
    @StringCheck(colName = "菜单路径",maxLen = 128)
    private String menuPath;
    @StringCheck(colName = "菜单图标",maxLen = 64)
    private String menuIcon;
}
