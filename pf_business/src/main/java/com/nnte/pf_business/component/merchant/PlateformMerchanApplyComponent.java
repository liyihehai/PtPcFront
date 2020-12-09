package com.nnte.pf_business.component.merchant;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.base.JedisComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.*;
import com.nnte.pf_business.component.mqcomp.EmailMQComponent;
import com.nnte.pf_business.component.mqcomp.SMMQComponent;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.entertity.EmailContent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.SMContent;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApply;
import com.nnte.pf_business.mapper.workdb.merchantapply.PlateformMerchanApplyService;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.request.RequestApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
/**
 * 商户申请组件
 * 日志打印位置：MerchantManager 商户管理
 * */
@BusiLogAttr(value = "MerchantManager")
public class PlateformMerchanApplyComponent extends BaseBusiComponent {

    @Autowired
    private PlateformMerchanApplyService plateformMerchanApplyService;
    @Autowired
    private JedisComponent jedisComponent;
    @Autowired
    private SMMQComponent sMMQComponent;
    @Autowired
    private EmailMQComponent emailMQComponent;
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;

    private static final String MERCHANT_APPLY_SM_KEY = "MERCHANT_APPLY_SM_KEY_";
    private static final String MERCHANT_APPLY_VERIFY_AESKEY = "merchant$Apply%AesKey@2020#05*28";

    /**
     * 申请状态定义 ： 申请状态:-1，删除，0：申请编辑,1:申请通过，2，待审核,3，申请未通过，4，待分配
     * */
    public static final Integer apply_state_del         = -1;   //申请状态 - 删除
    public static final Integer apply_state_edit        = 0;    //申请状态 - 编辑
    public static final Integer apply_state_suc         = 1;    //申请状态 - 通过
    public static final Integer apply_state_waitcheck   = 2;    //申请状态 - 待审核
    public static final Integer apply_state_failed      = 3;    //申请状态 - 未通过
    public static final Integer apply_state_waitdist    = 4;    //申请状态 - 待分配

