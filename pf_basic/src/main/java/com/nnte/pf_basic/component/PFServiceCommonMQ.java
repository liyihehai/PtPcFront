package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseLog;
import com.nnte.basebusi.base.PulsarComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.communicate.plateform.CommonContent;
import com.nnte.framework.base.SpringContextHolder;
import com.nnte.framework.utils.DateUtils;
import com.nnte.framework.utils.IpUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.annotation.PFServiceMQAnnotation;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.MqCommonConfig;
import com.nnte.pf_basic.entertity.MethodObject;
import com.nnte.pf_basic.entertity.TmpUploadFile;
import org.apache.commons.collections.map.HashedMap;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFServiceCommonMQ extends PulsarComponent<CommonContent> {
    public PFServiceCommonMQ(){
        setContentClazz(CommonContent.class);
    }
    public interface PFServiceMQInterface{
        default void onMessage(CommonContent content) throws Exception{
            Logger logger = LoggerFactory.getLogger(AppBasicConfig.JarLoggerName);
            logger.warn("CommonContent未处理:"+JsonUtil.beanToJson(content));
        }
    }

    public enum MsgType{
        ReloadUploadPath("ReloadUploadPath"),       //通知服务器重新加载上传文件地址配置
        RecordTmpUploadFile("RecordTmpUploadFile"), //记录一个临时上传文件
        RemoveTmpFileRecord("RemoveTmpFileRecord"); //删除临时文件记录，该文件转为正式文件

        private String value;
        MsgType(String val){
            value = val;
        }
        public String getValue(){return value;}
    }

    private final Map<String, MethodObject> processInterfaceMap = new HashedMap();
    public void registerProcess(MsgType msgType,MethodObject process){
        if (msgType==null || process==null)
            return;
        processInterfaceMap.put(msgType.getValue(),process);
    }
    /**
     * 扫描注册平台服务消息处理接口
     * */
    public void scanRegisterProcess(){
        ApplicationContext sch = SpringContextHolder.getApplicationContext();
        String[] names = sch.getBeanDefinitionNames();
        for (String beanName : names) {
            Object instanceBody = sch.getBean(beanName);
            if (instanceBody instanceof PFServiceMQInterface) {
                Method[] methods=instanceBody.getClass().getMethods();
                if (methods!=null && methods.length>0){
                    for(Method method:methods){
                        PFServiceMQAnnotation annotation = method.getAnnotation(PFServiceMQAnnotation.class);
                        if (annotation!=null){
                            registerProcess(annotation.value(),new MethodObject(method,instanceBody));
                        }
                    }
                }
            }
        }
    }

    @Autowired
    private MqCommonConfig mqCommonConfig;
    public void initProducer(boolean isPersistent, ProducerAccessMode accessMode) throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            createProducer(isPersistent,AppBasicConfig.App_Code,AppBasicConfig.Module_Code,CommonContent.TopicName,accessMode);
            outLogInfo("PFServiceCommonMQ Producer=CommonContent,...success");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }
    /**
     * 发送消息内容到MQ
     * */
    public synchronized void send2MQ(CommonContent content) throws BusiException{
        try {
            sendAsyncMessage(content);
            outLogDebug("发送PFServiceCommonMQ消息["+content.getMsgType()+":"+content.getMsgId()+"]");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    public void initConsumer(boolean isPersistent, SubscriptionType subscriptionType,int threadCount, int blockSize) throws BusiException {
        try {
            initPulsarClient(mqCommonConfig.getIp(),mqCommonConfig.getPort());
            String localIp= IpUtil.getLocalIp4Address().get().toString().replaceAll("/","");
            createConsumer(isPersistent,AppBasicConfig.App_Code,AppBasicConfig.Module_Code,
                    CommonContent.TopicName,localIp,"common",
                    subscriptionType,threadCount,blockSize);
            outLogDebug("PFServiceCommonMQ Consumer=CommonContent,...success");
            scanRegisterProcess();
            outLogDebug("PFServiceCommonMQ scanRegisterProcess,...success");
        } catch (Exception e) {
            throw new BusiException(e);
        }
    }

    @Override
    public void onConsumerMessageReceived(CommonContent receiveContent) {
        outLogDebug("收到PFServiceCommonMQ消息["+receiveContent.getMsgType()+":"+receiveContent.getMsgId()+"]");
        try {
            MethodObject methodObject  = processInterfaceMap.get(receiveContent.getMsgType());
            if (methodObject!=null){
                Method method=methodObject.getMethod();
                method.invoke(methodObject.getObject(),receiveContent);
            }else{
                Object component = methodObject.getObject();
                if (component instanceof PFServiceMQInterface){
                    ((PFServiceMQInterface) component).onMessage(receiveContent);
                }else
                    throw new BusiException("没有注册消息类型为"+receiveContent.getMsgType()+"的处理过程");
            }
        }catch (Exception e){
            outLogWarn("处理PFServiceCommonMQ消息["+receiveContent.getMsgType()+":"+receiveContent.getMsgId()+"]发生异常:"+e.getMessage());
            BaseLog.outLogExp(e);
        }
    }

    /**
     * 通知服务端刷新上传文件的路径配置
     * */
    public void notifyReloadUploadPath() throws BusiException{
        CommonContent content = new CommonContent();
        content.setMsgType(MsgType.ReloadUploadPath.getValue());
        content.setMsgId(StringUtils.GenerateToken());
        send2MQ(content);
    }
    /**
     * 通知服务器有一个临时文件需要记录，到时间删除
     * */
    public void RecordTmpUploadFile(String filePath,int hours) throws BusiException{
        CommonContent content = new CommonContent();
        content.setMsgType(MsgType.RecordTmpUploadFile.getValue());
        Date now = new Date();
        Date expDate=DateUtils.addNHours(now,hours);
        content.setMsgBody(JsonUtil.beanToJson(new TmpUploadFile(filePath,expDate)));
        content.setMsgId(StringUtils.GenerateToken());
        send2MQ(content);
    }
    /**
     * 通知服务器有一个临时文件需要转为正式文件
     * */
    public void RemoveTmpFileRecord(String filePath) throws BusiException{
        CommonContent content = new CommonContent();
        content.setMsgType(MsgType.RemoveTmpFileRecord.getValue());
        content.setMsgBody(filePath);
        content.setMsgId(StringUtils.GenerateToken());
        send2MQ(content);
    }
}
