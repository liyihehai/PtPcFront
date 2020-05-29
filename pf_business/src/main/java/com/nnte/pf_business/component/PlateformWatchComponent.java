package com.nnte.pf_business.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * 平台守护组件
 */
@Component
@BusiLogAttr("PlateformWatch")
public class PlateformWatchComponent extends BaseBusiComponent implements Runnable {
    @Getter
    private static int runState = 0;   //运行时状态：0未运行，1：运行时，2：停顿时
    @Setter
    private static int isContinue = 1;              //是否继续执行
    @Setter
    private static int sleepSeconds = 1000 * 60 * 3;//3分钟执行一次

    private static TreeMap<Integer, WatchRegisterItem> watchRegMap = new TreeMap<>();

    @AllArgsConstructor
    @Getter
    private class WatchRegisterItem {
        private WatchInterface itemInterface;
        private int itemIndex;
        private String itemName;
    }
    /**
     * 注册一个需要执行守护的组件
     * */
    public void registerWatchItem(WatchInterface inter, Integer index) throws BusiException {
        if (watchRegMap.get(index) != null)
            throw new BusiException(1, "项目组件序号已存在", BusiException.ExpLevel.ERROR);
        WatchRegisterItem item = new WatchRegisterItem(inter,index,inter.getClass().getName());
        watchRegMap.put(index, item);
    }

    /**
     * 启动守护线程
     * */
    public void startWatch(){
        if (runState==0)
            new Thread(this).start();
    }

    @Override
    public void run() {
        BaseNnte.outConsoleLog("---- 平台守护线程启动  ----");
        while (isContinue == 1) {
            runState = 1;
            watchProcess();
            runState = 2;
            if (isContinue == 1)
                ThreadUtil.Sleep(sleepSeconds);
            else
                break;
        }
        runState = 0;
        BaseNnte.outConsoleLog("---- 平台守护线程结束  ----");
    }

    /**
     * 平台守护组件的主函数
     */
    public void watchProcess() {
        try {
            if (watchRegMap!=null && watchRegMap.size()>0){
                Iterator<Integer> iterator =  watchRegMap.keySet().iterator();
                while(iterator.hasNext()) {
                    Integer key = iterator.next();
                    WatchRegisterItem item=watchRegMap.get(key);
                    if (item!=null && item.itemInterface!=null) {

                        item.itemInterface.runWatch();
                    }
                }
            }
        } catch (Exception e) {
            BusiException be=new BusiException(e,3999,BusiException.ExpLevel.ERROR);
            logException(be);
        }
    }
}
