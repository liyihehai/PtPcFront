package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class BasicGlobalComponent extends BaseComponent {
    @Autowired
    private PlateformOperatorService plateformOperatorService;

    public PlateformOperator getOperatorByCode(String code){
        PlateformOperator dto=new PlateformOperator();
        dto.setOpeCode(code);
        List<PlateformOperator> list=plateformOperatorService.findModelList(dto);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
}
