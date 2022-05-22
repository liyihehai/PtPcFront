package com.nnte.pf_basic.proxy;

import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.JedisComponent;
import com.nnte.pf_basic.component.PFAppLicenseComponent;
import com.nnte.pf_basic.mapper.workdb.appLicense.PlateformAppLicense;

public class LicenseLockExtendProxy {
    private JedisComponent jedisComponent;
    private PFAppLicenseComponent pfAppLicenseComponent;
    private Object target; //obj为委托类对象;

    private ThreadLocal<String> threadLocal_updateLock = new ThreadLocal<>();

    public LicenseLockExtendProxy(Object obj,JedisComponent jedis,
                                  PFAppLicenseComponent pfAppLicense) {
        this.target = obj;
        this.jedisComponent = jedis;
        this.pfAppLicenseComponent = pfAppLicense;
    }

    /**
     * 许可更改锁增强调用
     * */
    public Object licenseUpdateLockExtendInvoke(String methodName,Object... args) throws Exception{
        if (args.length<=0 || !(args[0] instanceof PlateformAppLicense))
            throw new BusiException("必须有一个参数且第一个参数必须是PlateformAppLicense");
        String updateLock=threadLocal_updateLock.get();
        if (StringUtils.isEmpty(updateLock)) {
            updateLock = pfAppLicenseComponent.makeLicenseUpdateLock((PlateformAppLicense) args[0]);
            if (!jedisComponent.setRedisLock(updateLock, 5))
                throw new BusiException("获取License修改锁失败");
            threadLocal_updateLock.set(updateLock);
            try {
                return BeanUtils.executeTargetMethod(target,methodName,args);
            } catch (Exception e){
                throw new BusiException(e.getMessage());
            }
            finally {
                jedisComponent.releaseRedisLock(updateLock);
            }
        }else{
            return BeanUtils.executeTargetMethod(target,methodName,args);
        }
    }
}
