package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.framework.utils.DateUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.entertity.LicenseState;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicenseService;
import com.nnte.pf_source.uti.MerchantLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFAppLicenseComponent extends BaseComponent {
    @Autowired
    private PlateformAppLicenseService plateformAppLicenseService;

    public MerchantLicense getLicense(String pmCode, String appCode, String moduleCode,
                                      String terminal){
        PlateformAppLicense dto = new PlateformAppLicense();
        dto.setPmCode(pmCode);
        dto.setAppCode(appCode);
        dto.setModuleCode(moduleCode);
        dto.setSort("t.end_date");
        dto.setDir("desc");
        List<PlateformAppLicense> list=plateformAppLicenseService.findModelList(dto);
        Date endDate=null;
        for(PlateformAppLicense license:list){
            if (license.getLicenseState().equals(LicenseState.Confirmed.getValue())||
                    license.getLicenseState().equals(LicenseState.ExecIng.getValue())){
                if (endDate==null || license.getEndDate().after(endDate)) {
                    if (license.getTerminals()!=null && license.getTerminals().length()>0) {
                        if (license.getTerminals().equals(terminal)||
                                StringUtils.isFindStr(license.getTerminals(),terminal))
                            endDate = license.getEndDate();
                    }
                }
            }
        }
        Date nowDate = DateUtils.dateToDate(new Date(),DateUtils.DF_YMD);
        if (nowDate.before(endDate))//表示已过期
            return null;
        MerchantLicense license = new MerchantLicense();
        license.setAppCode(appCode);
        license.setModuleCode(moduleCode);
        license.setExpireDate(endDate);
        return license;
    }
}
