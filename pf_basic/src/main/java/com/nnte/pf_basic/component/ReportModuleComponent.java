package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_source.uti.request.ReportModuleItem;
import com.nnte.pf_source.uti.response.ResponseReportModule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class ReportModuleComponent extends BaseComponent {
    public ResponseReportModule reportModule(List<ReportModuleItem> reportModuleItemList,
                                             PlateformMerchantUtiAccount puma) throws Exception{
        ResponseReportModule responseReportModule = new ResponseReportModule();
        return responseReportModule;
    }
}
