package com.nnte.pf_business.component;

import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.annotation.RocketmqMsgListener;
import com.nnte.framework.base.RocketMqComponent;
import com.nnte.framework.entity.FException;
import com.nnte.pf_business.entertity.OperatorInfo;
import org.springframework.stereotype.Component;

@Component
public class RocketmqConsumerComponent implements RocketmqMsgListener<OperatorInfo>{
    @Override
    public Class getBodyClass() {
        return OperatorInfo.class;
    }

    @Override
    public void onConsumeMessage(String msgId, String keys, OperatorInfo bodyObject) {
        if (bodyObject!=null){
            System.out.println("recv msgId="+msgId+" keys"+keys+" "+bodyObject.getOperatorCode());
        }
    }

    private static RocketMqComponent.RocketMQConsumer consumer = null;
    public void initConsumer() throws BusiException {
        try {
            consumer = RocketMqComponent.instancConsumer("consumer-group","139.196.177.32:9876",
                    "consumer",OperatorInfo.class,null,this);
        } catch (FException e) {
            throw new BusiException(e);
        }
    }
}
