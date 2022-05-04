package com.nnte.pf_pc_front.config;

import com.nnte.basebusi.annotation.RootConfigProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@RootConfigProperties(fileName = "pf-pc-front-config.properties",prefix = "pf.pc.front")
@Data
public class AppRootConfig {
    //本地工作环境
    private String debug;
    private String localHostName;
    private String localHostAbstractName;
    private String staticRoot;
    private String uploadStaticRoot;
    private String uploadFileServiceURL;    //文件上传服务URL
    //百度API-KEY
    private String baiduApiKey;
}
