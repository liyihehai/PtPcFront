package com.nnte.pf_pc_front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.nnte")
public class PcPlateformApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PcPlateformApplication.class, args);
    }
}
