<#include "../../taglib.ftl">
<div class="row level0">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>商户名称：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" placeholder="请输入" id="pmName"/>
                        </td>
                        <td class="st3_td_label"><strong>公司个人：</strong></td>
                        <td class="st3_td_input">
                            <select id="pmCompanyPerson" class="select2 form-control">
                                <option value="-1">全部</option>
                                <option value="1">公司</option>
                                <option value="2">个人</option>
                            </select>
                        </td>
                        <td class="st3_td_label"><strong>申请方式：</strong></td>
                        <td class="st3_td_input">
                            <select id="applyWays" class="select2 form-control">
                                <option value="-1">全部</option>
                                <option value="1">操作员申请</option>
                                <option value="2">网站自助申请</option>
                                <option value="3">APP自助申请</option>
                                <option value="4">业务员申请</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>申请人：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" placeholder="请输入" id="applyerName"/>
                        </td>
                        <td class="st3_td_label"><strong>申请时间：</strong></td>
                        <td class="st3_td_input">
                            <div class="input-group" style="width: 100%;">
                                <div class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </div>
                                <input type="text" class="form-control pull-right"
                                       id="createTimeRange" placeholder="开始时间－结束时间">
                            </div>
                        </td>
                        <td class="st3_td_label">
                        </td>
                        <td class="st3_td_input">
                            <button class="btn btn-info btn-flat" onclick="searchQuery()">查询</button>
                            <button class="btn btn-flat" onclick="resetQuery()">重置</button>
                        </td>
                    </tr>
                </table>
                </p>
                <div class="nav-tabs-custom" style="cursor: move;">
                    <!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-left ui-sortable-handle" style="width: 50%;">
                        <li class="active"><a href="#" data-toggle="tab" aria-expanded="true"
                                              onclick="selectState(0)">编辑</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(4)">待分配</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(2)">待审核</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(1)">已通过</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(3)">未通过</a></li>
                    </ul>
                    <ul class="nav nav-tabs pull-left ui-sortable-handle" style="width: 50%;">
                        <li style="float: right;">
                            <button type="button" class="btn bg-blue" onclick="newOpeApply()">操作员申请</button>
                            <button type="button" class="btn bg-maroon" onclick="exportApplyList()">导出</button>
                        </li>
                    </ul>
                </div>
                <table id="merchantApplyTable" class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr style="background-color: lightgrey;">
                        <th>商户名称</th>
                        <th>公司个人</th>
                        <th>申请方式</th>
                        <th>申请状态</th>
                        <th>申请人</th>
                        <th>申请时间</th>
                        <th>处理人</th>
                        <th>锁定时间</th>
                        <th>复核人</th>
                        <th>复核时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <!-- /.box-body -->
        </div>
        <!-- /.box -->
    </div>
    <!-- /.col -->
