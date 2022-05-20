package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.framework.utils.DateUtils;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.entertity.LicenseState;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicenseService;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_source.uti.MerchantLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFAppLicenseComponent extends BaseComponent {
    @Autowired
    private PlateformAppLicenseService plateformAppLicenseService;
    @Autowired
    private BasicGlobalComponent basicGlobalComponent;

    public boolean isTerminalInLicense(PlateformAppLicense license,String terminal){
        if (license.getTerminals()!=null && license.getTerminals().length()>0 &&(license.getTerminals().equals(terminal)||
                StringUtils.isFindStr(license.getTerminals(),terminal)))
            return true;
        return false;
    }
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
        Date laterStartDate=null;
        for(PlateformAppLicense license:list){
            if (!isTerminalInLicense(license,terminal))
                continue;
            if (license.getLicenseState().equals(LicenseState.Confirmed.getValue())||
                    license.getLicenseState().equals(LicenseState.ExecIng.getValue())){
                if (laterStartDate!=null){
                    if (DateUtils.addDay(license.getEndDate(),1).before(laterStartDate)){
                        endDate = license.getEndDate();
                        laterStartDate = license.getStareDate();
                        continue;
                    }
                }
                if (endDate==null || license.getEndDate().after(endDate)) {
                    endDate = license.getEndDate();
                    laterStartDate = license.getStareDate();
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

    private void autoLicenseStateUpdate(PlateformAppLicense license,Long nowDate,
                                        PlateformOperator operator){
        try {
            Long startDate = license.getStareDate().getTime();
            Integer state = 1;
            Date exeDate = new Date(nowDate);
            if (startDate <= nowDate) {
                state = 2;
            }
            Long endDate = license.getStareDate().getTime();
            if (endDate < nowDate) {
                state = 3;
                exeDate = new Date(endDate);
            }
            Integer remainderDays = DateUtils.getDaysBetween(exeDate,license.getEndDate());
            if (!license.getLicenseState().equals(state)) {
                //如果状态发生了变化，需要重新设置状态
                PlateformAppLicense updateLicense = new PlateformAppLicense();
                updateLicense.setId(license.getId());
                updateLicense.setLicenseState(state);
                updateLicense.setExeDate(exeDate);
                updateLicense.setRemainderDays(remainderDays);
                if (remainderDays<=0) {
                    updateLicense.setExeAmount(license.getLicenseAmount());
                    updateLicense.setRemainderAmount(0d);
                }else{
                    int totalDays=DateUtils.getDaysBetween(license.getStareDate(),license.getEndDate());
                    updateLicense.setExeAmount(NumberUtil.getScaleValue4Money(license.getLicenseAmount() * (totalDays - remainderDays) / totalDays));
                    updateLicense.setRemainderAmount(NumberUtil.getScaleValue4Money(license.getLicenseAmount() - updateLicense.getExeAmount()));
                }
                if (operator!=null)
                    updateLicense.setUpdateBy(operator.getOpeName());
                else
                    updateLicense.setUpdateBy("unknown");
                updateLicense.setUpdateDate(new Date());
                plateformAppLicenseService.updateModel(updateLicense);
            }
        }catch (Exception e){
            outLogExp(e);
        }
    }
    /**
     * 自动任务调用，license状态每日更新
     * 每天晚上00:00:01秒启动
     * */
    public void LicenseStateDailyUpdate() throws Exception{
        Map<String,Object> paramMap=new HashMap<>();
        List<AppendWhere> appendWhereList = new ArrayList<>();
        AppendWhere whereState = new AppendWhere(AppendWhere.Type_Direct);
        //查询出状态为:1待执行，2执行中
        whereState.setWhereTxt("1=1 and (t.License_state=1 or t.License_state=2)");
        appendWhereList.add(whereState);
        paramMap.put("appendWhereList", appendWhereList);
        List<PlateformAppLicense> list=plateformAppLicenseService.findModelListByMap(paramMap);
        Long nowDate = DateUtils.dateToDate(new Date(),DateUtils.DF_YMD).getTime();
        PlateformOperator operator = basicGlobalComponent.getOperatorByCode(AppBasicConfig.auto_task_operator);
        for(PlateformAppLicense license:list){
            autoLicenseStateUpdate(license,nowDate,operator);
        }
    }
}
