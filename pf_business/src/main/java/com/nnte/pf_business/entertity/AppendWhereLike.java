package com.nnte.pf_business.entertity;

public class AppendWhereLike extends AppendWhere {
    public AppendWhereLike(){
        setWhereType("like");
    }
    public AppendWhereLike(String colName,String whereVal){
        setWhereType("like");
        setColName(colName);
        setWhereVal(whereVal);
    }
}
