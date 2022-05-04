package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseLog;
import com.nnte.basebusi.base.PulsarComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.communicate.plateform.CruxOpeContent;
import com.nnte.framework.utils.IpUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.MqCommonConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class CruxOpeMQComponent extends PulsarComponent<CruxOpeContent> {
    public CruxOpeMQComponent(){
        setContentClazz(CruxOpeContent.class);
    }
    /**
     * 关键操作日志
     * */
    private static final Logger CruxOperateLogger = LogManager.getLogger("CruxOperateLogger");

    @Autowired
    private MqCommonConfig mqCommonConfig;

    public void initProducer() throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            createProducer(true,AppBasicConfig.App_Code,AppBasicConfig.Module_Code,
                    CruxOpeContent.TopicName, ProducerAccessMode.Shared);
            outLogInfo("CruxOpeMQComponent Producer=CruxOpeMQGroup,...success");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    /**
     * 发送短信内容到MQ
     * */
    public synchronized void send2MQ(CruxOpeContent content) throws BusiException{
        try {
            sendAsyncMessage(content);
            outLogInfo("发送关键变更["+content.getTypeName()+"]到日志");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
    /**
     * 发送操作到日志
     * */
    public void sendCruxOperate(String operator,String no,String name,Object srcObject,Object newObject) throws BusiException{
        CruxOpeContent content = new CruxOpeContent();
        content.setTypeNO(no);
        content.setTypeName(name);
        content.setOperatorName(operator);
        if (srcObject!=null) {
            if (srcObject instanceof String)
                content.setSrcJson((String)srcObject);
            else
                content.setSrcJson(JsonUtil.beanToJson(srcObject));
        }
        if (newObject!=null) {
            if (newObject instanceof String)
                content.setNewJson((String)newObject);
            else
                content.setNewJson(JsonUtil.beanToJson(newObject));
        }
        content.setOpeDate(new Date());
        send2MQ(content);
    }

    public void initConsumer() throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            String localIp= IpUtil.getLocalIp4Address().get().toString().replaceAll("/","");

            createCustmou(true,AppBasicConfig.App_Code,AppBasicConfig.Module_Code,
                    CruxOpeContent.TopicName,localIp,"crux",
                    SubscriptionType.Shared ,3,20);
            outLogInfo("CruxOpeMQComponent Consumer=crux,...success");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    private synchronized void outLogger(CruxOpeContent receiveContent){
        CruxOperateLogger.warn("TypeNO:"+ StringUtils.defaultString(receiveContent.getTypeNO()));
        CruxOperateLogger.warn("TypeName:"+StringUtils.defaultString(receiveContent.getTypeName()));
        CruxOperateLogger.warn("Operator:"+StringUtils.defaultString(receiveContent.getOperatorName()));
        CruxOperateLogger.warn("SrcObject:"+StringUtils.defaultString(receiveContent.getSrcJson()));
        CruxOperateLogger.warn("newObject:"+StringUtils.defaultString(receiveContent.getNewJson()));
    }
    @Override
    public void onConsumerMessageReceived(CruxOpeContent receiveContent) {
        outLogDebug("收到关键变更["+receiveContent.getTypeName()+"]");
        try {
            if (CruxOperateLogger!=null) {
                outLogger(receiveContent);
            }else
                throw new BusiException("没有定义正确的关键日志输出");
        }catch (Exception e){
            outLogWarn("记录关键变更["+receiveContent.getTypeName()+"]异常:"+e.getMessage());
            BaseLog.outLogExp(e);
        }
    }
}
