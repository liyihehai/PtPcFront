package com.nnte.pf_basic.mapper.workdb.library;

import com.nnte.framework.base.BaseService;
import com.nnte.framework.entity.PageData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PlateformLibraryService extends BaseService<PlateformLibraryDao,PlateformLibrary> {
    public PlateformLibraryService(){
        super(PlateformLibraryDao.class);
    }

    public PageData<PlateformLibrary> getDefaultListPageData(Map<String, Object> paramMap,
                                                             Integer pageNo, Integer pageSize) throws Exception {
        return getListPageData(getFullStatementName("findModelListByMap"),paramMap,pageNo,pageSize);
    }
}

