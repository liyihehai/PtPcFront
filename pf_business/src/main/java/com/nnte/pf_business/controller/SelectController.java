package com.nnte.pf_business.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.base.BaseController;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/sysset/select")
public class SelectController extends BaseController {

    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    /**
     * 查询当前所有可用的操作员列表(不分页)
     * */
    @RequestMapping(value = "/loadSelectOperator")
    @ResponseBody
    public Object loadSelectOperator(@Nullable @RequestBody JsonNode json) {
        try {
            return success("查询操作员用户角色成功", plateformOperatorComponent.loadSelectOperator());
        } catch (Exception e) {
            return onException(e);
        }
    }
}
