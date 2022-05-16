package com.nnte.pf_uti.config;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.ModuleInterface;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.pf_source.uti.request.UtiRequest;
import com.nnte.pf_source.uti.request.UtiURL;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@BusiLogAttr(PFUTIConfig.loggerName)
public class PFUTIConfig extends BaseComponent implements ModuleInterface {
    public static final String loggerName = "PFUTInterface";
    public static final String jarName = "pf_uti.jar";

    public final static String Module_Code = "UTInterface"; //统一交易接口
    static {
        AppRegistry.setAppModuleName(Module_Code,"统一交易接口");
    }

    private final static Map<String,Class<? extends UtiRequest>> utiUrlClassMap = new HashedMap();

    @Override
    public void initModule() {
        String pack = "com.nnte.pf_source.uti";
        Set<Class<? extends UtiRequest>> classSet = BeanUtils.getPackSubClass(pack, UtiRequest.class);
        if (classSet!=null && classSet.size()>0){
            for(Class<? extends UtiRequest> clazz:classSet){
                UtiURL utiURL=clazz.getAnnotation(UtiURL.class);
                if (utiURL!=null)
                    utiUrlClassMap.put(utiURL.url(),clazz);
            }
        }
    }

    public Class<? extends UtiRequest> getClassByUrl(String url){return utiUrlClassMap.get(url);}

    @Override
    public String getModuleJarName() {
        return jarName;
    }

    @Override
    public String getModuleLoggerName() {
        return loggerName;
    }
}
