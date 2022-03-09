package com.nnte.pf_pc_front.controller.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestMenu;
import com.nnte.pf_pc_front.PcPlateformApplication;
import com.nnte.pf_pc_front.config.AppModelConfig;
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
@RequestMapping(value = "/sysset")
public class SyssetController extends BaseController {
    @Autowired
    private PfBusinessComponent pfBusinessComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;
    /**
     * 显示菜单设置页面
     * */
    @ModuleEnter(path = "/sysset/menuset", name="菜单设置页面", desc = "平台系统菜单设置，系统超级管理员功能",
            roleRuler = "pf-menuset",appCode = PcPlateformApplication.App_Code,
            moduleCode = AppModelConfig.MODULE_SYSSETTING)
    @RequestMapping(value = "/menuset")
    public ModelAndView menuset(HttpServletRequest request,ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        setParamMapDataEnv(request,map);
        //-------默认超级管理员,取得所有功能----
        OperatorInfo opeInfo = new OperatorInfo();
        opeInfo.setOperatorCode("admin");
        //-------------------------------------
        List<PFMenu> pFMenuList=pfBusinessComponent.loadOperatorMenuFunctions(opeInfo,null);//显示所有状态的菜单
        List<MEnter> FEnterList=BaseBusiComponent.getSystemModuleEnters();
        Map<String,Object> envData=(Map)map.get("envData");
        String menuTreeRows=applyMenuTable(request,pFMenuList,
                StringUtils.defaultString(envData.get("staticRoot")),
                StringUtils.defaultString(envData.get("contextPath")));
        map.put("menuTreeRows",menuTreeRows);
        map.put("FEnterList",FEnterList);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/sysset/menu/menuset");
        return modelAndView;
    }



