package com.nnte.pf_merchant.component.mqcomp;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseLog;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.RocketmqMsgListener;
import com.nnte.framework.base.RocketMqComponent;
import com.nnte.framework.entity.FException;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.MailUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.PlateformSysParamComponent;
import com.nnte.pf_merchant.entertity.EmailContent;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * 邮件发送MQ组件
 * */
@Component
@ConfigurationProperties(prefix = "emailmq.merchant.apply")
@PropertySource(value = "classpath:emailmq.properties")
@BusiLogAttr("MqComponent")
public class EmailMQComponent extends BaseComponent implements RocketmqMsgListener<EmailContent> {
    public static RocketMqComponent.RocketMQProducer producer  = null;

    @Getter @Setter
    private String emailApplyGroup;
    @Getter @Setter
    private String emailApplyNamesrvAddr;
    @Getter @Setter
    private String emailSendApplyInstanceName;
    @Getter @Setter
    private String emailListenApplyInstanceName;

    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;

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
            producer = RocketMqComponent.instancProducer(getEmailApplyGroup(),
                    getEmailApplyNamesrvAddr(), EmailContent.class,getEmailSendApplyInstanceName());
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    /**
     * 发送短信内容到MQ
     * */
    public synchronized void send2MQ(EmailContent ec) throws BusiException{
        try {
            if (producer!=null) {
                producer.producerSendMessage("",ec);
                outLogInfo("发送邮件到地址："+ec.getEmail());
            }
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    /**
     * MQ消息接收部分
     * */
    private static RocketMqComponent.RocketMQConsumer consumer = null;

    @Override
    public Class getBodyClass() {
        return EmailContent.class;
    }

    @Override
    public void onConsumeMessage(String msgId, String keys, EmailContent bodyObject) {
        outLogInfo("收到发送商户确认邮件请求，email："+bodyObject.getEmail());
        try {
            String smtpJson = StringUtils.defaultString(plateformSysParamComponent.getSingleParamVText(PlateformSysParamComponent.SYS_SMTP_ACCOUNT_JSON));
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

    public void initConsumer() throws BusiException {
        try {
            consumer = RocketMqComponent.instancConsumer(getEmailApplyGroup(),
                    getEmailApplyNamesrvAddr(),getEmailListenApplyInstanceName(),
                    EmailContent.class,null,this);
        } catch (FException e) {
            throw new BusiException(e);
        }
    }
}
