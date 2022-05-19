package com.nnte.pf_basic.entertity;

public enum LicenseState {
    Modify(0),      //编辑
    Confirmed(1),   //待执行
    ExecIng(2),     //执行中
    Over(3),        //已结束
    Stopped(4),     //已终止
    Deleted(-1);    //已删除

    LicenseState(int val){
        this.value = val;
    }
    private int value;
    public int getValue(){return value;}
    public String getValueName(){
        switch (getValue()){
            case 0:{return "编辑";}
            case 1:{return "待执行";}
            case 2:{return "执行中";}
            case 3:{return "已结束";}
            case 4:{return "已终止";}
            case -1:{return "已删除";}
        }
        return "";
    }
}
