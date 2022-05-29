package com.nnte.pf_basic.mapper.workdb.reportModule;

import com.nnte.framework.base.BaseService;
import com.nnte.pf_basic.entertity.ReportModule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlateformReportModuleService extends BaseService<PlateformReportModuleDao,PlateformReportModule> {
    public PlateformReportModuleService(){
        super(PlateformReportModuleDao.class);
    }

    public List<ReportModule> queryAllReportModule(){
        try (ConnDaoSession cds=this.getDefaultConnDaoSession()){
            return cds.getSqlSession().selectList(getFullStatementName("queryAllReportModule"));
        }catch (Exception e){
            outLogExp(e);
            return null;
        }
    }
}

