package com.nnte.pf_pc_front.controller.api.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.RSAUtils;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.component.utiaccount.PlateformUtiAccountComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import com.nnte.pf_merchant.mapper.workdb.merchantUtiAccount.PlateformMerchantUtiAccount;
import com.nnte.pf_merchant.mapper.workdb.merchant_expand.PlateformMerchantExpand;
import com.nnte.pf_merchant.request.RequestMerchant;
import com.nnte.pf_merchant.request.RequestMerchantExpand;
import com.nnte.pf_pc_front.entity.RequestUTIAccount;
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
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/merchantSetting")
public class SettingController extends BaseController {
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PlateformUtiAccountComponent plateformUtiAccountComponent;

    /**
     * 返回商户列表数据
     * */
    @ModuleEnter(path = "/merchant/settingList", name="商户设置管理", desc = "平台商户基础信息设置管理，业务操作员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_WORKER,roleRuler = "pf-MerchantSet",
            moduleCode = PFMerchantConfig.Module_Code)
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
            AppendWhere.addLikeStringToWhereList(queryMerchant.getPmCode(),"t.pm_code",appendWhereList);
            AppendWhere.addLikeStringToWhereList(queryMerchant.getPmName(),"t.pm_name",appendWhereList);
            AppendWhere.addLikeStringToWhereList(queryMerchant.getPmShortName(),"t.pm_short_name",appendWhereList);
            AppendWhere.addDataRangeToWhereList(queryMerchant.getCreateTimeRange(),"t.create_time",appendWhereList);
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

    /**
     * 设置商户启动服务
     * */
    @RequestMapping(value = "/setMerchantStart")
    @ResponseBody
    public Object setMerchantStart(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestMerchant merchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (merchant==null)
                throw new BusiException("未取商户基础信息");
            plateformMerchanComponent.setMerchantStart(merchant,oi);
            return success("设置商户启动服务成功");
        } catch (Exception e) {
            return onException(e);
        }
    }
    /**
     * 设置商户暂停服务
     * */
    @RequestMapping(value = "/setMerchantPause")
    @ResponseBody
    public Object setMerchantPause(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestMerchant merchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (merchant==null)
                throw new BusiException("未取商户基础信息");
            plateformMerchanComponent.setMerchantPause(merchant,oi);
            return success("设置商户暂停服务成功");
        } catch (Exception e) {
            return onException(e);
        }
    }
    /**
     * 设置商户下架
     * */
    @RequestMapping(value = "/setMerchantOffLine")
    @ResponseBody
    public Object setMerchantOffLine(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            RequestMerchant merchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (merchant==null)
                throw new BusiException("未取商户基础信息");
            plateformMerchanComponent.setMerchantOffLine(merchant,oi);
            return success("设置商户下架成功");
        } catch (Exception e) {
            return onException(e);
        }
    }
    /**
     * 取得商户UTI账户信息
     * */
    @RequestMapping(value = "/getUtiAccountByMerchantCode")
    @ResponseBody
    public Object getUtiAccountByMerchantCode(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestMerchant merchant = JsonUtil.jsonToBean(json.toString(), RequestMerchant.class);
            if (merchant==null)
                throw new BusiException("未取商户基础信息");
            PlateformMerchantUtiAccount account=plateformUtiAccountComponent.getUtiAccountByMerchantCode(merchant.getPmCode());
            if (account!=null)
                return success("取得商户UTI账户信息成功", account);
            else
                return success("未取得商户UTI账户信息");
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 创建商户UTI账户
     * */
    @RequestMapping(value = "/merchantCreateUTIAccount")
    @ResponseBody
    public Object merchantCreateUTIAccount(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestUTIAccount utiAccount = JsonUtil.jsonToBean(json.toString(), RequestUTIAccount.class);
            if (utiAccount==null)
                throw new BusiException("未取商户基础信息");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            return success("创建商户UTI账户成功",
                    plateformUtiAccountComponent.merchantCreateUTIAccount(utiAccount.getPmCode(),oi.getOperatorName(),
                            utiAccount.getDefBackUrl(),utiAccount.getValidIpList(),utiAccount.getBackId(),
                            utiAccount.getBackKey(),utiAccount.getAccountMemo()));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 商户UTI账户重置
     * */
    @RequestMapping(value = "/merchantUTIAccountReset")
    @ResponseBody
    public Object merchantUTIAccountReset(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestUTIAccount utiAccount = JsonUtil.jsonToBean(json.toString(), RequestUTIAccount.class);
            if (utiAccount==null)
                throw new BusiException("未取商户基础信息");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            return success("商户UTI账户重置成功",
                    plateformUtiAccountComponent.merchantUTIAccountReset(utiAccount.getPmCode(),
                            null,oi.getOperatorName(),
                            utiAccount.getDefBackUrl(),utiAccount.getValidIpList(),
                            utiAccount.getBackId(),utiAccount.getBackKey(),
                            utiAccount.getAccountState(),utiAccount.getAccountMemo(),
                            utiAccount.getAppRsaPubkey(),utiAccount.getAppRsaPrikey(),
                            utiAccount.getMerRsaPubkey(),utiAccount.getMerRsaPrikey()));
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 创建产生密钥对
     * */
    @RequestMapping(value = "/utiAccountGenKeys")
    @ResponseBody
    public Object utiAccountGenKeys(HttpServletRequest request, @Nullable @RequestBody JsonNode json) {
        try {
            Map<String, Object> secKeys = RSAUtils.genKeyPair(1024);
            JsonNode jsonNode = JsonUtil.newJsonNode();
            ((ObjectNode) jsonNode).put("RSAPublicKey",RSAUtils.getPublicKey(secKeys));
            ((ObjectNode) jsonNode).put("RSAPrivateKey",RSAUtils.getPrivateKey(secKeys));
            return success("创建产生密钥对成功",jsonNode);
        }catch (Exception e) {
            return onException(e);
        }
    }
}
