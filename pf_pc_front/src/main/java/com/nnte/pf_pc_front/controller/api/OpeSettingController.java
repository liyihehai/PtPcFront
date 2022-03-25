package com.nnte.pf_pc_front.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.entertity.AppendWhere;
import com.nnte.pf_business.entertity.AppendWhereLike;
import com.nnte.pf_business.request.RequestOpe;
import com.nnte.pf_pc_front.PcPlateformApplication;
import com.nnte.pf_pc_front.config.AppModelConfig;
import com.nnte.pf_pc_front.config.SysRoleConfig;
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
@RequestMapping(value = "/api/operatorSetting")
public class OpeSettingController extends BaseController {
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    /**
     * 取得操作员设置页面列表
     * */
    @ModuleEnter(path = "/sysset/operatorSetList", name="操作员设置", desc = "平台系统操作员设置，系统管理员功能",
            sysRole = SysRoleConfig.SYS_MANAGER,roleRuler = "pf-operatorset",
            appCode = PcPlateformApplication.App_Code,moduleCode = AppModelConfig.MODULE_SYSSETTING)
    @RequestMapping(value = "/operatorSetList")
    @ResponseBody
    public Object operatorSetList(@Nullable @RequestBody JsonNode data){
        try {
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            RequestOpe queryOpe = JsonUtil.jsonToBean(data.toString(),RequestOpe.class);
            //----------------------------------------------
            Map<String,Object> paramMap=new HashedMap();
            paramMap.put("sort","create_time");
            paramMap.put("dir","desc");
            //----------------------------------------------
            List<AppendWhere> appendWhereList = new ArrayList<>();
            AppendWhere whereState=new AppendWhere(AppendWhere.Type_Direct);
            whereState.setWhereTxt("t.ope_state!=3");
            appendWhereList.add(whereState);
            if (StringUtils.isNotEmpty(queryOpe.getOpeCode()))
                appendWhereList.add(new AppendWhereLike("t.ope_code", queryOpe.getOpeCode()));
            if (appendWhereList.size() > 0)
                paramMap.put("appendWhereList", appendWhereList);
            //-----------------------------------------------
            PageData<RequestOpe> pageData = plateformOperatorComponent.operatorSetList(paramMap,pageNo,pageSize);
            return success("取得操作员设置页面列表成功!",pageData);
        } catch (Exception e) {
            return onException(e);
        }
    }
}
