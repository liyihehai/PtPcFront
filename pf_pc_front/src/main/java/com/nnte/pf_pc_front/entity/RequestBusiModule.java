package com.nnte.pf_pc_front.entity;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Data;

@Data
public class RequestBusiModule {
    @IntegerCheck(colName = "主键ID",nullValid=false)
    private Integer id;
    @StringCheck(colName = "模块代码",maxLen = 20)
    private String moduleCode;
    @StringCheck(colName = "模块名称",maxLen = 100)
    private String moduleName;
    @StringCheck(colName = "模块说明",maxLen = 256)
    private String moduleDesc;
    @StringCheck(colName = "最新版本",maxLen = 50)
    private String currentVersion;
    @IntegerCheck(colName = "模块类型",inVals = {1,2},nullValid=false)
    private Integer moduleType; //模块类型:1框架模块，2普通模块
    private Integer moduleStatus;
}
