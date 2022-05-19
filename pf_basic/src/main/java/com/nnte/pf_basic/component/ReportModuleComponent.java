package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.reportModule.PlateformReportModule;
import com.nnte.pf_basic.mapper.workdb.reportModule.PlateformReportModuleService;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_source.uti.MerchantLicense;
import com.nnte.pf_source.uti.request.ReportModuleItem;
import com.nnte.pf_source.uti.response.ResponseReportModule;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class ReportModuleComponent extends BaseComponent {
    @Autowired
    private PlateformReportModuleService plateformReportModuleService;
    @Autowired
    private PFAppLicenseComponent pfAppLicenseComponent;

    public String makeReportModule(PlateformReportModule module){
        String key = module.getPmCode()+"-"+module.getAppCode()+"-"+module.getModuleCode()+"-"+module.getReportTerminal();
        return key;
    }

    public String makeReportModule(String pmCode,ReportModuleItem moduleItem,String terminal){
        String key = pmCode+"-"+moduleItem.getAppCode()+"-"+moduleItem.getModuleCode()+"-"+terminal;
        return key;
    }
    /**
     * 记录模块报告，返回报告模块的license
     * */
    public ResponseReportModule reportModule(List<ReportModuleItem> reportModuleItemList,
                                             PlateformMerchantUtiAccount puma,
                                             String terminal,String IP) throws Exception{
        PlateformReportModule dto = new PlateformReportModule();
        dto.setPmCode(puma.getPmCode());
        List<PlateformReportModule> list=plateformReportModuleService.findModelList(dto);
        Map<String,PlateformReportModule> prmMap = new HashedMap();
        for(PlateformReportModule module:list){
            prmMap.put(makeReportModule(module),module);
        }
        ResponseReportModule responseReportModule = new ResponseReportModule();
        List<MerchantLicense> licenseItemList = new ArrayList<>();
        for(ReportModuleItem moduleItem:reportModuleItemList){
            String key=makeReportModule(puma.getPmCode(),moduleItem,terminal);
            PlateformReportModule module = prmMap.get(key);
            MerchantLicense license=pfAppLicenseComponent.getLicense(puma.getPmCode(),moduleItem.getAppCode(),
                    moduleItem.getModuleCode(),terminal);
            licenseItemList.add(license);
            if (license!=null) {
                if (module == null) {
                    PlateformReportModule addReportModule = new PlateformReportModule();
                    addReportModule.setPmCode(puma.getPmCode());
                    addReportModule.setAppCode(moduleItem.getAppCode());
                    addReportModule.setModuleCode(moduleItem.getModuleCode());
                    addReportModule.setModuleVersion(moduleItem.getModuleVersion());
                    addReportModule.setReportIp(IP);
                    addReportModule.setReportTerminal(terminal);
                    addReportModule.setRefreshTime(new Date());
                } else {//如果已经有该终端的模块记录,刷新时间，生成license
                    PlateformReportModule updateReportModule = new PlateformReportModule();
                    updateReportModule.setId(module.getId());
                    updateReportModule.setModuleVersion(module.getModuleVersion());
                    updateReportModule.setReportIp(IP);
                    updateReportModule.setRefreshTime(new Date());
                    plateformReportModuleService.updateModel(updateReportModule);
                }
            }
        }
        responseReportModule.setLicenseItemList(licenseItemList);
        return responseReportModule;
    }
}
