package com.nnte.pf_pc_front.interceptor;

import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.PlateformSysParamComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_pc_front.config.AppRootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class PfPcFrontMainInterceptor implements HandlerInterceptor {
    @Autowired
    private AppRootConfig appRootConfig;
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;

    private boolean excludePathPatterns(String path){
        String[] excludes={ ".*/applyVerify.*",
                            ".*/sysRepairing.*",
                            ".*/error.*",
                            ".*/resources/.*",
                            ".*/api/priCheck/.*",
                            ".*/api/login/account/.*"};
        for(String exclude:excludes){
            if (Pattern.matches(exclude, path))
                return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        BaseController.setCors(request,response);
        if (excludePathPatterns(request.getServletPath()))
            return true;
        //查询系统参数判断系统是否处于维护状态
        String sysRepairing=plateformSysParamComponent.getSingleParams("SYS_REPAIRING", PlateformSysParamComponent.SysparamValCol.VAL_100);
        if (StringUtils.isNotEmpty(sysRepairing) && sysRepairing.equals("1")){
            request.getRequestDispatcher("/main/sysRepairing").forward(request,response);
            return false;
        }
        //请求中添加系统数据环境
        Map<String, Object> envData = new HashMap<>();
        envData.put("contextPath",request.getContextPath());
        envData.put("debug", appRootConfig.getDebug().toUpperCase());
        envData.put("AppCode", AppBasicConfig.App_Code);
        envData.put("AppName", AppBasicConfig.App_Name);
        envData.put("staticRoot", appRootConfig.getStaticRoot());
        envData.put("localHostName", appRootConfig.getLocalHostName());
        envData.put("uploadStaticRoot", appRootConfig.getUploadStaticRoot());
        envData.put("uploadFileServiceURL", appRootConfig.getUploadFileServiceURL());
        request.setAttribute("envData",envData);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
