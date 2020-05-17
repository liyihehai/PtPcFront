package com.nnte.pf_business.component;

import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.RocketMqComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import org.springframework.stereotype.Component;

@Component
public class RocketmqProducerComponent {
    public static RocketMqComponent.RocketMQProducer producer  = null;
    /**
     * 连接Rocketmq nameserver;
     * */
    public void initProducer() throws BusiException{
        try {
            producer = RocketMqComponent.instancProducer("producer-group",
                    "139.196.177.32:9876", OperatorInfo.class,"producer");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    public void startTestProducer() throws BusiException{
        try {
            if (producer!=null) {
                for (int i = 0; i < 10; i++) {
                    OperatorInfo info = new OperatorInfo();
                    info.setOperatorCode("opeid="+i);
                    producer.producerSendMessage("",info);
                    System.out.println("send ope="+info.getOperatorCode());
                    Thread.sleep(100); // 每秒发送一次MQ
                }
            }
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
}
