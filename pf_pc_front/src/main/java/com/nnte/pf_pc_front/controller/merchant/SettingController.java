package com.nnte.pf_pc_front.controller.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.*;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_business.mapper.workdb.merchant_expand.PlateformMerchantExpand;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_business.request.RequestApply;
import com.nnte.pf_business.request.RequestMerchant;
import com.nnte.pf_pc_front.PcPlateformApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/merchant/merchantSetting")
public class SettingController extends BaseController {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    /**
     * 显示商户设置列表Index页面
     * */
    @ModuleEnter(path = "/merchant/merchantSetting/merchantIndex", name="商户设置管理", desc = "平台商户基础信息设置管理，业务操作员功能",
            sysRole = PfBusinessComponent.PLATEFORM_MERCAHNT_WORKER,roleRuler = "pf-Mer-SettingIndex",
            appCode = PcPlateformApplication.App_Code,moduleCode = PcPlateformApplication.MODULE_MERCHANT_MANAGE)
    @RequestMapping(value = "/merchantIndex")
    public ModelAndView merchantIndex(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map= BaseNnte.newMapRetObj();
        setParamMapDataEnv(request,map);
        OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
        map.put("OperatorInfo",oi);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/merchant/setting/merchantSettingIndex");
        return modelAndView;
    }

    /**
     * 返回商户列表数据
     * */
    @RequestMapping(value = "/merchantList")
    public void merchantList(HttpServletRequest request, HttpServletResponse response){
        Integer sEcho = NumberUtil.getDefaultInteger(request.getParameter("sEcho"));
        try {
            RequestMerchant merchant = new RequestMerchant();
            BaseController.copyFromRequestParams(request,merchant);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformMerchant dto = new PlateformMerchant();
            dto.setStart(NumberUtil.getDefaultInteger(request.getParameter("iDisplayStart")));
            dto.setLimit(NumberUtil.getDefaultInteger(request.getParameter("iDisplayLength")));
            if (merchant.getPmState()!=null && merchant.getPmState().equals(-2))
                merchant.setPmState(null);//查询全部
            BeanUtils.copyFromSrc(merchant, dto);
            if (dto.getPmCompanyPerson()!=null && dto.getPmCompanyPerson()<0)
                dto.setPmCompanyPerson(null);
            Map<String,Object> appendParam=null;
            Map<String,Object> tmpMap= DateUtils.splitDateRange(merchant.getCreateTimeRange(),DateUtils.DF_YMD);
            if (tmpMap!=null && NumberUtil.getDefaultInteger(tmpMap.get("suc")).equals(1)){
                appendParam = new HashMap<>();
                appendParam.put("createTimeStart",tmpMap.get("startdate"));
                appendParam.put("createTimeEnd",tmpMap.get("enddate"));
            }
            Map<String, Object> loadMap = plateformMerchanComponent.queryMerchantsForSetting(dto, oi,appendParam);
            Integer count = NumberUtil.getDefaultInteger(loadMap.get("count"));
            List<PlateformMerchant> lists = (List<PlateformMerchant>) loadMap.get("list");
            if (lists != null)
                printLoadListMsg(response, sEcho + 1, count, JsonUtil.beanToJson(lists));
            else
                throw new BusiException("没有查询到列表数据");
        }catch (BusiException be){
            List<PlateformMerchant> lists = new ArrayList<>();
            printLoadListMsg(response,sEcho + 1, 0, JsonUtil.beanToJson(lists));
        }
    }
    /**
     * 通过编码查询特定商户信息
     * */
    @RequestMapping(value = "/merchantDetailDialog")
    @ResponseBody
    public Map<String,Object> merchantDetailDialog(HttpServletRequest request, @RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        try {
            RequestMerchant rMerchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (rMerchant == null || rMerchant.getId()==null || rMerchant.getId()<=0)
                throw new BusiException("未取得商户编号信息");
            PlateformMerchant merchant=plateformMerchanComponent.getMerchantById(rMerchant.getId());
            if (merchant==null)
                throw new BusiException("未取得商户信息");
            PlateformMerchantExpand merchantExpand=plateformMerchanComponent.getMerchantExpandByCode(merchant.getPmCode());
            Map<String, Object> map = new HashMap<>();
            setParamMapDataEnv(request, map);
            map.put("merchant",merchant);
            map.put("merchantExpand",merchantExpand);
            String htmlBody = FreeMarkertUtil.getFreemarkerFtl(request, request.getServletContext(),
                    FreeMarkertUtil.pathType.cls, map, "/templates/front/merchant/setting/merchantDetail.ftl");
            ret.put("htmlBody", htmlBody);
            ret.put("data",map);
            BaseNnte.setRetTrue(ret,"取得商户信息成功");
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            return ret;
        }
        return ret;
    }
    /**
     * 通过编码查询特定商户信息
     * */
    @RequestMapping(value = "/queryMerchantDetail")
    @ResponseBody
    public Map<String,Object> queryMerchantDetail(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestMerchant rMerchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (rMerchant == null || rMerchant.getId() == null || rMerchant.getId() <= 0)
                throw new BusiException("未取得商户编号信息");
               PlateformMerchant merchant = plateformMerchanComponent.getMerchantById(rMerchant.getId());
            if (merchant == null)
                throw new BusiException("未取得商户信息");
            PlateformMerchantExpand merchantExpand = plateformMerchanComponent.getMerchantExpandByCode(merchant.getPmCode());
            setParamMapDataEnv(request, ret);
            ret.put("merchant", merchant);
            ret.put("merchantExpand", merchantExpand);
            BaseNnte.setRetTrue(ret,"取得商户信息成功");
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }
}
