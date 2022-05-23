package com.nnte.pf_merchant.component.license;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.pf_basic.component.BasicGlobalComponent;
import com.nnte.pf_basic.component.JedisComponent;
import com.nnte.pf_basic.component.PFAppLicenseComponent;
import com.nnte.pf_basic.component.PFBusiModuleComponent;
import com.nnte.pf_basic.entertity.LicenseCreateChannel;
import com.nnte.pf_basic.entertity.LicenseState;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicenseService;
import com.nnte.pf_basic.mapper.workdb.busiModule.PlateformBusiModule;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_basic.proxy.LicenseLockExtendProxy;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.entertity.AppLicenseItem;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
public class PlateformMerchantLicenseComponent extends BaseComponent {

    @Autowired
    private JedisComponent jedisComponent;
    @Autowired
    private PlateformAppLicenseService plateformAppLicenseService;
    @Autowired
    private BasicGlobalComponent basicGlobalComponent;
    @Autowired
    private PFBusiModuleComponent pfBusiModuleComponent;
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PFAppLicenseComponent pfAppLicenseComponent;

    /**
     * 返回商户许可列表数据
     */
    public PageData<AppLicenseItem> merchantLicenseList(Map<String, Object> paramMap,
                                                        Integer pageNo, Integer pageSize) throws Exception {
        PageData<PlateformAppLicense> pageData = plateformAppLicenseService.getListPageData("findModelListByMap", paramMap, pageNo, pageSize);
        PageData<AppLicenseItem> ret = new PageData<>();
        ret.setTotal(pageData.getTotal());
        ret.setSuccess(pageData.isSuccess());
        List<AppLicenseItem> list = new ArrayList<>();
        if (pageData.getData() != null && pageData.getData().size() > 0) {
            Map<String, Object> appNameMap = basicGlobalComponent.getBusiAppNameMap();
            Map<String, PlateformBusiModule> moduleMap = pfBusiModuleComponent.getValidBusiModuleMap();
            Set<Object> codeSet = BeanUtils.getCollectionFieldValueSet(pageData.getData(), "pmCode");
            Map<Object, PlateformMerchant> merchantMap = plateformMerchanComponent.getMerchantByCodeList(codeSet);
            Map<Object, PlateformMerchantUtiAccount> utiAccountMap = plateformMerchanComponent.getUtiAccountByCodeList(codeSet);
            for (PlateformAppLicense srcLicense : pageData.getData()) {
                AppLicenseItem item = new AppLicenseItem();
                BeanUtils.copyFromSrc(srcLicense, item);
                Object appName = appNameMap.get(srcLicense.getAppCode());
                item.setAppName(appName != null ? appName.toString() : null);
                PlateformBusiModule module = moduleMap.get(srcLicense.getModuleCode());
                item.setModuleName(module != null ? module.getModuleName() : null);
                PlateformMerchant merchant = merchantMap.get(srcLicense.getPmCode());
                item.setPmName(merchant != null ? merchant.getPmName() : null);
                item.setPmShortName(merchant != null ? merchant.getPmShortName() : null);
                PlateformMerchantUtiAccount utiAccount = utiAccountMap.get(srcLicense.getPmCode());
                item.setMerchantTerminals(utiAccount != null ? utiAccount.getTerminals() : null);
                list.add(item);
            }
        }
        ret.setData(list);
        return ret;
    }

    /**
     * 保存商户授权
     */
    public PlateformAppLicense saveMerchantLicense(PlateformAppLicense license, LicenseCreateChannel channel, String opeName) throws Exception {
        if (license.getId() == null || license.getId() <= 0) {
            return createMerchantLicense(license, channel, opeName);
        } else {
            return updateMerchantLicenseExtend(license, opeName);
        }
    }

