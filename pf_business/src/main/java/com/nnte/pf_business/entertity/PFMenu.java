package com.nnte.pf_business.entertity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.framework.utils.DateUtils;
import com.nnte.framework.utils.JsonUtil;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PFMenu extends PlateformMenus {
    private List<PFMenu> subMenuList;  //加载子菜单
    private List<PlateformFunctions> functionList; //加载功能
    public PFMenu(PlateformMenus pfm){
        setId(pfm.getId());
        setMenuCode(pfm.getMenuCode());
        setMenuName(pfm.getMenuName());
        setMenuClass(pfm.getMenuClass());
        setParentMenuCode(pfm.getParentMenuCode());
        setMenuState(pfm.getMenuState());
        setCreateTime(pfm.getCreateTime());
        setMenuPath(pfm.getMenuPath());
        setMenuIcon(pfm.getMenuIcon());
        subMenuList = new ArrayList<>();
    }
    public ObjectNode toObjectNode(){
        ObjectNode node = JsonUtil.newJsonNode();
        node.put("id",getId());
        node.put("menuCode",getMenuCode());
        node.put("menuName",getMenuName());
        node.put("menuClass",getMenuClass());
        node.put("parentMenuCode",getParentMenuCode());
        node.put("menuState",getMenuState());
        node.put("createTime", DateUtils.dateToString(getCreateTime(),DateUtils.DF_YMDHMS));
        node.put("menuPath",getMenuPath());
        node.put("menuIcon",getMenuIcon());
        return node;
    }
}
