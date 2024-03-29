package com.nnte.pf_business.component;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.entity.SysModule;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.AuthTokenDetailsDTO;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.component.PFBasicComponent;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperatorService;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functionrec.PlateformFunctionRec;
import com.nnte.pf_business.mapper.workdb.functionrec.PlateformFunctionRecService;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctionsService;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr("Pf_Business")
public class PfBusinessComponent extends BaseComponent {
    @Autowired
    private PlateformOperatorService plateformOperatorService;
    @Autowired
    private PlateformOperatorComponent plateformOperatorComponent;
    @Autowired
    private PlateformMenusService plateformMenusService;
    @Autowired
    private PlateformFunctionsService plateformFunctionsService;
    @Autowired
    private PlateformFunctionRecService plateformFunctionRecService;

    public final static String TOKEN_MERCHANT_CODE = "PlateformPCFront";
    private final String routePath = "routePath";
    @Autowired
    private PFBasicComponent pfBasicComponent;

    public PlateformMenusService getPlateformMenusService() {
        return plateformMenusService;
    }

    /**
     * 取得状态可用的单一操作员信息
     */
    private PlateformOperator queryOperator(String opeCode) throws BusiException {
        PlateformOperator ope = new PlateformOperator();
        if (StringUtils.isChinaPhone(opeCode))
            ope.setOpeMobile(opeCode);
        else
            ope.setOpeCode(opeCode);
        List<PlateformOperator> list = plateformOperatorService.findModelList(ope);
        if (list == null || list.size() <= 0)
            throw new BusiException("操作员编号不存在");
        if (list == null || list.size() > 1) {
            throw new BusiException("操作员编号重复");
        }
        PlateformOperator retOpe = list.get(0);
        if (!retOpe.getOpeState().equals(1))
            throw new BusiException("操作员状态不合法");
        return retOpe;
    }

