package com.nnte.pf_basic.component;

import com.nnte.basebusi.annotation.BusiLogAttr;
import com.nnte.basebusi.base.BaseComponent;
import com.nnte.basebusi.excption.BusiException;
import com.nnte.framework.entity.KeyValue;
import com.nnte.framework.entity.PageData;
import com.nnte.framework.utils.StringUtils;
import com.nnte.pf_basic.config.AppBasicConfig;
import com.nnte.pf_basic.config.DataLibraryConfig;
import com.nnte.pf_basic.mapper.workdb.library.PlateformLibrary;
import com.nnte.pf_basic.mapper.workdb.library.PlateformLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@BusiLogAttr(AppBasicConfig.JarLoggerName)
public class DataLibraryComponent extends BaseComponent {
    @Autowired
    private PlateformLibraryService plateformLibraryService;

    public List<KeyValue> getLibraryTypeList() {
        List<KeyValue> retList = new ArrayList<>();
        for (DataLibraryConfig.LibType type : DataLibraryConfig.getLibTypeList()) {
            retList.add(new KeyValue(type.getTypeCode(), type.getTypeName()));
        }
        return retList;
    }

    /**
     * 按分页取得特定分类的有效分项
     */
    public PageData<PlateformLibrary> getTypeItemListWithPage(String libTypeCode, String modelCode,
                                                              Map<String, Object> paramMap,
                                                              Integer pageNo, Integer pageSize) throws Exception {
        //if (StringUtils.isNotEmpty(libTypeCode))
        paramMap.put("libTypeCode", libTypeCode);
        paramMap.put("itemState", 1);//只有状态为1的才能查出来
        paramMap.put("modelCode", modelCode);
        return plateformLibraryService.getDefaultListPageData(paramMap, pageNo, pageSize);
    }

    /**
     * 保存数据字典分项
     */
    public void saveLibraryItem(Integer id, String libTypeCode,
                                String typeItemCode, String typeItemName,
                                Integer itemSort, String remark, String employeeName,
                                String appCode, String modelName) throws BusiException {
        boolean doNew = false; //确定是否是新增操作
        if (id == null || id <= 0)
            doNew = true;
        if (StringUtils.isEmpty(libTypeCode))
            throw new BusiException(2001, "字典分类代码不能为空");
        if (StringUtils.isEmpty(typeItemCode))
            throw new BusiException("分项代码不能为空");
        //先确定分类是否正确
        DataLibraryConfig.LibType libType = DataLibraryConfig.getLibTypeMap().get(libTypeCode);
        if (libType == null)
            throw new BusiException(2002, "分类代码不能找到具体的分类定义");
        if (doNew) {
            //如果是新增操作，需要判断分项代码是否重复
            PlateformLibrary srcItem = getValidSpectItem(typeItemCode, appCode, modelName);
            if (srcItem != null)
                throw new BusiException(2003, "分类下该编码的分项已经存在");
            PlateformLibrary newItem = new PlateformLibrary();
            newItem.setLibTypeCode(libTypeCode);
            newItem.setLibTypeName(libType.getTypeName());
            newItem.setTypeItemCode(typeItemCode);
            newItem.setTypeItemName(typeItemName);
            newItem.setItemSort(itemSort);
            newItem.setItemState(1);//默认为有效
            newItem.setAppCode(AppBasicConfig.App_Code);//设置应用代码
            newItem.setModelCode(DataLibraryConfig.Lib_ModelName);
            newItem.setCanModify(1);//可以增加的为可编辑项
            newItem.setRemark(remark);
            newItem.setCreateBy(employeeName);
            newItem.setCreateDate(new Date());
            PlateformLibrary addItem = plateformLibraryService.save(newItem, true);
            if (addItem == null || addItem.getId() == null || addItem.getId() <= 0)
                throw new BusiException("新增字典分项时错误");
        } else {
            //如果是编辑操作
            PlateformLibrary srcItem = plateformLibraryService.findModelByKey(id);
            if (srcItem == null)
                throw new BusiException(2004, "id无法确定字典分项");
            if (!srcItem.getLibTypeCode().equals(libTypeCode))
                throw new BusiException(2005, "字典分项与分类不符");
            if (!srcItem.getTypeItemCode().equals(typeItemCode))
                throw new BusiException(2006, "编辑时字典分项代码不能更改");
            if (srcItem.getCanModify() != null && srcItem.getCanModify().equals(0))
                throw new BusiException(2007, "该字典分项不可编辑");
            PlateformLibrary updateItem = new PlateformLibrary();
            updateItem.setId(id);
            updateItem.setTypeItemName(typeItemName);
            updateItem.setItemSort(itemSort);
            updateItem.setRemark(remark);
            updateItem.setUpdateBy(employeeName);
            updateItem.setUpdateDate(new Date());
            int count = plateformLibraryService.updateModel(updateItem);
            if (count != 1)
                throw new BusiException(2008, "更改字典分项时错误");
        }
    }

