package com.nnte.pf_source.uti.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@UtiURL(url = RequestReportModule.requestURL)
public class RequestReportModule extends UtiRequest {

    public static final String requestURL = "/uti/basic/reportModule";

    private String reportTerminal; //报告终端
    @JsonDeserialize(as=List.class,contentAs = ReportModuleItem.class)
    private List<ReportModuleItem> moduleItemList;

    public String getReportTerminal() {
        return reportTerminal;
    }

    public void setReportTerminal(String reportTerminal) {
        this.reportTerminal = reportTerminal;
    }

    public List<ReportModuleItem> getModuleItemList() {
        return moduleItemList;
    }

    public void setModuleItemList(List<ReportModuleItem> moduleItemList) {
        this.moduleItemList = moduleItemList;
    }
}
