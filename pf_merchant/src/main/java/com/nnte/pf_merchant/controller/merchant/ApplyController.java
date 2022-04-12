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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            //----------------------------------------------
            if (appendWhereList.size() > 0)
                paramMap.put("appendWhereList", appendWhereList);
            //----------------------------------------------
            PageData<PlateformMerchanApply> pageData = plateformMerchanApplyComponent.applyList(paramMap, pageNo, pageSize);
            return success("取得商户申请列表数据成功!", pageData);
        } catch (Exception e) {
            return onException(e);
        }
    }

    @RequestMapping(value = "/operatorApplyIndex")
    public ModelAndView operatorApplyIndex(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> map = BaseNnte.newMapRetObj();
        setParamMapDataEnv(request, map);
        OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
        map.put("OperatorInfo", oi);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/merchant/apply/operatorApplyIndex");
        return modelAndView;
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
     * 返回商户申请列表数据
     */
    @RequestMapping(value = "/list")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        Integer sEcho = NumberUtil.getDefaultInteger(request.getParameter("sEcho"));
        try {
            RequestApply apply = new RequestApply();
            BaseController.copyFromRequestParams(request, apply);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformMerchanApply dto = new PlateformMerchanApply();
            dto.setStart(NumberUtil.getDefaultInteger(request.getParameter("iDisplayStart")));
            dto.setLimit(NumberUtil.getDefaultInteger(request.getParameter("iDisplayLength")));
            BeanUtils.copyFromSrc(apply, dto);
            if (dto.getPmCompanyPerson() != null && dto.getPmCompanyPerson() < 0)
                dto.setPmCompanyPerson(null);
            if (dto.getApplyWays() != null && dto.getApplyWays() < 0)
                dto.setApplyWays(null);
            Map<String, Object> appendParam = null;
            Map<String, Object> tmpMap = DateUtils.splitDateRange(apply.getCreateTimeRange(), DateUtils.DF_YMD);
            if (tmpMap != null && NumberUtil.getDefaultInteger(tmpMap.get("suc")).equals(1)) {
                appendParam = new HashMap<>();
                appendParam.put("createTimeStart", tmpMap.get("startdate"));
                appendParam.put("createTimeEnd", tmpMap.get("enddate"));
            }
            Map<String, Object> loadMap = plateformMerchanApplyComponent.queryApplysForOperatorApply(dto, oi, appendParam);
            Integer count = NumberUtil.getDefaultInteger(loadMap.get("count"));
            List<PlateformMerchanApply> lists = (List<PlateformMerchanApply>) loadMap.get("list");
            if (lists != null)
                printLoadListMsg(response, sEcho + 1, count, JsonUtil.beanToJson(lists));
            else
                throw new BusiException("没有查询到列表数据");
        } catch (BusiException be) {
            List<PlateformMerchanApply> lists = new ArrayList<>();
            printLoadListMsg(response, sEcho + 1, 0, JsonUtil.beanToJson(lists));
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

    /**
     * 发送商户申请的验证短信
     */
    @RequestMapping(value = "/sendApplyVerifySM")
    @ResponseBody
    public Map<String, Object> sendApplyVerifySM(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || StringUtils.isEmpty(rApply.getApplyPhone()))
                throw new BusiException("未取得发送短信的目标号码");
            plateformMerchanApplyComponent.sendConfirmSM(rApply.getApplyPhone(), 120);
            BaseNnte.setRetTrue(ret, "发送商户申请验证短信成功");
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
     * 确认商户申请，商户状态变化：编辑状态 ==> 待审核状态
     */
    @RequestMapping(value = "/confirmApply")
    @ResponseBody
    public Map<String, Object> confirmApply(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || NumberUtil.getDefaultLong(rApply.getId()) <= 0)
                throw new BusiException("未取得目标商户申请ID");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.confirmApply(rApply, oi);
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }

    /**
     * 确认商户申请，商户状态变化：编辑状态 ==> 待分配状态
     */
    @RequestMapping(value = "/deleteApply")
    @ResponseBody
    public Map<String, Object> deleteApply(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            if (rApply == null || NumberUtil.getDefaultLong(rApply.getId()) <= 0)
                throw new BusiException("未取得目标商户申请ID");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.deleteApply(rApply, oi);
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }
    /**
     * ---------------------------------------------------------
     * */
    /**
     * 显示商户申请分配及审核列表Index页面
     */
    @ModuleEnter(path = "/merchant/merchantApply/applyCheckIndex", name = "商户申请分配及审核", desc = "平台商户申请管理，业务管理员功能",
            sysRole = PFMerchantSysRole.PLATEFORM_MERCAHNT_MANAGER, roleRuler = "pf-applyCheckIndex",
            moduleCode = PFMerchantConfig.MODULE_MERCHANT_MANAGE)
    @RequestMapping(value = "/applyCheckIndex")
    public ModelAndView applyCheckIndex(HttpServletRequest request, ModelAndView modelAndView) {
        Map<String, Object> map = BaseNnte.newMapRetObj();
        setParamMapDataEnv(request, map);
        OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
        map.put("OperatorInfo", oi);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/merchant/apply/applyCheckIndex");
        return modelAndView;
    }

    /**
     * 返回商户申请列表数据
     */
    @RequestMapping(value = "/applyCheckList")
    public void applyCheckList(HttpServletRequest request, HttpServletResponse response) {
        Integer sEcho = NumberUtil.getDefaultInteger(request.getParameter("sEcho"));
        try {
            RequestApply apply = new RequestApply();
            BaseController.copyFromRequestParams(request, apply);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformMerchanApply dto = new PlateformMerchanApply();
            dto.setStart(NumberUtil.getDefaultInteger(request.getParameter("iDisplayStart")));
            dto.setLimit(NumberUtil.getDefaultInteger(request.getParameter("iDisplayLength")));
            BeanUtils.copyFromSrc(apply, dto);
            if (dto.getPmCompanyPerson() != null && dto.getPmCompanyPerson() < 0)
                dto.setPmCompanyPerson(null);
            if (dto.getApplyWays() != null && dto.getApplyWays() < 0)
                dto.setApplyWays(null);
            Map<String, Object> appendParam = null;
            Map<String, Object> tmpMap = DateUtils.splitDateRange(apply.getCreateTimeRange(), DateUtils.DF_YMD);
            if (tmpMap != null && NumberUtil.getDefaultInteger(tmpMap.get("suc")).equals(1)) {
                appendParam = new HashMap<>();
                appendParam.put("createTimeStart", tmpMap.get("startdate"));
                appendParam.put("createTimeEnd", tmpMap.get("enddate"));
            }
            Map<String, Object> loadMap = plateformMerchanApplyComponent.queryApplysForApplyCheck(dto, oi, appendParam);
            Integer count = NumberUtil.getDefaultInteger(loadMap.get("count"));
            List<PlateformMerchanApply> lists = (List<PlateformMerchanApply>) loadMap.get("list");
            if (lists != null)
                printLoadListMsg(response, sEcho + 1, count, JsonUtil.beanToJson(lists));
            else
                throw new BusiException("没有查询到列表数据");
        } catch (BusiException be) {
            List<PlateformMerchanApply> lists = new ArrayList<>();
            printLoadListMsg(response, sEcho + 1, 0, JsonUtil.beanToJson(lists));
        }
    }

    /**
     * 手动分配商户申请给指定操作员，商户状态变化：待分配状态 ==> 待审核状态
     */
    @RequestMapping(value = "/applyDistribute")
    @ResponseBody
    public Map<String, Object> applyDistribute(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            String opeCode = node.getText("opeCode");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.applyDistribute(applyId, opeCode, oi);
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }

    /**
     * 驳回已提交待分配商户申请，商户状态变化：待分配状态 ==> 待编辑状态
     */
    @RequestMapping(value = "/applyReject")
    @ResponseBody
    public Map<String, Object> applyReject(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.applyReject(applyId, oi);
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }

    /**
     * 显示商户拒绝对话框
     */
    @RequestMapping(value = "/applyRefuseDialog")
    @ResponseBody
    public Map<String, Object> applyRefuseDialog(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            if (applyId == null || applyId <= 0)
                throw new BusiException("没有找到要拒绝的商户申请");
            PlateformMerchanApply apply = plateformMerchanApplyComponent.getMerchantApplyById(applyId);
            if (apply == null || apply.getId() == null || apply.getId() <= 0)
                throw new BusiException("没有找到要拒绝的商户申请(1)");
            if (apply.getApplyState() == null || !apply.getApplyState().equals(PlateformMerchanApplyComponent.apply_state_waitcheck))
                throw new BusiException("商户状态不为待审核，不能执行拒绝操作");
            Map<String, Object> map = new HashMap<>();
            setParamMapDataEnv(request, map);
            map.put("apply", apply);
            String htmlBody = FreeMarkertUtil.getFreemarkerFtl(request, request.getServletContext(),
                    FreeMarkertUtil.pathType.cls, map, "/templates/front/merchant/apply/applyRefuse.ftl");
            ret.put("htmlBody", htmlBody);
            BaseNnte.setRetTrue(ret, "取得商户拒绝对话框内容成功");
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }

    /**
     * 拒绝待审核商户申请，商户状态变化：待审核状态 ==> 已拒绝状态
     */
    @RequestMapping(value = "/applyRefuse")
    @ResponseBody
    public Map<String, Object> applyRefuse(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            Integer applyId = node.getInteger("id");
            String refuseReason = node.getText("refuseReason");
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.applyRefuse(applyId, refuseReason, oi);
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
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
     * 拒绝待审核商户申请，商户状态变化：待审核状态 ==> 已拒绝状态
     */
    @RequestMapping(value = "/applyPass")
    @ResponseBody
    public Map<String, Object> applyPass(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try {
            RequestApply rApply = JsonUtil.jsonToBean(json.toString(), RequestApply.class);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            ret = plateformMerchanApplyComponent.applyPass(rApply.getId(), rApply.getPmCode(),
                    rApply.getPmShortName(), rApply.getCheckDesc(), oi);
        } catch (Exception be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
            return ret;
        }
        return ret;
    }
}
