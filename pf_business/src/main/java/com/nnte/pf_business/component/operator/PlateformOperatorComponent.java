package com.nnte.pf_business.component.operator;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.base.BaseNnte;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.*;
import com.nnte.pf_basic.component.BasicGlobalComponent;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperator;
import com.nnte.pf_basic.mapper.workdb.operator.PlateformOperatorService;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctions;
import com.nnte.pf_business.mapper.workdb.functions.PlateformFunctionsService;
import com.nnte.pf_business.mapper.workdb.operole.PlateformOpeRole;
import com.nnte.pf_business.mapper.workdb.operole.PlateformOpeRoleEx;
import com.nnte.pf_business.mapper.workdb.operole.PlateformOpeRoleService;
import com.nnte.pf_business.mapper.workdb.role.PlateformRole;
import com.nnte.pf_business.mapper.workdb.role.PlateformRoleService;
import com.nnte.pf_business.request.RequestFunc;
import com.nnte.pf_business.request.RequestOpe;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
/**
 * 操作员管理组件
 * 日志打印位置：SystemManager 系统管理
 * */
@BusiLogAttr(value = "SystemManager")
public class PlateformOperatorComponent extends BaseComponent {
    @Autowired
    private PlateformOperatorService plateformOperatorService;
    @Autowired
    private PlateformOpeRoleService plateformOpeRoleService;
    @Autowired
    private PlateformFunctionsService plateformFunctionsService;
    @Autowired
    private PlateformRoleService plateformRoleService;
    @Autowired
    private BasicGlobalComponent basicGlobalComponent;
    /**
     * 操作员状态
     * */
    public enum OperatorState{
        UNVALID(0), //无效
        VALID(1),   //有效
        PAUSE(2),   //暂停
        DELED(3);   //已删除
        private int value;
        OperatorState(int val){
            value = val;
        }
        public int getValue(){return value;}
        public static OperatorState fromInt(int val) throws BusiException{
            switch (val){
                case 0:return UNVALID;
                case 1:return VALID;
                case 2:return PAUSE;
                case 3:return DELED;
            }
            throw new BusiException("无效的操作员状态值");
        }
        public String getName(){
            switch (value){
                case 0:return "无效";
                case 1:return "有效";
                case 2:return "暂停";
                case 3:return "已删除";
            }
            return "未知";
        }
    }
    /**
     * 操作员类型
     * */
    public enum OperatorType{
        SupperMgr(1),
        CommonOpe(2),
        AutoOpe(3);
        private int value;
        OperatorType(int val){
            value = val;
        }
        public int getValue(){return value;}
        public static OperatorType fromInt(int val) throws BusiException{
            switch (val){
                case 1:return SupperMgr;
                case 2:return CommonOpe;
                case 3:return AutoOpe;
            }
            throw new BusiException("无效的操作员类型");
        }
        public String getName(){
            switch (value){
                case 1:return "超级管理员";
                case 2:return "普通操作员";
                case 3:return "自动操作员";
            }
            return "未知";
        }
    }
    /**
     * 通过操作员编码查询操作员信息
     * */
    public PlateformOperator getOperatorByCode(String code){
        PlateformOperator dto=new PlateformOperator();
        dto.setOpeCode(code);
        List<PlateformOperator> list=plateformOperatorService.findModelList(dto);
        if (list==null || list.size()!=1)
            return null;
        return list.get(0);
    }
    /**
     * 通过操作员编码查询操作员信息并验证操作员状态
     * */
    public PlateformOperator getOpeByCodeValid(String code,boolean needSupper) throws BusiException{
        PlateformOperator operator = getOperatorByCode(code);
        if (operator == null)
            throw new BusiException(2001,"未找到编号为["+code+"]的操作员信息");
        if (operator.getOpeState()==null || !operator.getOpeState().equals(PlateformOperatorComponent.OperatorState.VALID.getValue()))
            throw new BusiException(2002,"编号为["+code+"]的操作员状态不合法");
        if (needSupper){
            if (operator.getOpeType()==null || !operator.getOpeType().equals(PlateformOperatorComponent.OperatorType.SupperMgr.getValue()))
                throw new BusiException(2002,"编号为["+code+"]的操作员不是超级管理员");
        }
        return operator;
    }
    /**
     * 按条件查询操作员列表
     * */
    public List<PlateformOperator> queryOperatorList(RequestOpe reqOpe){
        PlateformOperator dto = new PlateformOperator();
        if (StringUtils.isNotEmpty(reqOpe.getOpeCode()))
            dto.setOpeCode(reqOpe.getOpeCode());
        if (StringUtils.isNotEmpty(reqOpe.getOpeName()))
            dto.setOpeName(reqOpe.getOpeName());
        if (reqOpe.getOpeType()!=null && reqOpe.getOpeType()>=0)
            dto.setOpeType(reqOpe.getOpeType());
        if (StringUtils.isNotEmpty(reqOpe.getOpeMobile()))
            dto.setOpeMobile(reqOpe.getOpeMobile());
        if (reqOpe.getOpeState()!=null && reqOpe.getOpeState()>=0)
            dto.setOpeState(reqOpe.getOpeState());
        return plateformOperatorService.queryPlateformOperators(dto);
    }
    /**
     * 查询当前所有可用的操作员列表(不分页)
     * */
    public List<PlateformOperator> loadSelectOperator() throws BusiException{
        Map<String,Object> paramMap = new HashedMap();
        paramMap.put("sort", "create_time");
        paramMap.put("dir", "desc");
        List<AppendWhere> appendWhereList = new ArrayList<>();
        AppendWhere whereState = new AppendWhere(AppendWhere.Type_Direct);
        whereState.setWhereTxt("t.ope_state!=3");
        appendWhereList.add(whereState);
        paramMap.put("appendWhereList", appendWhereList);
        return plateformOperatorService.findModelListByMap(paramMap);
    }

