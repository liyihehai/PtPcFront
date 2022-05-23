package com.nnte.pf_source.uti.request;

@UtiURL(url = RequestToken.requestURL)
public class RequestToken extends UtiRequest {

    public static final String requestURL = "/uti/basic/merchantToken";

    private String mid;
    private String account;
    private String terminal;
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

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
