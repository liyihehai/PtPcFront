package com.nnte.pf_basic.mapper.workdb.utiAccount;
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

import java.util.Date;
/**
 * auto code,do not modify <2022-05-23 10:09:53>
 */
public class PlateformMerchantUtiAccount extends BaseModel {
    @DBPKColum private Integer id; //KEY id
    private String pmCode; //商户代码
    private String accountCode; //账户代码
    private String accountPws; //账户密码
    private String secType; //加密类型
    private String interfaceVersion; //接口版本
    private String appRsaPubkey; //APP RSA公钥
    private String appRsaPrikey; //APP RSA私钥
    private String merRsaPubkey; //商户 RSA公钥
    private String merRsaPrikey; //商户 RSA私钥
    private String defBackUrl; //默认回调地址
    private String validIpList; //有效IP地址列表
    private String backId; //商户认证号
    private String backKey; //商户认证密码
    private String accountMemo; //账户备注
    private Integer accountState; //账户状态:0新建,1开通,2暂停,3注销
    private Date busiOpenTime; //业务开通时间
    private Date busiExpireTime; //业务到期时间
    private String createBy; //创建人
    private Date createTime; //创建时间
    private String updateBy; //更改人
    private Date updateTime; //最后更改时间
    private String terminals; //终端:[{term:xxx,ip:xxx,name:xxx},{term:xxx}...]

    public PlateformMerchantUtiAccount(){}

    public Integer  getId(){ return id;}
    public void setId(Integer  id){ this.id = id;}
    public String  getPmCode(){ return pmCode;}
    public void setPmCode(String  pmCode){ this.pmCode = pmCode;}
    public String  getAccountCode(){ return accountCode;}
    public void setAccountCode(String  accountCode){ this.accountCode = accountCode;}
    public String  getAccountPws(){ return accountPws;}
    public void setAccountPws(String  accountPws){ this.accountPws = accountPws;}
    public String  getSecType(){ return secType;}
    public void setSecType(String  secType){ this.secType = secType;}
    public String  getInterfaceVersion(){ return interfaceVersion;}
    public void setInterfaceVersion(String  interfaceVersion){ this.interfaceVersion = interfaceVersion;}
    public String  getAppRsaPubkey(){ return appRsaPubkey;}
    public void setAppRsaPubkey(String  appRsaPubkey){ this.appRsaPubkey = appRsaPubkey;}
    public String  getAppRsaPrikey(){ return appRsaPrikey;}
    public void setAppRsaPrikey(String  appRsaPrikey){ this.appRsaPrikey = appRsaPrikey;}
    public String  getMerRsaPubkey(){ return merRsaPubkey;}
    public void setMerRsaPubkey(String  merRsaPubkey){ this.merRsaPubkey = merRsaPubkey;}
    public String  getMerRsaPrikey(){ return merRsaPrikey;}
    public void setMerRsaPrikey(String  merRsaPrikey){ this.merRsaPrikey = merRsaPrikey;}
    public String  getDefBackUrl(){ return defBackUrl;}
    public void setDefBackUrl(String  defBackUrl){ this.defBackUrl = defBackUrl;}
    public String  getValidIpList(){ return validIpList;}
    public void setValidIpList(String  validIpList){ this.validIpList = validIpList;}
    public String  getBackId(){ return backId;}
    public void setBackId(String  backId){ this.backId = backId;}
    public String  getBackKey(){ return backKey;}
    public void setBackKey(String  backKey){ this.backKey = backKey;}
    public String  getAccountMemo(){ return accountMemo;}
    public void setAccountMemo(String  accountMemo){ this.accountMemo = accountMemo;}
    public Integer  getAccountState(){ return accountState;}
    public void setAccountState(Integer  accountState){ this.accountState = accountState;}
    public Date  getBusiOpenTime(){ return busiOpenTime;}
    public void setBusiOpenTime(Date  busiOpenTime){ this.busiOpenTime = busiOpenTime;}
    public Date  getBusiExpireTime(){ return busiExpireTime;}
    public void setBusiExpireTime(Date  busiExpireTime){ this.busiExpireTime = busiExpireTime;}
    public String  getCreateBy(){ return createBy;}
    public void setCreateBy(String  createBy){ this.createBy = createBy;}
    public Date  getCreateTime(){ return createTime;}
    public void setCreateTime(Date  createTime){ this.createTime = createTime;}
    public String  getUpdateBy(){ return updateBy;}
    public void setUpdateBy(String  updateBy){ this.updateBy = updateBy;}
    public Date  getUpdateTime(){ return updateTime;}
    public void setUpdateTime(Date  updateTime){ this.updateTime = updateTime;}
    public String  getTerminals(){ return terminals;}
    public void setTerminals(String  terminals){ this.terminals = terminals;}
}
