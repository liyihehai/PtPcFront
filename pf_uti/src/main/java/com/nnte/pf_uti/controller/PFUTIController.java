package com.nnte.pf_uti.controller;

import com.nnte.basebusi.base.BaseController;
import com.nnte.pf_basic.component.ReportModuleComponent;
import com.nnte.pf_uti.component.PFUTIComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@RequestMapping(value = "/uti/basic")
public class PFUTIController extends BaseController {
    @Autowired
    private PFUTIComponent pfUTIComponent;

    @RequestMapping(value = "/merchantToken", method = RequestMethod.POST)
    @ResponseBody
    public Object merchantToken(HttpServletRequest request) throws Exception {
        String ip = BaseController.getIpAddr(request);
        return pfUTIComponent.merchantToken(ip);
    }

    @RequestMapping(value = "/reportModule", method = RequestMethod.POST)
    @ResponseBody
    public Object reportModule(HttpServletRequest request) throws Exception {
        return pfUTIComponent.reportModule();
    }
}
