package com.nnte.pf_pc_front.interceptor;

import com.nnte.framework.annotation.ConfigLoad;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PlateformSysParamComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class PfPcFrontMainInterceptor implements HandlerInterceptor {
    @ConfigLoad
    private ConfigInterface appconfig;
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查询系统参数判断系统是否处于维护状态
        String sysRepairing=plateformSysParamComponent.getSingleParams("SYS_REPAIRING", PlateformSysParamComponent.SysparamValCol.VAL_100);
        if (StringUtils.isNotEmpty(sysRepairing) && sysRepairing.equals("1")){
            request.getRequestDispatcher("/main/sysRepairing").forward(request,response);
            return false;
        }
        //请求中添加系统数据环境
        Map<String, Object> envData = new HashMap<>();
        envData.put("contextPath",request.getContextPath());
        envData.put("debug", appconfig.getConfig("debug").toUpperCase());
        envData.put("staticRoot", appconfig.getConfig("staticRoot"));
        envData.put("localHostName", appconfig.getConfig("localHostName"));
        envData.put("uploadStaticRoot", appconfig.getConfig("uploadStaticRoot"));
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
