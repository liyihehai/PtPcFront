package com.nnte.pf_merchant.component.utiaccount;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.DateUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_basic.mapper.workdb.utiAccount.PlateformMerchantUtiAccountService;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 平台UTI账户管理组件
 * */
@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
public class PlateformUtiAccountComponent extends BaseComponent {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PlateformMerchantUtiAccountService plateformMerchantUtiAccountService;

    public PlateformMerchantUtiAccount getUtiAccountByMerchantCode(String code){
        PlateformMerchantUtiAccount dto = new PlateformMerchantUtiAccount();
        dto.setPmCode(code);
        List<PlateformMerchantUtiAccount> list = plateformMerchantUtiAccountService.findModelList(dto);
        if (list !=null && list.size()>0)
            return list.get(0);
        return null;
    }
    //通过账户CODE查询唯一的合作商业务账号  李毅 2022/05/11
    public PlateformMerchantUtiAccount getUtiAccountByAccountCode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            PlateformMerchantUtiAccount dto = new PlateformMerchantUtiAccount();
            dto.setAccountCode(code);
            List<PlateformMerchantUtiAccount> list = plateformMerchantUtiAccountService.findModelList(dto);
            if (list != null && list.size() == 1)
                return list.get(0);
        }
        return null;
    }
    //产生不重复的随机账户账号
    public String genAccountCode()
    {
        String code=StringUtils.RandomString(20);//产生一个20位的随机账号
        int times=100;
        while(times>0)
        {
            if (getUtiAccountByAccountCode(code)==null)
                return code;
            times--;
        }
        return null;
    }
    /**
    * mcode:商户代码
    * 如果商户业务已经开户则返回suc=true,pba=业务账户对象
    */
    public PlateformMerchantUtiAccount merchantCreateUTIAccount(String mcode,String opeName,
                                                               String defBackUrl,String validIpList,
                                                               String backId,String backKey,
                                                               String accountMemo) throws Exception{
        if (StringUtils.isEmpty(mcode))
            throw new BusiException(1002, "商户编码不合法");
        PlateformMerchant merchant=plateformMerchanComponent.getMerchantByCode(mcode);
        if (merchant==null)
            throw new BusiException(1002, "没找到指定代码的商户");
        if (!(merchant.getPmState().equals(1)||merchant.getPmState().equals(2)))
            throw new BusiException(1002, "商户未认证，不能创建UTI账户");
        PlateformMerchantUtiAccount pba=getUtiAccountByMerchantCode(mcode);
        if(pba!=null)
            throw new BusiException(1002, "商户UTI账户已存在");
        PlateformMerchantUtiAccount newpba=new PlateformMerchantUtiAccount();
        newpba.setPmCode(mcode);
        newpba.setAccountCode(genAccountCode());
        newpba.setAccountPws(StringUtils.RandomString(20));//产生20位随机密码
        newpba.setSecType("RSA");//本版本默认加密算法为RSA加密
        newpba.setAccountState(0);//初始状态为新建(开户)
        newpba.setDefBackUrl(defBackUrl);
        newpba.setValidIpList(validIpList);
        newpba.setBackId(backId);
        newpba.setBackKey(backKey);
        newpba.setAccountMemo(accountMemo);
        newpba.setCreateTime(new Date());
        newpba.setCreateBy(opeName);
        return plateformMerchantUtiAccountService.save(newpba,true);
    }

    /**商户UTI开通  李毅 2022/05/11
    * 如果首次开通且没有指定开通业务的到期时间,默认为2999年12月31日到期(即永不到期)
    * 如果商户业务已经开通则返回suc=true,pba=业务账户对象,expDate=null表示业务无限期
    **/
    public PlateformMerchantUtiAccount merchantUTIAccountReset(String mcode,Date expDate,String opeName,
                                                               String defBackUrl,String validIpList,
                                                               String backId,String backKey,
                                                               Integer accountState,String accountMemo,
                                                               String appRsaPubkey,String appRsaPrikey,
                                                               String merRsaPubkey,String merRsaPrikey) throws Exception {
        PlateformMerchantUtiAccount pmua = getUtiAccountByMerchantCode(mcode);
        if (pmua == null || pmua.getId() == null || pmua.getId() <= 0)
            throw new BusiException(1102, "账户对象不合法");
        if (expDate == null)
            expDate = DateUtils.stringToDate("2999-12-31");
        Date curDate = new Date();
        if (expDate.before(curDate))
            throw new BusiException(1003, "到期时间限制业务开通");
        PlateformMerchantUtiAccount updatePmua = new PlateformMerchantUtiAccount();
        updatePmua.setId(pmua.getId());
        updatePmua.setDefBackUrl(StringUtils.defaultString(defBackUrl));
        updatePmua.setValidIpList(StringUtils.defaultString(validIpList));
        updatePmua.setBackId(StringUtils.defaultString(backId));
        updatePmua.setBackKey(StringUtils.defaultString(backKey));
        updatePmua.setAccountState(accountState);
        updatePmua.setAccountMemo(StringUtils.defaultString(accountMemo));
        updatePmua.setAppRsaPubkey(StringUtils.defaultString(appRsaPubkey));
        updatePmua.setAppRsaPrikey(StringUtils.defaultString(appRsaPrikey));
        updatePmua.setMerRsaPubkey(StringUtils.defaultString(merRsaPubkey));
        updatePmua.setMerRsaPrikey(StringUtils.defaultString(merRsaPrikey));
        updatePmua.setBusiExpireTime(expDate);
        updatePmua.setUpdateBy(opeName);
        updatePmua.setUpdateTime(curDate);
        return plateformMerchantUtiAccountService.save(updatePmua,true);
    }
}
