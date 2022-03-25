package com.nnte.pf_business.component.mqcomp;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.RocketmqMsgListener;
import com.nnte.framework.base.RocketMqComponent;
import com.nnte.framework.entity.FException;
import com.nnte.pf_business.entertity.SMContent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 短信发送MQ组件
 * */
@Component
@ConfigurationProperties(prefix = "smmq")
@PropertySource(value = "classpath:smmq.properties")
@BusiLogAttr("MqComponent")
public class SMMQComponent extends BaseComponent implements RocketmqMsgListener<SMContent> {
    public static RocketMqComponent.RocketMQProducer producer  = null;

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
            producer = RocketMqComponent.instancProducer(getSmMqGroup(),
                    getSmMqNamesrvAddr(), SMContent.class,getSmSendMqInstanceName());
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
    /**
     * 发送短信内容到MQ
     * */
    public synchronized void send2MQ(SMContent sm) throws BusiException{
        try {
            if (producer!=null) {
                producer.producerSendMessage(sm.getSmTag(),sm);
                outLogInfo("发送短信到号码："+sm.getPhoneNo());
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
        return SMContent.class;
    }

    @Override
    public void onConsumeMessage(String msgId, String keys, SMContent bodyObject) {
        outLogInfo("收到发送短信请求，phone："+bodyObject.getPhoneNo()+",content:"+bodyObject.getContent());
    }

    public void initConsumer() throws BusiException {
        try {
            consumer = RocketMqComponent.instancConsumer(getSmMqGroup(),
                    getSmMqNamesrvAddr(),getSmListenMqInstanceName(),
                    SMContent.class,null,this);
        } catch (FException e) {
            throw new BusiException(e);
        }
    }
}
