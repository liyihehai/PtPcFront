package com.nnte.pf_pc_front;

import com.fasterxml.jackson.databind.JsonNode;
import com.nnte.basebusi.annotation.ModuleEnter;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.base.BaseController;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.entity.AppendWhereLike;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.DataLibraryComponent;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.AppBasicSysRole;
import com.nnte.pf_basic.config.DataLibraryConfig;
import com.nnte.pf_basic.request.RequestLibraryItem;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
 * 数据字典
 * */
@Controller
@RequestMapping(value = "/api/basicInfo/library")
public class LibraryManageController extends BaseController {

    @Autowired
    private DataLibraryComponent dataLibraryComponent;

    /**
     * 获取数据字典分类列表
     * */
    @RequestMapping(value = "/getLibraryTypeList", method = RequestMethod.POST)
    @ResponseBody
    public Object getLibraryTypeList(@Nullable @RequestBody JsonNode data) {
        try{
            return success("获取数据字典分类列表成功",dataLibraryComponent.getLibraryTypeList());
        }catch (Exception e){
            return onException(e);
        }
    }
    /**
     * 分页取得数据字典子项列表
     * */
    @ModuleEnter(path = "/basicInfo/library", name = "数据字典设置",
            desc = "平台数据字典设置，系统管理员功能",
            sysRole = AppBasicSysRole.SYS_MANAGER,
            roleRuler = "pf-basic-info", moduleCode = AppBasicConfig.SYSTEM_BASIC_INFO)
    @RequestMapping(value = "/getLibraryItemList", method = RequestMethod.POST)
    @ResponseBody
    public Object getLibraryItemList(@NonNull @RequestBody JsonNode data) {
        try {
            JsonUtil.JNode jNode = new JsonUtil.JNode(data);
            Map<String,Object> retMap = new HashedMap();
            retMap.put("libTypeList",dataLibraryComponent.getLibraryTypeList());
            String libTypeCode = jNode.getText("libTypeCode");
            Integer pageNo = jNode.getInteger("current");
            Integer pageSize = jNode.getInteger("pageSize");
            Map<String,Object> appendMap = new HashedMap();
            //-------------------------------------------------
            appendMap.put("sort", "item_sort");
            appendMap.put("dir", "asc");
            Integer itemSort = jNode.getInteger("itemSort");
            if (itemSort != null) {
                appendMap.put("itemSort", itemSort);
            }
            Integer canModify = jNode.getInteger("canModify");
            if (canModify != null) {
                appendMap.put("canModify", canModify);
            }
            //-------------------------------------------------
            List<AppendWhere> appendWhereList = new ArrayList<>();
            String typeItemCode = jNode.getText("typeItemCode");
            if (StringUtils.isNotEmpty(typeItemCode)) {
                appendWhereList.add(new AppendWhereLike("t.type_item_code", typeItemCode));
            }
            String typeItemName = jNode.getText("typeItemName");
            if (StringUtils.isNotEmpty(typeItemName)) {
                appendWhereList.add(new AppendWhereLike("t.type_item_name", typeItemName));
            }
            if (appendWhereList.size() > 0)
                appendMap.put("appendWhereList", appendWhereList);
            //-------------------------------------------------
            retMap.put("libItemList",dataLibraryComponent.getTypeItemListWithPage(libTypeCode,
                    //DataLibraryConfig.Lib_ModelName,
                    null,
                    appendMap,pageNo, pageSize));
            return success("取得数据字典列表数据成功",retMap);
        }catch (Exception e){
            return onException(e);
        }
    }

    /**
     * 保存数据字典分类
     * */
    @RequestMapping(value = "/saveLibraryItem", method = RequestMethod.POST)
    @ResponseBody
    public Object saveLibraryItem(HttpServletRequest request,@NonNull @RequestBody JsonNode data) {
        try {
            RequestLibraryItem libraryItem = JsonUtil.jsonToBean(data.toString(), RequestLibraryItem.class);
            BaseComponent.checkModelFields(libraryItem);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            dataLibraryComponent.saveLibraryItem(libraryItem.getId(),libraryItem.getLibTypeCode(),
                    libraryItem.getTypeItemCode(), libraryItem.getTypeItemName(),
                    libraryItem.getItemSort(),libraryItem.getRemark(),oi.getOperatorName(),
                    AppBasicConfig.App_Code,DataLibraryConfig.Lib_ModelName);
            return success("保存数据字典分类成功");
        }catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 删除数据字典分项
     * */
    @RequestMapping(value = "/delLibraryItem", method = RequestMethod.POST)
    @ResponseBody
    public Object delLibraryItem(HttpServletRequest request,@NonNull @RequestBody JsonNode data) {
        try {
            JsonUtil.JNode jNode = new JsonUtil.JNode(data);
            OperatorInfo oi = (OperatorInfo) request.getAttribute("OperatorInfo");
            Integer id = jNode.getInteger("id");
            dataLibraryComponent.delLibraryItem(id,oi.getOperatorName());
            return success("保存数据字典分类成功");
        }catch (Exception e) {
            return onException(e);
        }
    }

    /**
     * 分类代码取得有效的分项集合
     * */
    @RequestMapping(value = "/getValidLibItems", method = RequestMethod.POST)
    @ResponseBody
    public Object getValidLibItems(@NonNull @RequestBody JsonNode data) {
        try {
            JsonUtil.JNode jNode = new JsonUtil.JNode(data);
            String libTypeCode = jNode.getText("libTypeCode");
            return success("取得有效的分项集合",dataLibraryComponent.getValidLibItems(libTypeCode,
                    null,null));
        }catch (Exception e) {
            return onException(e);
        }
    }
}