    /**
     * 删除数据字典分项
     */
    public void delLibraryItem(Integer id, String employeeName) throws BusiException {
        if (id == null || id <= 0)
            throw new BusiException(2009, "待删除字典分项ID未确定");
        PlateformLibrary srcItem = plateformLibraryService.findModelByKey(id);
        if (srcItem == null)
            throw new BusiException(2010, "未找到指定ID的字典分项");
        if (srcItem.getCanModify() != null && srcItem.getCanModify().equals(0))
            throw new BusiException(2011, "该字典分项不可编辑");
        //执行逻辑删除操作
        PlateformLibrary updateItem = new PlateformLibrary();
        updateItem.setId(id);
        updateItem.setItemState(-1);
        updateItem.setUpdateBy(employeeName);
        updateItem.setUpdateDate(new Date());
        int count = plateformLibraryService.updateModel(updateItem);
        if (count != 1)
            throw new BusiException(2012, "删除指定字典分项时错误");
    }

    /**
     * 按分项代码取得有效的分项单项
     */
    public PlateformLibrary getValidSpectItem(String typeItemCode, String appCode, String modelName) {
        PlateformLibrary dto = new PlateformLibrary();
        dto.setTypeItemCode(typeItemCode);
        dto.setItemState(1);
        dto.setAppCode(appCode);
        dto.setModelCode(modelName);
        List<PlateformLibrary> list = plateformLibraryService.findModelList(dto);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    /**
     * 分类代码取得有效的分项集合
     */
    public List<KeyValue> getValidLibItems(String libTypeCode, String appCode, String modelName) {
        PlateformLibrary dto = new PlateformLibrary();
        dto.setLibTypeCode(libTypeCode);
        dto.setSort("item_sort");
        dto.setDir("asc");
        dto.setItemState(1);
        dto.setAppCode(appCode);
        dto.setModelCode(modelName);
        List<PlateformLibrary> list = plateformLibraryService.findModelList(dto);
        List<KeyValue> retList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (PlateformLibrary lib : list) {
                retList.add(new KeyValue(lib.getTypeItemCode(), lib.getTypeItemName()));
            }
        }
        return retList;
    }
    /**
     * 取得指定类型的数据字典的MAP集合
     * */
    public Map<String, Object> getValidLibMap(String libTypeCode) throws Exception {
        DataLibraryConfig.LibType libType = DataLibraryConfig.getLibTypeByCode(libTypeCode);
        if (libType == null)
            throw new BusiException(2013, "数据字典分类项编号不正确");
        List<KeyValue> list=getValidLibItems(libTypeCode,null,null);
        Map<String,Object> retMap = new HashMap<>();
        for(KeyValue kv:list){
            retMap.put(kv.getKey(),kv.getValue());
        }
        return retMap;
    }
}
