package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.WatchAttr;
import com.nnte.basebusi.annotation.WatchInterface;
import com.nnte.framework.annotation.WorkDBAspect;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.BaseService;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.entertity.SysParamObj;
import com.nnte.pf_basic.mapper.workdb.sysparam.PlateformSysparam;
import com.nnte.pf_basic.mapper.workdb.sysparam.PlateformSysparamService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@WorkDBAspect
/**
 * 平台系统参数组件
 * */
@WatchAttr(value = "LoadAllSysParams",cron="0 0/5 0-23 * * ? ")//加载系统参数，每5分钟执行一次
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PlateformSysParamComponent implements WatchInterface {

    public enum SysParamValCol {
        VAL_100,
        VAL_200,
        VAL_TXT
    }

    private final static Map<String,SysParamObj> staticSysParamObjMap = new HashedMap();//静态系统参数MAP
    private final static List<SysParamObj> asyncParamPrefixList = new ArrayList<>();    //动态参数前缀定义列表
    public static void addStaticSysParam(SysParamObj sysParamObj){
        staticSysParamObjMap.put(sysParamObj.getParamKey(),sysParamObj);
    }
    /**
     * 系统参数(静态参数:Key值固定的参数)
     */
    public static final String SYS_REPAIRING = "SYS_REPAIRING";
    public static final String SYS_SECRETKEY = "SYS_SECRETKEY";
    public static final String SYS_SIGNATUREALGORITHM = "SYS_SIGNATUREALGORITHM";
    public static final String SYS_TOKENEXPIRETIME = "SYS_TOKENEXPIRETIME";
    public static final String SYS_SMTP_ACCOUNT_JSON = "SYS_SMTP_ACCOUNT_JSON";
    public static final String SYS_STATIC_URL_ROOT = "SYS_STATIC_URL_ROOT";
    public static final String SYS_IP_WHITE_LIST = "SYS_IP_WHITE_LIST";

    static {
        addStaticSysParam(new SysParamObj(SYS_REPAIRING, SysParamValCol.VAL_100));
        addStaticSysParam(new SysParamObj(SYS_SECRETKEY, SysParamValCol.VAL_100));
        addStaticSysParam(new SysParamObj(SYS_SIGNATUREALGORITHM, SysParamValCol.VAL_100));
        addStaticSysParam(new SysParamObj(SYS_TOKENEXPIRETIME, SysParamValCol.VAL_100));
        addStaticSysParam(new SysParamObj(SYS_SMTP_ACCOUNT_JSON, SysParamValCol.VAL_TXT));
        addStaticSysParam(new SysParamObj(SYS_STATIC_URL_ROOT, SysParamValCol.VAL_100));
        addStaticSysParam(new SysParamObj(SYS_IP_WHITE_LIST, SysParamValCol.VAL_TXT));
    }
    /**
     * 动态参数前缀定义（Key值不固定，本列表定义前缀，参数加载时将依据本列表添加静态参数列表）
     * */
    public static void addAsyncParamPrefix(SysParamObj sysParamObj){
        sysParamObj.setIsAsync("1");//设置为动态参数
        asyncParamPrefixList.add(sysParamObj);
    }
    static {
        //文件上传管理应用模块路径
        addAsyncParamPrefix(new SysParamObj("FILEUPLOADAPP",SysParamValCol.VAL_TXT));
    }
    @Autowired
    private PlateformSysparamService plateformSysparamService;

    private void setParamObjValue(SysParamObj paramObj){
        if (paramObj!=null && paramObj.getValCol() != null && paramObj.getParamObj()!=null) {
            PlateformSysparam param = paramObj.getParamObj();
            if (paramObj.getValCol().equals(SysParamValCol.VAL_100))
                paramObj.setParamValue(param.getValue1());
            else if (paramObj.getValCol().equals(SysParamValCol.VAL_200))
                paramObj.setParamValue(param.getValue2());
            else if (paramObj.getValCol().equals(SysParamValCol.VAL_TXT))
                paramObj.setParamValue(param.getValueText());
        }
    }
    private void loadAsyncParam(PlateformSysparam param){
        if (param!=null && param.getParamKey()!=null) {
            for (SysParamObj sysParamObj : asyncParamPrefixList) {
                if (sysParamObj.getParamKey() != null &&
                        param.getParamKey().indexOf(sysParamObj.getParamKey()) == 0) {
                    SysParamObj newAsyncParam = new SysParamObj(param.getParamKey(),sysParamObj.getValCol());
                    newAsyncParam.setIsAsync(sysParamObj.getIsAsync());
                    newAsyncParam.setParamObj(param);
                    newAsyncParam.setParamName(param.getParamName());
                    setParamObjValue(newAsyncParam);
                    staticSysParamObjMap.put(newAsyncParam.getParamKey(),newAsyncParam);
                }
            }
        }
    }
    /**
     * 初始化加载所有系统参数
     * */
    public void initLoadAllSysParams(){
        Map<String, Object> ret = queryPlateformParams();
        if (BaseNnte.getRetSuc(ret)) {
            List<PlateformSysparam> list = (List<PlateformSysparam>) ret.get("list");
            if (list != null && list.size() > 0) {
                for (PlateformSysparam param : list) {
                    SysParamObj obj=staticSysParamObjMap.get(param.getParamKey());
                    if (obj!=null) {
                        obj.setParamName(param.getParamName());
                        obj.setParamObj(param);
                        setParamObjValue(obj);
                    }else{
                        //如果不在静态函数中，就尝试通过动态函数前缀匹配加载
                        loadAsyncParam(param);
                    }
                }
            }
        }
    }
    /**
     * 实现守护功能，定时从系统参数中取得关键参数，避免程序每次都要
     * 通过执行数据库查询才能取得系统参数
     */
    @Override
    public void runWatch() {
        initLoadAllSysParams();
    }

    public Map<String, Object> queryPlateformParams() {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            PlateformSysparam dto = new PlateformSysparam();
            dto.setParamState(1);//只加载有效的系统参数
            List list = plateformSysparamService.findModelList(dto);
            ret.put("list", list);
            BaseNnte.setRetTrue(ret, "success");
            return ret;
        } catch (Exception e) {
            BaseNnte.setRetFalse(ret, 1002, e.getMessage());
            return ret;
        }
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
        SysParamObj sysParamObj=staticSysParamObjMap.get(key);
        if (sysParamObj!=null)
            return sysParamObj.getParamObj();
        return null;
    }

    public String getSingleParams(String key) {
        SysParamObj sysParamObj=staticSysParamObjMap.get(key);
        if (sysParamObj!=null)
            return sysParamObj.getParamValue();
        return null;
    }

    /**
     * 取得商户单值参数：商户单值参数通过key取得Value值
     */
    public String getSingleParams(String key, SysParamValCol vc) {
        if (StringUtils.isEmpty(key))
            return null;
        PlateformSysparam bsp = getSingleParamObj(key);
        if (bsp == null || bsp.getParamState() == null || !bsp.getParamState().equals(1))
            return null;
        if (vc != null) {
            if (vc.equals(SysParamValCol.VAL_100))
                return bsp.getValue1();
            if (vc.equals(SysParamValCol.VAL_200))
                return bsp.getValue2();
            if (vc.equals(SysParamValCol.VAL_TXT))
                return bsp.getValueText();
        }
        return bsp.getValue1();
    }

    /**
     * 一下为直接取出单值参数的值
     */
    public String getSingleParamV100(String key) {
        return getSingleParams(key, SysParamValCol.VAL_100);
    }

    public String getSingleParamV200(String key) {
        return getSingleParams(key, SysParamValCol.VAL_200);
    }

    public String getSingleParamVText(String key) {
        return getSingleParams(key, SysParamValCol.VAL_TXT);
    }

    /**
     * 保存商户单值参数（返回为0表示操作成功，失败返回非0）
     */
    public Integer saveSingleParams(BaseService.ConnDaoSession session,
                                    String paramType,String paramName,String paramKeyGroup,
                                    String key, SysParamValCol vc,
                                    String value, String opeCode, String opeName) {
        PlateformSysparam insertDto = getSingleParamObj(key);
        insertDto.setParamType(paramType);
        insertDto.setParamKeyGroup(paramKeyGroup);
        insertDto.setParamName(paramName);
        if (insertDto == null) {
            insertDto = new PlateformSysparam();
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
            if (vc.equals(SysParamValCol.VAL_100))
                insertDto.setValue1(value);
            if (vc.equals(SysParamValCol.VAL_200))
                insertDto.setValue2(value);
            if (vc.equals(SysParamValCol.VAL_TXT))
                insertDto.setValueText(value);
        } else
            insertDto.setValue1(value);
        PlateformSysparam saveDto;
        if (session==null)
            saveDto = plateformSysparamService.save(insertDto, false);
        else
            saveDto = plateformSysparamService.save(session, insertDto, false);
        if (saveDto != null && saveDto.getId() != null && saveDto.getId() > 0)
            return 0;
        return 9999;
    }

    public Integer saveSingleParams(String key,String value, String opeCode,String opeName) {
        SysParamObj sysParamObj=staticSysParamObjMap.get(key);
        if (sysParamObj==null || sysParamObj.getParamObj()==null)
            return 0;
        PlateformSysparam sysparam=sysParamObj.getParamObj();
        return saveSingleParams(null,sysparam.getParamType(),
                sysparam.getParamName(),sysparam.getParamKeyGroup(),key,
                sysParamObj.getValCol(),value,opeCode,opeName);
    }
    @Deprecated //不推荐使用该函数
    public Integer saveSingleParams(String paramType,String paramName,String paramKeyGroup,
                                    String key, SysParamValCol vc,String value, String opeCode,
                                    String opeName) {
        return saveSingleParams(null,paramType,paramName,paramKeyGroup,key,vc,value,opeCode,opeName);
    }
    //-------------------------------------------------------
    public String getSysRepairing(){
        return getSingleParams(SYS_REPAIRING);
    }
    public String getSysSecretkey(){
        return getSingleParams(SYS_SECRETKEY);
    }
    public String getSysSignaturealgorithm(){
        return getSingleParams(SYS_SIGNATUREALGORITHM);
    }
    public String getSysTokenexpiretime(){
        return getSingleParams(SYS_TOKENEXPIRETIME);
    }
    public String getSysSmtpAccountJson(){
        return getSingleParams(SYS_SMTP_ACCOUNT_JSON);
    }
    public String getSysStaticUrlRoot(){
        return getSingleParams(SYS_STATIC_URL_ROOT);
    }
    public String getSysIpWhiteList(){
        return getSingleParams(SYS_IP_WHITE_LIST);
    }
}
