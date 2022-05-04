package com.nnte.pf_basic.entertity;

import com.nnte.pf_basic.component.PlateformSysParamComponent;
import com.nnte.pf_basic.mapper.workdb.sysparam.PlateformSysparam;
import lombok.Data;

@Data
public class SysParamObj {
    public SysParamObj(String paramKey, PlateformSysParamComponent.SysParamValCol valCol){
        this.paramKey = paramKey;
        this.valCol = valCol;
        isAsync = "0";//默认为静态参数
    }

    private String paramKey;
    private PlateformSysParamComponent.SysParamValCol valCol;
    private String paramName;
    private String paramValue;
    private PlateformSysparam paramObj;
    private String isAsync;
}
