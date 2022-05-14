package com.nnte.pf_uti_client.config;

public class UtiClientConfig {
    private String merchantId;
    private String accountCode;
    private String accountPws;
    private String merchantPriKey;
    private String merchantPubKey;
    private String appPubKey;
    private String utiServer;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountPws() {
        return accountPws;
    }

    public void setAccountPws(String accountPws) {
        this.accountPws = accountPws;
    }

    public String getMerchantPriKey() {
        return merchantPriKey;
    }

    public void setMerchantPriKey(String merchantPriKey) {
        this.merchantPriKey = merchantPriKey;
    }

    public String getMerchantPubKey() {
        return merchantPubKey;
    }

    public void setMerchantPubKey(String merchantPubKey) {
        this.merchantPubKey = merchantPubKey;
    }

    public String getAppPubKey() {
        return appPubKey;
    }

    public void setAppPubKey(String appPubKey) {
        this.appPubKey = appPubKey;
    }

    public String getUtiServer() {
        return utiServer;
    }

    public void setUtiServer(String utiServer) {
        this.utiServer = utiServer;
    }
}
