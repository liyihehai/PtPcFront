<#include "../../taglib.ftl">
<style>
    .operator-code{
        width: 150px;
    }
    .operator-name{
        width: 200px;
    }
    .operator-type{

    }
    .operator-mobile{

    }
    .operator-state{
        width: 80px;
    }
    .operator-ope{
        width: 331px;
    }
    .operator-ope-scrol{
        width: 315px;
    }
</style>
<div class="row level0">
	<div class="col-xs-12">
		<div class="box">
			<div class="box-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员编号：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" id="opeCode"/>
                        </td>
                        <td class="st3_td_label"><strong>操作员名称：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" id="opeName"/>
                        </td>
                        <td class="st3_td_label"><strong>操作员类型：</strong></td>
                        <td class="st3_td_input">
                            <select id="opeType" class="select2 form-control">
                                <option value="-1">全部</option>
                                <option value="1">超级管理员</option>
                                <option value="2">普通操作员</option>
                                <option value="3">自动操作员</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>电话号码：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" id="opeMobile"/>
                        </td>
                        <td class="st3_td_label"><strong>操作员状态：</strong></td>
                        <td class="st3_td_input">
                            <select id="opeState" class="select2 form-control">
                                <option value="-1">全部</option>
                                <option value="0">未开通</option>
                                <option value="1" selected>有效</option>
                                <option value="2">暂停</option>
                                <option value="3">已删除</option>
                            </select>
                        </td>
                        <td class="st3_td_label"></td>
                        <td class="st3_td_input">
                            <button class="btn btn-info btn-flat" onclick="refreshOpes()">查询</button>
                            <button class="btn btn-warning btn-flat" onclick="opeAdd()">新增操作员</button>
                        </td>
                    </tr>
                </table>
                </p>
				<table class="table table-bordered table-hover" style="width: 100%;margin:0;">
					<thead>
					<tr style="background-color: lightgray;text-align: center;">
						<th class="operator-code">操作员代码</th>
						<th class="operator-name">操作员姓名</th>
						<th class="operator-type">操作员类型</th>
                        <th class="operator-mobile">操作员手机号码</th>
						<th class="operator-state">状态</th>
						<th class="operator-ope">操作</th>
					</tr>
					</thead>
                </table>
                <div style="overflow-y:scroll;height:calc(100vh - 62px);width:100%">
                <table id="operatorTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
					<tbody>
					</tbody>
				</table>
                </div>
			</div><!-- /.box-body -->
		</div><!-- /.box -->
	</div><!-- /.col -->
</div><!-- /.row -->
<#include "./operatorModify.ftl">
<#include "./operatorPws.ftl">
<#include "./operatorRole.ftl">
<#include "./operatorFunction.ftl">
<script>
    function mountActions() {
        $(".opeEdit").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            opeEdit(data);
        });
        $(".opeDel").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            opeDelete(data);
        });
        $(".opePws").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            opePws(data);
        });
        $(".opeRole").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            opeRole(data);
        });
        $(".opeFunction").off("click").on("click", function () {
            var data = $(event.target).attr("bData");
            opeFunction(data);
        });
    }
    function getItemFromTable(opeCode){
        var code=$("[data-tt-id='"+opeCode+"'] td:eq(0) t").html();
        if (code!=undefined && code!=null && code==opeCode){
            var opeItem = {};
            opeItem.opeCode=roleCode;
            opeItem.opeName=$("[data-tt-id='"+opeCode+"'] td:eq(1)").html();
            return opeItem;
        }
        return null;
    }
    function opeAdd(){
        operatorModifyInstance.initModal(null,1);
        operatorModifyInstance.getItemFromTable = getItemFromTable;
        operatorModifyInstance.onOpeAdded = onOpeAdded;
        $('#operatorModify').modal({backdrop: 'static', keyboard: false});
    }
    function onOpeAdded(opeItem){
        var rowItem = opeItem;
        var opeHtml="<#include "./operow.ftl">";
        $("#operatorTable tbody").append(opeHtml);
        mountActions();
        <#-- 新增成功，要将状态改为更改 -->
        operatorModifyInstance.initModal(opeItem,2);
    }
    function onOpeChanged(opeItem) {
        $("[data-tt-id='"+opeItem.opeCode+"'] td:eq(1)").html(opeItem.opeName);
        $("[data-tt-id='"+opeItem.opeCode+"'] td:eq(2)").html(opeItem.opeTypeName);
        $("[data-tt-id='"+opeItem.opeCode+"'] td:eq(3)").html(opeItem.opeMobile);
        $("[data-tt-id='"+opeItem.opeCode+"'] td:eq(4)").html(opeItem.opeStateName);
    }
    function opeEdit(opeCode){
        var ajga=new AppJSGlobAjax();
        var url="/operator/queryOperator";
        var collect_data = {opeCode: opeCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var operator = content.operator;
                operatorModifyInstance.getItemFromTable = getItemFromTable;
                operatorModifyInstance.onOpeChanged = onOpeChanged;
                operatorModifyInstance.initModal(operator,2);
                $('#operatorModify').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function opePws(opeCode){
        var ajga=new AppJSGlobAjax();
        var url="/operator/queryOperator";
        var collect_data = {opeCode: opeCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var operator = content.operator;
                operatorPwsInstance.initModal(operator);
                $('#operatorPws').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function opeRole(opeCode){
        var ajga=new AppJSGlobAjax();
        var url="/operator/queryOperatorRoles";
        var collect_data = {opeCode: opeCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var operator = content.operator;
                var opeRoles = content.opeRoles;
                operatorRoleInstance.initModal(operator,opeRoles);
                $('#operatorRole').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function opeFunction(opeCode){
        var ajga=new AppJSGlobAjax();
        var url="/operator/queryOperatorFunctions";
        var collect_data = {opeCode: opeCode};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var operator = content.operator;
                var opeFunctions = content.opeFunctions;
                operatorFunctionInstance.initModal(operator,opeFunctions);
                $('#operatorFunction').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    function opeDelete(opeCode){
        msgbox.showComfireBox("确定要删除本操作员吗？",opeCode,function (opeCode){
            var ajga=new AppJSGlobAjax();
            var url="/operator/deleteOpeByCode";
            this.collect_data = {
                opeCode: opeCode
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    refreshOpes();
                }
                msgbox.showMsgBox(content.msg);
            });
        });
    }
    function refreshOpes() {
        var ajga=new AppJSGlobAjax();
        var url="/operator/refreshOpes";
        this.collect_data = {
            opeCode:$("#opeCode").val(),
            opeName:$("#opeName").val(),
            opeType:$("#opeType").val(),
            opeMobile:$("#opeMobile").val(),
            opeState:$("#opeState").val()
        };
        var data = JSON.stringify(this.collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0||content.code==1001){
                var rows=content.opeRows;
                $("#operatorTable tbody").empty().append(rows);
                mountActions();
                if (content.code==1001)
                    msgbox.showMsgBox(content.msg);
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
    $(document).ready(function(){
        refreshOpes();
    });
</script>