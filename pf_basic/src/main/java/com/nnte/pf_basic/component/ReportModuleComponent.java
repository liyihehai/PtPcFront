package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.entertity.ReportModule;
import com.nnte.pf_basic.mapper.workdb.merchantEnter.PlateformMerchantEnter;
import com.nnte.pf_basic.mapper.workdb.merchantEnter.PlateformMerchantEnterService;
import com.nnte.pf_basic.mapper.workdb.reportModule.PlateformReportModule;
import com.nnte.pf_basic.mapper.workdb.reportModule.PlateformReportModuleService;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_source.uti.MerchantLicense;
import com.nnte.pf_source.uti.request.ReportFunctionEnter;
import com.nnte.pf_source.uti.request.ReportModuleItem;
import com.nnte.pf_source.uti.response.ResponseReportModule;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class ReportModuleComponent extends BaseComponent {
    @Autowired
    private PlateformReportModuleService plateformReportModuleService;
    @Autowired
    private PFAppLicenseComponent pfAppLicenseComponent;
    @Autowired
    private BasicGlobalComponent basicGlobalComponent;
    @Autowired
    private PlateformMerchantEnterService plateformMerchantEnterService;

    public String makeReportModule(PlateformReportModule module){
        String key = module.getPmCode()+"-"+module.getAppCode()+"-"+module.getModuleCode()+"-"+module.getReportTerminal();
        return key;
    }

    public String makeReportModule(String pmCode,ReportModuleItem moduleItem,String terminal){
        String key = pmCode+"-"+moduleItem.getAppCode()+"-"+moduleItem.getModuleCode()+"-"+terminal;
        return key;
    }
    private Map<String,PlateformReportModule> getMerchantReportModule(PlateformMerchantUtiAccount puma){
        PlateformReportModule dto = new PlateformReportModule();
        dto.setPmCode(puma.getPmCode());
        List<PlateformReportModule> list=plateformReportModuleService.findModelList(dto);
        Map<String,PlateformReportModule> prmMap = new HashedMap();
        for(PlateformReportModule module:list){
            prmMap.put(makeReportModule(module),module);
        }
        return prmMap;
    }
    /**
     * 记录模块报告，返回报告模块的license
     * */
    public ResponseReportModule reportModule(List<ReportModuleItem> reportModuleItemList,
                                             List<ReportFunctionEnter> reportFunctionEnterList,
                                             PlateformMerchantUtiAccount puma,String terminal,String IP) throws Exception{
        Map<String,PlateformReportModule> prmMap = getMerchantReportModule(puma);
        Map<String, Object> appMap=basicGlobalComponent.getBusiAppNameMap();
        ResponseReportModule responseReportModule = new ResponseReportModule();
        List<MerchantLicense> licenseItemList = new ArrayList<>();
        for(ReportModuleItem moduleItem:reportModuleItemList){
            String key=makeReportModule(puma.getPmCode(),moduleItem,terminal);
            PlateformReportModule module = prmMap.get(key);
            MerchantLicense license=pfAppLicenseComponent.getLicense(puma.getPmCode(),moduleItem.getAppCode(),
                    moduleItem.getModuleCode(),terminal);
            if (license!=null) {
                licenseItemList.add(license);
                if (module == null) {
                    PlateformReportModule addReportModule = new PlateformReportModule();
                    addReportModule.setPmCode(puma.getPmCode());
                    addReportModule.setAppCode(moduleItem.getAppCode());
                    addReportModule.setAppName(StringUtils.defaultString(appMap.get(moduleItem.getAppCode())));
                    addReportModule.setModuleCode(moduleItem.getModuleCode());
                    addReportModule.setModuleName(moduleItem.getModuleName());
                    addReportModule.setFrameModule(moduleItem.getFrameModule());
                    addReportModule.setModuleVersion(moduleItem.getModuleVersion());
                    addReportModule.setReportIp(IP);
                    addReportModule.setReportTerminal(terminal);
                    addReportModule.setRefreshTime(new Date());
                    plateformReportModuleService.addModel(addReportModule);
                } else {//如果已经有该终端的模块记录,刷新时间，生成license
                    PlateformReportModule updateReportModule = new PlateformReportModule();
                    updateReportModule.setId(module.getId());
                    updateReportModule.setAppName(StringUtils.defaultString(appMap.get(moduleItem.getAppCode())));
                    updateReportModule.setModuleName(moduleItem.getModuleName());
                    updateReportModule.setFrameModule(moduleItem.getFrameModule());
                    updateReportModule.setModuleVersion(module.getModuleVersion());
                    updateReportModule.setReportIp(IP);
                    updateReportModule.setRefreshTime(new Date());
                    plateformReportModuleService.updateModel(updateReportModule);
                }
            }
        }
        reportModuleFunction(reportFunctionEnterList);
        responseReportModule.setLicenseItemList(licenseItemList);
        return responseReportModule;
    }

    private String getEnterKey(String appCode,String moduleCode,String funCode){
        StringBuilder sb = new StringBuilder();
        sb.append(appCode).append("-").append(moduleCode)
                .append("-").append(funCode);
        return sb.toString();
    }

    public Map<String,ReportModule> getAllModuleMap(){
        Map<String,ReportModule> retMap = new HashMap<>();
        List<ReportModule> list=plateformReportModuleService.queryAllReportModule();
        if (list!=null && list.size()>0){
            for(ReportModule module:list) {
                if (!module.getAppCode().equals(module.getModuleCode())){
                    retMap.put(module.getAppCode()+"-"+module.getModuleCode(),module);
                }
            }
        }
        return retMap;
    }

    public void reportModuleFunction(List<ReportFunctionEnter> reportFunctionEnterList) throws Exception{
        if (reportFunctionEnterList==null || reportFunctionEnterList.size()<=0)
            return;
        Map<String, Object> appMap=basicGlobalComponent.getBusiAppNameMap();
        Map<String,ReportModule> moduleMap=getAllModuleMap();
        List<PlateformMerchantEnter> listEnter=plateformMerchantEnterService.findModelList(null);
        Map<String,PlateformMerchantEnter> map = new HashMap<>();
        for(PlateformMerchantEnter enter:listEnter){
            String key = getEnterKey(enter.getAppCode(),enter.getModuleCode(),enter.getFunCode());
            map.put(key,enter);
        }
        for(ReportFunctionEnter enter:reportFunctionEnterList){
            String key = getEnterKey(enter.getAppCode(),enter.getModuleCode(),enter.getFunCode());
            PlateformMerchantEnter srcEnter=map.get(key);
            PlateformMerchantEnter save = new PlateformMerchantEnter();
            if (srcEnter!=null){
                save.setId(srcEnter.getId());
                save.setFreshTime(new Date());
            }else {
                save.setAppCode(enter.getAppCode());
                save.setAppName(StringUtils.defaultString(appMap.get(enter.getAppCode())));
                save.setModuleCode(enter.getModuleCode());
                ReportModule module=moduleMap.get(enter.getAppCode()+"-"+enter.getModuleCode());
                save.setModuleName(module!=null?module.getModuleName():"");
                save.setFunCode(enter.getFunCode());
                save.setFunName(enter.getFunName());
                save.setFunPath(enter.getFunPath());
                save.setFreshTime(new Date());
            }
            plateformMerchantEnterService.save(save,false);
        }
    }
}
