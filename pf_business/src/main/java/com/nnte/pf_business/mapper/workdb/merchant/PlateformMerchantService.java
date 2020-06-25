package com.nnte.pf_business.mapper.workdb.merchant;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformMerchantService extends BaseService<PlateformMerchantDao,PlateformMerchant> {
    public PlateformMerchantService(){
        super(PlateformMerchantDao.class);
    }
    /**
     * 取得系统中最大商户编号
     * */
    public String findMaxMerchantCode(){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectOne("findMaxMerchantCode");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