    private List<PlateformAppLicense> getRefLicense(PlateformAppLicense license) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pmCode", license.getPmCode());
        paramMap.put("appCode", license.getAppCode());
        paramMap.put("moduleCode", license.getModuleCode());
        paramMap.put("sort", "mam_no");
        paramMap.put("dir", "desc");
        return plateformAppLicenseService.findModelListByMap(paramMap);
    }

    private PlateformAppLicense createMerchantLicense(PlateformAppLicense license, LicenseCreateChannel channel,
                                                      String opeName) throws Exception {
        PlateformAppLicense addLicense = new PlateformAppLicense();
        BeanUtils.copyFromSrc(license, addLicense);
        PlateformBusiModule module = pfBusiModuleComponent.getNotInvalidBMByCode(license.getModuleCode());
        addLicense.setModuleVersion(module.getCurrentVersion());
        addLicense.setLicenseState(LicenseState.Modify.getValue());
        addLicense.setExeAmount(0d);
        addLicense.setRemainderAmount(0d);
        addLicense.setCreateChannel(channel.getValue());
        addLicense.setCreateBy(opeName);
        addLicense.setCreateTime(new Date());
        String lock = pfAppLicenseComponent.makeLicenseCreateLock(license.getPmCode(),
                license.getAppCode(), license.getModuleCode());
        if (!jedisComponent.setRedisLock(lock, 5))
            throw new BusiException("获取License创建锁失败");
        try {
            PlateformAppLicense maxNoLicense = null;
            List<PlateformAppLicense> list = getRefLicense(license);
            if (list != null && list.size() > 0)
                maxNoLicense = list.get(0);
            if (maxNoLicense != null)
                addLicense.setMamNo(maxNoLicense.getMamNo() + 1);
            else
                addLicense.setMamNo(1);
            return plateformAppLicenseService.save(addLicense, true);
        } finally {
            jedisComponent.releaseRedisLock(lock);
        }
    }

    /**
     * 更改许可基本配置增强型调用
     */
    public PlateformAppLicense updateMerchantLicenseExtend(PlateformAppLicense license,
                                                           String opeName) throws Exception {
        LicenseLockExtendProxy proxy = new LicenseLockExtendProxy(this, jedisComponent,
                pfAppLicenseComponent);
        return (PlateformAppLicense) proxy.licenseUpdateLockExtendInvoke("updateMerchantLicense", license, opeName);
    }

    /**
     * 更改许可基本配置
     */
    private PlateformAppLicense updateMerchantLicense(PlateformAppLicense license,
                                                      String opeName) throws Exception {
        PlateformAppLicense updateLicense = new PlateformAppLicense();
        PlateformAppLicense src = plateformAppLicenseService.findModelByKey(license.getId());
        if (src == null)
            throw new BusiException("没找到指定的许可");
        if (!src.getLicenseState().equals(LicenseState.Modify.getValue()))
            throw new BusiException("指定的许可不处于可编辑状态");
        updateLicense.setId(src.getId());
        updateLicense.setCopyCount(license.getCopyCount());
        updateLicense.setTerminals(license.getTerminals());
        updateLicense.setStartDate(license.getStartDate());
        updateLicense.setMonthCount(license.getMonthCount());
        updateLicense.setEndDate(license.getEndDate());
        updateLicense.setUpdateBy(opeName);
        updateLicense.setUpdateDate(new Date());
        return plateformAppLicenseService.save(updateLicense, true);
    }

    /**
     * 确认许可状态
     */
    public void confirmMerchantLicenseExtend(Integer id, String opeName) throws Exception {
        PlateformAppLicense src = plateformAppLicenseService.findModelByKey(id);
        if (src == null)
            throw new BusiException("没找到指定的许可");
        confirmMerchantLicenseExtend(src, opeName);
    }

    private void confirmMerchantLicenseExtend(PlateformAppLicense src, String opeName) throws Exception {
        LicenseLockExtendProxy proxy = new LicenseLockExtendProxy(this, jedisComponent,
                pfAppLicenseComponent);
        proxy.licenseUpdateLockExtendInvoke("confirmMerchantLicense", src, opeName);
    }

    private void confirmMerchantLicense(PlateformAppLicense src, String opeName) throws Exception {
        if (!src.getLicenseState().equals(LicenseState.Modify.getValue()))
            throw new BusiException("指定的许可不处于可编辑状态");
        List<PlateformAppLicense> list = getRefLicense(src);
        if (list != null && list.size() > 0) {
            for (PlateformAppLicense refLicense : list) {
                if (refLicense.getLicenseState().equals(LicenseState.Confirmed.getValue()) ||
                        refLicense.getLicenseState().equals(LicenseState.ExecIng.getValue()) ||
                        refLicense.getLicenseState().equals(LicenseState.Over.getValue()) ||
                        refLicense.getLicenseState().equals(LicenseState.Stopped.getValue())) {
                    if (src.getStartDate().getTime() >= refLicense.getStartDate().getTime() &&
                            src.getStartDate().getTime() <= refLicense.getEndDate().getTime())
                        throw new BusiException("当前许可开始时间在许可[MAMNO=" + refLicense.getMamNo() + "]时间范围内");
                    if (src.getEndDate().getTime() >= refLicense.getStartDate().getTime() &&
                            src.getEndDate().getTime() <= refLicense.getEndDate().getTime())
                        throw new BusiException("当前许可结束时间在许可[MAMNO=" + refLicense.getMamNo() + "]时间范围内");
                }
            }
        }
        PlateformAppLicense updateLicense = new PlateformAppLicense();
        updateLicense.setId(src.getId());
        updateLicense.setLicenseState(LicenseState.Confirmed.getValue());
        updateLicense.setUpdateBy(opeName);
        updateLicense.setUpdateDate(new Date());
        plateformAppLicenseService.save(updateLicense, false);
    }

    public void deleteMerchantLicenseExtend(Integer id, String opeName) throws Exception {
        PlateformAppLicense src = plateformAppLicenseService.findModelByKey(id);
        if (src == null)
            throw new BusiException("没找到指定的许可");
        deleteMerchantLicenseExtend(src, opeName);
    }

    public void deleteMerchantLicenseExtend(PlateformAppLicense src, String opeName) throws Exception {
        LicenseLockExtendProxy proxy = new LicenseLockExtendProxy(this, jedisComponent,
                pfAppLicenseComponent);
        proxy.licenseUpdateLockExtendInvoke("deleteMerchantLicense", src, opeName);
    }

    public void deleteMerchantLicense(PlateformAppLicense src, String opeName) throws Exception {
        if (!src.getLicenseState().equals(LicenseState.Modify.getValue()))
            throw new BusiException("指定的许可不处于可编辑状态");
        plateformAppLicenseService.deleteModel(src.getId());
    }
}
