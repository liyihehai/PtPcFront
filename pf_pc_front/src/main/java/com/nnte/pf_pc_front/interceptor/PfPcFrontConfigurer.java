package com.nnte.pf_pc_front.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PfPcFrontConfigurer implements WebMvcConfigurer {
    @Autowired
    private PfPcFrontMainInterceptor pfPcFrontMainInterceptor;
    @Autowired
    private PfPcFrontNormalInterceptor pfPcFrontNormalInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pfPcFrontMainInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/**/sysRepairing","/**/error",
                        "/**/resources/**");//添加不拦截路径
        //登录拦截的管理器
        registry.addInterceptor(pfPcFrontNormalInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/**/sysRepairing",
                        "/**/login","/**/loginCheck",
                        "/**/error","/**/resources/**");//添加不拦截路径
    }
}
