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
                        <td class="st3_td_input" colspan="2">
                            <input type="text" class="form-control pull-right"
                                   id="createTimeRange" placeholder="开始时间－结束时间">
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
                <table id="merchantApplyTable" class="table table-bordered table-hover">
                    <thead>
                    <tr>
                        <th>商户名称</th>
                        <th>公司个人</th>
                        <th>申请方式</th>
                        <th>申请状态</th>
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
<!-- 弹出窗结束 -->
<script>
    var globUtil = new AppJSGlobUtil();
    var globAjax=new AppJSGlobAjax();
    var drp=new AppJSGlobDatePicker();
    drp.setDateRangePickerParams($('#createDateRange'),'right',false);
    var optionStat = 0;
    function selectState(option) {
        optionStat = option;
        table.ajax.reload();
    }
    function getPmCompanyPersonName(pmCompanyPerson){
        if (!pmCompanyPerson || pmCompanyPerson<=0 || pmCompanyPerson>2)
            return '未知';
        else if (pmCompanyPerson == 1)
            return '公司';
        else if (pmCompanyPerson == 2)
            return '个人';
    }
    function getApplyWaysName(applyWays) {
        if (!applyWays || applyWays<=0 || applyWays>4)
            return '未知';
        else if (applyWays == 1)
            return '操作员申请';
        else if (applyWays == 2)
            return '网站自助申请';
        else if (applyWays == 3)
            return 'APP自助申请';
        else if (applyWays == 4)
            return '业务员申请';
    }
    function getApplyStateName(applyState){
        if (applyState==undefined || applyState==null || applyState<-1 || applyState>4)
            return '未知';
        else if (applyState == -1)
            return '已删除';
        else if (applyState == 0)
            return '编辑';
        else if (applyState == 1)
            return '已通过';
        else if (applyState == 2)
            return '待审核';
        else if (applyState == 3)
            return '未通过';
        else if (applyState == 4)
            return '待分配';
    }
    var dt=new AppJSGlobDataTable();
    var tableHeight = $(document.body).height() - 215;
    var rowCount = parseInt(tableHeight / 42);
    console.info("tableHeight="+tableHeight+",rowCount="+rowCount);
    var table= $('#merchantApplyTable').DataTable({
        "iDisplayLength": rowCount,
        "sAjaxSource": globAjax.UrlAppendToken("${envData.contextPath!''}/merchant/merchantApply/list"),
        "sServerMethod": "POST",
        "sScrollY" : tableHeight,
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
        "aoColumns": [
            { "mData": "pmName" },
            { "mData": "pmCompanyPerson" },
            { "mData": "applyWays" },
            { "mData": "applyState" },
            { "mData": "opeName" },
            { "mData": "lockTime" },
            { "mData": "checkerName" },
            { "mData": "checkTime" },
            { "mData": "id" }],
        "oLanguage": dt.dataTableNormalSet("${envData.staticRoot!''}"),
        "columnDefs": [
            {
                "targets" : [1],
                "data" : "pmCompanyPerson",
                "render" : function(data,type,full)
                {
                    return getPmCompanyPersonName(data);
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
                "data" : "lockTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',data);
                }
            },{
                "targets" : [7],
                "data" : "checkTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',data);
                }
            },{
                "targets": [8],
                "render": function(data, type, full) {
                    return dt.tableAppendMenuFunction('操作',[
                        {funcName:'applyDetail',funcText:'申请详情',funcId:full.id},
                        {funcName:'applyEdit',funcText:'申请编辑',funcId:full.id}
                    ]);
                }
            }]
    });
    function newOpeApply(){
        applyModifyInstance.initModal(null,1);
        $('#applyModify').modal({backdrop: 'static', keyboard: false});
    }
    function applyDetail(id) {

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

</script>
</body>
</html>