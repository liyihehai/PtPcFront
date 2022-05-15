package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.PageData;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.mapper.workdb.busiModule.PlateformBusiModule;
import com.nnte.pf_basic.mapper.workdb.busiModule.PlateformBusiModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class PFBusiModuleComponent extends BaseComponent {
    @Autowired
    private PlateformBusiModuleService plateformBusiModuleService;

    public PlateformBusiModule getNotInvalidBMByCode(String code){
        PlateformBusiModule dto = new PlateformBusiModule();
        dto.setModuleCode(code);
        dto.setSort("create_date");
        dto.setDir("desc");
        List<PlateformBusiModule> list=plateformBusiModuleService.findModelList(dto);
        if (list!=null && list.size()>0){
            for(PlateformBusiModule module:list){
                if (!module.getModuleStatus().equals(-1))
                    return module;
            }
        }
        return null;
    }
    public PageData<PlateformBusiModule> getBusinessModuleList(Map<String,Object> appendParam, Integer pageNo, Integer pageSize) throws Exception{
        if (appendParam.get("sort") == null) {
            appendParam.put("sort", "create_date");
            appendParam.put("dir", "desc");
        }
        return plateformBusiModuleService.getListPageData("findModelListByMap", appendParam, pageNo, pageSize);
    }
    /**
     * 保存商业模块基础信息
     * */
    public PlateformBusiModule saveBusinessModule(PlateformBusiModule pbm,String opeName) throws Exception{
        if (pbm.getId()!=null && pbm.getId()>0){
            PlateformBusiModule bm=getNotInvalidBMByCode(pbm.getModuleCode());
            if (bm!=null && !bm.getId().equals(pbm.getId()))
                throw new BusiException("已存在代码为"+pbm.getModuleCode()+"的非作废模块,不能继续保存!");
            if (bm.getModuleStatus()==null || !bm.getModuleStatus().equals(0))
                throw new BusiException("代码为"+pbm.getModuleCode()+"的模块状态不为可编辑,不能继续保存!");
            PlateformBusiModule update = new PlateformBusiModule();
            update.setId(pbm.getId());
            update.setModuleName(pbm.getModuleName());
            update.setModuleDesc(pbm.getModuleDesc());
            update.setCurrentVersion(pbm.getCurrentVersion());
            update.setModuleType(pbm.getModuleType());
            update.setUpdateBy(opeName);
            update.setUpdateDate(new Date());
            return plateformBusiModuleService.save(update,true);
        }else{
            PlateformBusiModule bm=getNotInvalidBMByCode(pbm.getModuleCode());
            if (bm!=null)
                throw new BusiException("已存在代码为"+pbm.getModuleCode()+"的非作废模块,不能继续保存!");
            PlateformBusiModule newModule = new PlateformBusiModule();
            newModule.setModuleCode(pbm.getModuleCode());
            newModule.setModuleName(pbm.getModuleName());
            newModule.setCurrentVersion(pbm.getCurrentVersion());
            newModule.setModuleType(pbm.getModuleType());
            newModule.setModuleDesc(pbm.getModuleDesc());
            newModule.setModuleStatus(0);
            newModule.setCreateBy(opeName);
            newModule.setCreateDate(new Date());
            return plateformBusiModuleService.save(newModule,true);
        }
    }
    /**
     * 设置业务模块状态
     * */
    public void setBusinessModuleStatus(Integer id,Integer status,String opeName) throws Exception{
        PlateformBusiModule bm=plateformBusiModuleService.findModelByKey(id);
        if (bm==null)
            throw new BusiException("没有找打指定Id的模块定义");
        if (status==null)
            throw new BusiException("模块状态未指定");
        if (status.equals(1)){
            if (!(bm.getModuleStatus().equals(0) || bm.getModuleStatus().equals(2)))
                throw new BusiException("模块当前状态不是未上线或将作废，不能设置为已上线");
        }else if (status.equals(2)){
            if (!(bm.getModuleStatus().equals(1)))
                throw new BusiException("模块当前状态不是已上线，不能设置为将作废");
        }else if (status.equals(-1)){
            if (!(bm.getModuleStatus().equals(0) || bm.getModuleStatus().equals(2)))
                throw new BusiException("模块当前状态不是未上线或将作废，不能设置为已作废");
        }else{
            throw new BusiException("指定的模块状态错误");
        }
        PlateformBusiModule update = new PlateformBusiModule();
        update.setId(bm.getId());
        update.setModuleStatus(status);
        update.setUpdateBy(opeName);
        update.setUpdateDate(new Date());
        plateformBusiModuleService.updateModel(update);
    }
}
