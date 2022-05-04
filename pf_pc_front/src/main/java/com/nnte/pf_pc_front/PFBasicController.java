package com.nnte.pf_pc_front;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.component.PFBasicComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.AppBasicSysRole;
import com.nnte.pf_basic.entertity.AppFuncFactory;
import com.nnte.pf_pc_front.entity.ResponsUploadPathItem;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/pfbasic")
public class PFBasicController extends BaseController {
    private final static String IMAGE_UPLOAD_GROUP = "webstatic";

    @Autowired
    private PFBasicComponent pfBasicComponent;

    private ResponsUploadPathItem warpUploadPathItem(AppFuncFactory factory){
        ResponsUploadPathItem item = new ResponsUploadPathItem();
        item.setAppCode(factory.getAppCode());
        item.setAppName(factory.getAppName());
        factory.getFuncPathMap().forEach((funcCode,path)->{
            item.getFuncPathMap().add(path);
        });
        return item;
    }

    @ModuleEnter(path = "/basicInfo/sysparam/uploadPathParam", name = "上传文件路径设置",
            desc = "设置文件上传在服务器中的路径，系统管理员功能",
            sysRole = AppBasicSysRole.SYS_MANAGER,
            roleRuler = "pf-sysParam-uploadPath", moduleCode = AppBasicConfig.Module_Code)
    /**
     * 返回文件上传路径的定义
     * */
    @RequestMapping("/sysparam/uploadPathParam")
    @ResponseBody
    public Object uploadPathParam(@Nullable @RequestBody JsonNode json) {
        try {
            Map<String, AppFuncFactory> retMap = pfBasicComponent.uploadPathParam();
            List<ResponsUploadPathItem> retList = new ArrayList<>();
            retMap.forEach((appCode,factory)->{
                ResponsUploadPathItem item = warpUploadPathItem(factory);
                retList.add(item);
            });
            return success("返回文件上传路径的定义成功", retList);
        } catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 保存应用的上传文件路径定义
     * */
    @RequestMapping("/sysparam/saveUploadPathFactory")
    @ResponseBody
    public Object saveUploadPathFactory(HttpServletRequest request, @RequestBody ResponsUploadPathItem json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            pfBasicComponent.saveUploadPathFactory(json.getAppCode(),json.getFuncPathMap(),
                    oi.getOperatorCode(),oi.getOperatorName());
            return success("保存应用的上传文件路径定义成功");
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 删除上传文件路径设置
     * */
    @RequestMapping("/sysparam/deleteUploadPathItem")
    @ResponseBody
    public Object deleteUploadPathItem(HttpServletRequest request, @RequestBody JsonNode json) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            JsonUtil.JNode jNode=JsonUtil.createJNode(json);
            String appCode = jNode.getText("appCode");
            String funcCode = jNode.getText("funcCode");
            AppFuncFactory factory=pfBasicComponent.deleteUploadPathItem(appCode,funcCode,
                    oi.getOperatorCode(),oi.getOperatorName());
            return success("删除上传文件路径设置成功",warpUploadPathItem(factory));
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 通知服务器重载文件上传地址参数
     * */
    @RequestMapping("/sysparam/notifyReloadUploadPath")
    @ResponseBody
    public Object notifyReloadUploadPath(HttpServletRequest request) {
        try {
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            pfBasicComponent.notifyReloadUploadPath(oi.getOperatorCode(),oi.getOperatorName());
            return success("通知服务器重载文件上传地址参数成功");
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 查询地址的经纬度
     */
    @RequestMapping(value = "/queryAddressGeocode")
    @ResponseBody
    public Object queryAddressGeocode(@RequestBody JsonNode json) {
        try {
            JsonUtil.JNode node = JsonUtil.createJNode(json);
            if (node == null) {
                throw new BusiException(1002, "没有取得待查询的地址");
            }
            Map<String, Object> ret = new HashedMap();
            Double[] geocode = BaiduMapUtil.geocoder(node.getText("addr"));
            ret.put("geocode", NumberUtil.formatNumber(geocode[0], "0.###") + ":" +
                    NumberUtil.formatNumber(geocode[1], "0.###"));
            ret.put("lat", geocode[0]);
            ret.put("lng", geocode[1]);
            return success("取得地址经纬度成功",ret);
        } catch (Exception e) {
            return onException(e);
        }
    }
}
