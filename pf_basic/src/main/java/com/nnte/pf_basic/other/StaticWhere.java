package com.nnte.pf_basic.other;

import com.nnte.basebusi.entity.AppendWhere;
import com.nnte.framework.utils.StringUtils;

import java.util.Map;

public class StaticWhere {
    //商户简称做Like条件
    public static void addPmShortNameLikeToMap(String pmShortName,Map<String, Object> paramMap) throws Exception{
        if (StringUtils.isNotEmpty(pmShortName))
            AppendWhere.andWhereTxtToWhereMap("pm_code in (select pm_code from plateform_merchant where pm_short_name like '%"+pmShortName+"%')",paramMap);
    }
    //应用代码做Like条件
    public static void addAppCodeLikeToMap(String appCode, Map<String, Object> paramMap) throws Exception{
        if (StringUtils.isNotEmpty(appCode))
            AppendWhere.andWhereTxtToWhereMap("app_code in (select type_item_code from plateform_library where lib_type_code='Y001' and type_item_code like '%"+appCode+"%')",paramMap);
    }
    //应用名称做Like条件
    public static void addAppNameLikeToMap(String appName,Map<String, Object> paramMap) throws Exception{
        if (StringUtils.isNotEmpty(appName))
            AppendWhere.andWhereTxtToWhereMap("app_code in (select type_item_code from plateform_library where lib_type_code='Y001' and type_item_name like '%"+appName+"%')",paramMap);
    }
}
