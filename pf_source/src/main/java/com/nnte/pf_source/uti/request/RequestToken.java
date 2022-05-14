package com.nnte.pf_source.uti.request;

@UtiURL(url = "/uti/basic/merchantToken")
public class RequestToken extends UtiRequest {

    private String mid;
    private String account;
    private String seckey;
    private Long timeStamp;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSeckey() {
        return seckey;
    }

    public void setSeckey(String seckey) {
        this.seckey = seckey;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
