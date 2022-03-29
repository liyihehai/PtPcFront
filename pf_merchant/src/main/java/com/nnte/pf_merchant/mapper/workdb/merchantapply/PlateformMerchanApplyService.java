package com.nnte.pf_merchant.mapper.workdb.merchantapply;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlateformMerchanApplyService extends BaseService<PlateformMerchanApplyDao,PlateformMerchanApply> {
    public PlateformMerchanApplyService(){
        super(PlateformMerchanApplyDao.class);
    }

    public List<PlateformMerchanApply> findPlateformMerchanApplysCustmerList(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList(getFullStatementName("findPlateformMerchanApplysCustmerList"),paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }

    public Integer findPlateformMerchanApplysCustmerCount(Map<String,Object> paramMap){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectOne(getFullStatementName("findPlateformMerchanApplysCustmerCount"),paramMap);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }
}

