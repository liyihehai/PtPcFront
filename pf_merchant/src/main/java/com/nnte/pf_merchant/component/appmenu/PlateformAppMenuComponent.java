package com.nnte.pf_merchant.component.appmenu;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.entity.MEnter;
import com.nnte.basebusi.entity.OperatorInfo;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.BeanUtils;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.component.BasicGlobalComponent;
import com.nnte.pf_basic.mapper.workdb.merchantAppMenu.PlateformMerchantAppMenu;
import com.nnte.pf_basic.mapper.workdb.merchantAppMenu.PlateformMerchantAppMenuService;
import com.nnte.pf_basic.mapper.workdb.merchantEnter.PlateformMerchantEnter;
import com.nnte.pf_basic.mapper.workdb.merchantEnter.PlateformMerchantEnterService;
import com.nnte.pf_merchant.component.merchant.PlateformMerchanComponent;
import com.nnte.pf_merchant.config.PFMerchantConfig;
import com.nnte.pf_merchant.entertity.MerchantAppMenu;
import com.nnte.pf_merchant.mapper.workdb.merchant.PlateformMerchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(PFMerchantConfig.loggerName)
public class PlateformAppMenuComponent extends BaseComponent {

    @Autowired
    private PlateformMerchantAppMenuService plateformMerchantAppMenuService;
    @Autowired
    private PlateformMerchantEnterService plateformMerchantEnterService;
    @Autowired
    private BasicGlobalComponent basicGlobalComponent;
    @Autowired
    private PlateformMerchanComponent plateformMerchanComponent;
    /**
     * 返回商户菜单列表数据
     * */
    public PageData<MerchantAppMenu> merchantAppMenuList(Map<String, Object> paramMap,
                                                         Integer pageNo,Integer pageSize)throws Exception{
        PageData<PlateformMerchantAppMenu> pageData =plateformMerchantAppMenuService.getListPageData("findModelListByMap", paramMap, pageNo, pageSize);
        PageData<MerchantAppMenu> retPageData = new PageData<>();
        retPageData.setSuccess(true);
        retPageData.setTotal(pageData.getTotal());
        List<MerchantAppMenu> retList = new ArrayList<>();
        if (pageData.getData()!=null && pageData.getData().size()>0){
            Map<String, Object> appNameMap = basicGlobalComponent.getBusiAppNameMap();
            Set<Object> codeSet = BeanUtils.getCollectionFieldValueSet(pageData.getData(), "pmCode");
            Map<Object, PlateformMerchant> merchantMap = plateformMerchanComponent.getMerchantByCodeList(codeSet);
            for(PlateformMerchantAppMenu appMenu:pageData.getData()){
                MerchantAppMenu menu = new MerchantAppMenu();
                BeanUtils.copyFromSrc(appMenu,menu);
                Object name=appNameMap.get(menu.getAppCode());
                menu.setAppName(name!=null?name.toString():null);
                PlateformMerchant merchant = merchantMap.get(menu.getPmCode());
                menu.setPmName(merchant != null ? merchant.getPmName() : null);
                menu.setPmShortName(merchant != null ? merchant.getPmShortName() : null);
                retList.add(menu);
            }
        }
        retPageData.setData(retList);
        return retPageData;

    }
    /**
     * 返回应用入口列表
     * */
    public List<MEnter> merchantAppEnterList(String appCode) throws Exception{
        List<MEnter> retList = new ArrayList<>();
        PlateformMerchantEnter dto = new PlateformMerchantEnter();
        dto.setAppCode(appCode);
        List<PlateformMerchantEnter> list=plateformMerchantEnterService.findModelList(dto);
        if (list!=null && list.size()>0){
            for(PlateformMerchantEnter enter:list){
                MEnter mEnter = new MEnter();
                mEnter.setAppCode(appCode);
                mEnter.setModuleCode(enter.getModuleCode());
                mEnter.setName(enter.getFunName());
                mEnter.setPath(enter.getFunPath());
                mEnter.setRoleRuler(enter.getFunCode());
                retList.add(mEnter);
            }
        }
        return retList;
    }

