package com.nnte.pf_source.uti.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nnte.pf_source.uti.MerchantLicense;

import java.util.List;

public class ResponseReportModule {
    @JsonDeserialize(as=List.class,contentAs = MerchantLicense.class)
    private List<MerchantLicense> licenseItemList;

    public List<MerchantLicense> getLicenseItemList() {
        return licenseItemList;
    }

    public void setLicenseItemList(List<MerchantLicense> licenseItemList) {
        this.licenseItemList = licenseItemList;
    }
}
