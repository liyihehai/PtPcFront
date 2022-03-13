package com.nnte.pf_pc_front.config;

import com.nnte.basebusi.entity.DBSrcConfig;
import com.nnte.pf_business.mapper.workdb.DBPlateform;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "work.dbsrc")
@PropertySource(value = "classpath:work-dbsrc-config.properties")
@Component
public class WorkDBConfig extends DBSrcConfig {
    public final static String DB_NAME = DBPlateform.DB_NAME; //定义数据源名称
}
