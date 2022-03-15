package com.nnte.pf_business.entertity;

import com.nnte.framework.utils.DateUtils;

import java.util.Date;

public class AppendWhere {
    private static final String Type_Direct = "direct";
    private static final String Type_like = "like";
    private String whereTxt;
    private String whereType = Type_Direct;
    private String colName;
    private Object whereVal;

    public String getWhereType() {
        return whereType;
    }

    public void setWhereType(String whereType) {
        this.whereType = whereType;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Object getWhereVal() {
        return whereVal;
    }

    public void setWhereVal(Object whereVal) {
        this.whereVal = whereVal;
    }

    public String getWhereTxt() {
        return whereTxt;
    }

    public void setWhereTxt(String whereTxt) {
        this.whereTxt = whereTxt;
    }
    /**
     * 设置日期范围查询条件
     * */
    public static void andDateRange(AppendWhere where,String colName, Date stateDate, Date endDate){
        where.setWhereType(Type_Direct);
        if (stateDate==null && endDate==null)
            return;
        String wTxt = " and ";
        if (stateDate!=null)
            wTxt= wTxt + colName + ">='"+ DateUtils.dateToString_full(DateUtils.todayZeroTime(stateDate))+"'";
        if (endDate!=null)
            wTxt= wTxt + " and " + colName + "<='" + DateUtils.dateToString_full(DateUtils.todayNightZeroTime(endDate)) + "'";
        where.setWhereTxt(wTxt);
    }
}
