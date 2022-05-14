package com.nnte.pf_uti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.pf_source.uti.request.RequestToken;
import com.nnte.pf_source.uti.response.ResResult;
import com.nnte.pf_uti.component.PFUTIComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/uti/basic")
public class PFUTIController extends BaseController {
    @Autowired
    private PFUTIComponent pfUTIComponent;

    @RequestMapping(value = "/merchantToken", method = RequestMethod.POST)
    @ResponseBody
    public Object merchantToken(HttpServletRequest request) throws Exception {
        Map<String, Object> UTIData = (Map<String, Object>) request.getAttribute("UTIData");
        JsonNode datajson = (JsonNode) UTIData.get("content_json");
        String ip = UTIData.get("ip").toString();
        ResResult resResult = (ResResult)UTIData.get("resResult");
        return pfUTIComponent.merchantToken(datajson, ip,resResult);
    }
}
