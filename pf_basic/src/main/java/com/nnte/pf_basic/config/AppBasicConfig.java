package com.nnte.pf_basic.config;

import com.nnte.basebusi.annotation.RootConfigProperties;
import com.nnte.basebusi.entity.DBSrcConfig;
import com.nnte.framework.annotation.MybatisXmlMapper;
import org.springframework.stereotype.Component;

@Component
@RootConfigProperties(fileName = "work-dbsrc-config.properties",prefix = "work.dbsrc",superSet = true)
@MybatisXmlMapper("com.nnte.pf_basic.mapper.workdb")
public class AppBasicConfig extends DBSrcConfig {
    public static final String DB_Name = "PfWorkDB";
    public static final String JarLoggerName = "PfBasic";
}
