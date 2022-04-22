package com.nnte.pf_merchant.controller.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.AppendWhereLike;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.*;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_merchant.mapper.workdb.merchant_expand.PlateformMerchantExpand;
import com.nnte.pf_merchant.request.RequestMerchant;
import com.nnte.pf_merchant.request.RequestMerchantExpand;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/merchantSetting")
public class SettingController extends BaseController {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;

    /**
     * 返回商户列表数据
     * */
    @ModuleEnter(path = "/merchant/settingList", name="商户设置管理", desc = "平台商户基础信息设置管理，业务操作员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_WORKER,roleRuler = "pf-MerchantSet",
            moduleCode = PFMerchantConfig.MODULE_MERCHANT_MANAGE)
    @RequestMapping(value = "/merchantSettingList")
    @ResponseBody
    public Object merchantSettingList(@Nullable @RequestBody JsonNode data){

        try {
            Map<String, Object> paramMap = new HashedMap();
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            RequestMerchant queryMerchant = JsonUtil.jsonToBean(data.toString(), RequestMerchant.class);
            //-----------------------------------------
            if (queryMerchant.getPmState() != null) {
                paramMap.put("pmState", queryMerchant.getPmState());
            }
            if (queryMerchant.getPmCompanyPerson() != null) {
                paramMap.put("pmCompanyPerson", queryMerchant.getPmCompanyPerson());
            }
            //----------------------------------------------
            List<AppendWhere> appendWhereList = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryMerchant.getPmCode())) {
                appendWhereList.add(new AppendWhereLike("t.pm_code", queryMerchant.getPmCode()));
            }
            if (StringUtils.isNotEmpty(queryMerchant.getPmName())) {
                appendWhereList.add(new AppendWhereLike("t.pm_name", queryMerchant.getPmName()));
            }
            if (StringUtils.isNotEmpty(queryMerchant.getPmShortName())) {
                appendWhereList.add(new AppendWhereLike("t.pm_short_name", queryMerchant.getPmShortName()));
            }
            if (queryMerchant.getCreateTimeRange()!=null && queryMerchant.getCreateTimeRange().length>0){
                AppendWhere dateWhere = new AppendWhere(AppendWhere.Type_Direct);
                Date startTime = DateUtils.todayZeroTime(DateUtils.stringToDate(queryMerchant.getCreateTimeRange()[0]));
                Date endTime = null;
                if (queryMerchant.getCreateTimeRange().length > 1) {
                    endTime = DateUtils.todayNightZeroTime(DateUtils.stringToDate(queryMerchant.getCreateTimeRange()[1]));
                }
                AppendWhere.andDateRange(dateWhere, "t.create_time", startTime, endTime);
                appendWhereList.add(dateWhere);
            }
            //----------------------------------------------
            if (appendWhereList.size() > 0)
                paramMap.put("appendWhereList", appendWhereList);
            //----------------------------------------------
            PageData<PlateformMerchant> pageData = plateformMerchanComponent.queryMerchantsForSetting(paramMap, pageNo,pageSize);
            return success("取得商户列表数据成功!", pageData);
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 通过编码查询特定商户信息
     * */
    @RequestMapping(value = "/queryMerchantDetail")
    @ResponseBody
    public Object queryMerchantDetail(@RequestBody JsonNode json) {
        try {
            Map<String, Object> ret = new HashedMap();
            RequestMerchant rMerchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (rMerchant == null || rMerchant.getId() == null || rMerchant.getId() <= 0)
                throw new BusiException("未取得商户编号信息");
               PlateformMerchant merchant = plateformMerchanComponent.getMerchantById(rMerchant.getId());
            if (merchant == null)
                throw new BusiException("未取得商户信息");
            PlateformMerchantExpand merchantExpand = plateformMerchanComponent.getMerchantExpandByCode(merchant.getPmCode());
            RequestMerchantExpand expand = new RequestMerchantExpand();
            BeanUtils.copyFromSrc(merchantExpand,expand);
            ret.put("merchant", merchant);
            ret.put("merchantExpand", expand);
            return success("取得商户信息成功",ret);
        } catch (Exception e) {
            return onException(e);
        }
    }
    /**
     * 设置商户信息
     * */
    @RequestMapping(value = "/saveMerchantSetting")
    @ResponseBody
    public Object saveMerchantSetting(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            JsonUtil.JNode jNod = JsonUtil.createJNode(json);
            RequestMerchant merchant = JsonUtil.jsonToBean(jNod.get("merchant").toString(), RequestMerchant.class);
            Object expand=jNod.get("merchantExpand");
            RequestMerchantExpand merchantExpand = JsonUtil.jsonToBean(expand.toString(), RequestMerchantExpand.class);
            if (merchant==null || merchantExpand==null)
                throw new BusiException("未取商户基础信息及扩展信息");
            plateformMerchanComponent.saveMerchantSetting(merchant,merchantExpand,oi);
            return success("设置商户信息成功");
        } catch (Exception e) {
            return onException(e);
        }
    }

}