    /**
     * 返回商户菜单列表数据
     * */
    public PlateformMerchantAppMenu saveMerchantAppMenu(PlateformMerchantAppMenu menu, OperatorInfo oi)throws Exception{
        if (menu.getId()!=null && menu.getId()>0){
            PlateformMerchantAppMenu srcMenu=plateformMerchantAppMenuService.findModelByKey(menu.getId());
            if (srcMenu==null)
                throw new BusiException("没找到指定ID的菜单定义");
            if (!srcMenu.getMenuStatus().equals(0))
                throw new BusiException("菜单定义状态当前不处于可编辑状态");
            PlateformMerchantAppMenu update = new PlateformMerchantAppMenu();
            update.setId(menu.getId());
            update.setMenuContent(menu.getMenuContent());
            update.setUpdateBy(oi.getOperatorName());
            update.setUpdateDate(new Date());
            return plateformMerchantAppMenuService.save(update,true);
        }else {
            if (StringUtils.isEmpty(menu.getMenuContent()))
                throw new BusiException("菜单定义内容不能为空");
            menu.setMenuStatus(0);//默认状态为编辑
            menu.setCreateBy(oi.getOperatorName());
            menu.setCreateDate(new Date());
            return plateformMerchantAppMenuService.save(menu,true);
        }
    }
    /**
     * 确认商户应用菜单
     * */
    public void confirmMerchantAppMenu(Integer id,OperatorInfo oi)throws Exception{
        PlateformMerchantAppMenu srcMenu=plateformMerchantAppMenuService.findModelByKey(id);
        if (srcMenu==null)
            throw new BusiException("没有找到指定的商户菜单");
        if (!srcMenu.getMenuStatus().equals(0))
            throw new BusiException("商户菜单状态不为编辑状态，不能确认");
        PlateformMerchantAppMenu dto = new PlateformMerchantAppMenu();
        if (srcMenu.getMenuType().equals(1)) {
            dto.setAppCode(srcMenu.getAppCode());
            dto.setMenuType(1);
            dto.setMenuStatus(1);
            List<PlateformMerchantAppMenu> list=plateformMerchantAppMenuService.findModelList(dto);
            if (list!=null && list.size()>0)
                throw new BusiException("本应用已经存在确认过的商户全局菜单，不能确认");
        }else if (srcMenu.getMenuType().equals(2)){
            if (srcMenu.getPmCode()==null || srcMenu.getPmCode().length()<=0)
                throw new BusiException("商户菜单没有正确指定商户，不能确认");
            dto.setAppCode(srcMenu.getAppCode());
            dto.setMenuType(2);
            dto.setPmCode(srcMenu.getPmCode());
            dto.setMenuStatus(1);
            List<PlateformMerchantAppMenu> list=plateformMerchantAppMenuService.findModelList(dto);
            if (list!=null && list.size()>0)
                throw new BusiException("本应用在本商户已经存在确认过的商户菜单，不能确认");
        }else
            throw new BusiException("商户菜单类型未指定，不能确认");
        PlateformMerchantAppMenu update = new PlateformMerchantAppMenu();
        update.setId(id);
        update.setMenuStatus(1);
        update.setUpdateBy(oi.getOperatorName());
        update.setUpdateDate(new Date());
        plateformMerchantAppMenuService.updateModel(update);
    }
    /**
     * 取消确认商户应用菜单
     * */
    public void cancelMerchantAppMenu(Integer id,OperatorInfo oi)throws Exception{
        PlateformMerchantAppMenu srcMenu=plateformMerchantAppMenuService.findModelByKey(id);
        if (srcMenu==null)
            throw new BusiException("没有找到指定的商户菜单");
        if (!srcMenu.getMenuStatus().equals(1))
            throw new BusiException("商户菜单状态不为确认状态，不能取消确认");
        PlateformMerchantAppMenu update = new PlateformMerchantAppMenu();
        update.setId(id);
        update.setMenuStatus(0);
        update.setUpdateBy(oi.getOperatorName());
        update.setUpdateDate(new Date());
        plateformMerchantAppMenuService.updateModel(update);
    }

    /**
     * 删除商户应用菜单
     * */
    public void deleteMerchantAppMenu(Integer id,OperatorInfo oi)throws Exception{
        PlateformMerchantAppMenu srcMenu=plateformMerchantAppMenuService.findModelByKey(id);
        if (srcMenu==null)
            throw new BusiException("没有找到指定的商户菜单");
        if (!srcMenu.getMenuStatus().equals(0))
            throw new BusiException("商户菜单状态不为可编辑状态，不能删除");
        PlateformMerchantAppMenu update = new PlateformMerchantAppMenu();
        update.setId(id);
        update.setMenuStatus(-1);
        update.setUpdateBy(oi.getOperatorName());
        update.setUpdateDate(new Date());
        plateformMerchantAppMenuService.updateModel(update);
    }
    /**
     * 取得商户应用菜单：有限取得应用商户菜单，如果没有，则取应用全局菜单
     * */
    public PlateformMerchantAppMenu getValidMerchantAppMenu(String pmCode,String appCode){
        PlateformMerchantAppMenu menu = null;
        PlateformMerchantAppMenu dto=new PlateformMerchantAppMenu();
        dto.setPmCode(pmCode);
        dto.setAppCode(appCode);
        dto.setMenuType(2);
        dto.setMenuStatus(1);
        List<PlateformMerchantAppMenu> list=plateformMerchantAppMenuService.findModelList(dto);
        if (list!=null && list.size()==1)
            menu = list.get(0);
        if (menu==null){
            PlateformMerchantAppMenu dto_g=new PlateformMerchantAppMenu();
            dto_g.setAppCode(appCode);
            dto_g.setMenuType(1);
            dto_g.setMenuStatus(1);
            List<PlateformMerchantAppMenu> list_g=plateformMerchantAppMenuService.findModelList(dto_g);
            if (list_g!=null && list_g.size()==1)
                menu = list_g.get(0);
        }
        return menu;
    }
}
