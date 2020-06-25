<tr data-tt-id='${map.func.funCode!''}' data-tt-parent-id='${map.func.menuCode!''}'><@compress single_line=true>
    <td class="menu-code">${map.func.funCode!''}</td>
    <td class="menu-name"><img src='${map.staticRoot}/images${map.contextPath}/function.png'><t>${map.func.funName!''}</t></td>
    <td>${map.funcPath!''}</td>
    <td class="par-menu-code">${map.func.menuCode!''}</td>
    <td class="menu-state"><#if (map.func.funState==1)>可用<#else >不可用</#if></td>
    <td class="menu-ope-scrol">
        <button class='btn bg-green btn-in-row menuFunctionEdit' data-toggle='button' bData='${map.func.funCode!''}'>编辑</button>
        <button class='btn bg-maroon btn-in-row menuFunctionDel' data-toggle='button' bData='${map.func.funCode!''}'>删除</button>
    </td>
</tr></@compress>
