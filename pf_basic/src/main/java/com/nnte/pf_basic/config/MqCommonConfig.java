package com.nnte.pf_basic.config;

import com.nnte.basebusi.annotation.RootConfigProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@RootConfigProperties(fileName = "mq-common.properties",prefix = "mq.common",superSet = true)
@Data
public class MqCommonConfig {
    private String ip;
    private String port;
    private String serverUrl;
}
