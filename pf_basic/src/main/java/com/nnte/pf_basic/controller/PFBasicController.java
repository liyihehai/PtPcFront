package com.nnte.pf_basic.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.utils.BaiduMapUtil;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberUtil;
import com.nnte.pf_basic.component.PFBasicComponent;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/pfbasic")
public class PFBasicController extends BaseController {
    private final static String IMAGE_UPLOAD_GROUP = "webstatic";

    @Autowired
    private PFBasicComponent pfBasicComponent;

    /**
     * 处理图片文件上传并保存，上传文件的方式可以是直接上传图片文件或通过裁剪图片方式上传
     */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public Object uploadImage(MultipartHttpServletRequest request) {
        try {
            String url = pfBasicComponent.uploadImageFile(request, IMAGE_UPLOAD_GROUP);
            return success("上传文件成功", url);
        } catch (Exception e) {
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
