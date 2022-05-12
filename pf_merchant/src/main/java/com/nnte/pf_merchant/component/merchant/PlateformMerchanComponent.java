package com.nnte.pf_merchant.component.merchant;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.WorkDBAspect;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.BaseService;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.CruxOpeMQComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchantService;
import com.nnte.pf_merchant.mapper.workdb.merchant_expand.PlateformMerchantExpand;
import com.nnte.pf_merchant.mapper.workdb.merchant_expand.PlateformMerchantExpandService;
import com.nnte.pf_merchant.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_merchant.mapper.workdb.merchantapply.PlateformMerchanApplyService;
import com.nnte.pf_merchant.request.RequestMerchant;
import com.nnte.pf_merchant.request.RequestMerchantExpand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@WorkDBAspect
/**
 * 商户组件
 * 日志打印位置：MerchantManager 商户管理
 * */
@BusiLogAttr(PFMerchantConfig.loggerName)
public class PlateformMerchanComponent extends BaseComponent {
    @Autowired
    private PlateformMerchantService plateformMerchantService;
    @Autowired
    private PlateformMerchantExpandService plateformMerchantExpandService;
    @Autowired
    private PlateformMerchanApplyService plateformMerchanApplyService;
    @Autowired
    private CruxOpeMQComponent cruxOpeMQComponent;
    /**
     * 商户状态等静态定义
     */
    public static Integer Merchant_State_OffLine = -1;  //下架
    public static Integer Merchant_State_UnVerify = 0;   //未认证
    public static Integer Merchant_State_Service = 1;   //可服务
    public static Integer Merchant_State_Pause = 2;   //暂停服务

    /**
     * 取得用于商户申请通过时样例设置的商户编号
     */
    public String getCheckPassMerchantCode() {
        String maxCode = plateformMerchantService.findMaxMerchantCode();
        if (StringUtils.isEmpty(maxCode))
            return "1";
        return StringUtils.stringAddNumber(maxCode, 1);
    }

    /**
     * 商户申请通过
     */
    @DBSrcTranc(value = AppBasicConfig.DB_Name, autocommit = false)
    public Map<String, Object> passMerchantApply(Map<String, Object> paramMap,
                                                 PlateformMerchanApply apply, OperatorInfo oi) throws Exception {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        BaseService.ConnDaoSession session = BaseService.getThreadLocalSession();
        if (apply == null || apply.getApplyContent() == null)
            throw new BusiException("申请详细内容为空");
        PlateformMerchantExpand newExpandDto = JsonUtil.jsonToBean(apply.getApplyContent(), PlateformMerchantExpand.class);
        if (newExpandDto == null)
            throw new BusiException("申请详细内容不合法");
        if (StringUtils.isEmpty(newExpandDto.getPmEmail()))
            throw new BusiException("联系Email不能为空");
        //-----准备新商户信息------
        PlateformMerchant newDto = new PlateformMerchant();
        newDto.setPmCode(apply.getPmCode());
        newDto.setPmName(apply.getPmName());
        newDto.setPmShortName(StringUtils.defaultString(paramMap.get("pmShortName")));
        newDto.setPmCompanyPerson(apply.getPmCompanyPerson());
        newDto.setPmLogo(newExpandDto.getPmLogo());
        newDto.setPmState(Merchant_State_Pause);//申请通过的商户初始状态为暂停
        newDto.setCreateTime(new Date());
        newDto.setCreateBy(oi.getOperatorName());
        newDto.setApplyEmail(newExpandDto.getPmEmail());
        Integer count = plateformMerchantService.addModel(session, newDto);
        if (count == null || count != 1)
            throw new BusiException("新增商户主信息失败");
        //---准备商户附加信息----
        newExpandDto.setPmCode(newDto.getPmCode());
        newExpandDto.setCreateBy(oi.getOperatorName());
        newExpandDto.setCreateTime(newDto.getCreateTime());
        count = plateformMerchantExpandService.addModel(session, newExpandDto);
        if (count == null || count != 1)
            throw new BusiException("新增商户附加信息失败");
        //--从新设置申请表相关字段--
        PlateformMerchanApply updateApplyDto = new PlateformMerchanApply();
        updateApplyDto.setId(apply.getId());
        updateApplyDto.setCheckerCode(oi.getOperatorCode());
        updateApplyDto.setCheckerName(oi.getOperatorName());
        updateApplyDto.setCheckDesc(apply.getCheckDesc());
        updateApplyDto.setPmCode(newDto.getPmCode());
        updateApplyDto.setApplyState(PlateformMerchanApplyComponent.apply_state_suc);
        updateApplyDto.setCheckResult(1);//审核通过
        updateApplyDto.setCheckTime(newDto.getCreateTime());
        count = plateformMerchanApplyService.updateModel(session, updateApplyDto);
        if (count == null || count != 1)
            throw new BusiException("更改商户申请信息失败");
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-1", "新增商户注册", newDto, null);
        BaseNnte.setRetTrue(ret, "商户申请通过操作成功");
        return ret;
    }

    /**
     * 查询商户列表(分页)
     */
    public PageData<PlateformMerchant> queryMerchantsForSetting(Map<String, Object> appendParam, Integer pageNo, Integer pageSize) {
        if (appendParam.get("sort") == null) {
            appendParam.put("sort", "create_time");
            appendParam.put("dir", "desc");
        }
        return plateformMerchantService.getListPageData("findModelListByMap", appendParam, pageNo, pageSize);
    }

