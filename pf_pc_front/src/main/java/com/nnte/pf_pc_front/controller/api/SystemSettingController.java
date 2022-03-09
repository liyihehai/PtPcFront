package com.nnte.pf_pc_front.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.pf_business.component.PfBusinessComponent;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.PFMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/systemSetting")
public class SystemSettingController extends BaseController {

    @Autowired
    private PfBusinessComponent pfBusinessComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;

    public SystemSettingController(){
        this.setLoggerName("pf_business");
    }
    @RequestMapping(value = "/allMenuTree",method = {RequestMethod.GET})
    @ResponseBody
    public Object allMenuTree(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try {
            Map<String, Object> map = new HashMap<>();
            setParamMapDataEnv(request, map);
            //-------默认超级管理员,取得所有功能----
            OperatorInfo opeInfo = (OperatorInfo) request.getAttribute("OperatorInfo");
            plateformOperatorComponent.getOpeByCodeValid(opeInfo.getOperatorCode(),true);
            //-------------------------------------
            List<PFMenu> pFMenuList = pfBusinessComponent.loadOperatorMenuFunctions(opeInfo, null);//显示所有状态的菜单
            return BaseController.success("success", pfBusinessComponent.loadMenuFuncNode(pFMenuList,true));
        }catch (Exception e){
            return onException(e);
        }
    }
}
