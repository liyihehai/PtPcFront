package com.nnte.pf_source.uti.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@UtiURL(url = RequestReportModule.requestURL)
public class RequestReportModule extends UtiRequest {

    public static final String requestURL = "/uti/basic/reportModule";

    @JsonDeserialize(as=List.class,contentAs = ReportModuleItem.class)
    private List<ReportModuleItem> moduleItemList;

    @JsonDeserialize(as=List.class,contentAs = ReportFunctionEnter.class)
    private List<ReportFunctionEnter> functionEnterList;

    public List<ReportModuleItem> getModuleItemList() {
        return moduleItemList;
    }

    public void setModuleItemList(List<ReportModuleItem> moduleItemList) {
        this.moduleItemList = moduleItemList;
    }

    public List<ReportFunctionEnter> getFunctionEnterList() {
        return functionEnterList;
    }

    public void setFunctionEnterList(List<ReportFunctionEnter> functionEnterList) {
        this.functionEnterList = functionEnterList;
    }
}
