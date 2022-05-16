package com.nnte.pf_uti.component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.component.ReportModuleComponent;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.component.utiaccount.PlateformUtiAccountComponent;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_source.uti.UtiBodySign;
import com.nnte.pf_source.uti.request.RequestReportModule;
import com.nnte.pf_source.uti.request.RequestToken;
import com.nnte.pf_source.uti.request.UtiRequest;
import com.nnte.pf_source.uti.response.ResResult;
import com.nnte.pf_source.uti.response.ResponseReportModule;
import com.nnte.pf_source.uti.response.ResponseToken;
import com.nnte.pf_uti.config.PFUTIConfig;
import com.nnte.pf_uti.entertity.UtiTokenDTO;
import com.nnte.pf_uti.utils.UtiJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@BusiLogAttr(PFUTIConfig.loggerName)
public class PFUTIComponent extends BaseComponent {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PlateformUtiAccountComponent plateformUtiAccountComponent;
    @Autowired
    private ReportModuleComponent reportModuleComponent;

    private ThreadLocal<PlateformMerchantUtiAccount> thread_UtiAccount = new ThreadLocal<>();
    private ThreadLocal<ResResult> thread_ResResult = new ThreadLocal<>();
    private ThreadLocal<UtiRequest> thread_UtiRequest = new ThreadLocal<>();

    public PlateformMerchantUtiAccount getThreadUtiAccount(){return thread_UtiAccount.get();}
    public ResResult getThreadResResult(){return thread_ResResult.get();}
    public void setThreadResResult(ResResult resResult){thread_ResResult.set(resResult);}
    public void setThreadUtiRequest(UtiRequest utiRequest){thread_UtiRequest.set(utiRequest);}
    public UtiRequest getThreadUtiRequest(){return thread_UtiRequest.get();}

    //检查商户业务账户是否可以执行业务 李毅 2018/11/28
    public PlateformMerchant isPBAValid(PlateformMerchantUtiAccount pmua) throws Exception {
        if (pmua == null || pmua.getId() == null || pmua.getId() <= 0)
            throw new BusiException(1102, "账户对象不合法");
        //校验商户对象状态
        PlateformMerchant merchant = plateformMerchanComponent.getMerchantByCode(pmua.getPmCode());
        if (merchant == null)
            throw new BusiException(1002, "没找到指定代码的商户");
        if (!merchant.getPmState().equals(1))
            throw new BusiException(1103, "商户状态不可服务");
        //校验到期时间
        Date curDate = new Date();
        Date expDate = pmua.getBusiExpireTime();
        if (expDate == null || expDate.before(curDate))
            throw new BusiException(1106, "到期时间限制");
        //校验账户状态
        if (!pmua.getAccountState().equals(1))
            throw new BusiException(1108, "商户业务未处于开通状态");
        return merchant;
    }

