package com.nnte.pf_source.uti.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ResponseReportModule {
    @JsonDeserialize(as=List.class,contentAs = ModuleLicenseItem.class)
    private List<ModuleLicenseItem> licenseItemList;

    public List<ModuleLicenseItem> getLicenseItemList() {
        return licenseItemList;
    }

    public void setLicenseItemList(List<ModuleLicenseItem> licenseItemList) {
        this.licenseItemList = licenseItemList;
    }
}
