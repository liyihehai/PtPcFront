package com.nnte.pf_business.mapper.workdb.merchant;

import com.nnte.framework.base.BaseService;
import com.nnte.framework.utils.LogUtil;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApply;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlateformMerchantService extends BaseService<PlateformMerchantDao,PlateformMerchant> {
    public PlateformMerchantService(){
        super(PlateformMerchantDao.class);
    }
    /**
     * 取得系统中最大商户编号
     * */
    public String findMaxMerchantCode(){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectOne("findMaxMerchantCode");
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }

    public List<PlateformMerchant> findPlateformMerchansCustmerList(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("findPlateformMerchansCustmerList",paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }

    public Integer findPlateformMerchansCustmerCount(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectOne("findPlateformMerchansCustmerCount",paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }
}

