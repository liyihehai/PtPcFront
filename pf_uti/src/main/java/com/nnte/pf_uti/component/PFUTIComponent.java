package com.nnte.pf_uti.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.*;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.component.utiaccount.PlateformUtiAccountComponent;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_merchant.mapper.workdb.merchantUtiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_source.uti.response.ResResult;
import com.nnte.pf_source.uti.response.ResponseToken;
import com.nnte.pf_source.uti.response.UtiResponse;
import com.nnte.pf_uti.config.PFUTIConfig;
import com.nnte.pf_uti.entertity.UtiTokenDTO;
import com.nnte.pf_uti.utils.UtiJwtUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@BusiLogAttr(PFUTIConfig.loggerName)
public class PFUTIComponent extends BaseComponent {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PlateformUtiAccountComponent plateformUtiAccountComponent;

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
    public Map<String, Object> checkReqSignValid(String mid, String data, String ip) throws Exception {
        PlateformMerchantUtiAccount pmua = plateformUtiAccountComponent.getUtiAccountByAccountCode(mid);
        if (pmua == null)
            throw new BusiException(1001, "UTI商户账号不合法");
        if (StringUtils.isNotEmpty(pmua.getValidIpList()) && IpUtil.isPermited(ip, pmua.getValidIpList()))
            throw new BusiException("Ip地址不合法");
        PlateformMerchant merchant = isPBAValid(pmua);
        //logger.info("接收到待验签数据:"+data);
        String noSecData = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(data), pmua.getAppRsaPrikey()), "UTF-8");
        if (noSecData != null && noSecData.length() <= 0)
            throw new BusiException(1004, "解密失败，未取得待验签文本");
        // 	logger.info("接收到待验签数据解密:"+noSecData);
        ObjectNode noSignNode = JsonUtil.string2ObjectNode(noSecData);
        String signStr = noSignNode.get("sign").textValue();
        noSignNode.remove("sign");
        String waitSign = noSignNode.toString();
        if (!RSAUtils.verify(waitSign.getBytes("UTF-8"), pmua.getMerRsaPubkey(), signStr))
            throw new BusiException(1005, "验签失败，交易文本可能被篡改");
        Map<String, Object> ret = new HashedMap();
        ret.put("content_json", noSignNode);
        ret.put("sign", signStr);
        ret.put("content_merchant", merchant);
        ret.put("content_pmua", pmua);
        ret.put("ip",ip);
        return ret;
    }

    // 签名并返回需要发送的数据  李毅 2022/05/12
    // 未加签JSON数据先计算签名,然后将签名加入JSON数据中通过公钥加密,然后得到BASE64文本
    public String getRespSignString(PlateformMerchantUtiAccount pmua, ObjectNode respData,ResResult resResult) throws Exception {
        if (respData!=null) {
            resResult.setSuccess(true);
            resResult.setResultCode(0);
            resResult.setResponseTime((new Date()).getTime());
            resResult.setResult(respData.toString());
            resResult.setResultMessage("");
        }
        UtiResponse response = new UtiResponse();
        response.setRespText(JsonUtil.beanToJson(resResult));
        String sign = RSAUtils.sign(response.getRespText().getBytes("UTF-8"), pmua.getAppRsaPrikey());
        response.setSign(sign);
        String responseJson=JsonUtil.beanToJson(response);
        byte[] data = RSAUtils.encryptByPublicKey(responseJson.getBytes("UTF-8"), pmua.getMerRsaPubkey());
        return Base64Utils.encode(data);
    }

    //通过Token查询唯一的合作商UTI账号  李毅 2022/05/12
    public PlateformMerchantUtiAccount getPMUAByToken(String token) throws Exception {
        PlateformMerchantUtiAccount account = null;
        if (StringUtils.isNotEmpty(token)) {
            UtiTokenDTO tokenDTO = UtiJwtUtils.parseAndValidate(token);
            account = plateformUtiAccountComponent.getUtiAccountByAccountCode(tokenDTO.getUtiAccount());
        }
        if (account == null)
            throw new BusiException(1006, "通过token无法获取合作商UTI账号");
        return account;
    }

    /*
     * 接口函数,ACTION调用
     * 商户身份核验 李毅 2018/11/28
     * content:解密后的参数内容,参数内容必须符合json格式
     */
    public String merchantToken(String jsonContent,String ip,ResResult resResult) throws Exception {
        JsonNode content = JsonUtil.jsonToNode(jsonContent);
        return merchantToken(content,ip,resResult);
    }
    public String merchantToken(JsonNode content, String ip, ResResult resResult) throws Exception {
        String mid = content.get("mid").textValue();
        String seckey = content.get("seckey").textValue();
        Long timeStamp = content.get("timeStamp").longValue();
        PlateformMerchantUtiAccount puma = plateformUtiAccountComponent.getUtiAccountByMerchantCode(mid);
        PlateformMerchant merchant = isPBAValid(puma);
        if (merchant != null && seckey != null && seckey.equals(MD5Util.md5Hex(puma.getAccountPws()))) {//校验成功后,可以按需求生成Token了
            UtiTokenDTO utiTokenDTO=new UtiTokenDTO();
            utiTokenDTO.setUtiAccount(puma.getAccountCode());
            utiTokenDTO.setMerchantCode(mid);
            utiTokenDTO.setLoginIp(ip);
            utiTokenDTO.setTimeStamp(timeStamp);
            String token=UtiJwtUtils.createUtiToken(utiTokenDTO);
            ResponseToken responseToken = new ResponseToken();
            responseToken.setToken(token);
            responseToken.setCreateTime(utiTokenDTO.getCreateTime().getTime());
            responseToken.setExpireTime(utiTokenDTO.getExpirationDate().getTime());
            ObjectNode objectNode=JsonUtil.getObjectNodefromBean(responseToken);
            return getRespSignString(puma,objectNode,resResult);
        } else
            throw new BusiException(1105, "取商户信息失败(密码不符)");
    }
}
