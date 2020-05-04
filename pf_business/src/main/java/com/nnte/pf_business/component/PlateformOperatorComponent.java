package com.nnte.pf_business.component;

import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * 操作员管理组件
 * */
public class PlateformOperatorComponent {
    @Autowired
    private PlateformOperatorService plateformOperatorService;
    /**
     * 通过操作员编码查询操作员信息
     * */
    public PlateformOperator getOperatorByCode(String code){
        PlateformOperator dto=new PlateformOperator();
        dto.setOpeCode(code);
        List<PlateformOperator> list=plateformOperatorService.findModelList(dto);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
}
