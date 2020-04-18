package com.nnte.pf_business.component;

import com.nnte.framework.base.ConnSqlSessionFactory;
import com.nnte.framework.base.DataLibrary;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.mapper.workdb.sysparam.PalteformSysparam;
import com.nnte.pf_business.mapper.workdb.sysparam.PalteformSysparamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
/**
 * 平台系统参数组件
 * */
public class PlateformSysParamComponent implements WatchInterface{

    private static String preprocessParams = "'SYS_REPAIRING'";
    /**
     * 系统参数MAP，定时刷新保存preprocessParams定义的单值参数，程序可优先从MAP中获取
     * 参数定义，避免每次都通过数据库操作取得参数
     * */
    private static Map<String,PalteformSysparam> paramsMap = new HashMap<>();

    /**
     * 实现守护功能，定时从系统参数中取得关键参数，避免程序每次都要
     * 通过执行数据库查询才能取得系统参数
     * */
    @Override
    public void runWatch() {
        List<PalteformSysparam> list=palteformSysparamService.queryPlateformParamsByKeys(preprocessParams);
        if (list!=null && list.size()>0){
            for(PalteformSysparam param:list){
                paramsMap.put(param.getParamKey(),param);
            }
        }
    }

    public enum SysparamValCol{
        VAL_100,
        VAL_200,
        VAL_TXT
    }

    @Autowired
    private PalteformSysparamService palteformSysparamService;
    /**
     * 取得商户多值参数队列：商户多值参数通过class_id(分类号)取得，生成Key - Value键值对列表
     * */
    public List<KeyValue> getMulKVParams(String groupCode){
        List<PalteformSysparam> list=getMulParams(groupCode);
        if (list==null || list.size()<=0)
            return null;
        List<KeyValue> ret = new ArrayList<>();
        for(PalteformSysparam p:list){
            ret.add(new KeyValue(p.getParamKey(),p.getValue1()));
        }
        return ret;
    }
    /**
     * 取得多值参数列表
     * */
    public List<PalteformSysparam> getMulParams(String groupCode){
        if (StringUtils.isEmpty(groupCode))
            return null;
        PalteformSysparam dto = new PalteformSysparam();
        dto.setParamKeyGroup(groupCode);    //按分类号查询多键值对
        List<PalteformSysparam> list=palteformSysparamService.findModelList(dto);
        if (list==null || list.size()<=0)
            return null;
        return list;
    }
    /**
     * 取得商户多值参数的单项设置
     * */
    public PalteformSysparam getMulParamItem(String groupCode,String key){
        List<PalteformSysparam> list=getMulParams(groupCode);
        if (list==null || list.size()<=0)
            return null;
        for(PalteformSysparam bsp:list){
            if (bsp.getParamKey().equals(key))
                return bsp;
        }
        return null;
    }
    /**
     * 取得商户单参数对象
     * */
    public PalteformSysparam getSingleParamObj(String key){
        if (paramsMap!=null && paramsMap.size()>0){
            PalteformSysparam param=paramsMap.get(key);
            if (param!=null)
                return param;
        }
        PalteformSysparam dto = new PalteformSysparam();
        dto.setParamKey(key);
        List<PalteformSysparam> list=palteformSysparamService.findModelList(dto);
        if (list==null || list.size()<=0)
            return null;
        return list.get(0);
    }
    /**
     * 取得商户单值参数：商户单值参数通过key取得Value值
     * */
    public String getSingleParams(String key,SysparamValCol vc){
        if (StringUtils.isEmpty(key))
            return null;
        PalteformSysparam bsp=getSingleParamObj(key);
        if (bsp==null || bsp.getParamState()==null || !bsp.getParamState().equals(1))
            return null;
        if (vc!=null) {
            if (vc.equals(SysparamValCol.VAL_100))
                return bsp.getValue1();
            if (vc.equals(SysparamValCol.VAL_200))
                return bsp.getValue2();
            if (vc.equals(SysparamValCol.VAL_TXT))
                return bsp.getValueText();
        }
        return bsp.getValue1();
    }
    /**
     * 保存商户单值参数（返回为0表示操作成功，失败返回非0）
     * */
    public Integer saveSingleParams(ConnSqlSessionFactory cssf,String key, SysparamValCol vc,
                                            String value, String opeCode, String opeName){
        PalteformSysparam insertDto = getSingleParamObj(key);
        if (insertDto==null)
        {
            insertDto=new PalteformSysparam();
            insertDto.setParamName(DataLibrary.getSysParamDesc(insertDto.getParamType(),key));
            insertDto.setParamKey(key);             //按分类号查询多键值对
            insertDto.setParamState(1);             //参数状态为有效
            insertDto.setCreateTime(new Date());
            insertDto.setCreateOpeCode(opeCode);
            insertDto.setCreateOpeName(opeName);
        }
        else {
            if (insertDto.getParamState()==null || !insertDto.getParamState().equals(1))
                return -1;
        }
        if (vc!=null) {
            if (vc.equals(SysparamValCol.VAL_100))
                insertDto.setValue1(value);
            if (vc.equals(SysparamValCol.VAL_200))
                insertDto.setValue2(value);
            if (vc.equals(SysparamValCol.VAL_TXT))
                insertDto.setValueText(value);
        }else
            insertDto.setValue1(value);
        PalteformSysparam saveDto=palteformSysparamService.save(cssf,insertDto,false);
        if (saveDto!=null && saveDto.getId()!=null && saveDto.getId()>0)
            return 0;
        return 9999;
    }
}
