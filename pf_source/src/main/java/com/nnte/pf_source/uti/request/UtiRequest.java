package com.nnte.pf_source.uti.request;

public abstract class UtiRequest {
    public static final String version = "2";
    private String ver = version;
    public String getVer() {
        return ver;
    }
    private Long timeStamp;

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