    public PageData<RequestOpe> operatorSetList(Map<String,Object> paramMap, Integer pageNo, Integer pageSize){
        PageData<RequestOpe> retPd = new PageData<>();
        PageData<PlateformOperator> pageData=plateformOperatorService.getListPageData("findModelListByMap",paramMap,pageNo,pageSize);
        retPd.setSuccess(pageData.isSuccess());
        retPd.setTotal(pageData.getTotal());
        List<RequestOpe> rList = new ArrayList<>();
        for(PlateformOperator ope:pageData.getData()){
            RequestOpe ro = getRequestOpeByPO(ope);
            rList.add(ro);
        }
        retPd.setData(rList);
        return retPd;
    }
    /**
     * 按条件查询操作员列表
     * */
    public Map<String,Object> queryRequestOpeList(RequestOpe reqOpe){
        Map ret = BaseNnte.newMapRetObj();
        List<PlateformOperator> list=queryOperatorList(reqOpe);
        List<RequestOpe> retList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for (PlateformOperator ope : list) {
                RequestOpe ro = getRequestOpeByPO(ope);
                retList.add(ro);
            }
            ret.put("opeList",retList);
            BaseNnte.setRetTrue(ret,"查询操作员列表成功");
        }else
            BaseNnte.setRetFalse(ret,1001,"查询操作员列表成功,无操作员信息");
        return ret;
    }
    /**
     * 通过编码查询特定的操作员
     * */
    public PlateformOperator queryOperatorByCode(String code){
        PlateformOperator dto = new PlateformOperator();
        dto.setOpeCode(code);
        List<PlateformOperator> list = plateformOperatorService.findModelList(dto);
        if (list!=null && list.size()==1)
            return list.get(0);
        return null;
    }
    /**
     * 通过电话号码查询特定的操作员
     * */
    public PlateformOperator queryOperatorByMobile(String mobile){
        PlateformOperator dto = new PlateformOperator();
        dto.setOpeMobile(mobile);
        List<PlateformOperator> list = plateformOperatorService.findModelList(dto);
        if (list!=null && list.size()>0)
            return list.get(0);
        return null;
    }

    private RequestOpe getRequestOpeByPO(PlateformOperator pfo){
        RequestOpe retOpe = new RequestOpe();
        BeanUtils.copyFromSrc(pfo,retOpe);
        try {
            retOpe.setOpeStateName(OperatorState.fromInt(retOpe.getOpeState()).getName());
            retOpe.setOpeTypeName(OperatorType.fromInt(retOpe.getOpeType()).getName());
        }catch (BusiException be){
            outLogExp(be);
        }
        return retOpe;
    }
    /**
     * 通过编码查询特定的角色
     * */
    public RequestOpe queryRequestOperatorByCode(String code){
        PlateformOperator pfo = queryOperatorByCode(code);
        if (pfo==null)
            return null;
        return getRequestOpeByPO(pfo);
    }
    /**
     * 保存操作员更改,包括新增及更改
     * */
    public Map<String,Object> saveOperatorModify(PlateformOperator operator, int actionType) {
        Map<String, Object> retMap = BaseNnte.newMapRetObj();
        PlateformOperator srcOpe = queryOperatorByCode(operator.getOpeCode());
        PlateformOperator srcMobileOpe = queryOperatorByMobile(operator.getOpeMobile());
        if (actionType==1){
            //如果是新增操作
            if (srcOpe!=null){
                BaseNnte.setRetFalse(retMap,1002,"操作员编号已存在，不能新增");
                return retMap;
            }
            if (srcMobileOpe!=null){
                BaseNnte.setRetFalse(retMap,1002,"操作员电话号码已存在，不能新增");
                return retMap;
            }
            PlateformOperator newOpe = new PlateformOperator();
            newOpe.setOpeCode(operator.getOpeCode());
            newOpe.setOpeName(operator.getOpeName());
            newOpe.setOpeType(operator.getOpeType());
            newOpe.setOpeMobile(operator.getOpeMobile());
            newOpe.setOpeState(operator.getOpeState());
            newOpe.setCreateTime(new Date());
            int count = NumberUtil.getDefaultInteger(plateformOperatorService.addModel(newOpe));
            if (count!=1){
                BaseNnte.setRetFalse(retMap,1002,"新增操作员失败");
                return retMap;
            }
            BaseNnte.setRetTrue(retMap,"新增操作员操作成功");
        }else if(actionType==2){
            if (srcOpe==null){
                BaseNnte.setRetFalse(retMap,1002,"编号["+operator.getOpeCode()+"]操作员不存在，不能更改");
                return retMap;
            }
            if (srcMobileOpe!=null && !srcMobileOpe.getId().equals(srcOpe.getId())){
                BaseNnte.setRetFalse(retMap,1002,"操作员不能设置手机号["+operator.getOpeMobile()+"]，不能更改");
                return retMap;
            }
            PlateformOperator updateOpe = new PlateformOperator();
            updateOpe.setId(srcOpe.getId());
            updateOpe.setOpeName(operator.getOpeName());
            updateOpe.setOpeType(operator.getOpeType());
            updateOpe.setOpeMobile(operator.getOpeMobile());
            updateOpe.setOpeState(operator.getOpeState());
            int count = NumberUtil.getDefaultInteger(plateformOperatorService.updateModel(updateOpe));
            if (count!=1){
                BaseNnte.setRetFalse(retMap,1002,"更改操作员失败");
                return retMap;
            }
            BaseNnte.setRetTrue(retMap,"更改操作员成功");
        }
        return retMap;
    }
    /**
     * 按编号删除操作员(逻辑删除)
     * */
    public Map<String,Object> deleteOpeByCode(String opeCode,PlateformOperator curOpe){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        try {
            PlateformOperator ope = getAndCheckOperator(opeCode, curOpe);
            if (ope.getOpeState().equals(OperatorState.DELED.getValue())) {
                BaseNnte.setRetTrue(retMap, "删除操作员成功，无需删除");
                return retMap;
            }
            PlateformOperator updateDto = new PlateformOperator();
            updateDto.setId(ope.getId());
            updateDto.setOpeState(OperatorState.DELED.getValue());
            Integer count = plateformOperatorService.updateModel(updateDto);
            if (count != 1) {
                throw new BusiException("删除操作失败");
            }
            plateformOpeRoleService.deleteRoleListByOpeCode(updateDto.getOpeCode());
            retMap.put("opeCode", opeCode);
            BaseNnte.setRetTrue(retMap, "操作员删除成功");
        }catch (BusiException be){
            BaseNnte.setRetFalse(retMap, 1002, "操作员删除失败："+be.getMessage());
        }
        return retMap;
    }
    //取得并检测当前操作员能否操作目标操作员信息
    private PlateformOperator getAndCheckOperator(String opeCode,PlateformOperator curOpe) throws BusiException{
        PlateformOperator ope = queryOperatorByCode(opeCode);
        if (ope == null) {
            throw new BusiException(1002,"没找到指定编号的操作员");
        }
        if (ope.getOpeType().equals(OperatorType.SupperMgr.getValue())) {
            if (curOpe == null || !curOpe.getOpeType().equals(OperatorType.SupperMgr.getValue())) {
                throw new BusiException(1002,"当前操作员不是超级管理员，不能操作");
            }
        }
        return ope;
    }
    /**
     * 设置操作员密码
     * */
    public Map<String,Object> setPws(String opeCode,String aimPws,PlateformOperator curOpe){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        try {
            PlateformOperator ope = getAndCheckOperator(opeCode,curOpe);
            String tmpKey = ope.getTmpKey();
            if (StringUtils.isEmpty(tmpKey)) {
                BaseNnte.setRetFalse(retMap, 1002, "秘钥校验错误");
                return retMap;
            }
            PlateformOperator updateOpe = new PlateformOperator();
            updateOpe.setId(ope.getId());
            updateOpe.setTmpKey("");
            String decPassword = AESUtils.decryptECB(aimPws, tmpKey);
            String md5Password = MD5Util.md5For32UpperCase(decPassword);
            updateOpe.setOpePassword(md5Password);
            if (!NumberUtil.getDefaultInteger(plateformOperatorService.updateModel(updateOpe)).equals(1)) {
                BaseNnte.setRetFalse(retMap, 1002, "更改操作员密码失败");
                return retMap;
            }
            BaseNnte.setRetTrue(retMap, "更改操作员密码成功");
        }
        catch (UnsupportedEncodingException uee){
            BusiException be = new BusiException(uee);
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员密码错误："+be.getMessage());
            outLogExp(be);
        }
        catch (BusiException be){
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员密码错误："+be.getMessage());
            outLogExp(be);
        }
        return retMap;
    }
    /**
     * 查询操作员关联的用户角色列表(KEY-VALUE形式)
     * */
    public List<KeyValue> findRoleListOfOpe(String opeCode){
        PlateformOpeRole ope = new PlateformOpeRole();
        ope.setOpeCode(opeCode);
        List<PlateformOpeRoleEx> list=plateformOpeRoleService.findRoleListByOpeCode(ope);
        if (list!=null && list.size()>0){
            List<KeyValue> roleList = new ArrayList<>();
            for(PlateformOpeRoleEx role:list){
                roleList.add(new KeyValue(role.getRoleCode(),role.getRoleName()));
            }
            return roleList;
        }
        return null;
    }
    /**
     * 设置操作员角色
     * */
    public Map<String,Object> saveOperatorRoles(String opeCode,String userRoles,PlateformOperator curOpe){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        try {
            PlateformOperator ope = getAndCheckOperator(opeCode, curOpe);
            plateformOpeRoleService.deleteRoleListByOpeCode(ope.getOpeCode());
            if (StringUtils.isNotEmpty(userRoles)){
                if (userRoles.indexOf("'")<=0)
                    userRoles = StringUtils.stringsToSqlIn(userRoles);
                plateformOpeRoleService.insertRoleListByOpeCode(ope.getOpeCode(),userRoles);
            }
            BaseNnte.setRetTrue(retMap,"设置操作员角色成功");
        }catch (BusiException be){
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员角色错误："+be.getMessage());
            outLogExp(be);
        }catch (Exception e){
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员角色错误："+e.getMessage());
            outLogExp(e);
        }
        return retMap;
    }
    /**
     * 查询操作员关联的功能列表
     * */
    public List<PlateformFunctions> findFunctionListOfOpe(String opeCode){
        PlateformOperator ope = new PlateformOperator();
        ope.setOpeCode(opeCode);
        return plateformFunctionsService.findFunctionsByOpeCode(ope);
    }
    private String getOpeSysRoles(PlateformOperator ope){
        if (ope==null)
            return null;
        List<PlateformRole> roleList=plateformRoleService.findOpeRoleList(ope.getOpeCode());
        if (roleList!=null && roleList.size()>0){
            StringBuffer sysRoleBuf = new StringBuffer();
            Map<String,String> sysRoleMap = new HashMap<>();
            for(PlateformRole role:roleList){
                String sysRoleList=role.getSysroleList();
                if (StringUtils.isNotEmpty(sysRoleList)){
                    String[] sysRoles=sysRoleList.split(",");
                    for(String sr:sysRoles){
                        sysRoleMap.put(sr,sr);
                    }
                }
            }
            if (sysRoleMap.size()>0){
                for(String key:sysRoleMap.keySet()){
                    if (sysRoleBuf.length()>0)
                        sysRoleBuf.append(",");
                    sysRoleBuf.append(key);
                }
            }
            String sRet=sysRoleBuf.toString();
            return sRet.replaceAll("\\[","'").replaceAll("\\]","'");
        }
        return null;
    }
    private void loadValidFunctionsListToMap(List<PlateformFunctions> pfList,Map<String,PlateformFunctions> map,
                                             int keyType){
        if (pfList!=null && pfList.size()>0){
            for(PlateformFunctions fun:pfList)
                if (fun.getFunState().equals(1)) {
                    if (keyType==0)
                        map.put(fun.getFunCode(), fun);
                    else if (keyType==1) {
                        map.put(BaseComponent.getPathByRuler(fun.getAuthCode()), fun);
                    }
                }
        }
    }
    /**
     * 查询操作员关联的有效的功能列表，含系统角色功能标志，用户角色功能标志
     * 本函数主要用于检查操作员权限
     * keyType=0，返回的Map采用funCode作为KEY，keyType=1采用funPath作为KEY
     * */
    public Map<String,PlateformFunctions> getOpeValidFunctionsMap(PlateformOperator ope,int keyType){
        if (ope==null)
            return null;
        Map<String,PlateformFunctions> map=new HashMap<>();
        //操作员功能
        loadValidFunctionsListToMap(findFunctionListOfOpe(ope.getOpeCode()),map,keyType);
        //操作员角色功能
        loadValidFunctionsListToMap(plateformFunctionsService.findFunctionsByOpeRoles(ope),map,keyType);
        //操作员角色的系统角色功能
        String sysRoleCodeList = getOpeSysRoles(ope);
        if (StringUtils.isNotEmpty(sysRoleCodeList)) {
            loadValidFunctionsListToMap(plateformFunctionsService.findFunctionsBySysRoleCode(sysRoleCodeList),map,keyType);
        }
        return map;
    }
    /**
     * 查询操作员关联的功能列表，含系统角色功能标志，用户角色功能标志
     * */
    public Map<String,RequestFunc> getOpeFunctionsMap(PlateformOperator ope){
        if (ope==null)
            return null;
        //操作员功能
        List<PlateformFunctions> pfList=findFunctionListOfOpe(ope.getOpeCode());
        Map<String,RequestFunc> map=new HashMap<>();
        if (pfList!=null && pfList.size()>0) {
            for (PlateformFunctions func : pfList) {
                RequestFunc rf = new RequestFunc();
                BeanUtils.copyFromSrc(func, rf);
                rf.setOpeFunction(1);
                rf.setRoleFunction(0);
                rf.setSysRoleFunction(0);
                map.put(rf.getFunCode(), rf);
            }
        }
        //操作员角色功能
        List<PlateformFunctions> opeRolesPfList=plateformFunctionsService.findFunctionsByOpeRoles(ope);
        if (opeRolesPfList!=null && opeRolesPfList.size()>0) {
            for (PlateformFunctions func : opeRolesPfList) {
                RequestFunc rf = map.get(func.getFunCode());
                if (rf != null) {
                    rf.setRoleFunction(1);
                } else {
                    rf = new RequestFunc();
                    BeanUtils.copyFromSrc(func, rf);
                    rf.setOpeFunction(0);
                    rf.setRoleFunction(1);
                    rf.setSysRoleFunction(0);
                    map.put(rf.getFunCode(), rf);
                }
            }
        }
        //操作员角色的系统角色功能
        String sysRoleCodeList = getOpeSysRoles(ope);
        if (StringUtils.isNotEmpty(sysRoleCodeList)){
            List<PlateformFunctions> sysRolesPfList=plateformFunctionsService.findFunctionsBySysRoleCode(sysRoleCodeList);
            if (sysRolesPfList!=null && sysRolesPfList.size()>0){
                for (PlateformFunctions func : sysRolesPfList) {
                    RequestFunc rf = map.get(func.getFunCode());
                    if (rf != null) {
                        rf.setSysRoleFunction(1);
                    } else {
                        rf = new RequestFunc();
                        BeanUtils.copyFromSrc(func, rf);
                        rf.setOpeFunction(0);
                        rf.setRoleFunction(0);
                        rf.setSysRoleFunction(1);
                        map.put(rf.getFunCode(), rf);
                    }
                }
            }
        }
        return map;
    }
    /**
     * 查询操作员关联的功能列表，含系统角色功能标志，用户角色功能标志
     * */
    public List<RequestFunc> getOpeFunctionsList(PlateformOperator ope){
        List<RequestFunc> retList = new ArrayList<>();
        PlateformFunctions dto = new PlateformFunctions();
        dto.setSort("menu_code");
        dto.setDir("asc");
        List<PlateformFunctions> funcList = plateformFunctionsService.findModelList(dto);
        if (funcList!=null && funcList.size()>0) {
            Map<String, RequestFunc> map = getOpeFunctionsMap(ope);
            for(PlateformFunctions functions:funcList) {
                RequestFunc requestFunc = null;
                if (map!=null && map.size() > 0){
                    requestFunc=map.get(functions.getFunCode());
                }
                if (requestFunc==null){
                    requestFunc = new RequestFunc();
                    BeanUtils.copyFromSrc(functions,requestFunc);
                    requestFunc.setOpeFunction(0);
                    requestFunc.setRoleFunction(0);
                    requestFunc.setSysRoleFunction(0);
                }
                retList.add(requestFunc);
            }
        }
        return retList;
    }
    /**
     * 设置操作员功能
     * */
    public Map<String,Object> saveOperatorFunctions(String opeCode,String functions,PlateformOperator curOpe){
        Map<String,Object> retMap = BaseNnte.newMapRetObj();
        try {
            PlateformOperator ope = getAndCheckOperator(opeCode, curOpe);
            plateformFunctionsService.deleteFunctionsByOpeCode(ope.getOpeCode());
            if (StringUtils.isNotEmpty(functions)){
                functions=StringUtils.stringsToSqlIn(functions);
                plateformFunctionsService.insertFunctionsByOpeCode(ope.getOpeCode(),functions);
            }
            BaseNnte.setRetTrue(retMap,"设置操作员功能成功");
        }catch (BusiException be){
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员功能错误："+be.getMessage());
            outLogExp(be);
        }catch (Exception e){
            BaseNnte.setRetFalse(retMap, 1002, "设置操作员功能异常："+e.getMessage());
            outLogExp(e);
        }
        return retMap;
    }
}
