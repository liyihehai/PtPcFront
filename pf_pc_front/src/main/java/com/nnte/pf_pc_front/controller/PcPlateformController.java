package com.nnte.pf_pc_front.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/main")
public class PcPlateformController extends BaseController {

    @Autowired
    private PfBusinessComponent pfBusinessComponent;

    /**
     * 显示登陆页面
     * */
    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/login");
        return modelAndView;
    }
    /**
     * 在服务器端渲染菜单及功能树
     * */
    private String applyMenuFunc(List<PFMenu> menuFuncList,String contextPath){
        if (menuFuncList==null || menuFuncList.size()<=0)
            return null;
        StringBuffer menuBody = new StringBuffer();
        for(PFMenu menu:menuFuncList){
            menuBody.append("<li class=\"treeview\">")
                        .append("<a href=\"javascript:void(0);\">")
                            .append("<i class=\"fa fa-link\"></i>")
                            .append("<span>"+menu.getMenuName()+"</span>")
                            .append("<i class=\"fa fa-angle-left pull-right\"></i>")
                        .append("</a>");
            if (menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0) {
                menuBody.append("<ul class=\"treeview-menu\">");
                menuBody.append(applyMenuFunc(menu.getSubMenuList(),contextPath));
                menuBody.append("</ul>");
            }else if (menu.getFunctionList()!=null && menu.getFunctionList().size()>0){
                menuBody.append("<ul class=\"treeview-menu\">");
                for(PlateformFunctions func:menu.getFunctionList()){
                    menuBody.append("<li>")
                                .append("<a href=\"javascript:void(0);\" data-menuName=\""+func.getFunName()+"\" data-menukey=\""+func.getFunCode()+"\" data-link=\""+contextPath+func.getFunPath()+"\" class=\"u_a\">")
                                .append("<i class=\"fa fa-c fa-circle-o\"></i>"+func.getFunName())
                                .append("</a>")
                            .append("</li>");
                }
                menuBody.append("</ul>");
            }
            menuBody.append("</li>");
        }
        return menuBody.toString();
    }
    /**
     * 用户预校验
     * */
    @RequestMapping(value = "/priCheck")
    @ResponseBody
    public Object priCheck(HttpServletRequest request,@RequestBody JsonNode jsonParam){
        JsonUtil.JNode jnode = JsonUtil.createJNode(jsonParam);
        return pfBusinessComponent.priCheckUser(jnode.getText("userName"));
    }
    /**
     * 用户密码校验
     * */
    @RequestMapping(value = "/loginCheck")
    public ModelAndView loginCheck(HttpServletRequest request,
                                   ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        String userCode= StringUtils.defaultString(getRequestParam(request,null,"userName"));
        String password= StringUtils.defaultString(getRequestParam(request,null,"aimPwd"));
        setParamMapDataEnv(request,map);
        Map<String,Object> checkMap=pfBusinessComponent.checkUserPassword(userCode,password,
                BaseController.getIpAddr(request));
        if (BaseNnte.getRetSuc(checkMap)) {
            OperatorInfo opeInfo = (OperatorInfo)checkMap.get("OperatorInfo");
            String token=StringUtils.defaultString(opeInfo.getToken());
            BaseNnte.outConsoleLog("用户登录成功["+userCode+"]"+token);
            map.put("OperatorInfo",opeInfo);
            Map<String,Object> envData=(Map)map.get("envData");
            List<PFMenu> menuFuncList=(List<PFMenu>)checkMap.get("menuFuncList");
            map.put("MenuFunctions",applyMenuFunc(menuFuncList,
                    StringUtils.defaultString(envData.get("contextPath"))));
            modelAndView.setViewName("front/index");
        }else {
            map.put("message",checkMap.get("msg"));
            modelAndView.setViewName("front/login");
        }
        modelAndView.addObject("map", map);
        return modelAndView;
    }

    @RequestMapping(value = "/sysRepairing")
    public ModelAndView sysRepairing(ModelAndView modelAndView){
        modelAndView.setViewName("front/sysRepairing");
        return modelAndView;
    }

    @RequestMapping(value = "/homeIndex")
    public ModelAndView homeIndex(ModelAndView modelAndView){
        modelAndView.setViewName("front/homeIndex");
        return modelAndView;
    }
}
