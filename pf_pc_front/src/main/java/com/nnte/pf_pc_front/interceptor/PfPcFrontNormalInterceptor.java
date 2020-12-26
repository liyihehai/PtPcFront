package com.nnte.pf_pc_front.interceptor;

import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.ConfigLoad;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.base.ConfigInterface;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class PfPcFrontNormalInterceptor implements HandlerInterceptor {
    @ConfigLoad
    private ConfigInterface appconfig;
    @Autowired
    private PfBusinessComponent pfBusinessComponent;

    private boolean excludePathPatterns(String path){
        String[] excludes={".*/applyVerify.*",".*/sysRepairing.*",".*/login.*",".*/loginCheck.*",".*/priCheck.*",
        ".*/error.*",".*/resources/.*"};
        for(String exclude:excludes){
            if (Pattern.matches(exclude, path))
                return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (excludePathPatterns(request.getServletPath()))
            return true;
        //请求进入这个拦截器
        Map<String, Object> envData = (Map)request.getAttribute("envData");
        BaseNnte.outConsoleLog(request.getServletPath());
        int enterType = 0; //页面进入
        if (envData!=null) {
            Map<String,Object> retMap=BaseNnte.newMapRetObj();
            String loginIp=BaseController.getIpAddr(request);
            String token= StringUtils.defaultString(BaseController.getRequestParam(request,null,"token"));
            if (StringUtils.isEmpty(token)) {
                token = request.getHeader("Postman-Token");
                enterType = 1; //Ajax进入
            }
            try {
                Map checkMap=pfBusinessComponent.checkRequestToken(token, loginIp);
                OperatorInfo oi=(OperatorInfo)checkMap.get("OperatorInfo");
                request.setAttribute("OperatorInfo",oi);
                //如果是进入模块，还需要验证操作员模块权限
                pfBusinessComponent.checkRequestModule(oi,request.getServletPath());
                return true;
            }catch (BusiException be){
                if (be.getExpCode().equals(1010)) {
                    BaseNnte.setRetFalse(retMap, 1010, be.getMessage());
                    retMap.put("message", be.getMessage());
                    if (enterType==0)
                        BaseController.ResponsByFtl(request, response, retMap, "/templates/front/moduleFailed.ftl");
                        //    BaseController.ResponsByFtl(request, response, retMap, "/templates/front/jumplogin.ftl");
                    else
                        BaseController.printJsonObject(response,retMap);
                    return false;
                }
                BaseNnte.setRetFalse(retMap,1001,be.getMessage());
            }catch (Exception e){
                BaseNnte.setRetFalse(retMap,1001,"身份验证错误");
            }
            retMap.put("message",retMap.get("msg"));
            request.setAttribute("map",retMap);
            if (enterType==0)
                BaseController.ResponsByFtl(request, response, retMap, "/templates/front/jumplogin.ftl");
            else
                BaseController.printJsonObject(response,retMap);
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
