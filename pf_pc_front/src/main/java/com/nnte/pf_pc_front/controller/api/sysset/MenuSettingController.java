package com.nnte.pf_pc_front.controller.api.sysset;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.config.PFBusinessConfig;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/sysset/menuSetting")
public class MenuSettingController extends BaseController {

    @Autowired
    private PfBusinessComponent pfBusinessComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;


    @ModuleEnter(path = "/sysset/menuList", name = "菜单设置页面",
            desc = "平台系统菜单设置，系统超级管理员功能",
            roleRuler = "pf-menuset",
            moduleCode = PFBusinessConfig.MODULE_SYSTEM_SETTING)
    @RequestMapping(value = "/allMenuTree", method = {RequestMethod.GET})
    @ResponseBody
    public Object allMenuTree(HttpServletRequest request, @Nullable @RequestBody JsonNode data) {
        try {
            Map<String, Object> map = new HashMap<>();
            setParamMapDataEnv(request, map);
            //-------默认超级管理员,取得所有功能----
            OperatorInfo opeInfo = (OperatorInfo) request.getAttribute("OperatorInfo");
            plateformOperatorComponent.getOpeByCodeValid(opeInfo.getOperatorCode(), true);
            //-------------------------------------
            List<PFMenu> pFMenuList = pfBusinessComponent.loadOperatorMenuFunctions(opeInfo, null);//显示所有状态的菜单
            return BaseController.success("success", pfBusinessComponent.loadMenuFuncNode(pFMenuList, true));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 取得系统功能定义列表
     */
    @RequestMapping(value = "/getFEnter", method = {RequestMethod.GET})
    @ResponseBody
    public Object getFEnter() {
        try {
            List<MEnter> FEnterList = BaseComponent.getSystemModuleEnters();
            return BaseController.success("success", FEnterList);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 保存菜单信息更改，含新增和更改
     */
    @RequestMapping(value = "/saveMenuModify", method = {RequestMethod.POST})
    @ResponseBody
    public Object saveMenuModify(@Nullable @RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestMenu rMenu = JsonUtil.jsonToBean(json.toString(), RequestMenu.class);
            BaseComponent.checkModelFields(rMenu);
            PlateformMenus menu = new PlateformMenus();
            BeanUtils.copyFromSrc(rMenu, menu);
            Map<String, Object> retMap = plateformFunctionComponent.saveMenuModify(menu);
            if (BaseNnte.getRetSuc(retMap))
                return success("保存菜单信息成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 删除菜单定义（物理删除）
     */
    @RequestMapping(value = "/deleteMenu")
    @ResponseBody
    public Object deleteMenu(@Nullable @RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestMenu rMenu = JsonUtil.jsonToBean(json.toString(), RequestMenu.class);
            if (rMenu == null || StringUtils.isEmpty(rMenu.getMenuCode())) {
                return error("1002", "菜单代码不合法");
            }
            Map<String, Object> retMap = plateformFunctionComponent.deleteMenuByCode(rMenu.getMenuCode());
            if (BaseNnte.getRetSuc(retMap))
                return success("删除菜单信息成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 保存菜单功能，含更改及增加
     */
    @RequestMapping(value = "/saveFunctionModify")
    @ResponseBody
    public Object saveFunctionModify(@Nullable @RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestFunc rFunc = JsonUtil.jsonToBean(json.toString(), RequestFunc.class);
            BaseComponent.checkModelFields(rFunc);
            PlateformFunctions func = new PlateformFunctions();
            BeanUtils.copyFromSrc(rFunc, func);
            Map<String, Object> retMap = plateformFunctionComponent.saveFunctionModify(func, rFunc.getFunPath());
            if (BaseNnte.getRetSuc(retMap))
                return success("保存菜单功能信息成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 删除功能功能定义（物理删除）
     */
    @RequestMapping(value = "/deleteFuncByCode")
    @ResponseBody
    public Object deleteFuncByCode(@Nullable @RequestBody JsonNode json) {
        try {
            if (json == null)
                return error("参数为空");
            RequestFunc rFunc = JsonUtil.jsonToBean(json.toString(), RequestFunc.class);
            if (rFunc == null || StringUtils.isEmpty(rFunc.getFunCode())) {
                return error("1002", "功能菜单代码不合法");
            }
            Map<String, Object> retMap = plateformFunctionComponent.deleteFuncByCode(rFunc.getFunCode());
            if (BaseNnte.getRetSuc(retMap))
                return success("删除菜单功能信息成功!");
            throw new BusiException(NumberDefUtil.getDefInteger(retMap.get("code")),
                    StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception e) {
            return onException(e);
        }
    }
}
