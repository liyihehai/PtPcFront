package com.nnte.pf_pc_front.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppRegistry;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.NumberDefUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.component.menus.PlateformFunctionComponent;
import com.nnte.pf_business.component.roles.PlateformRoleComponent;
import com.nnte.pf_business.entertity.AppendWhere;
import com.nnte.pf_business.entertity.AppendWhereLike;
import com.nnte.pf_pc_front.PcPlateformApplication;
import com.nnte.pf_pc_front.config.AppModelConfig;
import com.nnte.pf_pc_front.config.SysRoleConfig;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/roleSetting")
public class RoleSettingController extends BaseController {

    @Autowired
    private PlateformRoleComponent plateformRoleComponent;
    @Autowired
    private PlateformFunctionComponent plateformFunctionComponent;

    /**
     * 获取角色设置列表
     * */
    @ModuleEnter(path = "/sysset/roleSetList", name="角色设置页面", desc = "平台系统角色设置，系统管理员功能",
            sysRole = SysRoleConfig.SYS_MANAGER,roleRuler = "pf-roleset",
            appCode = PcPlateformApplication.App_Code,moduleCode = AppModelConfig.MODULE_SYSSETTING)
    @RequestMapping(value = "/roleSetList",method = {RequestMethod.POST})
    @ResponseBody
    public Object roleSetList(HttpServletRequest request,@Nullable @RequestBody JsonNode data){
        try {
            Map<String, Object> map = new HashedMap();
            setParamMapDataEnv(request, map);
            Integer pageNo = NumberDefUtil.getDefInteger(data.get("current"));
            Integer pageSize = NumberDefUtil.getDefInteger(data.get("pageSize"));
            List<AppendWhere> appendWhereList = new ArrayList<>();
            String roleCodeLike = StringUtils.defaultString((data.get("roleCode")!=null)?data.get("roleCode").textValue():"");
            if (StringUtils.isNotEmpty(roleCodeLike))
                appendWhereList.add(new AppendWhereLike("t.role_code",roleCodeLike));
            String roleNameLike = StringUtils.defaultString((data.get("roleName")!=null)?data.get("roleName").textValue():"");
            if (StringUtils.isNotEmpty(roleNameLike))
                appendWhereList.add(new AppendWhereLike("t.role_name",roleNameLike));
            if (appendWhereList.size()>0)
                map.put("appendWhereList",appendWhereList);
            Integer roleState = NumberDefUtil.defIntegerNull((data.get("roleState")!=null &&
                    !data.get("roleState").textValue().equals("null"))?data.get("roleState").textValue():null);
            if (roleState!=null)
                map.put("roleState",roleState);
            map.put("roleList", plateformRoleComponent.queryRoleListPage(map,pageNo,pageSize));
            List<KeyValue> sysRoleList = AppRegistry.getSysRoleNameList();
            map.put("sysRoleList", sysRoleList);
            return success("获取角色设置列表成功",map);
        }catch (Exception e){
            return onException(e);
        }
    }
}
