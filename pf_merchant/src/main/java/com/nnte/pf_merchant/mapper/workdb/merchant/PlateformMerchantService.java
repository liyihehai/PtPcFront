package com.nnte.pf_merchant.mapper.workdb.merchant;

import com.nnte.framework.base.BaseService;
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
            return cds.getSqlSession().selectOne(getFullStatementName("findMaxMerchantCode"));
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }

    public List<PlateformMerchant> findPlateformMerchansCustmerList(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList(getFullStatementName("findPlateformMerchansCustmerList"),paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }

    public Integer findPlateformMerchansCustmerCount(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectOne(getFullStatementName("findPlateformMerchansCustmerCount"),paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }
}