    /**
     * 预校验用户账号，用于登录时前先校验用户名，并生成临时秘钥用于前端加密
     */
    public Map<String, Object> priCheckUser(String opeCode) {
        Map ret = BaseNnte.newMapRetObj();
        try {
            PlateformOperator ope = queryOperator(opeCode);
            PlateformOperator updateDto = new PlateformOperator();
            updateDto.setId(ope.getId());
            String tmpKey = StringUtils.getRandomStringByLength(16);
            updateDto.setTmpKey(tmpKey);
            Integer count = plateformOperatorService.updateModel(updateDto);
            if (!count.equals(1))
                throw new BusiException("生成临时秘钥失败");
            ret.put("tmpKey", tmpKey);
            BaseNnte.setRetTrue(ret, "预校验成功");
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1001, be.getMessage());
        }
        return ret;
    }

    /**
     * 校验操作员代码及密码
     */
    public Map<String, Object> checkUserPassword(String opeCode, String password, String loginIp) {
        Map ret = BaseNnte.newMapRetObj();
        try {
            PlateformOperator srcOpe = queryOperator(opeCode);
            String tmpKey = srcOpe.getTmpKey();
            if (StringUtils.isEmpty(tmpKey)) {
                throw new BusiException(1002, "秘钥校验错误");
            }
            PlateformOperator updateOpe = new PlateformOperator();
            updateOpe.setId(srcOpe.getId());
            updateOpe.setTmpKey("");
            plateformOperatorService.updateModel(updateOpe);
            String decPassword = AESUtils.decryptECB(password, tmpKey);
            String md5Password = MD5Util.md5For32UpperCase(decPassword);
            if (!srcOpe.getOpePassword().equals(md5Password)) {
                throw new BusiException(1002, "密码错误");
            }
            AuthTokenDetailsDTO atd = new AuthTokenDetailsDTO();
            atd.setUserCode(opeCode);
            atd.setUserName(srcOpe.getOpeName());
            atd.setMerchantCode(TOKEN_MERCHANT_CODE);//固定为平台PC前端系统
            atd.setLoginIp(loginIp);
            JwtUtils jwt = pfBasicComponent.createTokenJwt();
            String token = jwt.createJsonWebToken(atd);
            OperatorInfo opeInfo = new OperatorInfo();
            opeInfo.setOperatorCode(opeCode);
            opeInfo.setOperatorName(srcOpe.getOpeName());
            opeInfo.setToken(token);
            opeInfo.setLoginTime(DateUtils.dateToString(new Date(), DateUtils.DF_YMDHMS));
            opeInfo.setExpireTime(DateUtils.dateToString(atd.getExpirationDate(), DateUtils.DF_YMDHMS));
            ret.put("OperatorInfo", opeInfo);
            //取得菜单功能结构
            List<PFMenu> menuFuncList = loadOperatorMenuFunctions(opeInfo, 1);//只显示状态为1的菜单
            ret.put("menuFuncList", menuFuncList);
            BaseNnte.setRetTrue(ret, "密码校验成功");
        } catch (BusiException be) {
            BaseNnte.setRetFalse(ret, 1002, be.getMessage());
        } catch (Exception e) {
            BaseNnte.setRetFalse(ret, 9999, e.getMessage());
            outLogExp(e);
        }
        return ret;
    }

    private void loadMenuNodeSub(List<PFMenu> subMenuList,ObjectNode parentNodeMenu,boolean isForEdit) throws BusiException{
        ArrayNode childrenMenuArray = JsonUtil.newJsonNode().arrayNode();
        for(PFMenu menu:subMenuList) {
            ObjectNode nodeMenu = createNodeFromMenu(menu,isForEdit);
            nodeMenu.put(routePath,parentNodeMenu.get(routePath).textValue()+"/"+menu.getMenuName());
            if (menu.getSubMenuList() != null && menu.getSubMenuList().size() > 0) {
                loadMenuNodeSub(menu.getSubMenuList(), nodeMenu,isForEdit);
            }
            if (menu.getFunctionList() != null && menu.getFunctionList().size() > 0) {
                loadMenuNodeFunc(menu.getFunctionList(), nodeMenu,isForEdit);
            }
            childrenMenuArray.add(nodeMenu);
        }
        parentNodeMenu.set("children",childrenMenuArray);
    }

    private void loadMenuNodeFunc(List<PlateformFunctions> menuFuncList,ObjectNode parentNodeMenu,boolean isForEdit) throws BusiException{
        ArrayNode childrenFuncArray;
        if (parentNodeMenu.get("children")!=null)
            childrenFuncArray=(ArrayNode)parentNodeMenu.get("children");
        else
            childrenFuncArray = JsonUtil.newJsonNode().arrayNode();
        for(PlateformFunctions func:menuFuncList){
            ObjectNode nodeFunc;
            if (isForEdit){
                nodeFunc = JsonUtil.getObjectNodefromBean(func);
                if (nodeFunc==null)
                    throw new BusiException(1003,"菜单对象生成节点信息错误");
            }else {
                nodeFunc = JsonUtil.newJsonNode();
                nodeFunc.put("name", func.getFunName());
                nodeFunc.put("path", func.getFunPath());
                nodeFunc.put("icon", func.getFunIcon());
            }
            nodeFunc.put(routePath,parentNodeMenu.get(routePath).textValue()+"/"+func.getFunName());
            childrenFuncArray.add(nodeFunc);
        }
        parentNodeMenu.set("children",childrenFuncArray);
    }

    private ObjectNode createNodeFromMenu(PFMenu menu,boolean isForEdit) throws BusiException{
        ObjectNode nodeMenu;
        if (isForEdit){
            PlateformMenus pm = new PlateformMenus();
            BeanUtils.copyProperties(menu,pm);
            nodeMenu = JsonUtil.getObjectNodefromBean(pm);
            if (nodeMenu==null)
                throw new BusiException(1003,"菜单对象生成节点信息错误");
        }else {
            nodeMenu = JsonUtil.newJsonNode();
            nodeMenu.put("name", menu.getMenuName());
            nodeMenu.put("path", menu.getMenuPath());
            nodeMenu.put("icon", menu.getMenuIcon());
        }
        return nodeMenu;
    }
    private boolean delBlankMenu(List<PFMenu> menuFuncList,int index){
        PFMenu curMenu=menuFuncList.get(index);
        if (curMenu.getFunctionList()!=null && curMenu.getFunctionList().size()>0)
            return false;
        if (curMenu.getSubMenuList()!=null && curMenu.getSubMenuList().size()>0){
            while(true){
                if (curMenu.getSubMenuList()==null || curMenu.getSubMenuList().size()<=0) {
                    menuFuncList.remove(index);
                    return true;
                }
                if (!delBlankMenu(curMenu.getSubMenuList(),0))
                    return false;
            }
        }else
            menuFuncList.remove(index);
        return true;
    }
    /**
     * 在服务器端将菜单及功能树转化前端需要的菜单结构
     * parentNodeMenu 初始化为 JsonUtil.newJsonNode().arrayNode();
     * */
    public ArrayNode loadMenuFuncNode(List<PFMenu> menuFuncList,boolean isForEdit) throws BusiException{
        ArrayNode childrenMenuArray = JsonUtil.newJsonNode().arrayNode();
        if (menuFuncList==null || menuFuncList.size()<=0)
            return childrenMenuArray;
        if(!isForEdit){//如果菜单用于前端使用而不是编辑，需要去掉空菜单
            for(int i=0;i<menuFuncList.size();i++){
                if (delBlankMenu(menuFuncList,i))
                    i--;
            }
        }
        for(PFMenu menu:menuFuncList){
            ObjectNode nodeMenu= createNodeFromMenu(menu,isForEdit);
            nodeMenu.put(routePath,menu.getMenuName());
            if (menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0) {
                loadMenuNodeSub(menu.getSubMenuList(),nodeMenu,isForEdit);
            }
            if (menu.getFunctionList()!=null && menu.getFunctionList().size()>0){
                loadMenuNodeFunc(menu.getFunctionList(),nodeMenu,isForEdit);
            }
            childrenMenuArray.add(nodeMenu);
        }

        return childrenMenuArray;
    }
    /**
     * 校验操作员是否具备请求的模块的权限,拦截器调用
     */
    public void checkRequestModule(OperatorInfo opeInfo, String path) throws BusiException {
        try {
            MEnter me = BaseComponent.getSystemMEnter(path);
            if (me != null) {
                PlateformOperator pfo = plateformOperatorComponent.getOperatorByCode(opeInfo.getOperatorCode());
                if (!pfo.getOpeState().equals(PlateformOperatorComponent.OperatorState.VALID.getValue())) {
                    throw new Exception("操作员状态不合法");
                }
                opeInfo.setOperatorType(pfo.getOpeType());
                //如果模块或权限没有定义，只能是超级系统管理员才能进入
                if (StringUtils.isEmpty(me.getSysRole()) || StringUtils.isEmpty(me.getRoleRuler())) {
                    if (pfo.getOpeType().equals(1))
                        return;//如果操作员是超级管理员，直接放行
                    throw new Exception("只有超级管理员才能进入模块[" + me.getName() + "]");
                } else {
                    //如果模块有权限限制，当前操作员不是超级管理员，则需要校验权限，否则不需要校验
                    if (!pfo.getOpeType().equals(1)) {
                        Map<String,PlateformFunctions> funcMap=plateformOperatorComponent.getOpeValidFunctionsMap(pfo,1);
                        if (funcMap==null)
                            throw new Exception("当前操作员分配进入模块的功能");
                        if (funcMap.get(path)==null)
                            throw new Exception("当前操作员没有权限进入模块[" + me.getName() + "]");
                    }
                }
            }
        } catch (Exception e) {
            throw new BusiException(1010, "模块权限校验失败(" + e.getMessage() + ")");
        }
    }
    /**
     * 取得父菜单对象
     * */
    private PFMenu getParenMenu(List<PFMenu> root,String parentMenuCode){
        for(PFMenu menu:root){
            if (menu.getMenuCode().equals(parentMenuCode))
                return menu;
            if (menu.getSubMenuList()!=null && menu.getSubMenuList().size()>0){
                PFMenu subMenu=getParenMenu(menu.getSubMenuList(),parentMenuCode);
                if (subMenu!=null)
                    return subMenu;
            }
        }
        return null;
    }

    /**
     * 按菜单等级顺序装载菜单定义，并将功能挂载到菜单
     */
    private void loadMenuAndFunction(List<PFMenu> root, List<PlateformMenus> list,
                              Map<String, List<PlateformFunctions>> funcMap) {
        for(PlateformMenus menu:list){
            if (menu.getMenuClass().equals(1)){
                //如果菜单等级为1，表示是顶级菜单
                PFMenu pfMenu = new PFMenu(menu);
                mountFunctionToMenu(funcMap, pfMenu);
                root.add(pfMenu);
            }else{
                PFMenu parentMenu = getParenMenu(root,menu.getParentMenuCode());
                if (parentMenu!=null){
                    PFMenu pfMenu = new PFMenu(menu);
                    mountFunctionToMenu(funcMap, pfMenu);
                    parentMenu.getSubMenuList().add(pfMenu);
                }
            }
        }
    }

    /**
     * 装载操作员的菜单及功能
     */
    public List<PFMenu> loadOperatorMenuFunctions(OperatorInfo opeInfo, Integer menuState) {
        PlateformOperator ope = plateformOperatorComponent.getOperatorByCode(opeInfo.getOperatorCode());
        if (ope != null) {
            //装载菜单
            PlateformMenus paramMenu = new PlateformMenus();
            if (menuState != null)
                paramMenu.setMenuState(menuState);
            List<PlateformMenus> list = plateformMenusService.queryPMenusOrderByClass(paramMenu);
            if (list == null || list.size() <= 0)
                return null;
            List<PFMenu> menuFuncList = new ArrayList();
            Map<String, List<PlateformFunctions>> funcMap = loadOperatorFunctions(ope, null);//菜单装载功能
            loadMenuAndFunction(menuFuncList,list,funcMap);
            return menuFuncList;
        }
        return null;
    }

    /**
     * 挂载功能到菜单,如果挂载成功返回true,否则返回false
     */
    private void mountFunctionToMenu(Map<String, List<PlateformFunctions>> funcMap,
                                     PFMenu menu) {
        List<PlateformFunctions> list = funcMap.get(menu.getMenuCode());
        if (list != null)
            menu.setFunctionList(list);
    }

    /**
     * 装载操作员功能
     */
    public Map<String, List<PlateformFunctions>> loadOperatorFunctions(PlateformOperator operator, Integer state) {
        Map<String, List<PlateformFunctions>> retMap = new HashMap<>();
        PlateformFunctions param = new PlateformFunctions();
        param.setFunState(state);
        List<PlateformFunctions> funcList = null;
        if (operator.getOpeType().equals(1)) {
            //如果是超级管理员，自动拥有所有功能
            funcList = plateformFunctionsService.queryAllPlateformFunctions(param);
        }else {
            //如果不是超级管理员，按自定义功能，角色功能加载功能定义
            Map<String,PlateformFunctions> funcMap = plateformOperatorComponent.getOpeValidFunctionsMap(operator,0);
            if (funcMap!=null){
                funcList = new ArrayList<>();
                for(String key:funcMap.keySet()){
                    funcList.add(funcMap.get(key));
                }
            }
        }
        if (funcList!=null) {
            for (PlateformFunctions func : funcList) {
                List<PlateformFunctions> list = retMap.get(func.getMenuCode());
                if (list == null) {
                    List<PlateformFunctions> newList = new ArrayList<>();
                    newList.add(func);
                    retMap.put(func.getMenuCode(), newList);
                } else
                    list.add(func);
            }
        }
        return retMap;
    }
    private PlateformFunctionRec getFunctionRecByPath(String path) throws BusiException{
        if (StringUtils.isEmpty(path))
            return null;
        PlateformFunctionRec dto=new PlateformFunctionRec();
        dto.setFunPath(path);
        List<PlateformFunctionRec> list=plateformFunctionRecService.findModelList(dto);
        if (list==null||list.size()<=0)
            return null;
        if(list.size()==1)
            return list.get(0);
        throw new BusiException("模块路径重复");
    }

    private PlateformFunctionRec getFunctionRecByAuthCode(String authCode) throws BusiException{
        if (StringUtils.isEmpty(authCode))
            return null;
        PlateformFunctionRec dto=new PlateformFunctionRec();
        dto.setAuthCode(authCode);
        List<PlateformFunctionRec> list=plateformFunctionRecService.findModelList(dto);
        if (list==null||list.size()<=0)
            return null;
        if(list.size()==1)
            return list.get(0);
        throw new BusiException("模块权限重复");
    }

    /**
     * 在系统中注册功能模块信息
     */
    public void registerFunctions(String appCode, String appName, Map<String, SysModule> moduleMap,
                                  List<MEnter> functionModuleList) {
        try {
            if (functionModuleList != null && functionModuleList.size() > 0) {
                for (MEnter me : functionModuleList) {
                    SysModule module=moduleMap.get(me.getModuleCode());
                    PlateformFunctionRec dto = new PlateformFunctionRec();
                    PlateformFunctionRec srcFRec = getFunctionRecByAuthCode(me.getRoleRuler());
                    //---------------------
                    dto.setFunName(me.getName());
                    dto.setSysRoleCode(me.getSysRole());
                    dto.setSysRoleName(me.getName());
                    dto.setFunPath(me.getPath());
                    dto.setAppCode(appCode);
                    dto.setAppName(appName);
                    dto.setModuleCode(me.getModuleCode());
                    if (module!=null)
                        dto.setModuleName(module.getModuleName());
                    dto.setModuleVersion(module.getModuleVersion());
                    //----------------------
                    if (srcFRec==null){
                        dto.setAuthCode(me.getRoleRuler());
                        plateformFunctionRecService.addModel(dto);
                    }else{
                        dto.setId(srcFRec.getId());
                        plateformFunctionRecService.updateModel(dto);
                    }
                }
            }
        }catch (BusiException be){
            outLogExp(be);
        }
    }

}