package com.nnte.pf_basic.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.AuthTokenDetailsDTO;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.entertity.AppFuncFactory;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFBasicComponent extends BaseComponent {
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;
    @Autowired
    private DataLibraryComponent dataLibraryComponent;
    @Autowired
    private PFServiceCommonMQ pfServiceCommonMQ;
    @Autowired
    private CruxOpeMQComponent cruxOpeMQComponent;

    public void loadFuncPathToFactory(String paramKey,AppFuncFactory factory){
        String ArrayJson = plateformSysParamComponent.getSingleParamVText(paramKey);
        if (ArrayJson == null)
            return;
        JsonNode jsonNode = JsonUtil.jsonToNode(ArrayJson);
        if (jsonNode != null && jsonNode.isArray() && jsonNode.size() > 0) {
            for (int i = 0; i < jsonNode.size(); i++) {
                AppFuncFactory.AppFuncFilePath af = JsonUtil.jsonToBean(jsonNode.get(i).toString(), AppFuncFactory.AppFuncFilePath.class);
                if (af != null && af.getAppCode().equals(factory.getAppCode())) {
                    factory.getFuncPathMap().put(af.getFuncCode(), af);
                }
            }
        }
    }
    public String makeParamKey(String appCode){
        return  "FILEUPLOADAPP_" + appCode;
    }
    public Map<String, AppFuncFactory> uploadPathParam(){
        Map<String, AppFuncFactory> appFuncFactoryMap = new HashedMap();
        List<KeyValue> kvs = dataLibraryComponent.getValidLibItems("0000", null, null);
        if (kvs != null && kvs.size() > 0) {
            for (KeyValue app : kvs) {
                AppFuncFactory factory = new AppFuncFactory();
                factory.setAppCode(app.getKey());
                factory.setAppName(app.getValue().toString());
                appFuncFactoryMap.put(factory.getAppCode(), factory);
                String paramKey = makeParamKey(factory.getAppCode());
                loadFuncPathToFactory(paramKey,factory);
            }
        }
        return appFuncFactoryMap;
    }

    public String isAppCodeValid(String appCode) throws Exception{
        List<KeyValue> kvs = dataLibraryComponent.getValidLibItems("0000", null, null);
        if (kvs != null && kvs.size() > 0) {
            for(KeyValue kv:kvs){
                if (kv.getKey().equals(appCode)){
                    return kv.getValue().toString();
                }
            }
        }
        throw new BusiException("应用代码无效");
    }
    public void saveUploadPathFactory(String appCode,List<AppFuncFactory.AppFuncFilePath> appFuncFilePathList,
                                      String operatorCode,String operatorName) throws Exception{
        isAppCodeValid(appCode);
        String paramKey = "FILEUPLOADAPP_" + appCode;
        if (appFuncFilePathList.size()>0){
            for(AppFuncFactory.AppFuncFilePath affp:appFuncFilePathList){
                if (StringUtils.isEmpty(affp.getFuncCode()))
                    throw new BusiException("应用编号无效");
                if (StringUtils.isEmpty(affp.getPath()))
                    throw new BusiException("路径不能为空");
                if (StringUtils.isEmpty(affp.getPathMask()))
                    throw new BusiException("路径掩码不能为空");
            }
        }
        String param = JsonUtil.beanToJson(appFuncFilePathList);
        int result = plateformSysParamComponent.saveSingleParams("APPOPS","文件上传管理应用模块路径",
                null,paramKey,PlateformSysParamComponent.SysparamValCol.VAL_TXT,
                param,operatorCode,operatorName);
        if (result!=0)
            throw new BusiException("保存参数失败");
        cruxOpeMQComponent.sendCruxOperate(operatorName,"0002-1","更改文件上传路径参数",paramKey,param);
    }

    public AppFuncFactory deleteUploadPathItem(String appCode,String funcCode,
                                     String operatorCode,String operatorName) throws Exception{
        String appName=isAppCodeValid(appCode);
        AppFuncFactory factory = new AppFuncFactory();
        factory.setAppCode(appCode);
        factory.setAppName(appName);
        String paramKey = makeParamKey(appCode);
        loadFuncPathToFactory(paramKey,factory);
        if (factory.getFuncPathMap()==null || factory.getFuncPathMap().size()<=0)
            throw new BusiException("没有功能编号为["+funcCode+"]的文件上传路径配置(1)");
        AppFuncFactory.AppFuncFilePath path=factory.getFuncPathMap().get(funcCode);
        if (path==null)
            throw new BusiException("没有功能编号为["+funcCode+"]的文件上传路径配置(2)");
        factory.getFuncPathMap().remove(funcCode);
        List<AppFuncFactory.AppFuncFilePath> newList=new ArrayList<>();
        Set<String> keys=factory.getFuncPathMap().keySet();
        for(String key:keys){
            newList.add(factory.getFuncPathMap().get(key));
        }
        saveUploadPathFactory(appCode,newList,operatorCode,operatorName);
        return factory;
    }
    //通知服务器重载文件上传地址参数
    public void notifyReloadUploadPath(String operatorCode,String operatorName) throws Exception{
        pfServiceCommonMQ.notifyReloadUploadPath();
        cruxOpeMQComponent.sendCruxOperate(operatorName,"0002-2","通知服务器重载文件上传地址参数",operatorCode,operatorName);
    }

    public JwtUtils createTokenJwt() {
        String secretKey = plateformSysParamComponent.getSingleParamV100("SYS_SECRETKEY");
        String sign = plateformSysParamComponent.getSingleParamV100("SYS_SIGNATUREALGORITHM");
        String expireTime = plateformSysParamComponent.getSingleParamV100("SYS_TOKENEXPIRETIME");
        JwtUtils jwt = new JwtUtils();
        jwt.initJwtParams(SignatureAlgorithm.forName(sign), secretKey, expireTime);
        return jwt;
    }
    /**
     * 校验请求的Token,拦截器调用
     */
    public Map<String, Object> checkRequestToken(String token, String loginIp) throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        JwtUtils jwt = createTokenJwt();
        try {
            String ipWhileList = plateformSysParamComponent.getSingleParamVText("SYS_IP_WHITE_LIST");
            AuthTokenDetailsDTO atd = jwt.parseAndValidate(token);
            if (StringUtils.isNotEmpty(ipWhileList) && IpUtil.isPermited(loginIp,ipWhileList))
                throw new BusiException("Ip地址不合法");
            OperatorInfo opeInfo = new OperatorInfo();
            opeInfo.setOperatorCode(atd.getUserCode());
            opeInfo.setOperatorName(atd.getUserName());
            Date now = new Date();
            Date preExpTime = new Date((now.getTime() + 60 * 60 * 1000));//计算当前时间之后1小时的时间
            if (preExpTime.after(atd.getExpirationDate())) {
                //如果当前时间向后推1小时Token将要到期，要重新生成Token,此功能实现Token自动延期
                opeInfo.setLoginTime(DateUtils.dateToString(now, DateUtils.DF_YMDHMS));
                opeInfo.setToken(jwt.createJsonWebToken(atd));
            } else {
                opeInfo.setLoginTime(DateUtils.dateToString(new Date(atd.getExpirationDate().getTime() - jwt.getExpiredTime()),
                        DateUtils.DF_YMDHMS));
                opeInfo.setToken(token);
            }
            ret.put("OperatorInfo", opeInfo);
            BaseNnte.setRetTrue(ret,"token验证成功");
        } catch (Exception e) {
            throw new BusiException(e,1009);
        }
        return ret;
    }
}