    /**
     * 查询商户申请的操作员申请列表
     * 如果是超级管理员，则查询所有的操作员申请记录，
     * 如果不是超级管理员，只能查询操作员自己申请的申请记录
     * */
    public Map<String, Object> queryApplysForOperatorApply(PlateformMerchanApply dto, OperatorInfo oi,
                                                           Map<String,Object> appendParam) throws BusiException{
        if (!dto.getApplyState().equals(apply_state_waitdist)||!dto.getApplyState().equals(apply_state_edit)){
            PlateformOperator ope=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            if (!ope.getOpeType().equals(PlateformOperatorComponent.OperatorType.SupperMgr.getValue()))
                dto.setCreatorCode(oi.getOperatorCode());//如果已经分配了操作员
        }
        return queryApplyList(dto,appendParam);
    }
    /**
     * 查询商户申请的用于审核的列表
     * 如果是超级管理员，则查询指定申请状态下所有的操作员申请记录，
     * 如果不是超级管理员，在待审核，通过，拒绝 状态下只能查询分配给自己的申请记录
     * */
    public Map<String, Object> queryApplysForApplyCheck(PlateformMerchanApply dto, OperatorInfo oi,
                                                           Map<String,Object> appendParam) throws BusiException{
        if (dto.getApplyState().equals(apply_state_waitcheck)||dto.getApplyState().equals(apply_state_suc)
            ||dto.getApplyState().equals(apply_state_failed)){
            PlateformOperator ope=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            if (!ope.getOpeType().equals(PlateformOperatorComponent.OperatorType.SupperMgr.getValue()))
                dto.setOpeCode(oi.getOperatorCode());
        }
        return queryApplyList(dto,appendParam);
    }
    /**
     * 查询申请列表
     */
    public Map<String, Object> queryApplyList(PlateformMerchanApply dto,Map<String,Object> appendParam)
            throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        Map<String,Object> paramMap=MapUtil.beanToMap(dto);
        if (appendParam!=null && appendParam.size()>0){
            paramMap.putAll(appendParam);
        }
        if (paramMap.get("sort")==null) {
            paramMap.put("sort", "create_time");
            paramMap.put("dir", "desc");
        }
        Integer count=plateformMerchanApplyService.findPlateformMerchanApplysCustmerCount(paramMap);
        if (count!=null && count>0) {
            ret.put("count", count);
            List<PlateformMerchanApply> list = plateformMerchanApplyService.findPlateformMerchanApplysCustmerList(paramMap);
            ret.put("list", list);
            BaseNnte.setRetTrue(ret, "查询列表成功");
        }else{
            ret.put("count", 0);
            ret.put("list", null);
            BaseNnte.setRetTrue(ret, "查询列表成功");
        }
        return ret;
    }

    /**
     * 检测用户邮箱是否存在
     */
    public boolean isMerchantEmailExist(String eMail) {
        PlateformMerchanApply dto = new PlateformMerchanApply();
        dto.setApplyEmail(eMail);
        List<PlateformMerchanApply> list = plateformMerchanApplyService.findModelList(dto);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    /**
     * 检测商户名称是否存在
     */
    public boolean isMerchantPmNameExist(String pmName) {
        PlateformMerchanApply dto = new PlateformMerchanApply();
        dto.setPmName(pmName);
        List<PlateformMerchanApply> list = plateformMerchanApplyService.findModelList(dto);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    /**
     * 通过ID查询申请
     */
    public PlateformMerchanApply getMerchantApplyById(Integer id) {
        if (id==null || id<=0)
            return null;
        PlateformMerchanApply ret = plateformMerchanApplyService.findModelByKey(id);
        return ret;
    }

    /**
     * 通过电话号码发送一条确认短信，用于验证电话号码的真实性
     */
    public Map<String,Object> sendConfirmSM(String phoneNo, Integer seconds) {
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        try{
            Integer random = StringUtils.getRandomNum(100000, 999999);
            SMContent sm = new SMContent();
            sm.setPhoneNo(phoneNo);
            sm.setContent(random.toString());
            sm.setSendTime(DateUtils.dateToString(new Date(), DateUtils.DF_YMDHMS));
            sm.setSeconds(seconds);
            sMMQComponent.send2MQ(sm);
            //--执行短信发送操作---
            //--在Redis挂载该消息--
            String key = MERCHANT_APPLY_SM_KEY+phoneNo;
            jedisComponent.setObj(key,seconds,sm);
            String msg = "短信验证码发送成功["+sm.getPhoneNo()+":"+sm.getContent()+"]";
            BaseNnte.setRetTrue(ret,msg);
            logFileMsg(msg);
        }catch (BusiException be){
            BaseNnte.setRetFalse(ret,1002,be.getMessage());
            logException(be);
        }
        return ret;
    }

    /**
     * 对申请信息发送确认邮件
     */
    public void sendConfirmEmail(PlateformMerchanApply apply,Map<String, Object> envData) throws BusiException{
        try {
            if (apply==null)
                throw new Exception("商户申请对象为NULL");
            if (apply.getEmailConfirmState()!=null && apply.getEmailConfirmState().equals(1))
                throw new Exception("商户申请验证邮件已经确认，无需再次发送");
            ObjectNode node = JsonUtil.newJsonNode();
            String randomCode=StringUtils.getRandomStringByLength(16);
            node.put("id", apply.getId());
            node.put("emailRandCode", randomCode);
            String json = node.toString();
            String verifyLink = Base64Utils.encode(AESUtils.encryptECB(json, MERCHANT_APPLY_VERIFY_AESKEY).getBytes());
            String host = appConfig.getConfig("localHostName");
            String contextPath = StringUtils.defaultString(envData.get("contextPath"));
            String verifyUrl = host + contextPath + "/merchant/merchantApply/applyVerify?v=" + verifyLink;
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("verifyUrl",verifyUrl);
            String content = FreeMarkertUtil.getFreemarkerFtl(this,FreeMarkertUtil.pathType.cls,paramMap,"/templates/front/merchant/apply/emailverify.ftl");
            EmailContent ec = new EmailContent();
            ec.setId(NumberDefUtil.getDefLong(apply.getId()));
            ec.setEmail(apply.getApplyEmail());
            ec.setRandomCode(randomCode);
            ec.setTitle("商户申请确认邮件");
            ec.setContent(content);
            emailMQComponent.send2MQ(ec);
            //更改邮件验证信息
            PlateformMerchanApply updateDto = new PlateformMerchanApply();
            updateDto.setId(apply.getId());
            updateDto.setEmailSendState(1);//已发送
            updateDto.setEmailRandomCode(ec.getRandomCode());
            updateDto.setEmailSendTime(new Date());
            updateDto.setEmailConfirmState(0);//未确认
            plateformMerchanApplyService.updateModel(updateDto);
        }catch (Exception e){
            throw new BusiException(e);
        }
    }
    /**
     * 商户申请邮件验证
     * */
    public Map<String,Object> applyVerify(String vContent)throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        try{
            String json=AESUtils.decryptECB(new String(Base64Utils.decode(vContent)),MERCHANT_APPLY_VERIFY_AESKEY);
            JsonUtil.JNode node=JsonUtil.createJNode(JsonUtil.jsonToNode(json));
            if (node==null)
                throw new Exception("验证内容为空");
            PlateformMerchanApply apply=getMerchantApplyById(node.getInteger("id"));
            if (apply==null)
                throw new Exception("没找到指定的申请记录");
            if (apply.getEmailConfirmState()!=null && apply.getEmailConfirmState().equals(1)){
                BaseNnte.setRetTrue(ret,"验证成功，申请已验证!");
                return ret;
            }
            String randomCode=node.getText("emailRandCode");
            if (StringUtils.isEmpty(randomCode))
                throw new Exception("验证信息不合法");
            if (randomCode.equals(apply.getEmailRandomCode())){
                PlateformMerchanApply updateDto = new PlateformMerchanApply();
                updateDto.setId(apply.getId());
                updateDto.setEmailConfirmState(1);//设置邮件确认状态为1
                plateformMerchanApplyService.updateModel(updateDto);
                ret.put("apply",getMerchantApplyById(apply.getId()));
                BaseNnte.setRetTrue(ret,"验证成功!");
                return ret;
            }else
                throw new Exception("验证失败，验证信息不合法");
        }catch (Exception e){
            throw new BusiException(e);
        }
    }
    /**
     * 商户验证短信
     * */
    public SMContent applyVerifySM(String phoneNo,String smRandomCode)throws BusiException{
        try{
            //--在Redis挂载该消息--
            String key = MERCHANT_APPLY_SM_KEY+phoneNo;
            SMContent sm=jedisComponent.getObj(key,SMContent.class);
            if (sm==null)
                throw new BusiException("验证失败，验证短信已过期");
            if (sm.getContent()==null || !sm.getContent().equals(smRandomCode))
                throw new BusiException("验证失败，验证码错误");
            return sm;
        }catch (Exception e){
            throw new BusiException(e);
        }
    }
    /**
     * 保存商户申请信息
     */
    public Map<String, Object> saveApplyModify(RequestApply rApply, OperatorInfo oi,
                                               Map<String, Object> envData) throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        if (rApply.getActionType().equals(1)) {
            PlateformMerchanApply dto = new PlateformMerchanApply();
            if (isMerchantPmNameExist(rApply.getPmName()))
                throw new BusiException("商户名称已占用");
            dto.setPmName(rApply.getPmName());
            dto.setConfirmType(rApply.getConfirmType());
            if (dto.getConfirmType().equals(1)) {
                if (StringUtils.isEmpty(rApply.getApplyEmail()))
                    throw new BusiException("邮件确认时邮箱不能为空");
                dto.setApplyEmail(rApply.getApplyEmail());
            } else if (dto.getConfirmType().equals(2)) {
                if (StringUtils.isEmpty(rApply.getApplyPhone()))
                    throw new BusiException("电话号码确认时号码不能为空");
                if (StringUtils.isEmpty(rApply.getSmRandomCode()))
                    throw new BusiException("电话号码确认短信验证码不能为空");
                SMContent sm=applyVerifySM(rApply.getApplyPhone(),rApply.getSmRandomCode());
                dto.setApplyPhone(rApply.getApplyPhone());
                dto.setSmSendState(1);
                dto.setSmRandomCode(sm.getContent());
                dto.setSmSendTime(DateUtils.stringToDate(sm.getSendTime(),DateUtils.DF_YMDHMS));
                dto.setSmConfirmState(1);
            }
            dto.setPmCompanyPerson(rApply.getPmCompanyPerson());
            dto.setApplyWays(rApply.getApplyWays());
            if (dto.getApplyWays().equals(1)) {//如果是操作员申请，申请人填操作员姓名
                dto.setApplyerCode(oi.getOperatorCode());
                dto.setApplyerName(oi.getOperatorName());
            }
            else {//如果不是操作员申请，申请人填申请请求中的申请人姓名
                if (dto.getApplyWays().equals(2)|| dto.getApplyWays().equals(3)){
                    if (StringUtils.isEmpty(rApply.getApplyerCode()))
                        throw new BusiException("用户申请时申请人代码不能为空");
                }
                else if (dto.getApplyWays().equals(4)){
                    if (StringUtils.isEmpty(rApply.getApplyerCode()))
                        throw new BusiException("业务员申请时申请人代码不能为空");
                }
                dto.setApplyerCode(rApply.getApplyerCode());
                dto.setApplyerName(rApply.getApplyerName());
            }
            dto.setApplyContent(rApply.getApplyContent());
            dto.setApplyMemo(rApply.getApplyMemo());
            dto.setApplyState(apply_state_edit);//初始状态为申请编辑
            dto.setCreatorCode(oi.getOperatorCode());
            dto.setCreatorName(oi.getOperatorName());
            dto.setCreateTime(new Date());
            Integer count = plateformMerchanApplyService.addModel(dto);
            if (count == null || count <= 0 || dto.getId() == null || dto.getId() <= 0)
                throw new BusiException("保存商户申请时出现异常");
            PlateformMerchanApply apply = getMerchantApplyById(dto.getId());
            if (dto.getConfirmType().equals(1))
                sendConfirmEmail(apply,envData);//如果是邮件确认，发送确认邮件消息
            ret.put("apply",apply);
            BaseNnte.setRetTrue(ret,"新增商户申请成功");
        } else {//如果是编辑操作
            PlateformMerchanApply srcApply=getMerchantApplyById(rApply.getId());
            if (srcApply==null)
                throw new BusiException("没找到需要更改的商户申请");
            if (srcApply.getApplyState()==null || !srcApply.getApplyState().equals(0))
                throw new BusiException("商户申请状态不为可编辑");
            PlateformMerchanApply updateDto=new PlateformMerchanApply();
            updateDto.setId(srcApply.getId());
            updateDto.setPmName(rApply.getPmName());
            updateDto.setApplyMemo(rApply.getApplyMemo());
            updateDto.setPmCompanyPerson(rApply.getPmCompanyPerson());
            updateDto.setConfirmType(rApply.getConfirmType());
            if (updateDto.getConfirmType().equals(1)){
                if (StringUtils.isEmpty(rApply.getApplyEmail()))
                    throw new BusiException("邮件确认时邮箱不能为空");
                if (!rApply.getApplyEmail().equals(srcApply.getApplyEmail())){
                    updateDto.setApplyEmail(rApply.getApplyEmail());
                    updateDto.setEmailSendState(0);
                    updateDto.setEmailRandomCode("");
                    updateDto.setEmailConfirmState(0);
                }
            }else if(updateDto.getConfirmType().equals(2)){
                if (StringUtils.isEmpty(rApply.getApplyPhone()))
                    throw new BusiException("电话号码确认时号码不能为空");
                if (srcApply.getSmConfirmState()==null || 1!=srcApply.getSmConfirmState()
                        ||!StringUtils.defaultString(rApply.getApplyPhone()).equals(srcApply.getApplyPhone())){
                    if (StringUtils.isEmpty(rApply.getSmRandomCode()))
                        throw new BusiException("电话号码确认短信验证码不能为空");
                    if (!rApply.getApplyPhone().equals(srcApply.getApplyPhone())){
                        SMContent sm=applyVerifySM(rApply.getApplyPhone(),rApply.getSmRandomCode());
                        updateDto.setApplyPhone(rApply.getApplyPhone());
                        updateDto.setSmSendState(1);
                        updateDto.setSmRandomCode(sm.getContent());
                        updateDto.setSmSendTime(DateUtils.stringToDate(sm.getSendTime(),DateUtils.DF_YMDHMS));
                        updateDto.setSmConfirmState(1);
                    }
                }
            }
            updateDto.setApplyWays(rApply.getApplyWays());
            if (updateDto.getApplyWays().equals(1))//如果是操作员申请，申请人填操作员姓名
                updateDto.setApplyerName(oi.getOperatorName());
            else//如果不是操作员申请，申请人填申请请求中的申请人姓名
                updateDto.setApplyerName(rApply.getApplyerName());
            updateDto.setApplyContent(rApply.getApplyContent());
            Integer count = plateformMerchanApplyService.updateModel(updateDto);
            if (count == null || count <= 0)
                throw new BusiException("保存商户申请变更时出现异常");
            PlateformMerchanApply apply = getMerchantApplyById(updateDto.getId());
            if (apply.getConfirmType().equals(1) && !NumberUtil.getDefaultInteger(apply.getEmailConfirmState()).equals(1)){
                sendConfirmEmail(apply,envData);//如果是邮件确认，发送确认邮件消息
            }
            ret.put("apply",apply);
            BaseNnte.setRetTrue(ret,"更改商户申请成功");
        }
        return ret;
    }
    /**
     * 确认商户申请，商户状态变化：编辑状态 ==> 待审核状态
     * 要验证邮箱或短信验证是否通过
     * */
    public Map confirmApply(RequestApply rApply,OperatorInfo oi)throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(rApply.getId());
        if (apply==null || apply.getId()==null || apply.getId()<=0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState()==null || !apply.getApplyState().equals(apply_state_edit))
            throw new BusiException("商户申请状态不是编辑状态");
        //按验证方式判断是否验证过
        if (apply.getConfirmType()==null)
            throw new BusiException("商户申请验证方式未确定");
        if (apply.getConfirmType().equals(1)) {//如果是邮箱验证
            if (apply.getEmailConfirmState()==null || !apply.getEmailConfirmState().equals(1))
                throw new BusiException("商户申请邮件验证未完成");
        }else if (apply.getConfirmType().equals(2)){
            //如果是短信验证
            if (apply.getSmConfirmState()==null || !apply.getSmConfirmState().equals(1))
                throw new BusiException("商户申请短信验证未完成");
        }
        PlateformMerchanApply updateDto=new PlateformMerchanApply();
        updateDto.setId(apply.getId());
        updateDto.setApplyState(apply_state_waitdist);
        updateDto.setConfirmCode(oi.getOperatorCode());
        updateDto.setConfirmName(oi.getOperatorName());
        updateDto.setConfirmTime(new Date());
        Integer count = plateformMerchanApplyService.updateModel(updateDto);
        if (count == null || count <= 0)
            throw new BusiException("保存商户申请变更时出现异常");
        apply.setApplyState(apply_state_waitdist);
        ret.put("apply",apply);
        BaseNnte.setRetTrue(ret,"确认商户申请成功");
        return ret;
    }

    /**
     * 删除商户申请，商户状态变化：编辑状态 ==> 物理删除，非编辑非删除状态 ==> 删除状态
     * */
    public Map deleteApply(RequestApply rApply,OperatorInfo oi)throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(rApply.getId());
        if (apply == null || apply.getId() == null || apply.getId() <= 0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState() == null || apply.getApplyState().equals(apply_state_del))
            throw new BusiException("商户申请当前状态不可删除");
        if (apply.getApplyState().equals(apply_state_edit)){//物理删除
            Integer count = plateformMerchanApplyService.deleteModel(apply.getId());
            if (count == null || count <= 0)
                throw new BusiException("删除商户申请时出现异常");
        }else{
            PlateformMerchanApply updateDto=new PlateformMerchanApply();
            updateDto.setId(apply.getId());
            updateDto.setApplyState(apply_state_del);
            Integer count = plateformMerchanApplyService.updateModel(updateDto);
            if (count == null || count <= 0)
                throw new BusiException("删除商户申请时出现异常(1)");
        }
        BaseNnte.setRetTrue(ret,"删除商户申请["+apply.getPmName()+"]成功");
        return ret;
    }
    /**
     * 将商户申请手动分配给指定的操作员
     * */
    public Map applyDistribute(Integer applyId,String opeCode,OperatorInfo oi)throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(applyId);
        if (apply == null || apply.getId() == null || apply.getId() <= 0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState()==null || !apply.getApplyState().equals(apply_state_waitdist))
            throw new BusiException("商户申请未处于待分配状态，不能分配操作员");
        PlateformOperator ope=plateformOperatorComponent.getOperatorByCode(opeCode);
        if (ope == null || ope.getId() == null || ope.getId()<=0)
            throw new BusiException("未找到指定的操作员");
        PlateformMerchanApply updateDto = new PlateformMerchanApply();
        updateDto.setId(apply.getId());
        updateDto.setOpeCode(ope.getOpeCode());
        updateDto.setOpeName(ope.getOpeName());
        updateDto.setLockTime(new Date());
        updateDto.setApplyState(apply_state_waitcheck);
        Integer count = plateformMerchanApplyService.updateModel(updateDto);
        if (count == null || count <= 0)
            throw new BusiException("分配商户申请["+apply.getPmName()+"]失败");
        BaseNnte.setRetTrue(ret,"分配商户申请["+apply.getPmName()+"]成功");
        return ret;
    }

    /**
     * 驳回商户申请
     * */
    public Map applyReject(Integer applyId,OperatorInfo oi)throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(applyId);
        if (apply == null || apply.getId() == null || apply.getId() <= 0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState()==null || !apply.getApplyState().equals(apply_state_waitdist))
            throw new BusiException("商户申请未处于待分配状态，不能执行驳回操作");
        if (apply.getApplyWays()==null || apply.getApplyWays().equals(2) || apply.getApplyWays().equals(3))
            throw new BusiException("商户申请的申请方式为自助申请，不能执行驳回操作");
        PlateformMerchanApply updateDto = new PlateformMerchanApply();
        updateDto.setId(apply.getId());
        updateDto.setApplyState(apply_state_edit);
        Integer count = plateformMerchanApplyService.updateModel(updateDto);
        if (count == null || count <= 0)
            throw new BusiException("驳回商户申请["+apply.getPmName()+"]失败");
        BaseNnte.setRetTrue(ret,"驳回商户申请["+apply.getPmName()+"]成功");
        return ret;
    }
    /**
     * 拒绝商户申请
     * */
    public Map applyRefuse(Integer applyId,String refuseReason,OperatorInfo oi)throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(applyId);
        if (apply == null || apply.getId() == null || apply.getId() <= 0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState()==null || !apply.getApplyState().equals(apply_state_waitcheck))
            throw new BusiException("商户申请未处于待审核状态，不能执行驳回操作");
        if (StringUtils.isEmpty(refuseReason))
            throw new BusiException("拒绝商户申请时拒绝原因不能为空");
        PlateformMerchanApply updateDto = new PlateformMerchanApply();
        updateDto.setId(apply.getId());
        updateDto.setApplyState(apply_state_failed);
        updateDto.setCheckResult(0); //审核不通过
        updateDto.setCheckerCode(oi.getOperatorCode());
        updateDto.setCheckerName(oi.getOperatorName());
        updateDto.setCheckTime(new Date());
        updateDto.setCheckDesc(refuseReason);
        Integer count = plateformMerchanApplyService.updateModel(updateDto);
        if (count == null || count <= 0)
            throw new BusiException("拒绝商户申请["+apply.getPmName()+"]失败");
        BaseNnte.setRetTrue(ret,"拒绝商户申请["+apply.getPmName()+"]成功");
        return ret;
    }
    /**
     * 商户申请通过操作
     * */
    public Map applyPass(Integer applyId,String pmCode,String pmShortName,String checkDesc,OperatorInfo oi)
            throws BusiException{
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        PlateformMerchanApply apply = getMerchantApplyById(applyId);
        if (apply == null || apply.getId() == null || apply.getId() <= 0)
            throw new BusiException("未找到指定的商户申请");
        if (apply.getApplyState()==null || !apply.getApplyState().equals(apply_state_waitcheck))
            throw new BusiException("商户申请未处于待审核状态，不能执行审核通过操作");
        Map<String, Object> paramMap=new HashMap<>();
        apply.setCheckDesc(checkDesc);
        apply.setPmCode(pmCode);
        paramMap.put("pmShortName",pmShortName);
        ret=plateformMerchanComponent.passMerchantApply(paramMap,apply,oi);
        return ret;
    }
}