    public PlateformMerchant getMerchantById(Integer id) {
        return plateformMerchantService.findModelByKey(id);
    }
    public PlateformMerchant getMerchantByCode(String code) {
        PlateformMerchant dto = new PlateformMerchant();
        dto.setPmCode(code);
        List<PlateformMerchant> list = plateformMerchantService.findModelList(dto);
        if (list !=null && list.size()>0)
            return list.get(0);
        return null;
    }

    public PlateformMerchantExpand getMerchantExpandByCode(String code) {
        return plateformMerchantExpandService.findModelByKey(code);
    }

    /**
     * 更改商户基础信息及商户扩展信息
     * 只有商户在暂停状态下才能更改
     */
    @DBSrcTranc(value = AppBasicConfig.DB_Name, autocommit = false)
    public void saveMerchantSetting(RequestMerchant merchant, RequestMerchantExpand merchantExpand,
                                    OperatorInfo oi) throws BusiException {
        PlateformMerchant srcMerchant = getMerchantById(merchant.getId());
        if (srcMerchant == null)
            throw new BusiException("没有找到商户基础信息");
        if (!Merchant_State_Pause.equals(srcMerchant.getPmState()))
            throw new BusiException("商户状态不为暂停服务，不能更改商户信息");
        BaseService.ConnDaoSession session = BaseService.getThreadLocalSession();
        PlateformMerchant updateMerchant = new PlateformMerchant();
        updateMerchant.setId(merchant.getId());
        updateMerchant.setPmLogo(merchantExpand.getPmLogo());
        updateMerchant.setPmName(merchant.getPmName());
        updateMerchant.setPmShortName(merchant.getPmShortName());
        updateMerchant.setPmCompanyPerson(merchant.getPmCompanyPerson());
        updateMerchant.setApplyEmail(merchant.getApplyEmail());
        updateMerchant.setUpdateBy(oi.getOperatorName());
        updateMerchant.setUpdateTime(new Date());
        int mainCount = plateformMerchantService.updateModel(session, updateMerchant);
        if (mainCount != 1)
            throw new BusiException("商户主信息未变更！");
        PlateformMerchantExpand expand = new PlateformMerchantExpand();
        expand.setPmCode(merchant.getPmCode());
        //-----------------------------------------
        BeanUtils.copyFromSrc(merchantExpand, expand);
        expand.setCreateBy(null);
        expand.setCreateTime(null);
        //-----------------------------------------
        expand.setUpdateBy(oi.getOperatorName());
        expand.setUpdateTime(updateMerchant.getUpdateTime());
        int expCount = plateformMerchantExpandService.updateModel(session, expand);
        if (expCount != 1)
            throw new BusiException("商户扩展信息未变更！");
        //记录更改日志--------------------------------
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-2", "商户主信息更改", srcMerchant, updateMerchant);
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-3", "商户扩展信息更改", null, expand);
        //-------------------------------------------
    }
    private int setMerchantPmState(Integer id,String operatorName,Integer state){
        PlateformMerchant updateMerchant = new PlateformMerchant();
        updateMerchant.setId(id);
        updateMerchant.setPmState(state);
        updateMerchant.setUpdateBy(operatorName);
        updateMerchant.setUpdateTime(new Date());
        return plateformMerchantService.updateModel(updateMerchant);
    }
    /**
     * 设置商户状态由暂停==>可服务
     * */
    public void setMerchantStart(RequestMerchant merchant,OperatorInfo oi) throws Exception{
        PlateformMerchant srcMerchant = getMerchantById(merchant.getId());
        if (srcMerchant == null)
            throw new BusiException("没有找到商户基础信息");
        if (!Merchant_State_Pause.equals(srcMerchant.getPmState()))
            throw new BusiException("商户状态不为暂停服务，不能启动商户服务");
        int count = setMerchantPmState(srcMerchant.getId(),oi.getOperatorName(),Merchant_State_Service);
        if (count != 1)
            throw new BusiException("设置商户状态为可服务失败！");
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-4", "商户启动服务", srcMerchant, null);
    }
    //设置商户状态由可服务==>暂停
    public void setMerchantPause(RequestMerchant merchant,OperatorInfo oi) throws Exception{
        PlateformMerchant srcMerchant = getMerchantById(merchant.getId());
        if (srcMerchant == null)
            throw new BusiException("没有找到商户基础信息");
        if (!Merchant_State_Service.equals(srcMerchant.getPmState()))
            throw new BusiException("商户状态不为可服务，不能暂停商户服务");
        int count = setMerchantPmState(srcMerchant.getId(),oi.getOperatorName(),Merchant_State_Pause);
        if (count != 1)
            throw new BusiException("设置商户状态为可服务失败！");
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-5", "商户暂停服务", srcMerchant, null);
    }
    /**
     * 设置商户状态由暂停==>下架
     * */
    public void setMerchantOffLine(RequestMerchant merchant,OperatorInfo oi) throws Exception{
        PlateformMerchant srcMerchant = getMerchantById(merchant.getId());
        if (srcMerchant == null)
            throw new BusiException("没有找到商户基础信息");
        if (!Merchant_State_Pause.equals(srcMerchant.getPmState()))
            throw new BusiException("商户状态不为暂停服务，不能下架商户");
        int count = setMerchantPmState(srcMerchant.getId(),oi.getOperatorName(),Merchant_State_OffLine);
        if (count != 1)
            throw new BusiException("设置商户状态为下架失败！");
        cruxOpeMQComponent.sendCruxOperate(oi.getOperatorName(),"0001-6", "商户下架", srcMerchant, null);
    }

}
