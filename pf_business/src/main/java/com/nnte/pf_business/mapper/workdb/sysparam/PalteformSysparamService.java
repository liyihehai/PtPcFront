package com.nnte.pf_business.mapper.workdb.sysparam;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PalteformSysparamService extends BaseService<PalteformSysparamDao,PalteformSysparam> {
    public PalteformSysparamService(){
        super(PalteformSysparamDao.class);
    }

    public List<PalteformSysparam> queryPlateformParamsByKeys(String keys){
        try (connDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList("queryPlateformParamsByKeys",keys);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

