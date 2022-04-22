package com.nnte.pf_merchant.controller.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.AppendWhereLike;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.component.PFBasicComponent;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanApplyComponent;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_merchant.request.RequestApply;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/merchantApply")
public class ApplyController extends BaseController {
    @Autowired
    private PlateformMerchanApplyComponent plateformMerchanApplyComponent;
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    @Autowired
    private PFBasicComponent pfBasicComponent;


    private Map<String, Object> getListParam(RequestApply queryApply) throws BusiException{
        //----------------------------------------------
        Map<String, Object> paramMap = new HashedMap();
        paramMap.put("sort", "create_time");
        paramMap.put("dir", "desc");
        //----------------------------------------------
        if (queryApply.getApplyState() != null) {
            paramMap.put("applyState", queryApply.getApplyState());
        }
        if (queryApply.getPmCompanyPerson() != null) {
            paramMap.put("pmCompanyPerson", queryApply.getPmCompanyPerson());
        }
        if (queryApply.getApplyWays() != null) {
            paramMap.put("applyWays", queryApply.getApplyWays());
        }
        //----------------------------------------------
        List<AppendWhere> appendWhereList = new ArrayList<>();
        AppendWhere whereState = new AppendWhere(AppendWhere.Type_Direct);
        whereState.setWhereTxt("t.apply_state!=(-1)");
        appendWhereList.add(whereState);
        if (StringUtils.isNotEmpty(queryApply.getPmName())) {
            appendWhereList.add(new AppendWhereLike("t.pm_name", queryApply.getPmName()));
        }
        if (StringUtils.isNotEmpty(queryApply.getCreatorName())) {
            appendWhereList.add(new AppendWhereLike("t.creator_name", queryApply.getCreatorName()));
        }
        if (StringUtils.isNotEmpty(queryApply.getConfirmName())) {
            appendWhereList.add(new AppendWhereLike("t.confirm_name", queryApply.getConfirmName()));
        }
        if (StringUtils.isNotEmpty(queryApply.getCheckerName())) {
            appendWhereList.add(new AppendWhereLike("t.checker_name", queryApply.getCheckerName()));
        }
        if (queryApply.getCreateTimeRange()!=null && queryApply.getCreateTimeRange().length>0){
            AppendWhere dateWhere = new AppendWhere(AppendWhere.Type_Direct);
            Date startTime = DateUtils.todayZeroTime(DateUtils.stringToDate(queryApply.getCreateTimeRange()[0]));
            Date endTime = null;
            if (queryApply.getCreateTimeRange().length > 1) {
                endTime = DateUtils.todayNightZeroTime(DateUtils.stringToDate(queryApply.getCreateTimeRange()[1]));
            }
            AppendWhere.andDateRange(dateWhere, "t.create_time", startTime, endTime);
            appendWhereList.add(dateWhere);
        }
        //----------------------------------------------
        if (appendWhereList.size() > 0)
            paramMap.put("appendWhereList", appendWhereList);
        //----------------------------------------------
        return paramMap;
    }
    /**
     * 显示商户申请列表Index页面
     */
    @ModuleEnter(path = "/merchant/applyList", name = "操作员商户申请", desc = "平台商户申请管理，业务操作员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_WORKER, roleRuler = "pf-operatorApplyIndex",
            moduleCode = PFMerchantConfig.MODULE_MERCHANT_MANAGE)
    @RequestMapping(value = "/applyList", method = {RequestMethod.POST})
    @ResponseBody
    public Object applyList(@Nullable @RequestBody JsonNode data) {
        try {
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            RequestApply queryApply = JsonUtil.jsonToBean(data.toString(), RequestApply.class);
            Map<String, Object> paramMap = getListParam(queryApply);
            PageData<PlateformMerchanApply> pageData = plateformMerchanApplyComponent.applyList(paramMap, pageNo, pageSize);
            return success("取得商户申请列表数据成功!", pageData);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 邮件验证
     */
    @RequestMapping(value = "/applyVerify")
    public ModelAndView applyVerify(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> param = new HashMap<>();
        try {
            String vContent = StringUtils.defaultString(BaseController.getRequestParam(request, null, "v"));
            if (StringUtils.isEmpty(vContent))
                throw new BusiException("验证信息为空");
            Map<String, Object> ret = plateformMerchanApplyComponent.applyVerify(vContent);
            if (BaseNnte.getRetSuc(ret)) {
                param.put("msg", ret.get("msg"));
                param.put("apply", ret.get("apply"));
                modelAndView.addObject("map", param);
                modelAndView.setViewName("front/merchant/apply/applyEmailVerifySuc");
            }
        } catch (BusiException be) {
            param.put("msg", be.getMessage());
            modelAndView.addObject("map", param);
            modelAndView.setViewName("front/merchant/apply/applyEmailVerifyFailed");
        }
        return modelAndView;
    }

    /**
     * 保存商户申请信息
     */
    @RequestMapping(value = "/saveMerchantApply")
    @ResponseBody
    public Object saveMerchantApply(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null)
                throw new BusiException("未取得商户申请信息");
            BaseComponent.checkModelFields(rApply);
            Map<String, Object> ret = new HashedMap();
            setParamMapDataEnv(request, ret);
            Map<String, Object> envData = (Map) ret.get("envData");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.saveApplyModify(rApply, oi, envData);
            if (BaseNnte.getRetSuc(retMap))
                return success("保存商户申请信息成功");
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (BusiException be) {
            return onException(be);
        }
    }
    /**
     * 确认商户申请，商户状态变化：编辑状态 ==> 待审核状态
     */
    @RequestMapping(value = "/confirmApply")
    @ResponseBody
    public Object confirmApply(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || NumberUtil.getDefaultLong(rApply.getId()) <= 0)
                throw new BusiException("未取得目标商户申请ID");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.confirmApply(rApply, oi);
            if (BaseNnte.getRetSuc(retMap))
                return success("确认商户申请成功");
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (BusiException be) {
            return onException(be);
        }
    }
    /**
     * 删除商户申请，商户状态变化：编辑状态 ==> 已删除状态
     */
    @RequestMapping(value = "/deleteApply")
    @ResponseBody
    public Object deleteApply(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || NumberUtil.getDefaultLong(rApply.getId()) <= 0)
                throw new BusiException("未取得目标商户申请ID");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.deleteApply(rApply, oi);
            if (BaseNnte.getRetSuc(retMap))
                return success("删除商户申请成功");
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (BusiException be) {
            return onException(be);
        }
    }
    /**
     * 发送商户申请的验证短信
     */
    @RequestMapping(value = "/sendApplyVerifySM")
    @ResponseBody
    public Object sendApplyVerifySM(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || StringUtils.isEmpty(rApply.getApplyPhone()))
                throw new BusiException("未取得发送短信的目标号码");
            Map<String, Object> retMap = plateformMerchanApplyComponent.sendConfirmSM(rApply.getApplyPhone(), 120);
            if (BaseNnte.getRetSuc(retMap))
                return success(StringUtils.defaultString(retMap.get("msg")));
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (BusiException be) {
            return onException(be);
        }
    }
    /**
     * 通过编码查询特定操作员信息
     */
    @RequestMapping(value = "/queryMerchantApply")
    @ResponseBody
    public Map<String, Object> queryMerchantApply(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || rApply.getId() == null || rApply.getId() <= 0)
                throw new BusiException("未取得商户申请编号信息");
            PlateformMerchanApply apply = plateformMerchanApplyComponent.getMerchantApplyById(rApply.getId());
            if (apply == null)
                throw new BusiException("未取得商户申请信息");
            ret.put("apply", apply);
            BaseNnte.setRetTrue(ret, "取得商户申请信息成功");
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }
    /**
     * 发送商户申请的验证邮件
     */
    @RequestMapping(value = "/sendApplyVerifyEmail")
    @ResponseBody
    public Map<String, Object> sendApplyVerifyEmail(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || rApply.getId() == null || rApply.getId() <= 0)
                throw new BusiException("未取得商户申请编号信息");
            PlateformMerchanApply apply = plateformMerchanApplyComponent.getMerchantApplyById(rApply.getId());
            if (apply == null)
                throw new BusiException("未取得商户申请信息");
            setParamMapDataEnv(request, ret);
            Map<String, Object> envData = (Map) ret.get("envData");
            envData.get("");
            plateformMerchanApplyComponent.sendConfirmEmail(apply, envData);
            BaseNnte.setRetTrue(ret, "发送商户申请邮件成功");
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }
    @RequestMapping(value = "/cropperLogo")
    public ModelAndView cropperLogo(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> map = new HashMap<>();
        setParamMapDataEnv(request, map);
        map.put("ratio_w", 1);
        map.put("ratio_h", 1);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/cropper/cropperForWeb");
        return modelAndView;
    }

    @RequestMapping(value = "/cropperPmImg")
    public ModelAndView cropperPmImg(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> map = new HashMap<>();
        setParamMapDataEnv(request, map);
        map.put("ratio_w", 1.618);
        map.put("ratio_h", 1);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/cropper/cropperForWeb");
        return modelAndView;
    }

    @RequestMapping(value = "/cropperPmCertificatePic")
    public ModelAndView cropperPmCertificatePic(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> map = new HashMap<>();
        setParamMapDataEnv(request, map);
        map.put("ratio_w", 0.85);
        map.put("ratio_h", 1);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/cropper/cropperForWeb");
        return modelAndView;
    }
    /**
     * ---------------------------------------------------------
     * */
    /**
     * 返回商户申请审核列表数据
     */
    @ModuleEnter(path = "/merchant/applyCheckList", name = "商户申请分配及审核", desc = "平台商户申请管理，业务管理员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_MANAGER, roleRuler = "pf-applyCheck",
            moduleCode = PFMerchantConfig.MODULE_MERCHANT_MANAGE)
    @RequestMapping(value = "/applyCheckList")
    @ResponseBody
    public Object applyCheckList(HttpServletRequest request,@Nullable @RequestBody JsonNode data) {
        try {
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            RequestApply applyRequest = JsonUtil.jsonToBean(data.toString(), RequestApply.class);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> paramMap = getListParam(applyRequest);
            PageData<PlateformMerchanApply> pageData = plateformMerchanApplyComponent.queryApplysForApplyCheck(applyRequest.getApplyState(), oi, paramMap,pageNo,pageSize);
            return success("取得商户申请审核列表数据成功!", pageData);
        } catch (Exception be) {
            return onException(be);
        }
    }

    /**
     * 手动分配商户申请给指定操作员，商户状态变化：待分配状态 ==> 待审核状态
     */
    @RequestMapping(value = "/applyDistribute")
    @ResponseBody
    public Object applyDistribute(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            String opeCode = node.getText("opeCode");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.applyDistribute(applyId, opeCode, oi);
            if (BaseNnte.getRetSuc(retMap))
                return success(StringUtils.defaultString(retMap.get("msg")));
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception be) {
            return onException(be);
        }
    }

    /**
     * 驳回已提交待分配商户申请，商户状态变化：待分配状态 ==> 待编辑状态
     */
    @RequestMapping(value = "/applyReject")
    @ResponseBody
    public Object applyReject(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.applyReject(applyId, oi);
            if (BaseNnte.getRetSuc(retMap))
                return success(StringUtils.defaultString(retMap.get("msg")));
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception be) {
            return onException(be);
        }
    }
    /**
     * 拒绝待审核商户申请，商户状态变化：待审核状态 ==> 已拒绝状态
     */
    @RequestMapping(value = "/applyRefuse")
    @ResponseBody
    public Object applyRefuse(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            String refuseReason = node.getText("refuseReason");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.applyRefuse(applyId, refuseReason, oi);
            if (BaseNnte.getRetSuc(retMap))
                return success(StringUtils.defaultString(retMap.get("msg")));
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception be) {
            return onException(be);
        }
    }

    /**
     * 显示商户审核通过对话框
     */
    @RequestMapping(value = "/applyPassDialog")
    @ResponseBody
    public Map<String, Object> applyPassDialog(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            if (applyId == null || applyId <= 0)
                throw new BusiException("没有找到要通过的商户申请");
            PlateformMerchanApply apply = plateformMerchanApplyComponent.getMerchantApplyById(applyId);
            if (apply == null || apply.getId() == null || apply.getId() <= 0)
                throw new BusiException("没有找到要通过的商户申请(1)");
            if (apply.getApplyState() == null || !apply.getApplyState().equals(PlateformMerchanApplyComponent.apply_state_waitcheck))
                throw new BusiException("商户状态不为待审核，不能执行通过操作");
            apply.setPmCode(plateformMerchanComponent.getCheckPassMerchantCode());
            Map<String, Object> map = new HashMap<>();
            setParamMapDataEnv(request, map);
            map.put("apply", apply);
            String htmlBody = FreeMarkertUtil.getFreemarkerFtl(request, request.getServletContext(),
                    FreeMarkertUtil.pathType.cls, map, "/templates/front/merchant/apply/applyPass.ftl");
            ret.put("htmlBody", htmlBody);
            BaseNnte.setRetTrue(ret, "取得商户通过对话框内容成功");
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }

    /**
     * 通过待审核商户申请，商户状态变化：待审核状态 ==> 已通过状态
     */
    @RequestMapping(value = "/applyPass")
    @ResponseBody
    public Object applyPass(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Map<String, Object> retMap = plateformMerchanApplyComponent.applyPass(rApply.getId(), rApply.getPmCode(),
                    rApply.getPmShortName(), rApply.getCheckDesc(), oi);
            if (BaseNnte.getRetSuc(retMap))
                return success(StringUtils.defaultString(retMap.get("msg")));
            else
                return error(StringUtils.defaultString(retMap.get("msg")));
        } catch (Exception be) {
            return onException(be);
        }
    }
}