</div>
<!-- /.row -->
<!-- 弹出窗 项目新增，更改-->
<#include "./applyModify.ftl">
<#include "./applyDetail.ftl">
<script src="${envData.staticRoot!''}/js/front/merchant/apply/apply.js?v=1.1.1"></script>
<!-- 弹出窗结束 -->
<script>
    var globUtil = new AppJSGlobUtil();
    var globAjax=new AppJSGlobAjax();
    var drp=new AppJSGlobDatePicker();
    drp.setDateRangePickerParams($('#createTimeRange'),'left',false);
    var optionStat = 0;
    function selectState(option) {
        optionStat = option;
        merchantApplyTable.ajax.reload();
    }
    var dt=new AppJSGlobDataTable();
    var tableHeight = $(document.body).height() - 215;
    var rowCount = parseInt(tableHeight / 42);
    console.info("tableHeight="+tableHeight+",rowCount="+rowCount);
    var merchantApplyTable= $('#merchantApplyTable').DataTable({
        "iDisplayLength": rowCount,
        "sAjaxSource": globAjax.UrlAppendToken("${envData.contextPath!''}/merchant/merchantApply/list"),
        "sServerMethod": "POST",
        //横向滚动
        sScrollX: "100%",
        //纵向高度 滚动
        sScrollY : tableHeight,
        bScrollCollapse: false,
        "fnServerParams": function ( aoData ) {
            aoData.push(
                { "name":"pmName","value":$('#pmName').val()},
                { "name":"pmCompanyPerson","value":$('#pmCompanyPerson').val()},
                { "name":"applyWays","value":$('#applyWays').val()},
                { "name":"applyerName","value":$('#applyerName').val()},
                { "name":"createTimeRange","value":$('#createTimeRange').val()},
                { "name":"applyState","value":optionStat}
            );
        },
        columns: [
            { "data": "pmName",         "orderable": false ,"width":"170px","searchable": false},
            { "data": "pmCompanyPerson","orderable": false ,"width":"50px","searchable": false},
            { "data": "applyWays",      "orderable": false ,"width":"50px","searchable": false},
            { "data": "applyState",     "orderable": false ,"width":"50px","searchable": false},
            { "data": "applyerName",    "orderable": false ,"width":"50px","searchable": false},
            { "data": "createTime",     "orderable": false ,"width":"100px","searchable": false},
            { "data": "opeName",        "orderable": false ,"width":"50px","searchable": false},
            { "data": "lockTime",       "orderable": false ,"width":"100px","searchable": false},
            { "data": "checkerName",    "orderable": false ,"width":"50px","searchable": false},
            { "data": "checkTime",      "orderable": false ,"width":"100px","searchable": false},
            { "data": "id",             "orderable": false ,"width":"150px","searchable": false}
        ],
        "oLanguage": dt.dataTableNormalSet("${envData.staticRoot!''}"),
        "columnDefs": [
            {
                "targets" : [1],
                "data" : "pmCompanyPerson",
                "render" : function(data,type,full)
                {
                    return GlobalOptionName.getPmCompanyPersonName(data);
                }
            },{
                "targets" : [2],
                "data" : "applyWays",
                "render" : function(data, type,full)
                {
                    return getApplyWaysName(data);
                }
            },{
                "targets" : [3],
                "data" : "applyState",
                "render" : function(data, type,full)
                {
                    return getApplyStateName(data);
                }
            },{
                "targets" : [5],
                "data" : "createTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',globUtil.long2Date(data));
                }
            },{
                "targets" : [7],
                "data" : "lockTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',globUtil.long2Date(data));
                }
            },{
                "targets" : [9],
                "data" : "checkTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',globUtil.long2Date(data));
                }
            },{
                "targets": [10],
                "render": function(data, type, full) {
                    if (full.applyState!=undefined && full.applyState!=null){
                        if (full.applyState==0){
                            return dt.tableAppendOpeButton([
                                {bgColor:'bg-green',bgFunc:'applyEdit',bgData:data,bgTitle:'编辑'},
                                {bgColor:'bg-red',bgFunc:'applyConfirm',bgData:data,bgTitle:'确认'},
                                {bgColor:'bg-yellow',bgFunc:'applyDelete',bgData:data,bgTitle:'删除'}
                            ]);
                        }else{
                            return dt.tableAppendOpeButton([
                                {bgColor:'bg-blue',bgFunc:'applyDetail',bgData:data,bgTitle:'详情'}
                            ]);
                        }
                    }
                    return "";
                }
            }]
    });

    window.onresize = function (){
        GlobalUtil.dataTableSetFixCols(merchantApplyTable,1,1);
    };
    window.onresize();

    function searchQuery(){
        merchantApplyTable.ajax.reload();
    }
    function resetQuery() {
        $("#pmName").val("");
        $("#pmCompanyPerson").val(-1);
        $("#applyWays").val(-1);
        $('#applyerName').val("");
        $('#createTimeRange').val("");
    }
    function newOpeApply(){
        applyModifyInstance.initModal(null,1);
        $('#applyModify').modal({backdrop: 'static', keyboard: false});
    }

    function applyConfirm(id) {
        msgbox.showComfireBox("确定要提交本商户申请吗？提交后本申请将进入审批流程。",id,doApplyConfirm);
    }

    function doApplyConfirm(id) {
        var ajga=new AppJSGlobAjax();
        var url="/merchant/merchantApply/confirmApply";
        var collect_data = {id: id};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                msgbox.showMsgBox(content.msg);
                searchQuery();
            }else
                msgbox.showMsgBox(content.msg);
        });
    }

    function applyDelete(id){
        msgbox.showComfireBox("确定要删除本商户申请吗？删除后本申请将不可恢复。",id,function (id) {
            var ajga=new AppJSGlobAjax();
            var url="/merchant/merchantApply/deleteApply";
            var collect_data = {id: id};
            var data = JSON.stringify(collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    msgbox.showMsgBox(content.msg);
                    searchQuery();
                }else
                    msgbox.showMsgBox(content.msg);
            });
        });
    }

    function applyEdit(id) {
        var ajga=new AppJSGlobAjax();
        var url="/merchant/merchantApply/queryMerchantApply";
        var collect_data = {id: id};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var apply = content.apply;
                applyModifyInstance.initModal(apply,2);
                $('#applyModify').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }

    function applyDetail(id){
        var ajga=new AppJSGlobAjax();
        var url="/merchant/merchantApply/queryMerchantApply";
        var collect_data = {id: id};
        var data = JSON.stringify(collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var apply = content.apply;
                applyDetailInstance.initModal(apply);
                $('#applyDetail').modal({backdrop: 'static', keyboard: false});
            }else
                msgbox.showMsgBox(content.msg);
        });
    }
</script>
</body>
</html>