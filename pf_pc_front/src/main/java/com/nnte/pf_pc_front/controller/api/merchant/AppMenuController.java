package com.nnte.pf_pc_front.controller.api.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.mapper.workdb.merchantAppMenu.PlateformMerchantAppMenu;
import com.nnte.pf_basic.other.StaticWhere;
import com.nnte.pf_merchant.component.appmenu.PlateformAppMenuComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.entertity.MerchantAppMenu;
import com.nnte.pf_merchant.request.RequestAppMenu;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/appMenu")
public class AppMenuController extends BaseController {
    @Autowired
    private PlateformAppMenuComponent plateformAppMenuComponent;


    /**
     * 返回商户菜单列表数据
     * */
    @ModuleEnter(path = "/merchant/appMenuList", name="商户菜单管理", desc = "平台管理员为商户应用设置菜单，平台商户管理员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_MANAGER,roleRuler = "pf-MerchantAppMenu",
            moduleCode = PFMerchantConfig.Module_Code)
    @RequestMapping(value = "/merchantAppMenuList")
    @ResponseBody
    public Object merchantAppMenuList(@Nullable @RequestBody JsonNode data){
        try{
            Map<String, Object> paramMap = new HashedMap();
            paramMap.put("sort", "create_date");
            paramMap.put("dir", "desc");
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            //-------------------------------------------------
            MerchantAppMenu appMenu =  JsonUtil.jsonToBean(data.toString(),MerchantAppMenu.class);
            AppendWhere.andWhereTxtToWhereMap("t.menu_status>=0",paramMap);
            AppendWhere.addEqualsToWhereMap(appMenu.getMenuStatus(),"menuStatus",paramMap);
            AppendWhere.addEqualsToWhereMap(appMenu.getMenuType(),"menuType",paramMap);
            if (appMenu.getCreateDateRange()!=null && appMenu.getCreateDateRange().length==2) {
                Date start=DateUtils.stringToDate(appMenu.getCreateDateRange()[0]);
                Date end=DateUtils.stringToDate(appMenu.getCreateDateRange()[1]);
                AppendWhere.andDateRangeToWhereMap("t.create_date",start ,end , paramMap);
            }
            StaticWhere.addAppCodeLikeToMap(appMenu.getAppCode(),paramMap);
            StaticWhere.addAppNameLikeToMap(appMenu.getAppName(),paramMap);
            StaticWhere.addPmShortNameLikeToMap(appMenu.getPmShortName(),paramMap);
            //-------------------------------------------------
            PageData<MerchantAppMenu> pageData = plateformAppMenuComponent.merchantAppMenuList(paramMap, pageNo, pageSize);
            return success("返回商户菜单列表数据成功!", pageData);
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 返回应用入口列表
     * */
    @RequestMapping(value = "/merchantAppEnterList")
    @ResponseBody
    public Object merchantAppEnterList(@Nullable @RequestBody JsonNode data){
        try{
            MerchantAppMenu appMenu = JsonUtil.jsonToBean(data.toString(),MerchantAppMenu.class);
            if (appMenu==null || appMenu.getAppCode()==null)
                throw new BusiException("没有指定应用");
            List<MEnter> retList=plateformAppMenuComponent.merchantAppEnterList(appMenu.getAppCode());
            return success("返回应用入口列表成功!", retList);
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 保存商户应用菜单
     * */
    @RequestMapping(value = "/saveMerchantAppMenu")
    @ResponseBody
    public Object saveMerchantAppMenu(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try{
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestAppMenu appMenu = JsonUtil.jsonToBean(data.toString(),RequestAppMenu.class);
            BaseComponent.checkModelFields(appMenu);
            if (appMenu==null || appMenu.getAppCode()==null)
                throw new BusiException("没有指定应用");
            PlateformMerchantAppMenu menu = new PlateformMerchantAppMenu();
            BeanUtils.copyFromSrc(appMenu,menu);
            return success("返回应用入口列表成功!",
                    plateformAppMenuComponent.saveMerchantAppMenu(menu,oi));
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 确认商户应用菜单
     * */
    @RequestMapping(value = "/confirmMerchantAppMenu")
    @ResponseBody
    public Object confirmMerchantAppMenu(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try{
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestAppMenu appMenu = JsonUtil.jsonToBean(data.toString(),RequestAppMenu.class);
            plateformAppMenuComponent.confirmMerchantAppMenu(appMenu.getId(),oi);
            return success("确认商户应用菜单成功!");
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 取消确认商户应用菜单
     * */
    @RequestMapping(value = "/cancelMerchantAppMenu")
    @ResponseBody
    public Object cancelMerchantAppMenu(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try{
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestAppMenu appMenu = JsonUtil.jsonToBean(data.toString(),RequestAppMenu.class);
            plateformAppMenuComponent.cancelMerchantAppMenu(appMenu.getId(),oi);
            return success("取消确认商户应用菜单成功!");
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 删除商户应用菜单
     * */
    @RequestMapping(value = "/deleteMerchantAppMenu")
    @ResponseBody
    public Object deleteMerchantAppMenu(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try{
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestAppMenu appMenu = JsonUtil.jsonToBean(data.toString(),RequestAppMenu.class);
            plateformAppMenuComponent.deleteMerchantAppMenu(appMenu.getId(),oi);
            return success("删除商户应用菜单成功!");
        }catch (Exception e){
            return onException(e);
        }
    }
}
