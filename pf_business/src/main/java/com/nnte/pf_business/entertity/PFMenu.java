package com.nnte.pf_business.entertity;

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
        subMenuList = new ArrayList<>();
    }
}
