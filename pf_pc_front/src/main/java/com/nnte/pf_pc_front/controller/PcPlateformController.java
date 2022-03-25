package com.nnte.pf_pc_front.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.FException;
import com.nnte.framework.utils.*;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/main")
public class PcPlateformController extends BaseController {

    @Autowired
    private PfBusinessComponent pfBusinessComponent;

    private static String IMAGE_UPLOAD_GROUP="webstatic";

    /**
     * 显示登陆页面
     * */
    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/login");
        return modelAndView;
    }
    /**
     * 在服务器端渲染菜单及功能树
     * */
    private String applyMenuFunc(List<PFMenu> menuFuncList,String contextPath){
        if (menuFuncList==null || menuFuncList.size()<=0)
            return null;
        StringBuffer menuBody = new StringBuffer();
        for(PFMenu menu:menuFuncList){
            menuBody.append("<li class=\"treeview\">")
                        .append("<a href=\"javascript:void(0);\">")
                            .append("<i class=\"fa fa-link\"></i>")
                            .append("<span>"+menu.getMenuName()+"</span>")
                            .append("<i class=\"fa fa-angle-left pull-right\"></i>")
                        .append("</a>");
            if (menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0) {
                menuBody.append("<ul class=\"treeview-menu\">");
                menuBody.append(applyMenuFunc(menu.getSubMenuList(),contextPath));
                menuBody.append("</ul>");
            }else if (menu.getFunctionList()!=null && menu.getFunctionList().size()>0){
                menuBody.append("<ul class=\"treeview-menu\">");
                for(PlateformFunctions func:menu.getFunctionList()){
                    menuBody.append("<li>")
                                .append("<a href=\"javascript:void(0);\" data-menuName=\""+func.getFunName()+"\" data-menukey=\""+func.getFunCode()+"\" data-link=\""+contextPath+ BaseComponent.getPathByRuler(func.getAuthCode())+"\" class=\"u_a\">")
                                .append("<i class=\"fa fa-c fa-circle-o\"></i>"+func.getFunName())
                                .append("</a>")
                            .append("</li>");
                }
                menuBody.append("</ul>");
            }
            menuBody.append("</li>");
        }
        return menuBody.toString();
    }
    /**
     * 用户预校验
     * */
    @RequestMapping(value = "/priCheck")
    @ResponseBody
    public Object priCheck(HttpServletRequest request,@RequestBody JsonNode jsonParam){
        JsonUtil.JNode jnode = JsonUtil.createJNode(jsonParam);
        return pfBusinessComponent.priCheckUser(jnode.getText("userName"));
    }
    /**
     * 用户密码校验
     * */
    @RequestMapping(value = "/loginCheck")
    public ModelAndView loginCheck(HttpServletRequest request,
                                   ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        String userCode= StringUtils.defaultString(getRequestParam(request,null,"userName"));
        String password= StringUtils.defaultString(getRequestParam(request,null,"aimPwd"));
        setParamMapDataEnv(request,map);
        Map<String,Object> checkMap=pfBusinessComponent.checkUserPassword(userCode,password,
                BaseController.getIpAddr(request));
        if (BaseNnte.getRetSuc(checkMap)) {
            OperatorInfo opeInfo = (OperatorInfo)checkMap.get("OperatorInfo");
            String token=StringUtils.defaultString(opeInfo.getToken());
            outLogInfo("用户登录成功["+userCode+"]"+token);
            map.put("OperatorInfo",opeInfo);
            Map<String,Object> envData=(Map)map.get("envData");
            List<PFMenu> menuFuncList=(List<PFMenu>)checkMap.get("menuFuncList");
            map.put("MenuFunctions",applyMenuFunc(menuFuncList,
                    StringUtils.defaultString(envData.get("contextPath"))));
            modelAndView.setViewName("front/index");
        }else {
            map.put("message",checkMap.get("msg"));
            modelAndView.setViewName("front/login");
        }
        modelAndView.addObject("map", map);
        return modelAndView;
    }

    @RequestMapping(value = "/sysRepairing")
    public ModelAndView sysRepairing(ModelAndView modelAndView){
        modelAndView.setViewName("front/sysRepairing");
        return modelAndView;
    }

    @RequestMapping(value = "/homeIndex")
    public ModelAndView homeIndex(ModelAndView modelAndView){
        modelAndView.setViewName("front/homeIndex");
        return modelAndView;
    }

    @RequestMapping(value = "/queryAddressGeocode")
    @ResponseBody
    public Map<String,Object> queryAddressGeocode(@RequestBody JsonNode json){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        JsonUtil.JNode node=JsonUtil.createJNode(json);
        if (node==null){
            BaseNnte.setRetFalse(ret,1002,"没有取得待查询的地址");
            return ret;
        }
        try {
            Double[] geocode=BaiduMapUtil.geocoder(node.getText("addr"));
            ret.put("geocode", NumberUtil.formatNumber(geocode[0],"0.###")+":"+
                    NumberUtil.formatNumber(geocode[1],"0.###"));
            ret.put("lat",geocode[0]);
            ret.put("lng",geocode[1]);
            BaseNnte.setRetTrue(ret,"取得地址经纬度成功");
        }catch (FException fe){
            BaseNnte.setRetFalse(ret,1002,fe.getMessage());
        }
        return ret;
    }

    private Map<String,Object> getUploadCropperContent(HttpServletRequest request){
        Map<String,Object> ret=BaseNnte.newMapRetObj();
        String cropper=request.getParameter("cropper");
        String imgtype=request.getParameter("imgtype");
        if (StringUtils.isEmpty(cropper)||StringUtils.isEmpty(imgtype)){
            BaseNnte.setRetFalse(ret,1002,"没有取得图片裁剪数据");
            return ret;
        }
        try {
            String title = "data:"+imgtype+";base64,";
            String imgStr = cropper.substring(title.length());
            byte[] bytes=Base64Utils.decode(imgStr);
            ret.put("bytes",bytes);
        } catch (Exception e) {
            BaseNnte.setRetFalse(ret,1002,"没有取得图片裁剪数据:"+e.getMessage());
            return ret;
        }
        ret.put("filename",imgtype.replaceAll("/","."));
        BaseNnte.setRetTrue(ret,  "取得上传的图片裁剪内容成功");
        return ret;
    }

    private Map<String,Object> getUploadFileContent(HttpServletRequest request){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile templateFile = multipartRequest.getFile("uploadfile");
        if (templateFile == null) {
            BaseNnte.setRetFalse(ret, 1003, "不能取得上传的文件内容");
            return ret;
        }
        String filename = templateFile.getOriginalFilename();
        ret.put("filename",filename);
        byte[] bytes;
        try {
            bytes = templateFile.getBytes();
            if (bytes == null || bytes.length <= 0) {
                BaseNnte.setRetFalse(ret, 1003, "不能取得上传的文件内容");
                return ret;
            }
            ret.put("bytes",bytes);
            BaseNnte.setRetTrue(ret,  "取得上传的文件内容成功");
        } catch (IOException e) {
            outLogExp(e);
            BaseNnte.setRetFalse(ret, 1003, "不能取得上传的文件内容");
            return ret;
        }
        return ret;
    }
    /**
     * 处理图片文件上传并保存，上传文件的方式可以是直接上传图片文件或通过裁剪图片方式上传
     * */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public Map<String, Object> uploadImage(HttpServletRequest request,String uploadtype,String appendJson){
        Map<String,Object> ret = BaseNnte.newMapRetObj();
        JsonUtil.JNode node=JsonUtil.createJNode(JsonUtil.jsonToNode(UrlEncodeUtil.UrlDecode(appendJson)));
        if (node==null){
            BaseNnte.setRetFalse(ret,1003,"不能取得上传的附加数据");
            return ret;
        }
        setParamMapDataEnv(request,ret);
        Map<String, Object> envData = (Map)ret.get("envData");
        String httpname=StringUtils.defaultString(envData.get("uploadStaticRoot"))+"/";
        String srcUrl=node.getText("srcUrl");
        String srcFile=null;
        if (!StringUtils.isEmpty(srcUrl) && srcUrl.indexOf(httpname)>=0){
            String t = srcUrl.replace(httpname,"");
            int findex = t.indexOf("/");
            if (findex>=0){
                srcFile = t.substring(findex,t.length());
            }
        }
        Map<String,Object> contentMap;
        if (NumberUtil.getDefaultInteger(uploadtype).equals(1)) {
            contentMap = getUploadFileContent(request);
        }else{//如果不是文件上传，按裁剪上传处理
            contentMap = getUploadCropperContent(request);
        }
        if (!BaseNnte.getRetSuc(contentMap))
            return contentMap;
        String filename=(String)contentMap.get("filename");
        byte[] bytes = (byte[])contentMap.get("bytes");
        Map<String,Object> uploadRet=pfBusinessComponent.uploadImageFile(IMAGE_UPLOAD_GROUP,filename,srcFile,bytes);
        String submitName=StringUtils.defaultString(uploadRet.get("submitName"));
        String file=submitName.replaceAll(":","/");
        ret.put("fileUrl",httpname+file);
        BaseNnte.setRetTrue(ret,"上传文件成功");
        return ret;
    }

    /**
     * 打开WEB端裁剪图片对话框界面
     * */
    @RequestMapping(value = "/cropper/cropperForWeb")
    public ModelAndView cropperForWeb(HttpServletRequest request,ModelAndView modelAndView){
        Map<String,Object> map=new HashMap<>();
        setParamMapDataEnv(request,map);
        modelAndView.addObject("map", map);
        modelAndView.setViewName("front/cropper/cropperForWeb");
        return modelAndView;
    }
}
