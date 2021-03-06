<#include "../../taglib.ftl">
<style>
    .role-code{
        width: 80px;
    }
    .role-name{
        width: 210px;
    }
    .role-state{
        width: 80px;
    }
    .role-ope{
        width: 210px;
    }
    .role-ope-scrol{
        width: 194px;
    }
</style>
<div class="row level0">
	<div class="col-xs-12">
		<div class="box">
			<div class="box-body">
                <div class="nav-tabs-custom" style="cursor: move;">
					<!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-left ui-sortable-handle" style="width: 100%;padding-bottom: 7px;">
                        <li class="pull-right">
                            <button class="btn btn-warning btn-flat" onclick="refreshRoles()">刷新列表</button>
                        </li>
                        <li class="pull-right">
                            <button class="btn btn-danger btn-flat" onclick="newRole()">新增角色</button>
                        </li>
                    </ul>
				</div>
				<table id="roleTreeTableHead" class="table table-bordered table-hover" style="width: 100%;margin:0;">
					<thead>
					<tr style="background-color: lightgray;text-align: center;">
						<th class="role-code">角色代码</th>
						<th class="role-name">角色名称</th>
						<th>系统角色</th>
						<th class="role-state">角色状态</th>
						<th class="role-ope">操作</th>
					</tr>
					</thead>
                </table>
                <div style="overflow-y:scroll;height:calc(100vh - 62px);width:100%">
                <table id="roleTreeTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
					<tbody>
                    ${map.roleRows!''}
					</tbody>
				</table>
                </div>
			</div><!-- /.box-body -->
		</div><!-- /.box -->
	</div><!-- /.col -->
</div><!-- /.row -->
<#include "./roleModify.ftl">
<#include "./roleFunction.ftl">
<script>
    function mountActions() {
        $(".roleEdit").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            roleEdit(data);
        });
        $(".roleDel").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            roleDelete(data);
        });
        $(".roleFunction").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            roleFunction(data);
        });
    }
    function roleEdit(roleCode){
        var ajga=new AppJSGlobAjax();
        var url="/role/queryRole";
        var collect_data = {roleCode: roleCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var role = content.role;
                roleModifyInstance.getItemFromTable = getItemFromTable;
                roleModifyInstance.onRoleChanged = onRoleChanged;
                roleModifyInstance.initModal(role,2);
                $('#roleModify').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function roleFunction(roleCode){
        var ajga=new AppJSGlobAjax();
        var url="/role/queryRoleFunctions";
        var collect_data = {roleCode: roleCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var role = content.role;
                var roleFunctions = content.roleFunctions;
                roleFunctionInstance.initModal(role,roleFunctions);
                $('#roleFunction').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function getItemFromTable(roleCode){
        var code=$("[data-tt-id='"+roleCode+"'] td:eq(0) t").html();
        if (code!=undefined && code!=null && code==roleCode){
            var roleItem = {};
            roleItem.roleCode=roleCode;
            roleItem.roleName=$("[data-tt-id='"+menuCode+"'] td:eq(1)").html();
            return roleItem;
        }
        return null;
    }
    function newRole(){
        roleModifyInstance.initModal(null,1);
        roleModifyInstance.getItemFromTable = getItemFromTable;
        roleModifyInstance.onRoleAdded = onRoleAdded;
        $('#roleModify').modal({backdrop: 'static', keyboard: false});
    }
    function onRoleChanged(roleItem) {
        $("[data-tt-id='"+roleItem.roleCode+"'] td:eq(1)").html(roleItem.roleName);
        $("[data-tt-id='"+roleItem.roleCode+"'] td:eq(2) input").val(roleItem.sysroleNameList);
        $("[data-tt-id='"+roleItem.roleCode+"'] td:eq(3)").html(getStateNameByVal(roleItem.roleState));
    }
    function onRoleAdded(roleItem){
        var roleHtml="<tr data-tt-id='"+roleItem.roleCode+"'>"+
            "<td class=\"role-code\"><img src='${map.envData.staticRoot}/images${map.envData.contextPath}/role.png'><t>"+roleItem.roleCode+"</t></td>\n" +
            "<td class=\"role-name\">"+roleItem.roleName+"</td>\n" +
            "<td class=\"td-control\"><input type=\"text\" class=\"form-control\" value=\""+roleItem.sysroleNameList+"\" readonly=true style=\"width: 100%;\"></td>\n" +
            "<td class=\"role-state\">"+getStateNameByVal(roleItem.roleState)+"</td>\n" +
            "<td class=\"role-ope-scrol\">\n" +
            "<button class='btn bg-green btn-in-row roleEdit' data-toggle='button' bData='"+roleItem.roleCode+"'>编辑</button>\n" +
            "<button class='btn bg-maroon btn-in-row roleDel' data-toggle='button' bData='"+roleItem.roleCode+"'>删除</button>\n" +
            "<button class='btn bg-info btn-in-row roleFunction' data-toggle='button' bData='"+roleItem.roleCode+"'>功能</button>\n" +
            "</td>\n" +
            "</tr>";
        $("#roleTreeTable tbody").append(roleHtml);
        mountActions();
        <#-- 新增成功，要将状态改为更改 -->
        roleModifyInstance.initModal(roleItem,2);
    }
    function getStateNameByVal(stateValue){
        return (stateValue==1)?"可用":"不可用";
    }
    function roleDelete(roleCode){
        msgbox.showComfireBox("确定要删除本角色吗？",roleCode,function (roleCode){
            var ajga=new AppJSGlobAjax();
            var url="/role/deleteRoleByCode";
            this.collect_data = {
                roleCode: roleCode
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    $("[data-tt-id='"+content.roleCode+"']").remove();
                }
                msgbox.showMsgBox(content.msg);
            });
        });
    }
    function refreshRoles() {
        var ajga=new AppJSGlobAjax();
        var url="/role/refreshRoles";
        this.collect_data = {};
        var data = JSON.stringify(this.collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var rows=content.roleRows;
                $("#roleTreeTable tbody").empty().append(rows);
                mountActions();
            }
            msgbox.showMsgBox(content.msg);
        });
    }
    $(document).ready(function(){
        mountActions();
    });
</script>