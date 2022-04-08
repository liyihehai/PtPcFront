package com.nnte.pf_pc_front.interceptor;

import com.nnte.framework.annotation.ConfigLoad;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.PlateformSysParamComponent;
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
    @ConfigLoad
    private ConfigInterface appconfig;
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;

    private String corsAllowMethods = "GET,PUT,POST,OPTIONS,DELETE";

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
    private void setCors(HttpServletRequest request, HttpServletResponse response){
        // 不使用*，自动适配跨域域名，避免携带Cookie时失效
        String origin = request.getHeader("Origin");
        if(StringUtils.isNotEmpty(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        // 自适应所有自定义头
        String headers = request.getHeader("Access-Control-Request-Headers");
        if(StringUtils.isNotEmpty(headers)) {
            response.setHeader("Access-Control-Allow-Headers", headers);
            response.setHeader("Access-Control-Expose-Headers", headers);
        }

        // 允许跨域的请求方法类型
        response.setHeader("Access-Control-Allow-Methods", corsAllowMethods);
        // 预检命令（OPTIONS）缓存时间，单位：秒
        response.setHeader("Access-Control-Max-Age", "1800");
        // 明确许可客户端发送Cookie，不允许删除字段即可
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setCors(request,response);
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
