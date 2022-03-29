package com.nnte.pf_basic.config;

import com.nnte.basebusi.entity.DBSrcConfig;
import com.nnte.framework.annotation.MybatisXmlMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "work.dbsrc")
@PropertySource(value = "classpath:work-dbsrc-config.properties")
@MybatisXmlMapper("com.nnte.pf_basic.mapper.workdb")
public class AppBasicConfig extends DBSrcConfig {
    public static final String DB_Name = "PfWorkDB";
}
