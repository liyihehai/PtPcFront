<tr data-tt-id='${opeRow.opeCode!''}'><@compress single_line=true>
    <td class='operator-code'><span class='glyphicon glyphicon-user'></span><t>${opeRow.opeCode!''}</t></td>
    <td class='operator-name'>${opeRow.opeName!''}</td>
    <td class='operator-type'>${opeRow.opeTypeName!''}</td>
    <td class='operator-mobile'>${opeRow.opeMobile!''}</td>
    <td class='operator-state'>${opeRow.opeStateName!''}</td>
    <td class='operator-ope-scrol'>
        <#if (opeRow?? && opeRow.opeState?? && opeRow.opeState!=3)>
            <button class='btn bg-green btn-in-row opeEdit' data-toggle='button' bData='${opeRow.opeCode!''}'>编辑</button>
            <button class='btn bg-maroon btn-in-row opeDel' data-toggle='button' bData='${opeRow.opeCode!''}'>删除</button>
            <button class='btn bg-maroon btn-in-row opePws' data-toggle='button' bData='${opeRow.opeCode!''}'>密码</button>
            <button class='btn bg-maroon btn-in-row opeRole' data-toggle='button' bData='${opeRow.opeCode!''}'>角色</button>
        </#if>
    </td>
</tr></@compress>