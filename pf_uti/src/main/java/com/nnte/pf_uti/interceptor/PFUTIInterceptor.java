package com.nnte.pf_uti.interceptor;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.pf_merchant.mapper.workdb.merchantUtiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_source.uti.response.ResResult;
import com.nnte.pf_uti.component.PFUTIComponent;
import com.nnte.pf_uti.config.PFUTIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Component
@BusiLogAttr(PFUTIConfig.loggerName)
public class PFUTIInterceptor extends BaseComponent implements HandlerInterceptor {

    @Autowired
    private PFUTIComponent pfUTIComponent;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //请求进入这个拦截器
        ResResult resResult = new ResResult();
        resResult.setSuccess(false);
        resResult.setResultCode(9999);
        resResult.setResultMessage("未知错误");
        resResult.setRequestTime((new Date()).getTime());
        String name = request.getDispatcherType().name();
        String loginIp = BaseController.getIpAddr(request);
        try {
            String req_data = request.getParameter("req_data");
            outLogDebug("IP=" + loginIp + ",name=" + name + ",req_data=" + req_data);
            String mid = request.getParameter("mid");
            if (mid == null || req_data == null)
                throw new BusiException("");
                //解密验签
            Map<String, Object> ret=pfUTIComponent.checkReqSignValid(mid, req_data,loginIp);
            ret.put("resResult",resResult);
            request.setAttribute("UTIData", ret);
            return true;
        }catch (BusiException be) {
            BaseController.printJsonObject(response, BaseController.error(be.getExpCode().toString(), be.getMessage()));
            return false;
        }catch (Exception e){
            BaseController.printJsonObject(response, BaseController.error(e.getMessage()));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex!=null) {
            Map<String, Object> utiData = (Map<String, Object>) request.getAttribute("UTIData");
            ResResult resResult = (ResResult) utiData.get("resResult");
            PlateformMerchantUtiAccount pmua = (PlateformMerchantUtiAccount)utiData.get("content_pmua");
            resResult.setSuccess(false);
            resResult.setResult(null);
            if (ex instanceof BusiException){
                BusiException bex = (BusiException) ex;
                resResult.setResultCode(bex.getExpCode());
            }else
                resResult.setResultCode(9999);
            resResult.setResultMessage(ex.getMessage());
            resResult.setResponseTime((new Date()).getTime());
            String respString = pfUTIComponent.getRespSignString(pmua,null,resResult);
            BaseController.printJsonObject(response, respString);
        }
    }
}
