package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.WatchAttr;
import com.nnte.basebusi.annotation.WatchInterface;
import com.nnte.framework.annotation.WorkDBAspect;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.BaseService;
import com.nnte.framework.base.DataLibrary;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.sysparam.PlateformSysparam;
import com.nnte.pf_basic.mapper.workdb.sysparam.PlateformSysparamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@WorkDBAspect
/**
 * 平台系统参数组件
 * */
@WatchAttr(value = 100)
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PlateformSysParamComponent implements WatchInterface {

    /**
     * 系统参数
     */
    public static final String SYS_SMTP_ACCOUNT_JSON = "SYS_SMTP_ACCOUNT_JSON";

    private static StringBuffer preprocessParamsBuffer = new StringBuffer();

    static {
        preprocessParamsBuffer.append("'SYS_REPAIRING'").append(",");           //系统维修参数
        preprocessParamsBuffer.append("'SYS_SECRETKEY'").append(",");           //用于Token生成的secretKey
        preprocessParamsBuffer.append("'SYS_SIGNATUREALGORITHM'").append(",");  //用于Token生成的signatureAlgorithm加密方式
        preprocessParamsBuffer.append("'SYS_TOKENEXPIRETIME'");                 //生成的Token的超时时间
    }

    /**
     * 系统参数MAP，定时刷新保存preprocessParams定义的单值参数，程序可优先从MAP中获取
     * 参数定义，避免每次都通过数据库操作取得参数
     */
    private static Map<String, PlateformSysparam> paramsMap = new HashMap<>();

    @Autowired
    private PlateformSysparamService plateformSysparamService;

    /**
     * 实现守护功能，定时从系统参数中取得关键参数，避免程序每次都要
     * 通过执行数据库查询才能取得系统参数
     */
    @Override
    public void runWatch() {
        Map<String, Object> ret = queryPlateformParamsByKeys(preprocessParamsBuffer.toString());
        if (BaseNnte.getRetSuc(ret)) {
            List<PlateformSysparam> list = (List<PlateformSysparam>) ret.get("list");
            if (list != null && list.size() > 0) {
                for (PlateformSysparam param : list) {
                    paramsMap.put(param.getParamKey(), param);
                }
            }
        }
    }

    public Map<String, Object> queryPlateformParamsByKeys(String keys) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            List list = plateformSysparamService.queryPlateformParamsByKeys(keys);
            ret.put("list", list);
            BaseNnte.setRetTrue(ret, "success");
            return ret;
        } catch (Exception e) {
            BaseNnte.setRetFalse(ret, 1002, e.getMessage());
            return ret;
        }
    }

    public enum SysparamValCol {
        VAL_100,
        VAL_200,
        VAL_TXT
    }

    /**
     * 取得商户多值参数队列：商户多值参数通过class_id(分类号)取得，生成Key - Value键值对列表
     */
    public List<KeyValue> getMulKVParams(String groupCode) {
        List<PlateformSysparam> list = getMulParams(groupCode);
        if (list == null || list.size() <= 0)
            return null;
        List<KeyValue> ret = new ArrayList<>();
        for (PlateformSysparam p : list) {
            ret.add(new KeyValue(p.getParamKey(), p.getValue1()));
        }
        return ret;
    }

    /**
     * 取得多值参数列表
     */
    public List<PlateformSysparam> getMulParams(String groupCode) {
        if (StringUtils.isEmpty(groupCode))
            return null;
        PlateformSysparam dto = new PlateformSysparam();
        dto.setParamKeyGroup(groupCode);    //按分类号查询多键值对
        List<PlateformSysparam> list = plateformSysparamService.findModelList(dto);
        if (list == null || list.size() <= 0)
            return null;
        return list;
    }

    /**
     * 取得商户多值参数的单项设置
     */
    public PlateformSysparam getMulParamItem(String groupCode, String key) {
        List<PlateformSysparam> list = getMulParams(groupCode);
        if (list == null || list.size() <= 0)
            return null;
        for (PlateformSysparam bsp : list) {
            if (bsp.getParamKey().equals(key))
                return bsp;
        }
        return null;
    }

    /**
     * 取得商户单参数对象
     */
    public PlateformSysparam getSingleParamObj(String key) {
        if (paramsMap != null && paramsMap.size() > 0) {
            PlateformSysparam param = paramsMap.get(key);
            if (param != null)
                return param;
        }
        PlateformSysparam dto = new PlateformSysparam();
        dto.setParamKey(key);
        List<PlateformSysparam> list = plateformSysparamService.findModelList(dto);
        if (list == null || list.size() <= 0)
            return null;
        return list.get(0);
    }

    /**
     * 取得商户单值参数：商户单值参数通过key取得Value值
     */
    public String getSingleParams(String key, SysparamValCol vc) {
        if (StringUtils.isEmpty(key))
            return null;
        PlateformSysparam bsp = getSingleParamObj(key);
        if (bsp == null || bsp.getParamState() == null || !bsp.getParamState().equals(1))
            return null;
        if (vc != null) {
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
     * 一下为直接取出单值参数的值
     */
    public String getSingleParamV100(String key) {
        return getSingleParams(key, SysparamValCol.VAL_100);
    }

    public String getSingleParamV200(String key) {
        return getSingleParams(key, SysparamValCol.VAL_200);
    }

    public String getSingleParamVText(String key) {
        return getSingleParams(key, SysparamValCol.VAL_TXT);
    }

    /**
     * 保存商户单值参数（返回为0表示操作成功，失败返回非0）
     */
    public Integer saveSingleParams(BaseService.ConnDaoSession session, String key, SysparamValCol vc,
                                    String value, String opeCode, String opeName) {
        PlateformSysparam insertDto = getSingleParamObj(key);
        if (insertDto == null) {
            insertDto = new PlateformSysparam();
            insertDto.setParamName(DataLibrary.getSysParamDesc(insertDto.getParamType(), key));
            insertDto.setParamKey(key);             //按分类号查询多键值对
            insertDto.setParamState(1);             //参数状态为有效
            insertDto.setCreateTime(new Date());
            insertDto.setCreateOpeCode(opeCode);
            insertDto.setCreateOpeName(opeName);
        } else {
            if (insertDto.getParamState() == null || !insertDto.getParamState().equals(1))
                return -1;
        }
        if (vc != null) {
            if (vc.equals(SysparamValCol.VAL_100))
                insertDto.setValue1(value);
            if (vc.equals(SysparamValCol.VAL_200))
                insertDto.setValue2(value);
            if (vc.equals(SysparamValCol.VAL_TXT))
                insertDto.setValueText(value);
        } else
            insertDto.setValue1(value);
        PlateformSysparam saveDto = plateformSysparamService.save(session, insertDto, false);
        if (saveDto != null && saveDto.getId() != null && saveDto.getId() > 0)
            return 0;
        return 9999;
    }
}