    // 验证请求的参数是否合法,拦截器调用,用于检查加密及验签 李毅 2018/11/27
    // @data:商户端传来的经过加密然后BASE64处理过的文本数据
    // 如果验签成功,MAP中content_json返回JSON报文对象
    public void checkReqSignValid(String mid, String data, String ip,Class<? extends UtiRequest> clazz) throws Exception {
        PlateformMerchantUtiAccount pmua = plateformUtiAccountComponent.getUtiAccountByAccountCode(mid);
        if (pmua == null)
            throw new BusiException(1001, "UTI商户账号不合法");
        thread_UtiAccount.set(pmua);
        if (StringUtils.isNotEmpty(pmua.getValidIpList()) && IpUtil.isPermited(ip, pmua.getValidIpList()))
            throw new BusiException("Ip地址不合法");
        isPBAValid(pmua);
        //接收到待验签数据,格式应该是:
        String noSecData = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(data), pmua.getAppRsaPrikey()), "UTF-8");
        if (noSecData != null && noSecData.length() <= 0)
            throw new BusiException(1004, "解密失败，未取得待验签文本");
        // 	logger.info("接收到待验签数据解密:"+noSecData);
        UtiBodySign bodySign = JsonUtil.jsonToBean(noSecData,UtiBodySign.class);
        String signStr = bodySign.getSign();
        if (!RSAUtils.verify(bodySign.getBody().getBytes("UTF-8"), pmua.getMerRsaPubkey(), signStr))
            throw new BusiException(1005, "验签失败，交易文本可能被篡改");
        UtiRequest request = JsonUtil.jsonToBean(bodySign.getBody(),clazz);
        if (request==null)
            throw new BusiException(1006, "不能解析报文数据");
        setThreadUtiRequest(request);
    }

    // 签名并返回需要发送的数据  李毅 2022/05/12
    // 未加签JSON数据先计算签名,然后将签名加入JSON数据中通过公钥加密,然后得到BASE64文本
    public String getRespSignString(PlateformMerchantUtiAccount pmua, ObjectNode respData) throws Exception {
        ResResult resResult = getThreadResResult();
        if (respData!=null) {
            resResult.setSuccess(true);
            resResult.setResultCode(0);
            resResult.setResponseTime((new Date()).getTime());
            resResult.setResult(respData.toString());
            resResult.setResultMessage("");
        }
        UtiBodySign response = new UtiBodySign();
        response.setBody(JsonUtil.beanToJson(resResult));
        String sign = RSAUtils.sign(response.getBody().getBytes("UTF-8"), pmua.getAppRsaPrikey());
        response.setSign(sign);
        String responseJson=JsonUtil.beanToJson(response);
        byte[] data = RSAUtils.encryptByPublicKey(responseJson.getBytes("UTF-8"), pmua.getMerRsaPubkey());
        return Base64Utils.encode(data);
    }

    //通过Token查询唯一的合作商UTI账号  李毅 2022/05/12
    public String getPMUAByToken(String token) throws Exception {
        try {
            UtiTokenDTO tokenDTO = UtiJwtUtils.parseAndValidate(token);
            return tokenDTO.getUtiAccount();
        }catch (Exception e) {
            throw new BusiException(1006, "通过token无法获取合作商UTI账号:"+e.getMessage());
        }
    }

    /*
     * 接口函数,ACTION调用
     * 商户身份核验 李毅 2018/11/28
     * content:解密后的参数内容,参数内容必须符合json格式
     */
    public String merchantToken(String ip) throws Exception {
        RequestToken requestToken = (RequestToken)getThreadUtiRequest();
        PlateformMerchantUtiAccount puma = thread_UtiAccount.get();
        if (requestToken.getSeckey() != null && requestToken.getSeckey().equals(MD5Util.md5Hex(puma.getAccountPws()))) {//校验成功后,可以按需求生成Token了
            UtiTokenDTO utiTokenDTO=new UtiTokenDTO();
            utiTokenDTO.setUtiAccount(puma.getAccountCode());
            utiTokenDTO.setMerchantCode(requestToken.getMid());
            utiTokenDTO.setLoginIp(ip);
            utiTokenDTO.setTimeStamp(requestToken.getTimeStamp());
            String token=UtiJwtUtils.createUtiToken(utiTokenDTO);
            ResponseToken responseToken = new ResponseToken();
            responseToken.setToken(token);
            responseToken.setCreateTime(utiTokenDTO.getCreateTime().getTime());
            responseToken.setExpireTime(utiTokenDTO.getExpirationDate().getTime());
            ObjectNode objectNode=JsonUtil.getObjectNodefromBean(responseToken);
            return getRespSignString(puma,objectNode);
        } else
            throw new BusiException(1105, "取商户信息失败(密码不符)");
    }

    public String reportModule() throws Exception{
        RequestReportModule requestReportModule = (RequestReportModule)getThreadUtiRequest();
        PlateformMerchantUtiAccount puma = thread_UtiAccount.get();
        ResponseReportModule responseReportModule = reportModuleComponent.reportModule(requestReportModule.getModuleItemList(),puma);
        ObjectNode objectNode=JsonUtil.getObjectNodefromBean(responseReportModule);
        return getRespSignString(puma,objectNode);
    }
}
