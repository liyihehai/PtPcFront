package com.nnte.pf_uti_client.component;

import com.nnte.pf_source.uti.MerchantLicense;
import com.nnte.pf_source.uti.UtiBodySign;
import com.nnte.pf_source.uti.request.*;
import com.nnte.pf_source.uti.response.ResResult;
import com.nnte.pf_source.uti.response.ResponseReportModule;
import com.nnte.pf_source.uti.response.ResponseToken;
import com.nnte.pf_uti_client.config.UtiClientConfig;
import com.nnte.pf_uti_client.utils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtiClient {
    private UtiClientConfig config = new UtiClientConfig();
    private ResponseToken tokenObject;

    public void initUtiClient(String merchantId,
                              String accountCode,String terminal, String accountPws,
                              String merchantPriKey, String merchantPubKey,
                              String appPubKey, String utiServer) {
        config.setMerchantId(merchantId);
        config.setAccountCode(accountCode);
        config.setTerminal(terminal);
        config.setAccountPws(accountPws);
        config.setMerchantPriKey(merchantPriKey);
        config.setMerchantPubKey(merchantPubKey);
        config.setAppPubKey(appPubKey);
        config.setUtiServer(utiServer);
    }

    // @data:商户端传来的经过加密然后BASE64处理过的文本数据
    // 如果验签成功,MAP中返回JSON报文对象
    public ResResult checkResponseSignValid(String data) throws Exception {
        String noSecData = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(data), config.getMerchantPriKey()), "UTF-8");
        if (noSecData != null && noSecData.length() <= 0)
            throw new Exception("解密失败，未取得待验签文本");
        UtiBodySign response=JsonUtil.jsonToBean(noSecData,UtiBodySign.class);
        String signStr = response.getSign();
        String waitSign = response.getBody();
        if (!RSAUtils.verify(waitSign.getBytes("UTF-8"), config.getAppPubKey(), signStr))
            throw new Exception("验签失败，交易文本可能被篡改");
        return JsonUtil.jsonToBean(waitSign,ResResult.class);
    }

    // 签名并返回需要发送的数据  李毅 2022/05/12
    // 未加签JSON数据先计算签名,然后将签名加入JSON数据中通过公钥加密,然后得到BASE64文本
    public String getRequestSignString(UtiRequest request) throws Exception {
        String srcStr = JsonUtil.beanToJson(request);
        String sign = RSAUtils.sign(srcStr.getBytes("UTF-8"), config.getMerchantPriKey());
        UtiBodySign bodySign = new UtiBodySign();
        bodySign.setBody(srcStr);
        bodySign.setSign(sign);
        byte[] data = RSAUtils.encryptByPublicKey(JsonUtil.beanToJson(bodySign).getBytes("UTF-8"), config.getAppPubKey());
        return Base64Utils.encode(data);
    }

    private String getRequestUrl(UtiRequest request) throws Exception {
        UtiURL utiURL = request.getClass().getAnnotation(UtiURL.class);
        if (utiURL == null)
            throw new Exception("not find class " + request.getClass().getSimpleName() + " UtiURL annotation");
        String url = utiURL.url();
        return url;
    }

    public <T> T postTrade(UtiRequest request, String token,Class<T> clazz) throws Exception {
        String req_data = getRequestSignString(request);
        String url = StringUtils.pathAppend(config.getUtiServer(), getRequestUrl(request));
        if (token==null)
            url = url + "?mid=" + config.getAccountCode() + "&req_data=" + UrlEncodeUtil.UrlEncode(req_data);
        else
            url = url + "?token=" + token + "&req_data=" + UrlEncodeUtil.UrlEncode(req_data);
        String retString = HttpUtil.sendPostURL(url);
        ResResult resResult = checkResponseSignValid(retString);
        if (resResult == null)
            throw new Exception("不能解析返回报文");
        if (!resResult.isSuccess())
            throw new Exception(resResult.getResultMessage());
        Long times = resResult.getResponseTime()-resResult.getRequestTime();
        System.out.println("times=" + times);
        return JsonUtil.jsonToBean(resResult.getResult(), clazz);
    }

    /*
     * 商户身份核验:https://[domain]/uti/basic/merchantToken
     */
    private ResponseToken requestToken() throws Exception {
        RequestToken requestToken = new RequestToken();
        requestToken.setMid(config.getMerchantId());
        requestToken.setAccount(config.getAccountCode());
        requestToken.setTerminal(config.getTerminal());
        requestToken.setSeckey(MD5Util.md5Hex(config.getAccountPws()));
        requestToken.setTimeStamp((new Date()).getTime());
        ResponseToken resResult = postTrade(requestToken,null, ResponseToken.class);
        return resResult;
    }

    public synchronized String getToken() throws Exception{
        if (tokenObject==null){
            tokenObject = requestToken();
        }
        Date now = new Date();
        Date checkTime = DateUtils.addDateMinute(now,30);//默认30分钟
        Date expireTime = new Date(tokenObject.getExpireTime());
        if (checkTime.after(expireTime)){
            tokenObject = requestToken();
        }
        return tokenObject.getToken();
    }

    /*
     * 报告模块并获取许可:https://[domain]/uti/basic/reportModule
     */
    private ResponseReportModule reportModule(List<ReportModuleItem> moduleItemList) throws Exception {
        RequestReportModule requestReportModule = new RequestReportModule();
        requestReportModule.setModuleItemList(moduleItemList);
        requestReportModule.setTimeStamp((new Date()).getTime());
        ResponseReportModule result = postTrade(requestReportModule,getToken(), ResponseReportModule.class);
        return result;
    }

    public static void main(String[] args) {

        try {
            UtiClient client = new UtiClient();
            String terminal = "term:kOe5PH8AdZ";
            String appPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqAU9C6YtSKUSWMAb+Pa4D/4TLRCuBRYJ+tP0TZXrz466tBWtw2jeVPwEduJbzNh9y0+1vSqbodzy5l4kbTAEtBL48vmlHbsd3ATsTAy3Lby3/BGt5rxj21Q4Tym29S88SI2HffSl/HC6xWS+rj97eh2rreESVigPbmt3mrHzHTwIDAQAB";
            String merPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD2gkvHEae5VCFw8Xc45spbbp8ZJwHPRoD+yWBercYLHupwyTH6OkEBwYYr2hIiSpgY85eRBlJfiObZEGd5VWSRnt9bJZ/qOu29hAIqKmfjnbwrYQrE6jHSZgmD479BXEBc8yETAam+c29PrLiEcRl0Lrn85f+vchkM65ulY/ZnCwIDAQAB";
                              //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXfkBFwsR6SPc7wBHqerlL7Y+goao8U1qtzcvldzeAI4NZf/GzboNZplc9jH54EMqf5uVc5SmuTHeQeawErnT+n4u5A8V8JspPht41GyY+fVEEqirCceK1q8JllKdmq0uER9QodM0+iBzYKnPd9FgKITw0X6/1B6WWjwwmbjHJxwIDAQAB
            String merPriKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPaCS8cRp7lUIXDxdzjmyltunxknAc9GgP7JYF6txgse6nDJMfo6QQHBhivaEiJKmBjzl5EGUl+I5tkQZ3lVZJGe31sln+o67b2EAioqZ+OdvCthCsTqMdJmCYPjv0FcQFzzIRMBqb5zb0+suIRxGXQuufzl/69yGQzrm6Vj9mcLAgMBAAECgYBt35edDD6A+YHKUKY+Q4gwhrb9RAindZprdiCxMq1SKZ5/VP4ktTBicvq1swZgKHVwsap95p5ynJegapJkX25qJb6mHDcStQwnaJ67DM5kztEibfiKK+91Ut77Mx+aqp9k4iZkJoew4DYVZq+fNPENie+uwoiuIruWSBBvauHYmQJBAP0ca+Q04j2aAsgDUHP+XUHIuKJEwVVDBzKpX0WcLk5sW97jo0t+6dpEwxwUuBF+vT/c8UD+Wv+OmE/05rvk49cCQQD5UpVrNg35ZLEziGbJsFBI2KnwV4k3lXP9L6k0ZPrvGgXQxg/ND076J+Enyg2os3xWlxGsEuDAqfqVZ7zHPS/tAkBkay8+9eBAkmbCFdaIfoVfQRRHhMGzYwlrBxl+U41nncQBNQ0nOa3r49YIwQuaHU9WFAE/SOqvc1JofoBAUXo3AkEAvtXSXqySR6kl5U7rdkdJPGjysm8eLXhsvofjmKIOXENr3iAr6gZkqh8qPN1wVQeFDXY2ZaJRe8bfccpkeZBBBQJAGFOJfkggKyHmZsmJekRbuv09jLgOGW9SdOGDXLVlribOnjXlaJQhrfTyoSlxjLtzq4a/gErewu1p9Z/kGCLPKg==";
                              //MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJd+QEXCxHpI9zvAEep6uUvtj6ChqjxTWq3Ny+V3N4Ajg1l/8bNug1mmVz2MfngQyp/m5VzlKa5Md5B5rASudP6fi7kDxXwmyk+G3jUbJj59UQSqKsJx4rWrwmWUp2arS4RH1Ch0zT6IHNgqc930WAohPDRfr/UHpZaPDCZuMcnHAgMBAAECgYEAhRozBFf84ugq+P9nfln3ZgPClsKT3M7rtBtF00XsqJQLOt6UES+/Dkx9CCHhmEJAlT98NCNQfMjIoFKW1cImHZYZ/AxLiWz54QGVf7vfQDq5JdSr20VXkqUOSTolto1ZRqQkNmODDRyg2m3xvq8wp4yshY3nDj5Wvsqr1FXiu/kCQQDpl+ju/gu4ZqUEKiylURnv/bObEcY5dxm0SpWugRcHyCtE3o/gLgLOofDr7KX/4D5It8btWKydLMS0gMfZ6u1tAkEApgZMWh6MlbrWTPxKgK0dpKoxFJFFBXP/rUCzB6CsO8J2PvrUuLvze/5IvnVg+AFhbnJJhZSECKhS30bLskuXgwJAev8t+4lxCmuhwAuk7ndBvQhNJf257lA0DKapIfV+9u4DOoQmJdiUSdEjVlaJIa4lnYyHBjqGyUvlV1Xn5Wq6EQJBAIQO5HKWdBqxN76auyQpDzgoS8vhVTZmM0v696ysh/Ms1eN4nvWmQqEw/WnJce0zI+23KHYBURiV0v1YkbPrXsMCQC8UAFbElitGj50zUGXD7DKDR4hpgz6gjMJTbGoZEk4o8kFUcMQu623EMKPLkMlj+KsmgFb3sz8zAr6aqLQcEeY=
            client.initUtiClient("0001", "Brc0r3W81ULSRgjFc1hq", terminal,"m1b3zaW3KiSM3s55A3q2",
                    merPriKey, merPubKey, appPubKey, "http://127.0.0.1:9002/pfservice");

            List<ReportModuleItem> list = new ArrayList<>();
            ReportModuleItem item = new ReportModuleItem();
            item.setAppCode("MerGroupBackend");
            item.setModuleCode("MerGroupBackend");
            item.setModuleVersion("1.0.1");
            list.add(item);
            ResponseReportModule responseReportModule=client.reportModule(list);
            if (responseReportModule!=null){
                List<MerchantLicense> llist=responseReportModule.getLicenseItemList();
                if (llist!=null && llist.size()>0){
                    for(MerchantLicense license:llist){
                        System.out.println(JsonUtil.beanToJson(license));
                    }
                }
            }
            /*
            String token = client.getToken();
            System.out.println("token=" + token);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        try {
            Map<String,Object> keyMap=RSAUtils.genKeyPair(1024);
            String appPubKey=RSAUtils.getPublicKey(keyMap);
            String appPriKey=RSAUtils.getPrivateKey(keyMap);
            keyMap=RSAUtils.genKeyPair(1024);
            String merPubKey=RSAUtils.getPublicKey(keyMap);
            String merPriKey=RSAUtils.getPrivateKey(keyMap);
            ObjectNode requestNode = JsonUtil.newJsonNode();
            requestNode.put("ver", "2");
            requestNode.put("mid", "0001");
            requestNode.put("account", "8pDKtfdvnXYgaEzGeC6m");
            requestNode.put("seckey", "mA5xuft6oBskk1KVVTXP");
            requestNode.put("timeStamp", (new Date()).getTime());
            System.out.println("requestNode="+requestNode.toString());
            String srcStr = requestNode.toString();
            String sign = RSAUtils.sign(srcStr.getBytes("UTF-8"), merPriKey);
            requestNode.put("sign", sign);
            byte[] data = RSAUtils.encryptByPublicKey(requestNode.toString().getBytes("UTF-8"), appPubKey);
            String req_data = Base64Utils.encode(data);
            System.out.println("req_data="+req_data);
            String noSecData = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(req_data), appPriKey), "UTF-8");
            System.out.println("noSecData="+noSecData);
            ObjectNode noSignNode = (ObjectNode) new ObjectMapper().readTree(noSecData);
            String signStr = noSignNode.get("sign").textValue();
            noSignNode.remove("sign");
            String waitSign = noSignNode.toString();
            boolean isVerify=RSAUtils.verify(waitSign.getBytes("UTF-8"), merPubKey, signStr);
            System.out.println("isVerify="+((isVerify)?"true":"false"));
        }catch (Exception e){
            e.printStackTrace();
        }
        */
    }
}
