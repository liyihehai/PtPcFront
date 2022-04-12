package com.nnte.pf_merchant.component.mqcomp;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.RootConfigProperties;
import com.nnte.basebusi.base.PulsarComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.IpUtil;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.entertity.SMContent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 短信发送MQ组件
 * */
@Component
@RootConfigProperties(fileName = "smmq.properties",prefix = "smmq")
@BusiLogAttr(PFMerchantConfig.loggerName)
public class SMMQComponent extends PulsarComponent<SMContent> {
    public SMMQComponent(){
        setContentClazz(SMContent.class);
    }

    @Override
    public void onConsumerMessageReceived(SMContent bodyObject) {
        outLogInfo("收到发送短信请求，phone："+bodyObject.getPhoneNo()+",content:"+bodyObject.getContent());
    }

    @Getter @Setter
    private String smMqGroup;
    @Getter @Setter
    private String smMqNamesrvAddr;
    @Getter @Setter
    private String smSendMqInstanceName;
    @Getter @Setter
    private String smListenMqInstanceName;
    /**
     * 连接Rocketmq nameserver;
     * */
    public void initProducer() throws BusiException {
        try {
            String ipport = getSmMqNamesrvAddr();
            String[] s=ipport.split(":");
            initPulsarClient(s[0],s[1]);
            createProducer(getSmMqGroup());
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
    /**
     * 发送短信内容到MQ
     * */
    public synchronized void send2MQ(SMContent sm) throws BusiException{
        try {
            sendAsyncMessage(sm);
            outLogInfo("发送短信到号码："+sm.getPhoneNo());
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    public void initConsumer() throws BusiException {
        try {
            String ipport = getSmMqNamesrvAddr();
            String[] s=ipport.split(":");
            initPulsarClient(s[0],s[1]);
            String localIp= IpUtil.getLocalIp4Address().get().toString().replaceAll("/","");
            createCustmou(getSmMqGroup(),getSmMqGroup()+"-"+localIp,3,20);
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
}
