package com.nnte.pf_pc_front.controller;

import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/main")
public class PcPlateformController extends BaseController {

    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        BaseNnte.setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/login");
        return modelAndView;
    }

    @RequestMapping(value = "/loginCheck")
    public ModelAndView loginCheck(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        String userName= StringUtils.defaultString(getRequestParam(request,null,"userName"));
        String password= StringUtils.defaultString(getRequestParam(request,null,"password"));
        BaseNnte.setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/index");
        return modelAndView;
    }

    @RequestMapping(value = "/sysRepairing")
    public ModelAndView sysRepairing(ModelAndView modelAndView){
        modelAndView.setViewName("front/sysRepairing");
        return modelAndView;
    }
}
