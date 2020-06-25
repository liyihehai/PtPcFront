package com.nnte.pf_business.component.menus;

import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_business.entertity.PFFunction;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctionsService;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PlateformFunctionComponent {
    @Autowired
    private PlateformFunctionsService plateformFunctionsService;
    @Autowired
    private PlateformMenusService plateformMenusService;
    /**
     * 保存菜单更改,包括新增及更改
     * */
    public Map<String,Object> saveMenuModify(PlateformMenus menu){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        if (menu==null)
            return retMap;
        if (StringUtils.isEmpty(menu.getMenuCode())||
                StringUtils.isEmpty(menu.getParentMenuCode())){
            BaseNnte.setRetFalse(retMap,1002,"菜单编号不合法");
            return retMap;
        }
        int codeCount=getMenuCountByCode(menu.getMenuCode());
        if (codeCount<=0){
            //需要新增菜单
            PlateformMenus newMenu=new PlateformMenus();
            if (menu.getMenuCode().equals(menu.getParentMenuCode())) {//如果是一级菜单
                if (menu.getMenuCode().length()!=2){
                    BaseNnte.setRetFalse(retMap,1002,"每级菜单编号长度为2字符");
                    return retMap;
                }
                newMenu.setMenuCode(menu.getMenuCode());
                newMenu.setMenuClass(1);
                newMenu.setMenuState(menu.getMenuState());
                newMenu.setParentMenuCode(menu.getParentMenuCode());
            }
            else{
                PlateformMenus parenMenu=getMenuByCode(menu.getParentMenuCode());
                if (parenMenu==null){
                    BaseNnte.setRetFalse(retMap,1002,"没有找到父级菜单");
                    return retMap;
                }
                if (menu.getMenuCode().length()-parenMenu.getMenuCode().length()!=2 ||
                        !StringUtils.left(menu.getMenuCode(),
                                parenMenu.getMenuCode().length()).equals(parenMenu.getMenuCode())){
                    BaseNnte.setRetFalse(retMap,1002,"子菜单编号必须为父菜单编号加2字符");
                    return retMap;
                }
                if (menu.getMenuState().equals(1)){
                    if (!parenMenu.getMenuState().equals(1)){
                        BaseNnte.setRetFalse(retMap,1002,"父菜单状态为不可用，子菜单状态不能为可用");
                        return retMap;
                    }
                }
                newMenu.setMenuCode(menu.getMenuCode());
                newMenu.setMenuClass(parenMenu.getMenuClass()+1);
                newMenu.setParentMenuCode(parenMenu.getMenuCode());
            }
            newMenu.setMenuName(menu.getMenuName());
            newMenu.setCreateTime(new Date());
            newMenu.setMenuState(menu.getMenuState());
            Integer count=plateformMenusService.addModel(newMenu);
            if (count!=null && count.equals(1)){
                BaseNnte.setRetTrue(retMap,"新增菜单信息成功");
                return retMap;
            }
            BaseNnte.setRetFalse(retMap,1003,"新增菜单信息失败");
        }else{
            PlateformMenus srcMenu=getMenuByCode(menu.getMenuCode());
            if (srcMenu==null){
                BaseNnte.setRetFalse(retMap,1002,"不能确定父级菜单");
                return retMap;
            }
            PlateformMenus updateMenu= new PlateformMenus();
            if (menu.getMenuState().equals(1) && !srcMenu.getMenuState().equals(1)){
                PlateformMenus parenMenu=getMenuByCode(menu.getParentMenuCode());
                if (!parenMenu.getMenuState().equals(1)){
                    BaseNnte.setRetFalse(retMap,1002,"父菜单状态为不可用，子菜单状态不能为可用");
                    return retMap;
                }
            }
            if (menu.getMenuState().equals(0) && !srcMenu.getMenuState().equals(0)){
                //如果要改变菜单状态为0，需要菜单的子菜单或子功能没有状态为1的记录
                List<PlateformMenus> submenuList=this.getSubMenusByParentCode(menu.getMenuCode());
                for(PlateformMenus submenu:submenuList){
                    if (submenu.getMenuState().equals(1)){
                        BaseNnte.setRetFalse(retMap,1002,"存在状态为可用的子菜单，不能设置为不可用");
                        return retMap;
                    }
                }
                List<PlateformFunctions> functionList=this.getFunctionsByMenuCode(menu.getMenuCode());
                for(PlateformFunctions func:functionList){
                    if (func.getFunState().equals(1)){
                        BaseNnte.setRetFalse(retMap,1002,"存在状态为可用的子功能，不能设置为不可用");
                        return retMap;
                    }
                }
            }
            updateMenu.setId(srcMenu.getId());
            updateMenu.setMenuName(menu.getMenuName());
            updateMenu.setMenuState(menu.getMenuState());
            Integer count=plateformMenusService.updateModel(updateMenu);
            if (count!=null && count.equals(1)){
                BaseNnte.setRetTrue(retMap,"更改菜单信息成功");
                return retMap;
            }
            BaseNnte.setRetFalse(retMap,1003,"更改菜单信息失败");
        }
        return retMap;
    }
    /**
     * 通过编号取得唯一的菜单
     * */
    public PlateformMenus getMenuByCode(String code){
        if (StringUtils.isEmpty(code))
            return null;
        PlateformMenus menu = new PlateformMenus();
        menu.setMenuCode(code);
        List<PlateformMenus> list=plateformMenusService.findModelList(menu);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
    /**
     * 按编号查询菜单数量，不管菜单的状态
     * */
    public int getMenuCountByCode(String code){
        if (StringUtils.isEmpty(code))
            return 0;
        PlateformMenus menu = new PlateformMenus();
        menu.setMenuCode(code);
        List<PlateformMenus> list=plateformMenusService.findModelList(menu);
        if (list==null || list.size()<=0)
            return 0;
        return list.size();
    }
    /**
     * 查询子菜单列表
     * */
    public List<PlateformMenus> getSubMenusByParentCode(String parentMenuCode){
        PlateformMenus menu = new PlateformMenus();
        menu.setParentMenuCode(parentMenuCode);
        return plateformMenusService.findModelList(menu);
    }
    /**
     * 按父菜单编号查询菜单数量，不管菜单状态
     * */
    public int getMenuCountByParentCode(String parentMenuCode){
        if (StringUtils.isEmpty(parentMenuCode))
            return 0;
        List<PlateformMenus> list=getSubMenusByParentCode(parentMenuCode);
        if (list==null || list.size()<=0)
            return 0;
        return list.size();
    }
    /**
     * 查询子功能列表
     * */
    public List<PlateformFunctions> getFunctionsByMenuCode(String menuCode){
        PlateformFunctions func = new PlateformFunctions();
        func.setMenuCode(menuCode);
        return plateformFunctionsService.findModelList(func);
    }
    /**
     * 按父菜单编号查询功能数量，不管菜单状态
     * */
    public int getFunctionCountByParentCode(String parentMenuCode){
        if (StringUtils.isEmpty(parentMenuCode))
            return 0;
        List<PlateformFunctions> list=getFunctionsByMenuCode(parentMenuCode);
        if (list==null || list.size()<=0)
            return 0;
        return list.size();
    }
    /**
     * 按编号删除菜单
     * */
    public Map<String,Object> deleteMenuByCode(String menuCode){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        if (StringUtils.isEmpty(menuCode)){
            BaseNnte.setRetFalse(retMap,1002,"菜单编号不合法");
            return retMap;
        }
        PlateformMenus menu = getMenuByCode(menuCode);
        if (menu==null){
            BaseNnte.setRetFalse(retMap,1002,"没找到指定编号的菜单");
            return retMap;
        }
        int subMenuCount=getMenuCountByParentCode(menuCode);
        if (subMenuCount>0){
            BaseNnte.setRetFalse(retMap,1002,"菜单有子菜单不能删除");
            return retMap;
        }
        int subFunctionCount=getFunctionCountByParentCode(menuCode);
        if (subFunctionCount>0){
            BaseNnte.setRetFalse(retMap,1002,"菜单有子功能不能删除");
            return retMap;
        }
        Integer count=plateformMenusService.deleteModel(menu.getId());
        if (count!=1){
            BaseNnte.setRetFalse(retMap,1002,"菜单删除失败");
            return retMap;
        }
        retMap.put("menuCode",menuCode);
        retMap.put("parentMenuCode",menu.getParentMenuCode());
        BaseNnte.setRetTrue(retMap,"菜单删除成功");
        return retMap;
    }
    /**
     * 按编号删除功能菜单
     * */
    public Map<String,Object> deleteFuncByCode(String funCode){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        if (StringUtils.isEmpty(funCode)){
            BaseNnte.setRetFalse(retMap,1002,"功能菜单编号不合法");
            return retMap;
        }
        PlateformFunctions func = getFuncByCode(funCode);
        if (func==null){
            BaseNnte.setRetFalse(retMap,1002,"没找到指定编号的功能菜单");
            return retMap;
        }
        Integer count=plateformFunctionsService.deleteModel(func.getId());
        if (count!=1){
            BaseNnte.setRetFalse(retMap,1002,"功能菜单删除失败");
            return retMap;
        }
        retMap.put("funCode",funCode);
        retMap.put("parentMenuCode",func.getMenuCode());
        BaseNnte.setRetTrue(retMap,"功能菜单删除成功");
        return retMap;
    }
    /**
     * 按编号查询功能数量，不管功能的状态
     * */
    public int getFuncCountByCode(String code){
        if (StringUtils.isEmpty(code))
            return 0;
        PlateformFunctions func = new PlateformFunctions();
        func.setFunCode(code);
        List<PlateformFunctions> list=plateformFunctionsService.findModelList(func);
        if (list==null || list.size()<=0)
            return 0;
        return list.size();
    }
    /**
     * 通过编号取得唯一的菜单
     * */
    public PlateformFunctions getFuncByCode(String code){
        if (StringUtils.isEmpty(code))
            return null;
        PlateformFunctions func = new PlateformFunctions();
        func.setFunCode(code);
        List<PlateformFunctions> list=plateformFunctionsService.findModelList(func);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
    /**
     * 保存功能更改,包括新增及更改
     * */
    public Map<String,Object> saveFunctionModify(PlateformFunctions func,String funcPath){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        if (func==null)
            return retMap;
        if (StringUtils.isEmpty(func.getMenuCode())||
                StringUtils.isEmpty(func.getFunCode())){
            BaseNnte.setRetFalse(retMap,1002,"功能编号不合法");
            return retMap;
        }
        PlateformMenus parenMenu=getMenuByCode(func.getMenuCode());
        if (parenMenu==null){
            BaseNnte.setRetFalse(retMap,1002,"没有找到父级菜单");
            return retMap;
        }
        if (func.getFunState().equals(1)){
            if (!parenMenu.getMenuState().equals(1)){
                BaseNnte.setRetFalse(retMap,1002,"父菜单不可用时，功能不能设置为可用");
                return retMap;
            }
        }
        MEnter menter=BaseBusiComponent.getSystemMEnter(funcPath);
        if (menter==null){
            BaseNnte.setRetFalse(retMap,1002,"功能路径不合法:"+funcPath);
            return retMap;
        }
        int codeCount=getFuncCountByCode(func.getFunCode());
        if (codeCount<=0){
            //需要新增功能菜单
            PlateformFunctions newFunc=new PlateformFunctions();
            if (func.getFunCode().length()-parenMenu.getMenuCode().length()!=2 ||
                    !StringUtils.left(func.getFunCode(),
                            parenMenu.getMenuCode().length()).equals(parenMenu.getMenuCode())){
                BaseNnte.setRetFalse(retMap,1002,"子菜单编号必须为父菜单编号加2字符");
                return retMap;
            }
            newFunc.setMenuCode(parenMenu.getMenuCode());
            newFunc.setFunCode(func.getFunCode());
            newFunc.setFunName(func.getFunName());
            newFunc.setAuthCode(menter.getRoleRuler());
            newFunc.setFunParam(func.getFunParam());
            newFunc.setCreateTime(new Date());
            newFunc.setFunState(func.getFunState());
            Integer count=plateformFunctionsService.addModel(newFunc);
            if (count!=null && count.equals(1)){
                BaseNnte.setRetTrue(retMap,"新增功能信息成功");
                return retMap;
            }
            BaseNnte.setRetFalse(retMap,1003,"新增功能信息失败");
        }else{
            PlateformFunctions srcFunc=getFuncByCode(func.getFunCode());
            if (srcFunc==null){
                BaseNnte.setRetFalse(retMap,1002,"没有找到指定的功能菜单");
                return retMap;
            }
            PlateformFunctions updateFunc= new PlateformFunctions();
            updateFunc.setId(srcFunc.getId());
            updateFunc.setFunName(func.getFunName());
            updateFunc.setAuthCode(menter.getRoleRuler());
            updateFunc.setFunParam(func.getFunParam());
            updateFunc.setFunState(func.getFunState());
            Integer count=plateformFunctionsService.updateModel(updateFunc);
            if (count!=null && count.equals(1)){
                BaseNnte.setRetTrue(retMap,"更改功能菜单信息成功");
                return retMap;
            }
            BaseNnte.setRetFalse(retMap,1003,"更改功能菜单信息失败");
        }
        return retMap;
    }
    /**
     * 查询指定状态的功能列表，如果状态为null,查询所有状态的功能列表
     * */
    public List<PFFunction> queryPlateformFunctionList(Integer funState){
        PlateformFunctions func = new PlateformFunctions();
        func.setFunState(funState);
        List<PlateformFunctions> list= plateformFunctionsService.queryAllPlateformFunctions(func);
        if (list==null || list.size()<=0)
            return null;
        List<PFFunction> retList = new ArrayList<>();
        list.stream().forEach(e->{
            PFFunction pff = new PFFunction();
            BeanUtils.copyFromSrc(e,pff);
            pff.setFunPath(BaseBusiComponent.getPathByRuler(e.getAuthCode()));
            retList.add(pff);
        });
        return retList;
    }
}
