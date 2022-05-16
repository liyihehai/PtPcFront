package com.nnte.pf_source.uti.request;

public abstract class UtiRequest {
    public static final String version = "2";
    private String ver = version;
    public String getVer() {
        return ver;
    }
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
