package com.nnte.pf_pc_front.interceptor;

import com.nnte.basebusi.base.BaseBusi;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.OperatorInfo;
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
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class PfPcFrontNormalInterceptor extends BaseBusi implements HandlerInterceptor {
    @ConfigLoad
    private ConfigInterface appconfig;
    @Autowired
    private PfBusinessComponent pfBusinessComponent;

    private boolean excludePathPatterns(String path) {
        String[] excludes = {".*/applyVerify.*",
                ".*/sysRepairing.*",
                ".*/login.*",
                ".*/loginCheck.*",
                ".*/priCheck.*",
                ".*/error.*",
                ".*/resources/.*",
                ".*/api/priCheck/.*",
                ".*/api/login/account/.*"};
        for (String exclude : excludes) {
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
        Map<String, Object> envData = (Map) request.getAttribute("envData");
        outLogTrace(request.getServletPath());
        int enterType = 0; //页面进入
        if (envData != null) {
            Map<String, Object> retMap = BaseNnte.newMapRetObj();
            String loginIp = BaseController.getIpAddr(request);
            String token = StringUtils.defaultString(BaseController.getRequestParam(request, null, "token"));
            if (StringUtils.isEmpty(token)) {
                token = request.getHeader("AjaxToken");
                if (StringUtils.isEmpty(token)) {
                    Enumeration<String> es = request.getHeaderNames();
                    while(es.hasMoreElements()){
                        String headerName = es.nextElement();
                        String headValue=request.getHeader(headerName);
                        outLogDebug(headerName+":"+headValue);
                    }
                    token = request.getHeader("ajaxtoken");
                }
                enterType = 1; //Ajax进入
            }
            try {
                Map checkMap = pfBusinessComponent.checkRequestToken(token, loginIp);
                OperatorInfo oi = (OperatorInfo) checkMap.get("OperatorInfo");
                request.setAttribute("OperatorInfo", oi);
                //如果是进入模块，还需要验证操作员模块权限
                pfBusinessComponent.checkRequestModule(oi, request.getServletPath());
                return true;
            } catch (BusiException be) {
                if (enterType == 0) {
                    BaseNnte.setRetFalse(retMap, be.getExpCode(), be.getMessage());
                    retMap.put("message", be.getMessage());
                    BaseController.ResponsByFtl(request, response, retMap, "/templates/front/moduleFailed.ftl");
                } else
                    BaseController.printJsonObject(response, BaseController.error(be.getExpCode().toString(), be.getMessage()));
                return false;
            } catch (Exception e) {
                BaseNnte.setRetFalse(retMap, 1001, "身份验证错误");
                if (enterType == 0) {
                    BaseNnte.setRetFalse(retMap, 1001, e.getMessage());
                    retMap.put("message", e.getMessage());
                    BaseController.ResponsByFtl(request, response, retMap, "/templates/front/moduleFailed.ftl");
                } else
                    BaseController.printJsonObject(response, BaseController.error("1001", e.getMessage()));
                return false;
            }
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
