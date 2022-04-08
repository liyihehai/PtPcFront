package com.nnte.pf_pc_front.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.entity.ResponseResult;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.entertity.PFMenu;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api")
public class LoginController extends BaseController {

    @Autowired
    private PfBusinessComponent pfBusinessComponent;

    @GetMapping(value = "/currentUser")
    @ResponseBody
    public ResponseResult currentUser(@RequestBody JsonNode data){
        return error("取得操作员信息错误!");
    }
    /**
     * 用户预校验 POST /api/priCheck
     * */
    @PostMapping(value = "/priCheck")
    @ResponseBody
    public ResponseResult priCheck(@RequestBody JsonNode data){
        JsonUtil.JNode jnode = JsonUtil.createJNode(data);
        Map<String, Object> retMap = pfBusinessComponent.priCheckUser(jnode.getText("username"));
        ResponseResult ret = new ResponseResult();
        ret.setSuccess((boolean)retMap.get("suc"));
        ret.setErrorCode(retMap.get("code").toString());
        ret.setErrorMessage(retMap.get("msg").toString());
        ret.setData(retMap.get("tmpKey"));
        return ret;
    }
    /**
     * 登录接口 POST /api/login/account
     * */
    @PostMapping(value = "/login/account")
    @ResponseBody
    public ResponseResult account(HttpServletRequest request,@RequestBody JsonNode data){
        try {
            JsonUtil.JNode jnode = JsonUtil.createJNode(data);
            String userCode = jnode.getText("username");
            String password = jnode.getText("password");
            Map<String, Object> dataMap = new HashedMap();
            Map<String, Object> checkMap = pfBusinessComponent.checkUserPassword(userCode, password,
                    BaseController.getIpAddr(request));
            if (BaseNnte.getRetSuc(checkMap)) {
                OperatorInfo opeInfo = (OperatorInfo) checkMap.get("OperatorInfo");
                String token = StringUtils.defaultString(opeInfo.getToken());
                outLogInfo("用户登录成功[" + userCode + "]" + token);
                dataMap.put("OperatorInfo", opeInfo);
                List<PFMenu> menuFuncList = (List<PFMenu>) checkMap.get("menuFuncList");
                dataMap.put("MenuFunctions", pfBusinessComponent.loadMenuFuncNode(menuFuncList, false));
                dataMap.put("envData",request.getAttribute("envData"));
                return BaseController.success("登录成功!", dataMap);
            } else {
                return BaseController.error(StringUtils.defaultString(checkMap.get("msg")));
            }
        }catch (Exception e){
            return onException(e);
        }
    }
}
