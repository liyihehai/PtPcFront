package com.nnte.pf_basic.entertity;

import lombok.Data;

@Data
public class ReportModule {
    private String appCode;         //应用代码
    private String moduleCode;      //模块代码
    private String moduleName;      //模块名称
    private Integer frameModule;    //框架模块:1框架模块，非1商业模块
}
