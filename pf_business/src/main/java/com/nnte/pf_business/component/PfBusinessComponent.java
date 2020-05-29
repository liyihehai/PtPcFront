package com.nnte.pf_business.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseBusiComponent;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.AuthTokenDetailsDTO;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.utils.*;
import com.nnte.pf_business.component.operator.PlateformOperatorComponent;
import com.nnte.pf_business.entertity.OperatorInfo;
import com.nnte.pf_business.entertity.PFMenu;
import com.nnte.pf_business.mapper.workdb.functionrec.PlateformFunctionRec;
import com.nnte.pf_business.mapper.workdb.functionrec.PlateformFunctionRecService;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctionsService;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenus;
import com.nnte.pf_business.mapper.workdb.menus.PlateformMenusService;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_business.mapper.workdb.operator.PlateformOperatorService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr("Pf_Business")
public class PfBusinessComponent extends BaseBusiComponent{
    @Autowired
    private PlateformSysParamComponent plateformSysParamComponent;
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

    public JwtUtils createTokenJwt() {
        String secretKey = plateformSysParamComponent.getSingleParamV100("SYS_SECRETKEY");
        String sign = plateformSysParamComponent.getSingleParamV100("SYS_SIGNATUREALGORITHM");
        String expireTime = plateformSysParamComponent.getSingleParamV100("SYS_TOKENEXPIRETIME");
        JwtUtils jwt = new JwtUtils();
        jwt.initJwtParams(SignatureAlgorithm.forName(sign), secretKey, expireTime);
        return jwt;
    }

    public PlateformMenusService getPlateformMenusService() {
        return plateformMenusService;
    }

    /***
     * 系统角色定义
     * */
    public static final String SYS_MANAGER = "SYS_MANAGER";
    public static final String PLATEFORM_MERCAHNT_MANAGER = "PLATEFORM_MERCAHNT_MANAGER";
    public static final String PLATEFORM_MERCAHNT_WORKER = "PLATEFORM_MERCAHNT_WORKER";
    public static final String MERCHANT_OPERATOR = "MERCHANT_OPERATOR";
    public static final String MERCHANT_MANAGER = "MERCHANT_MANAGER";

    private static Map<String, Object> SystemRoleMap = new HashMap<>();
    private static List<KeyValue> SystemRoleList = new ArrayList<>();

    static {
        SystemRoleList.add(new KeyValue(SYS_MANAGER, "系统管理员"));
        SystemRoleList.add(new KeyValue(PLATEFORM_MERCAHNT_MANAGER, "平台商户管理员"));
        SystemRoleList.add(new KeyValue(PLATEFORM_MERCAHNT_WORKER, "平台商户业务员"));
        SystemRoleList.add(new KeyValue(MERCHANT_OPERATOR, "商家业务操作员"));
        SystemRoleList.add(new KeyValue(MERCHANT_MANAGER, "商家业务管理员"));
        //--------------------------------------
        for (KeyValue kv : SystemRoleList) {
            SystemRoleMap.put(kv.getKey(), kv.getValue().toString());
        }
    }

    public static Map<String, Object> getSystemRoleMap() {
        return SystemRoleMap;
    }

    public static List<KeyValue> getSystemRoleList() {
        return SystemRoleList;
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
                throw new BusiException(1002, "秘钥校验错误", null);
            }
            PlateformOperator updateOpe = new PlateformOperator();
            updateOpe.setId(srcOpe.getId());
            updateOpe.setTmpKey("");
            plateformOperatorService.updateModel(updateOpe);
            String decPassword = AESUtils.decryptECB(password, tmpKey);
            String md5Password = MD5Util.md5For32UpperCase(decPassword);
            if (!srcOpe.getOpePassword().equals(md5Password)) {
                throw new BusiException(1002, "密码错误", null);
            }
            AuthTokenDetailsDTO atd = new AuthTokenDetailsDTO();
            atd.setUserCode(opeCode);
            atd.setUserName(srcOpe.getOpeName());
            atd.setMerchantCode(TOKEN_MERCHANT_CODE);//固定为平台PC前端系统
            atd.setLoginIp(loginIp);
            JwtUtils jwt = createTokenJwt();
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
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 校验请求的Token,拦截器调用
     */
    public Map<String, Object> checkRequestToken(String token, String loginIp) throws BusiException {
        Map ret = BaseNnte.newMapRetObj();
        JwtUtils jwt = createTokenJwt();
        try {
            AuthTokenDetailsDTO atd = jwt.parseAndValidate(token);
            if (!atd.getLoginIp().equals(loginIp))
                throw new BusiException("Ip地址不合法");
            OperatorInfo opeInfo = new OperatorInfo();
            opeInfo.setOperatorCode(atd.getUserCode());
            opeInfo.setOperatorName(atd.getUserName());
            Date now = new Date();
            Date preExpTime = new Date((now.getTime() + 60 * 60 * 1000));//计算当前时间之后1小时的时间
            if (preExpTime.after(atd.getExpirationDate())) {
                //如果当前时间向后推1小时Token将要到期，要重新生成Token,此功能实现Token自动延期
                opeInfo.setLoginTime(DateUtils.dateToString(now, DateUtils.DF_YMDHMS));
                opeInfo.setToken(jwt.createJsonWebToken(atd));
            } else {
                opeInfo.setLoginTime(DateUtils.dateToString(new Date(atd.getExpirationDate().getTime() - jwt.getExpiredTime()),
                        DateUtils.DF_YMDHMS));
                opeInfo.setToken(token);
            }
            ret.put("OperatorInfo", opeInfo);
        } catch (Exception e) {
            throw new BusiException(e,1009, BusiException.ExpLevel.WARN);
        }
        return ret;
    }

