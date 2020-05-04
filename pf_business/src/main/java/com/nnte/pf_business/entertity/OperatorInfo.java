package com.nnte.pf_business.entertity;

import lombok.Data;

/**
 * 前端使用的操作员信息
 * */
@Data
public class OperatorInfo {
    private String operatorCode;
    private String operatorName;
    private String token;
    private String expireTime;  //YYYY-MM-DD hh:mm:ss
    private String loginTime;   //YYYY-MM-DD hh:mm:ss
}
