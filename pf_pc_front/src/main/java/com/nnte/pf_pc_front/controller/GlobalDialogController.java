package com.nnte.pf_pc_front.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.FreeMarkertUtil;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.request.RequestOpe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "/global/dialog")
public class GlobalDialogController extends BaseController {
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;

    /**
     * 显示操作员选择对话框
     */
    @RequestMapping(value = "/operatorSelect")
    @ResponseBody
    public Map<String, Object> operatorSelect(HttpServletRequest request, @RequestBody JsonNode json) {
        Map<String, Object> ret = BaseNnte.newMapRetObj();
        Map<String, Object> map = new HashMap<>();
        setParamMapDataEnv(request, map);
        RequestOpe reqOpe = new RequestOpe();
        reqOpe.setOpeState(1);//查询所有状态为可用的操作员
        List<PlateformOperator> list = plateformOperatorComponent.queryOperatorList(reqOpe);
        if (list == null || list.size() <= 0) {
            BaseNnte.setRetFalse(ret, 1002, "没有取得操作员信息");
            return ret;
        }
        map.put("operatorList", list);
        String htmlBody = FreeMarkertUtil.getFreemarkerFtl(request, request.getServletContext(),
                FreeMarkertUtil.pathType.cls, map, "/templates/front/global/operatorSelect.ftl");
        ret.put("htmlBody", htmlBody);
        BaseNnte.setRetTrue(ret, "查询操作员成功");
        return ret;
    }
}
