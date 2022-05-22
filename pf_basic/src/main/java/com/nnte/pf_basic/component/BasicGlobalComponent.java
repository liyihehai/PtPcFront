package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class BasicGlobalComponent extends BaseComponent {
    @Autowired
    private PlateformOperatorService plateformOperatorService;
    @Autowired
    private DataLibraryComponent dataLibraryComponent;

    public PlateformOperator getOperatorByCode(String code){
        PlateformOperator dto=new PlateformOperator();
        dto.setOpeCode(code);
        List<PlateformOperator> list=plateformOperatorService.findModelList(dto);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
    //获取商业应用的 编号-名称 Map结构
    public Map<String,Object> getBusiAppNameMap(){
        Map<String,Object> retMap = new HashMap<>();
        try {
            retMap = dataLibraryComponent.getValidLibMap("Y001");
        }catch (Exception e){
            outLogExp(e);
        }
        return retMap;
    }
}
