package com.nnte.pf_basic.annotation;

import com.nnte.pf_basic.component.PFServiceCommonMQ;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
/***
 * 平台服务MQ处理注解 李毅 2022/4/27
 * 加载方法为：
 *
 * 使用该注解的组件一定继承了PFServiceCommonMQ.PFServiceMQInterface接口，
 * 本注解为该接口提供自动注册时需要的消息类型msgType
 */
public @interface PFServiceMQAnnotation {
    PFServiceCommonMQ.MsgType value();
}
