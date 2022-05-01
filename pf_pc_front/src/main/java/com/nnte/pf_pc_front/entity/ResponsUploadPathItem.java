package com.nnte.pf_pc_front.entity;

import com.nnte.pf_basic.entertity.AppFuncFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponsUploadPathItem {
    private String appCode;
    private String appName;
    List<AppFuncFactory.AppFuncFilePath> funcPathMap=new ArrayList<>();
}