    @RequestMapping(value = "/refreshMenus")
    @ResponseBody
    public Map<String,Object> refreshMenus(HttpServletRequest request){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        Map<String,Object> map=new HashMap<>();
        setParamMapDataEnv(request,map);
        //-------默认超级管理员,取得所有功能----
        OperatorInfo opeInfo = new OperatorInfo();
        opeInfo.setOperatorCode("admin");
        //-------------------------------------
        List<PFMenu> pFMenuList=pfBusinessComponent.loadOperatorMenuFunctions(opeInfo,null);//显示所有状态的菜单
        Map<String,Object> envData=(Map)map.get("envData");
        String menuTreeRows=applyMenuTable(request,pFMenuList,
                StringUtils.defaultString(envData.get("staticRoot")),
                StringUtils.defaultString(envData.get("contextPath")));
        ret.put("menuTreeRows",menuTreeRows);
        BaseNnte.setRetTrue(ret,"刷新成功");
        return ret;
    }
    /**
     * 通过FTL渲染一个特定的功能
     * */
    private String applyPFunctionByFtl(HttpServletRequest request,PlateformFunctions func,
                                    String staticRoot,String contextPath){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("func",func);
        paramMap.put("staticRoot",staticRoot);
        paramMap.put("contextPath",contextPath);
        paramMap.put("funcPath",BaseBusiComponent.getPathByRuler(func.getAuthCode()));
        return FreeMarkertUtil.getFreemarkerFtl(request,request.getServletContext(),FreeMarkertUtil.pathType.cls,paramMap, "/templates/front/sysset/menu/function.ftl");
    }
    /**
     * 通过FTL渲染一个特定的菜单
     * */
    private String applyPFMenuByFtl(HttpServletRequest request,PFMenu menu,
                                    String staticRoot,String contextPath){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("menu",menu);
        paramMap.put("staticRoot",staticRoot);
        paramMap.put("contextPath",contextPath);
        return FreeMarkertUtil.getFreemarkerFtl(request,request.getServletContext(),FreeMarkertUtil.pathType.cls,paramMap, "/templates/front/sysset/menu/menu.ftl");
    }
    /**
     * 渲染菜单，如果有子菜单或功能，需要
     * */
    private String applyPFMenu(HttpServletRequest request,PFMenu menu,
                               String staticRoot,String contextPath){
        StringBuffer menuBody = new StringBuffer();
        menuBody.append(applyPFMenuByFtl(request,menu,staticRoot,contextPath));
        List<PFMenu> subMenuList=menu.getSubMenuList();
        List<PlateformFunctions> functionList=menu.getFunctionList();
        if (subMenuList!=null && subMenuList.size()>0){
            for(PFMenu subMenu:subMenuList)
                menuBody.append(applyPFMenu(request,subMenu,staticRoot,contextPath));
        }else if (functionList!=null && functionList.size()>0){
            for(PlateformFunctions func:functionList)
                menuBody.append(applyPFunctionByFtl(request,func,staticRoot,contextPath));
        }
        return menuBody.toString();
    }
    /**
     * 在服务器端渲染菜单及功能树
     * */
    private String applyMenuTable(HttpServletRequest request,List<PFMenu> pfMenuList ,
                                  String staticRoot,String contextPath){
        if (pfMenuList==null || pfMenuList.size()<=0)
            return null;
        StringBuffer menuBody = new StringBuffer();
        for(PFMenu menu:pfMenuList){
            menuBody.append(applyPFMenu(request,menu,staticRoot,contextPath));
        }
        return menuBody.toString();
    }
    /**
     * 保存菜单信息更改，含新增和更改
     * */
    @RequestMapping(value = "/saveMenuModify")
    @ResponseBody
    public Map<String,Object> saveMenuModify(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestMenu rMenu= JsonUtil.jsonToBean(json.toString(),RequestMenu.class);
        try {
            BaseBusiComponent.checkModelFields(rMenu);
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            return ret;
        }
        PlateformMenus menu = new PlateformMenus();
        BeanUtils.copyFromSrc(rMenu,menu);
        return plateformFunctionComponent.saveMenuModify(menu);
    }
    /**
     * 删除菜单定义（物理删除）
     * */
    @RequestMapping(value = "/deleteMenuByCode")
    @ResponseBody
    public Map<String,Object> deleteMenuByCode(HttpServletRequest request, @RequestBody JsonNode json){
        RequestMenu rMenu= JsonUtil.jsonToBean(json.toString(),RequestMenu.class);
        if (rMenu==null||StringUtils.isEmpty(rMenu.getMenuCode())){
            Map<String,Object> ret=BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(ret,1002,"菜单代码不合法");
            return ret;
        }
        return plateformFunctionComponent.deleteMenuByCode(rMenu.getMenuCode());
    }
    /**
     * 删除功能菜单定义（物理删除）
     * */
    @RequestMapping(value = "/deleteFuncByCode")
    @ResponseBody
    public Map<String,Object> deleteFuncByCode(HttpServletRequest request, @RequestBody JsonNode json){
        RequestFunc rFunc= JsonUtil.jsonToBean(json.toString(), RequestFunc.class);
        if (rFunc==null||StringUtils.isEmpty(rFunc.getFunCode())){
            Map<String,Object> ret=BaseNnte.newMapRetObj();
            BaseNnte.setRetFalse(ret,1002,"功能菜单代码不合法");
            return ret;
        }
        return plateformFunctionComponent.deleteFuncByCode(rFunc.getFunCode());
    }
    /**
     * 保存菜单功能，含更改及增加
     * */
    @RequestMapping(value = "/saveFunctionModify")
    @ResponseBody
    public Map<String,Object> saveFunctionModify(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        RequestFunc rFunc= JsonUtil.jsonToBean(json.toString(), RequestFunc.class);
        try {
            BaseBusiComponent.checkModelFields(rFunc);
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            return ret;
        }
        PlateformFunctions func = new PlateformFunctions();
        BeanUtils.copyFromSrc(rFunc,func);
        return plateformFunctionComponent.saveFunctionModify(func,rFunc.getFunPath());
    }
}
