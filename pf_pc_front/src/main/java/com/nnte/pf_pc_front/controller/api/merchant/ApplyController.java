package com.nnte.pf_pc_front.controller.api.merchant;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanApplyComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.config.PFMerchantSysRole;
import com.nnte.pf_merchant.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_merchant.request.RequestApply;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/merchant/merchantApply")
public class ApplyController extends BaseController {
    @Autowired
    private PlateformMerchanApplyComponent plateformMerchanApplyComponent;

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
        AppendWhere.addLikeStringToWhereList(queryApply.getPmName(),"t.pm_name",appendWhereList);
        AppendWhere.addLikeStringToWhereList(queryApply.getCreatorName(),"t.creator_name",appendWhereList);
        AppendWhere.addLikeStringToWhereList(queryApply.getConfirmName(),"t.confirm_name",appendWhereList);
        AppendWhere.addLikeStringToWhereList(queryApply.getCheckerName(),"t.checker_name",appendWhereList);
        AppendWhere.addDataRangeToWhereList(queryApply.getCreateTimeRange(),"t.create_time",appendWhereList);
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
            moduleCode = PFMerchantConfig.Module_Code)
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
     * 返回商户申请审核列表数据
     */
    @ModuleEnter(path = "/merchant/applyCheckList", name = "商户申请分配及审核", desc = "平台商户申请管理，业务管理员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_MANAGER, roleRuler = "pf-applyCheck",
            moduleCode = PFMerchantConfig.Module_Code)
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
