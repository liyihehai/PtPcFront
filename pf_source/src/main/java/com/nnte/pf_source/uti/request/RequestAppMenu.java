package com.nnte.pf_source.uti.request;

@UtiURL(url = RequestAppMenu.requestURL)
public class RequestAppMenu extends UtiRequest {
    public static final String requestURL = "/uti/basic/requestAppMenu";

    private String appCode;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
