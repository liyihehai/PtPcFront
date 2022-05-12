package com.nnte.pf_pc_front.entity;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Data;

@Data
public class RequestUTIAccount {
    @IntegerCheck(colName = "主键ID",nullValid=false)
    private Integer id; //KEY id
    @StringCheck(colName = "商户代码",maxLen = 20)
    private String pmCode; //商户代码
    @StringCheck(colName = "账户代码",maxLen = 50)
    private String accountCode; //账户代码
    @StringCheck(colName = "账户密码",maxLen = 50)
    private String accountPws; //账户密码
    @StringCheck(colName = "加密类型",maxLen = 20)
    private String secType; //加密类型
    @StringCheck(colName = "接口版本",maxLen = 50)
    private String interfaceVersion; //接口版本
    @StringCheck(colName = "APP RSA公钥",maxLen = 2048)
    private String appRsaPubkey; //APP RSA公钥
    @StringCheck(colName = "APP RSA私钥",maxLen = 2048)
    private String appRsaPrikey; //APP RSA私钥
    @StringCheck(colName = "商户 RSA公钥",maxLen = 2048)
    private String merRsaPubkey; //商户 RSA公钥
    @StringCheck(colName = "商户 RSA私钥",maxLen = 2048)
    private String merRsaPrikey; //商户 RSA私钥
    @StringCheck(colName = "默认回调地址",maxLen = 200)
    private String defBackUrl; //默认回调地址
    @StringCheck(colName = "有效IP地址列表",maxLen = 100)
    private String validIpList; //有效IP地址列表
    @StringCheck(colName = "商户认证号",maxLen = 50)
    private String backId; //商户认证号
    @StringCheck(colName = "商户认证密码",maxLen = 50)
    private String backKey; //商户认证密码
    @StringCheck(colName = "账户备注",maxLen = 200)
    private String accountMemo; //账户备注
    @IntegerCheck(colName = "账户状态",nullValid=false)
    private Integer accountState; //账户状态:0新建,1开通,2暂停,3注销
}
