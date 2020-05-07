<#list map.roleList as roleRow>
    <tr data-tt-id='${roleRow.roleCode!''}'>
    <td class="role-code"><img src='${map.envData.staticRoot}/images${map.envData.contextPath}/role.png'><t>${roleRow.roleCode!''}</t></td>
    <td class="role-name">${roleRow.roleName!''}</td>
    <td><input type="text" value="${roleRow.sysroleNameList!''}" readonly=true style="width: 100%"></td>
    <td class="role-state"><#if (roleRow.roleState==1)>可用<#else >不可用</#if></td>
    <td class="role-ope-scrol">
<button class='btn bg-green btn-in-row roleEdit' data-toggle='button' bData='${roleRow.roleCode!''}'>编辑</button>
<button class='btn bg-maroon btn-in-row roleDel' data-toggle='button' bData='${roleRow.roleCode!''}'>删除</button>
    </td>
    </tr>
</#list>
					