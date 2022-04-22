package com.nnte.pf_merchant.component.mqcomp;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.annotation.RootConfigProperties;
import com.nnte.basebusi.base.PulsarComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.communicate.plateform.SMContent;
import com.nnte.framework.utils.IpUtil;
import com.nnte.pf_basic.config.MqCommonConfig;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信发送MQ组件
 * */
@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
public class SMMQComponent extends PulsarComponent<SMContent> {
    public SMMQComponent(){
        setContentClazz(SMContent.class);
    }

    @Autowired
    private MqCommonConfig mqCommonConfig;

    @Override
    public void onConsumerMessageReceived(SMContent bodyObject) {
        outLogInfo("收到发送短信请求，phone："+bodyObject.getPhoneNo()+",content:"+bodyObject.getContent());
    }

    /**
     * 连接Rocketmq nameserver;
     * */
    public void initProducer() throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            createProducer(SMContent.TopicName);
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
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            String localIp= IpUtil.getLocalIp4Address().get().toString().replaceAll("/","");
            createCustmou(SMContent.TopicName,SMContent.TopicName+"-"+localIp,3,20);
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
}
