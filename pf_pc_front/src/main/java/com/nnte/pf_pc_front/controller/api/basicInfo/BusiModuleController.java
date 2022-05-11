package com.nnte.pf_pc_front.controller.api.basicInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.pf_basic.component.PFBusiModuleComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.AppBasicSysRole;
import com.nnte.pf_basic.mapper.workdb.busiModule.PlateformBusiModule;
import com.nnte.pf_pc_front.entity.RequestBusiModule;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务模块
 * */
@Controller
@RequestMapping(value = "/api/basicInfo/busiModule")
public class BusiModuleController extends BaseController {

    @Autowired
    private PFBusiModuleComponent pfBusiModuleComponent;
    /**
     * 分页查询业务模块列表
     * */
    @ModuleEnter(path = "/basicInfo/busimodule", name = "业务模块设置",
            desc = "平台业务系统模块设置，系统管理员功能",
            sysRole = AppBasicSysRole.SYS_MANAGER,
            roleRuler = "pf-busi-module", moduleCode = AppBasicConfig.Module_Code)
    @RequestMapping(value = "/getBusinessModuleList", method = RequestMethod.POST)
    @ResponseBody
    public Object getBusinessModuleList(@NonNull @RequestBody JsonNode data) {
        try {
            RequestBusiModule moduleItem = JsonUtil.jsonToBean(data.toString(), RequestBusiModule.class);
            Map<String, Object> paramMap = new HashedMap();
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            //----------------------------------------------
            if (moduleItem.getModuleStatus() != null) {
                paramMap.put("moduleStatus", moduleItem.getModuleStatus());
            }
            List<AppendWhere> appendWhereList = new ArrayList<>();
            AppendWhere.addLikeStringToWhereList(moduleItem.getModuleCode(),"t.module_code",appendWhereList);
            AppendWhere.addLikeStringToWhereList(moduleItem.getModuleName(),"t.module_name",appendWhereList);
            AppendWhere.addLikeStringToWhereList(moduleItem.getCurrentVersion(),"t.current_version",appendWhereList);
            AppendWhere.addLikeStringToWhereList(moduleItem.getModuleDesc(),"t.module_desc",appendWhereList);
            //----------------------------------------------
            if (appendWhereList.size() > 0)
                paramMap.put("appendWhereList", appendWhereList);
            //----------------------------------------------

            PageData<PlateformBusiModule> pageData = pfBusiModuleComponent.getBusinessModuleList(paramMap, pageNo,pageSize);
            return success("取得商户列表数据成功!", pageData);
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 保存业务模块定义
     * */
    @RequestMapping(value = "/saveBusinessModule", method = RequestMethod.POST)
    @ResponseBody
    public Object saveBusinessModule(HttpServletRequest request, @NonNull @RequestBody JsonNode data){
        try {
            RequestBusiModule moduleItem = JsonUtil.jsonToBean(data.toString(), RequestBusiModule.class);
            BaseComponent.checkModelFields(moduleItem);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            PlateformBusiModule module = new PlateformBusiModule();
            module.setId(moduleItem.getId());
            module.setModuleCode(moduleItem.getModuleCode());
            module.setModuleName(moduleItem.getModuleName());
            module.setModuleDesc(moduleItem.getModuleDesc());
            module.setCurrentVersion(moduleItem.getCurrentVersion());
            PlateformBusiModule retModule=pfBusiModuleComponent.saveBusinessModule(module,oi.getOperatorName());
            return success("保存业务模块定义成功",retModule);
        }catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 设置业务模块状态
     * */
    @RequestMapping(value = "/setBusinessModuleStatus", method = RequestMethod.POST)
    @ResponseBody
    public Object setBusinessModuleStatus(HttpServletRequest request, @NonNull @RequestBody JsonNode data){
        try {
            RequestBusiModule moduleItem = JsonUtil.jsonToBean(data.toString(), RequestBusiModule.class);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            pfBusiModuleComponent.setBusinessModuleStatus(moduleItem.getId(),moduleItem.getModuleStatus(),oi.getOperatorName());
            return success("设置业务模块状态成功");
        }catch (Exception e) {
            return onException(e);
        }
    }
}