    /**
     * 校验操作员是否具备请求的模块的权限,拦截器调用
     */
    public void checkRequestModule(OperatorInfo opeInfo, String path) throws BusiException {
        try {
            MEnter me = BaseBusiComponent.getSystemMEnter(path);
            if (me != null) {
                PlateformOperator pfo = plateformOperatorComponent.getOperatorByCode(opeInfo.getOperatorCode());
                if (!pfo.getOpeState().equals(PlateformOperatorComponent.OperatorState.VALID.getValue())) {
                    throw new Exception("操作员状态不合法");
                }
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
            throw new BusiException(1010, "模块权限校验失败(" + e.getMessage() + ")", BusiException.ExpLevel.WARN);
        }
    }

    /**
     * 装载菜单定义
     */
    private void loadAllMenus(List<PFMenu> root, List<PlateformMenus> list,
                              Map<String, List<PlateformFunctions>> funcMap) {
        while (list.size() > 0) {
            PFMenu pfMenu = new PFMenu(list.get(0));
            if (root.size() == 0) {
                mountFunctionToMenu(funcMap, pfMenu);
                root.add(pfMenu);
                list.remove(0);
            } else {
                PFMenu lastBroMenu = root.get(root.size() - 1);
                if (lastBroMenu.getParentMenuCode().equals(pfMenu.getParentMenuCode())) {
                    //如果和最后的菜单有相同父菜单，表示是要么是兄弟菜单，要么是子菜单
                    if (lastBroMenu.getMenuClass().equals(pfMenu.getMenuClass())) {
                        //如果是兄弟菜单
                        mountFunctionToMenu(funcMap, pfMenu);
                        root.add(pfMenu);
                        list.remove(0);
                        continue;
                    } else {
                        //如果是下级菜单(不可能是上级菜单)
                        loadAllMenus(lastBroMenu.getSubMenuList(), list, funcMap);
                        continue;
                    }
                } else {
                    //如果父菜单不同，判断是否是同级菜单
                    if (pfMenu.getMenuClass().equals(lastBroMenu.getMenuClass())) {
                        //如果是同级菜单，表示是兄弟菜单
                        mountFunctionToMenu(funcMap, pfMenu);
                        root.add(pfMenu);
                        list.remove(0);
                        continue;
                    } else {
                        //如果不是同级菜单，判断是否是子菜单，否则应返回上级处理
                        if (pfMenu.getParentMenuCode().equals(lastBroMenu.getMenuCode())) {
                            loadAllMenus(lastBroMenu.getSubMenuList(), list, funcMap);
                            continue;
                        }
                        return;
                    }
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
            List<PlateformMenus> list = plateformMenusService.queryAllPlateformMenus(paramMenu);
            if (list == null || list.size() <= 0)
                return null;
            List<PFMenu> menuFuncList = new ArrayList();
            Map<String, List<PlateformFunctions>> funcMap = loadOperatorFunctions(ope, null);//菜单装载功能
            loadAllMenus(menuFuncList, list, funcMap);
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
    /**
     * 在系统中注册功能模块信息
     */
    public void registerFunctions(String appCode, String appName, Map<String, String> moduleMap,
                                  List<MEnter> functionModuleList) {
        try {
            if (functionModuleList != null && functionModuleList.size() > 0) {
                for (MEnter me : functionModuleList) {
                    PlateformFunctionRec dto = new PlateformFunctionRec();
                    PlateformFunctionRec srcFRec = getFunctionRecByPath(me.getPath());
                    //---------------------
                    dto.setFunName(me.getName());
                    dto.setSysRoleCode(me.getSysRole());
                    dto.setSysRoleName(me.getName());
                    dto.setAuthCode(me.getRoleRuler());
                    dto.setAppCode(appCode);
                    dto.setAppName(appName);
                    dto.setModuleCode(me.getModuleCode());
                    if (moduleMap!=null)
                        dto.setModuleName(moduleMap.get(me.getModuleCode()));
                    dto.setModuleVersion(me.getModuleVersion());
                    //----------------------
                    if (srcFRec==null){
                        dto.setFunPath(me.getPath());
                        plateformFunctionRecService.addModel(dto);
                    }else{
                        dto.setId(srcFRec.getId());
                        plateformFunctionRecService.updateModel(dto);
                    }
                }
            }
        }catch (BusiException be){
            logException(be);
        }
    }
}