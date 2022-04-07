package com.nnte.pf_basic.mapper.workdb.library;

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class PlateformLibraryService extends BaseService<PlateformLibraryDao,PlateformLibrary> {
    public PlateformLibraryService(){
        super(PlateformLibraryDao.class);
    }
}

