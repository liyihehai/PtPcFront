package com.nnte.pf_pc_front.config;

import com.nnte.basebusi.annotation.RootConfigProperties;
import com.nnte.framework.base.ConfigInterface;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(prefix = "pf.pc.front")
//@PropertySource(value = "classpath:pf-pc-front-config.properties")
@RootConfigProperties(fileName = "pf-pc-front-config.properties",prefix = "pf.pc.front")
@Data
public class AppRootConfig implements ConfigInterface {
    //本地工作环境
    private String debug;
    private String localHostName;
    private String localHostAbstractName;
    private String staticRoot;
    private String uploadStaticRoot;
    //百度API-KEY
    private String baiduApiKey;
}
