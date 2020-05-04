package com.nnte.pf_pc_front.interceptor;

import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.ConfigLoad;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class PfPcFrontNormalInterceptor implements HandlerInterceptor {
    @ConfigLoad
    private ConfigInterface appconfig;
    @Autowired
    private PfBusinessComponent pfBusinessComponent;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求进入这个拦截器
        Map<String, Object> envData = (Map)request.getAttribute("envData");
        BaseNnte.outConsoleLog(request.getServletPath());
        if (envData!=null) {
            Map<String,Object> retMap=BaseNnte.newMapRetObj();
            String loginIp=BaseController.getIpAddr(request);
            String token= StringUtils.defaultString(BaseController.getRequestParam(request,null,"token"));
            if (StringUtils.isEmpty(token))
                token=request.getHeader("Postman-Token");
            try {
                Map checkMap=pfBusinessComponent.checkRequestToken(token, loginIp);
                request.setAttribute("OperatorInfo",checkMap.get("OperatorInfo"));
                return true;
            }catch (BusiException be){
                BaseNnte.setRetFalse(retMap,1001,be.getMessage());
            }catch (Exception e){
                BaseNnte.setRetFalse(retMap,1001,"身份验证错误");
            }
            retMap.put("message",retMap.get("msg"));
            request.setAttribute("map",retMap);
            response.sendRedirect(envData.get("contextPath")+"/main/login");
            return false;
        }
        return false;        //有的话就继续操作
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
