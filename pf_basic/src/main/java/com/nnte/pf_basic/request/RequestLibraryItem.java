package com.nnte.pf_basic.request;

import com.nnte.basebusi.annotation.IntegerCheck;
import com.nnte.basebusi.annotation.StringCheck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestLibraryItem {
    @IntegerCheck(colName = "分类子项ID",nullValid=false)
    private Integer id;
    @StringCheck(colName = "字典分类编号",maxLen = 30)
    private String libTypeCode;
    @StringCheck(colName = "分类子项编号",maxLen = 20)
    private String typeItemCode;
    @StringCheck(colName = "分类子项名称",maxLen = 50)
    private String typeItemName;
    private Integer itemSort;
    private String remark;
}
