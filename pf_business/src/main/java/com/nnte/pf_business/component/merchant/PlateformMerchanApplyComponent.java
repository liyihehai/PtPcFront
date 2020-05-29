package com.nnte.pf_business.component.merchant;

import com.fasterxml.jackson.databind.JsonNode;
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

    private static final String MERCHANT_APPLY_SM_KEY = "MERCHANT_APPLY_SM_KEY_";
    private static final String MERCHANT_APPLY_VERIFY_AESKEY = "merchant$Apply%AesKey@2020#05*28";
    /**
     * 查询申请列表(超级管理员查询所有数据,其他的角色查询权限范围内的申请数据)
     */
    public Map<String, Object> queryApplyList(PlateformMerchanApply dto, OperatorInfo oi) throws BusiException {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        if (!dto.getApplyState().equals(4)||!dto.getApplyState().equals(0)){
            PlateformOperator ope=plateformOperatorComponent.getOperatorByCode(oi.getOperatorCode());
            if (!ope.getOpeType().equals(PlateformOperatorComponent.OperatorType.SupperMgr.getValue()))
                dto.setOpeCode(oi.getOperatorCode());//如果已经分配了操作员
        }
        Integer count=plateformMerchanApplyService.findModelCount(dto);
        if (count!=null && count>0) {
            ret.put("count", count);
            List<PlateformMerchanApply> list = plateformMerchanApplyService.findModelWithPg(dto);
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
    public PlateformMerchanApply getMerchantApplyById(Long id) {
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
            ec.setId(apply.getId());
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
            PlateformMerchanApply apply=getMerchantApplyById(Long.valueOf(node.getInteger("id")));
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
            if (dto.getApplyWays().equals(1))//如果是操作员申请，申请人填操作员姓名
                dto.setApplyerName(oi.getOperatorName());
            else//如果不是操作员申请，申请人填申请请求中的申请人姓名
                dto.setApplyerName(rApply.getApplyerName());
            dto.setApplyContent(rApply.getApplyContent());
            dto.setApplyMemo(rApply.getApplyMemo());
            dto.setApplyState(0);//初始状态为申请编辑
            dto.setCreateTime(new Date());
            Integer count = plateformMerchanApplyService.addModel(dto);
            if (count == null || count <= 0 || dto.getId() == null || dto.getId() <= 0)
                throw new BusiException("保存商户申请时出现异常");
            PlateformMerchanApply apply = getMerchantApplyById(dto.getId());
            if (dto.getConfirmType().equals(1))
                sendConfirmEmail(apply,envData);//如果是邮件确认，发送确认邮件消息

        } else {//如果是编辑操作

        }
        return ret;
    }
}
