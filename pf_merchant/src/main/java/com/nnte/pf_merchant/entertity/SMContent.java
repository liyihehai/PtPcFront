package com.nnte.pf_merchant.entertity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 短信验证对象
 * */
public class SMContent {
    private String phoneNo;     //电话号码
    private String content;     //短信内容
    private String sendTime;    //短信发送时间 yyyy-MM-dd hh:mm:ss
    private Integer seconds;    //存在秒数
    private String smTag;       //短信区分标志
}
