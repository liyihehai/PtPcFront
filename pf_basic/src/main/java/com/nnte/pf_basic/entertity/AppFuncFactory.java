package com.nnte.pf_basic.entertity;

import lombok.Data;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

@Data
public class AppFuncFactory {
    private String appCode;
    private String appName;
    private Map<String,AppFuncFilePath> funcPathMap=new HashedMap();
    @Data
    public static class AppFuncFilePath{
        private String appCode; //应用代码
        private String FuncCode;//功能代码
        private String path;    //绝对路径
        private String pathMask;//路径掩码,返回路径时从绝对路径中截断路径掩码返回
    }
}
