package com.nnte.pf_business.entertity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 邮件验证对象
 * */
public class EmailContent {
    private Long id;            //记录ID
    private String email;       //邮件地址
    private String randomCode;  //随机码
    private String title;       //邮件抬头
    private String content;     //邮件文本内容
}
