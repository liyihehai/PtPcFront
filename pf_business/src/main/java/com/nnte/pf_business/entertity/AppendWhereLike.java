package com.nnte.pf_business.entertity;

import com.nnte.basebusi.excption.BusiException;

public class AppendWhereLike extends AppendWhere {
    public AppendWhereLike() throws BusiException {
        super(AppendWhere.Type_like);
    }
    public AppendWhereLike(String colName,String whereVal) throws BusiException{
        super(AppendWhere.Type_like);
        setColName(colName);
        setWhereVal(whereVal);
    }
}
