package com.nnte.pf_uti_client.entertity;

public class TKeyValue<TK,TV> {
    private TK key;
    private TV value;

    public TKeyValue(TK k,TV v){
        key=k;
        value=v;
    }
    public TK getKey() {
        return key;
    }
    public void setKey(TK key) {
        this.key = key;
    }
    public TV getValue() {
        return value;
    }
    public void setValue(TV value) {
        this.value = value;
    }
}
