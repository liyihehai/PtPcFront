package com.nnte.pf_pc_front.controller.api.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.entertity.LicenseCreateChannel;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;
import com.nnte.pf_basic.other.StaticWhere;
import com.nnte.pf_merchant.component.license.PlateformMerchantLicenseComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.entertity.AppLicenseItem;
import com.nnte.pf_merchant.request.RequestLicense;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/merchantLicense")
public class LicenseController extends BaseController {
    @Autowired
    private PlateformMerchantLicenseComponent plateformMerchantLicenseComponent;


    public static void addModuleNameLikeToMap(String moduleName,Map<String, Object> paramMap) throws Exception{
        if (StringUtils.isNotEmpty(moduleName))
            AppendWhere.andWhereTxtToWhereMap("module_code in (select module_code from plateform_busi_module where module_name like '%"+moduleName+"%')",paramMap);
    }
    public static void addModuleVersionLikeToMap(String moduleVersion,Map<String, Object> paramMap) throws Exception{
        if (StringUtils.isNotEmpty(moduleVersion))
            AppendWhere.andWhereTxtToWhereMap("module_code in (select module_code from plateform_busi_module where current_version like '%"+moduleVersion+"%')",paramMap);
    }
    /**
     * 返回商户许可列表数据
     * */
    @ModuleEnter(path = "/merchant/licenseList", name="商户许可管理", desc = "平台商户基础信息设置管理，业务操作员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_WORKER,roleRuler = "pf-MerchantLicense",
            moduleCode = PFMerchantConfig.Module_Code)
    @RequestMapping(value = "/merchantLicenseList")
    @ResponseBody
    public Object merchantLicenseList(@Nullable @RequestBody JsonNode data){
        try{
            Map<String, Object> paramMap = new HashedMap();
            paramMap.put("sort", "create_time");
            paramMap.put("dir", "desc");
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            //-------------------------------------------------
            RequestLicense license =  JsonUtil.jsonToBean(data.toString(),RequestLicense.class);
            AppendWhere.addEqualsToWhereMap(license.getLicenseState(),"licenseState",paramMap);
            AppendWhere.addEqualsToWhereMap(license.getMamNo(),"mamNo",paramMap);
            StaticWhere.addPmShortNameLikeToMap(license.getPmShortName(),paramMap);
            StaticWhere.addAppNameLikeToMap(license.getAppName(),paramMap);
            addModuleNameLikeToMap(license.getModuleName(),paramMap);
            addModuleVersionLikeToMap(license.getModuleVersion(),paramMap);
            AppendWhere.andDateRangeToWhereMap("t.start_date",license.getStartDate(),
                    "t.end_date",license.getEndDate(),paramMap);
            //-------------------------------------------------
            PageData<AppLicenseItem> pageData = plateformMerchantLicenseComponent.merchantLicenseList(paramMap, pageNo, pageSize);
            return success("取得商户许可列表数据成功!", pageData);
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 保存商户许可信息
     * */
    @RequestMapping(value = "/saveMerchantLicense")
    @ResponseBody
    public Object saveMerchantLicense(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestLicense licenseItem = JsonUtil.jsonToBean(json.toString(), RequestLicense.class);
            if (licenseItem==null)
                throw new BusiException("未取商户许可信息");
            BaseComponent.checkModelFields(licenseItem);
            PlateformAppLicense license = new PlateformAppLicense();
            BeanUtils.copyFromSrc(licenseItem,license);
            return success("保存商户许可信息成功",plateformMerchantLicenseComponent.saveMerchantLicense(license, LicenseCreateChannel.Plate,oi.getOperatorName()));
        } catch (Exception e) {
            return onException(e);
        }
    }
    /**
     * 确认商户许可，编辑->待执行->执行中
     * */
    @RequestMapping(value = "/confirmMerchantLicense")
    @ResponseBody
    public Object confirmMerchantLicense(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestLicense licenseItem = JsonUtil.jsonToBean(json.toString(), RequestLicense.class);
            if (licenseItem==null || licenseItem.getId()==null || licenseItem.getId()<=0)
                throw new BusiException("未取商户许可信息");
            plateformMerchantLicenseComponent.confirmMerchantLicenseExtend(licenseItem.getId(),oi.getOperatorName());
            return success("确认商户许可成功");
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 删除商户许可
     * */
    @RequestMapping(value = "/deleteMerchantLicense")
    @ResponseBody
    public Object deleteMerchantLicense(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestLicense licenseItem = JsonUtil.jsonToBean(json.toString(), RequestLicense.class);
            if (licenseItem==null || licenseItem.getId()==null || licenseItem.getId()<=0)
                throw new BusiException("未取商户许可信息");
            plateformMerchantLicenseComponent.deleteMerchantLicenseExtend(licenseItem.getId(),oi.getOperatorName());
            return success("确认商户许可成功");
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 商户UTI账户重置终端
     */
    @RequestMapping(value = "/resetLicenseTerminal")
    @ResponseBody
    public Object resetLicenseTerminal(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestLicense licenseItem = JsonUtil.jsonToBean(json.toString(), RequestLicense.class);
            if (licenseItem==null || licenseItem.getId()==null || licenseItem.getId()<=0)
                throw new BusiException("未取商户许可信息");
            plateformMerchantLicenseComponent.resetLicenseTerminal(licenseItem.getId(),
                    licenseItem.getTerminals(),oi.getOperatorName());
            return success("商户UTI账户重置终端成功");
        } catch (Exception e) {
            return onException(e);
        }
    }
}
