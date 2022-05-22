package com.nnte.pf_merchant.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Data;

import java.util.Date;
@Data
public class RequestLicense {
    private Integer id; //主键ID
    @StringCheck(colName = "商户代码",maxLen = 10,nullValid = false)
    private String pmCode; //商户代码
    @StringCheck(colName = "应用代码",maxLen = 50,nullValid = false)
    private String appCode; //应用代码
    @StringCheck(colName = "模块代码",maxLen = 50,nullValid = false)
    private String moduleCode; //模块代码
    private Integer mamNo; //MAM序号:商户代码+应用代码+模块代码+序号（每次+1）
    private String moduleVersion; //模块版本
    @IntegerCheck(colName = "收费类型",inVals = {1,2},nullValid = false)
    private Integer feeType; //收费类型:1收费，2试用
    private Integer licenseState; //License状态:0编辑，1待执行，2执行中，3执行结束，4已终止，-1已删除
    @IntegerCheck(colName = "拷贝数",minVal = 1,maxVal = 50,nullValid = false)
    private Integer copyCount; //拷贝数1<= x <=50
    @StringCheck(colName = "模块代码",nullValid = false)
    private String terminals; //终端序列
    private Date startDate; //开始日期
    @IntegerCheck(colName = "月数",minVal = 1,maxVal = 72,nullValid = false)
    private Integer monthCount; //月数
    private Date endDate; //结束日期
    private String orderNo; //订单号

    private String pmName;      //商户名称
    private String pmShortName; //商户简称
    private String appName;     //应用名称
    private String moduleName;  //模块名称
}
