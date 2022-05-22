package com.nnte.pf_basic.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLibraryConfig {
    public static String Lib_ModelName = "PF_APP";
    @Data
    @AllArgsConstructor
    public static class LibType{
        private String typeCode;//字典分类代码
        private String typeName;//字典分类名称
    }
    @Getter
    private static Map<String,LibType> libTypeMap = new HashMap<>();
    @Getter
    private static List<LibType> libTypeList = new ArrayList<>();
    {
        libTypeList.add(new LibType("0000","系统应用"));
        libTypeList.add(new LibType("0001","行业分类"));//classification of professions
        libTypeList.add(new LibType("0002","证件类型"));
        libTypeList.add(new LibType("Y001","商业应用"));

        for(LibType libType:libTypeList)
            libTypeMap.put(libType.typeCode,libType);
    }
    public static LibType getLibTypeByCode(String libTypeCode){
        return libTypeMap.get(libTypeCode);
    }
}
