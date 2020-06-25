<tr data-tt-id='${map.menu.menuCode!''}' <#if (map.menu.menuClass != 1)>data-tt-parent-id='${map.menu.parentMenuCode!''}'</#if>><@compress single_line=true>
    <td class="menu-code">${map.menu.menuCode!''}</td>
    <td class="menu-name"><img src='${map.staticRoot}/images${map.contextPath}/menu.png'><t>${map.menu.menuName!''}</t></td>
    <td>${map.menu.menuClass!''}</td>
    <td class="par-menu-code">${map.menu.parentMenuCode!''}</td>
    <td class="menu-state"><#if (map.menu.menuState==1)>可用<#else >不可用</#if></td>
    <td class="menu-ope-scrol">
        <button class='btn bg-green btn-in-row menuEdit' data-toggle='button' bData='${map.menu.menuCode!''}'>编辑</button>
        <#if (((!(map.menu.functionList)??) || map.menu.functionList?size lt 1) && ((!(map.menu.subMenuList)??) || map.menu.subMenuList?size lt 1))>
            <button class='btn bg-maroon btn-in-row menuDel' data-toggle='button' bData='${map.menu.menuCode!''}'>删除</button>
        </#if>
        <#if ((!(map.menu.functionList)??) || map.menu.functionList?size lt 1)>
            <button class='btn bg-teal btn-in-row addSubMenu' data-toggle='button' bData='${map.menu.menuCode!''}'>增加菜单</button>
        </#if>
        <#if ((!(map.menu.subMenuList)??) || map.menu.subMenuList?size lt 1)>
            <button class='btn bg-purple btn-in-row addFunction' data-toggle='button' bData='${map.menu.menuCode!''}'>增加功能</button>
        </#if>
    </td>
</tr></@compress>
