package com.nnte.pf_business.component.merchant;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.DBSrcTranc;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.WorkDBAspect;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.BaseService;
import com.nnte.framework.base.ConnSqlSessionFactory;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.MapUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_business.mapper.workdb.merchant.PlateformMerchantService;
import com.nnte.pf_business.mapper.workdb.merchant_expand.PlateformMerchantExpand;
import com.nnte.pf_business.mapper.workdb.merchant_expand.PlateformMerchantExpandService;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApplyService;
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
@BusiLogAttr(value = "MerchantManager")
public class PlateformMerchanComponent extends BaseBusiComponent {
    @Autowired
    private PlateformMerchantService plateformMerchantService;
    @Autowired
    private PlateformMerchantExpandService plateformMerchantExpandService;
    @Autowired
    private PlateformMerchanApplyService plateformMerchanApplyService;
    /**
     * 商户状态等静态定义
     * */
    public static Integer Merchant_State_OffLine  =-1;  //下架
    public static Integer Merchant_State_UnVerify =0;   //未认证
    public static Integer Merchant_State_Service  =1;   //可服务
    public static Integer Merchant_State_Pause    =2;   //暂停服务
    /**
     * 取得用于商户申请通过时样例设置的商户编号
     * */
    public String getCheckPassMerchantCode(){
        String maxCode=plateformMerchantService.findMaxMerchantCode();
        if (StringUtils.isEmpty(maxCode))
            return "1";
        return StringUtils.stringAddNumber(maxCode,1);
    }
    /**
     * 商户申请通过
     * */
    @DBSrcTranc(autocommit = false)
    public Map<String,Object> passMerchantApply(Map<String, Object> paramMap,
                                                PlateformMerchanApply apply, OperatorInfo oi){
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            BaseService.ConnDaoSession session = BaseService.getThreadLocalSession();
            if (apply == null || apply.getApplyContent() == null)
                throw new BusiException("申请详细内容为空");
            PlateformMerchantExpand newExpandDto = JsonUtil.jsonToBean(apply.getApplyContent(),PlateformMerchantExpand.class);
            if (newExpandDto==null)
                throw new BusiException("申请详细内容不合法");
            if (StringUtils.isEmpty(newExpandDto.getPmEmail()))
                throw new BusiException("联系Email不能为空");
            //-----准备新商户信息------
            PlateformMerchant newDto = new PlateformMerchant();
            newDto.setPmCode(apply.getPmCode());
            newDto.setPmName(apply.getPmName());
            newDto.setPmShortName(StringUtils.defaultString(paramMap.get("pmShortName")));
            newDto.setPmCompanyPerson(apply.getPmCompanyPerson());
            newDto.setPmState(Merchant_State_Pause);//申请通过的商户初始状态为暂停
            newDto.setCreateTime(new Date());
            newDto.setApplyEmail(newExpandDto.getPmEmail());
            Integer count=plateformMerchantService.addModel(session,newDto);
            if (count==null || count!=1)
                throw new BusiException("新增商户主信息失败");
            //---准备商户附加信息----
            newExpandDto.setPmCode(newDto.getPmCode());
            newExpandDto.setCreateTime(newDto.getCreateTime());
            count=plateformMerchantExpandService.addModel(session,newExpandDto);
            if (count==null || count!=1)
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
            count=plateformMerchanApplyService.updateModel(session,updateApplyDto);
            if (count==null || count!=1)
                throw new BusiException("更改商户申请信息失败");
            BaseNnte.setRetTrue(ret,"商户申请通过操作成功");
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
        }
        return ret;
    }

    public Map queryMerchantsForSetting(PlateformMerchant dto,OperatorInfo oi,Map<String,Object> appendParam)
            throws BusiException{
        return queryMerchantList(dto,appendParam);
    }

    /**
     * 查询商户列表
     */
    public Map<String, Object> queryMerchantList(PlateformMerchant dto,Map<String,Object> appendParam)
            throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        Map<String,Object> paramMap= MapUtil.beanToMap(dto);
        if (appendParam!=null && appendParam.size()>0){
            paramMap.putAll(appendParam);
        }
        if (paramMap.get("sort")==null) {
            paramMap.put("sort", "create_time");
            paramMap.put("dir", "desc");
        }
        Integer count=plateformMerchantService.findPlateformMerchansCustmerCount(paramMap);
        if (count!=null && count>0) {
            ret.put("count", count);
            List<PlateformMerchant> list = plateformMerchantService.findPlateformMerchansCustmerList(paramMap);
            ret.put("list", list);
            BaseNnte.setRetTrue(ret, "查询列表成功");
        }else{
            ret.put("count", 0);
            ret.put("list", null);
            BaseNnte.setRetTrue(ret, "查询列表成功");
        }
        return ret;
    }
    public PlateformMerchant getMerchantById(Integer id){
        return plateformMerchantService.findModelByKey(id);
    }

    public PlateformMerchantExpand getMerchantExpandByCode(String code){
        return plateformMerchantExpandService.findModelByKey(code);
    }
}
