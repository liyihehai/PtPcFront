package com.nnte.pf_merchant.component.mqcomp;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseLog;
import com.nnte.basebusi.base.PulsarComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.communicate.plateform.EmailContent;
import com.nnte.framework.utils.IpUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.MailUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.PlateformSysParamComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.MqCommonConfig;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import lombok.Data;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * 邮件发送MQ组件
 * */
@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
public class EmailMQComponent extends PulsarComponent<EmailContent> {
    public EmailMQComponent(){
        setContentClazz(EmailContent.class);
    }

    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;
    @Autowired
    private MqCommonConfig mqCommonConfig;

    @Override
    public void onConsumerMessageReceived(EmailContent bodyObject) {
        outLogInfo("收到发送商户确认邮件请求，email："+bodyObject.getEmail());
        try {
            String smtpJson = StringUtils.defaultString(plateformSysParamComponent.getSysSmtpAccountJson());
            SysSmtpAccount ssa = JsonUtil.jsonToBean(smtpJson, SysSmtpAccount.class);
            if (ssa == null)
                throw new BusiException("邮件账户JSON数据定义不合法");
            JavaMailSenderImpl sender=MailUtils.setInitData(ssa.getSmtp_host(), ssa.getPort(), ssa.getUsername(), ssa.getPasswd(),
                    ssa.isTsl());
            MailUtils.richContentSend(sender,bodyObject.getEmail(),bodyObject.getTitle(),
                    bodyObject.getContent(),null);
            outLogInfo("发送商户确认邮件请求成功，email："+bodyObject.getEmail());
        }catch (Exception e){
            outLogWarn("发送商户确认邮件请求失败，email："+bodyObject.getEmail()+",err:"+e.getMessage());
            BaseLog.outLogExp(e);
        }
    }

    @Data
    public static class SysSmtpAccount{
        private String smtp_host;
        private Integer port;
        private String username;
        private String passwd;
        private boolean isTsl;
    }

    /**
     * 连接Rocketmq nameserver;
     * */
    public void initProducer() throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            createProducer(false, AppBasicConfig.App_Code,PFMerchantConfig.Module_Code,
                    EmailContent.TopicName, ProducerAccessMode.Shared);
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    /**
     * 发送短信内容到MQ
     * */
    public synchronized void send2MQ(EmailContent ec) throws BusiException{
        try {
            sendAsyncMessage(ec);
            outLogInfo("发送邮件到地址："+ec.getEmail());
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    public void initConsumer() throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            String localIp=IpUtil.getLocalIp4Address().get().toString().replaceAll("/","");
            createConsumer(false, AppBasicConfig.App_Code,PFMerchantConfig.Module_Code,
                    EmailContent.TopicName,localIp,"email",
                    SubscriptionType.Failover, 3,20);
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
}
