package com.nnte.pf_business.mapper.workdb.sysparam;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlateformSysparamService extends BaseService<PlateformSysparamDao,PlateformSysparam> {
    public PlateformSysparamService(){
        super(PlateformSysparamDao.class);
    }
	
	public List<PlateformSysparam> queryPlateformParamsByKeys(String keys){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryPlateformParamsByKeys",keys);
        }catch (Exception e){
            outLogExp(e);
            return null;
        }	
    }
}

