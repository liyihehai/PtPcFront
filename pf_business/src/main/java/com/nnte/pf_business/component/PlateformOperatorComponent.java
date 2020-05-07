package com.nnte.pf_business.component;

import com.nnte.basebusi.excption.BusiException;
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
     * 操作员状态
     * */
    public enum OperatorState{
        UNVALID(0), //无效
        VALID(1),   //有效
        PAUSH(2);   //暂停
        private int value;
        OperatorState(int val){
            value = val;
        }
        public int getValue(){return value;}
        public static OperatorState fromInt(int val) throws BusiException{
            switch (val){
                case 0:return UNVALID;
                case 1:return VALID;
                case 2:return PAUSH;
            }
            throw new BusiException("无效的操作员状态值");
        }
    }
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
