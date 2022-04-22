<#if (map.roleList??)>
<#list map.roleList as roleRow>
    <tr data-tt-id='${roleRow.roleCode!''}'>
    <td class="role-code"><img src='${map.envData.staticRoot}/images${map.envData.contextPath}/role.png'><t>${roleRow.roleCode!''}</t></td>
    <td class="role-name">${roleRow.roleName!''}</td>
    <td class="td-control"><input type="text" class="form-control" value="${roleRow.sysroleNameList!''}" readonly=true style="width: 100%"></td>
    <td class="role-state"><#if (roleRow.roleState==1)>可用<#else >不可用</#if></td>
    <td class="role-ope-scrol">
<button class='btn bg-green btn-in-row roleEdit' data-toggle='button' bData='${roleRow.roleCode!''}'>编辑</button>
<button class='btn bg-maroon btn-in-row roleDel' data-toggle='button' bData='${roleRow.roleCode!''}'>删除</button>
<button class='btn bg-info btn-in-row roleFunction' data-toggle='button' bData='${roleRow.roleCode!''}'>功能</button>
    </td>
    </tr>
</#list>
</#if>
					