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
                        <td class="st3_td_label"><strong>商户简称：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" placeholder="请输入" id="pmShortName"/>
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>商户代码：</strong></td>
                        <td class="st3_td_input">
                            <input type="text" class="form-control" placeholder="请输入" id="pmCode"/>
                        </td>
                        <td class="st3_td_label"><strong>创建时间：</strong></td>
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
                                              onclick="selectState(1)">可服务</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(2)">暂停服务</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(0)">未认证</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(-1)">下架</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true"
                               onclick="selectState(-2)">全部</a></li>
                    </ul>
                    <ul class="nav nav-tabs pull-left ui-sortable-handle" style="width: 50%;">
                        <li style="float: right;">
                            <button type="button" class="btn bg-maroon" onclick="exportMerchantList()">导出</button>
                        </li>
                    </ul>
                </div>
                <table id="merchantTable" class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr style="background-color: lightgrey;">
                        <th>商户代码</th>
                        <th>商户名称</th>
                        <th>商户简称</th>
                        <th>公司个人</th>
                        <th>商户状态</th>
                        <th>创建时间</th>
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
<script src="${envData.staticRoot!''}/js/dataLib/tradelib.js?v=1.1.2"></script>
<script src="${envData.staticRoot!''}/js/dataLib/china.city.data.js?v=1.1.2"></script>
<script src="${envData.staticRoot!''}/js/utils/cityselutil.js?v=1.1.3"></script>
<script src="${envData.staticRoot!''}/js/front/merchant/setting/setting.js?v=1.1.8"></script>
<!-- 弹出窗结束 -->
<script>
    var globUtil = new AppJSGlobUtil();
    var globAjax=new AppJSGlobAjax();
    var drp=new AppJSGlobDatePicker();
    drp.setDateRangePickerParams($('#createTimeRange'),'left',false);
    var optionStat = 1;
    function selectState(option) {
        optionStat = option;
        merchantTable.ajax.reload();
    }
    var tableHeight = $(document.body).height() - 215;
    var rowCount = parseInt(tableHeight / 42);
    var dt=new AppJSGlobDataTable();
    var merchantTable= $('#merchantTable').DataTable({
        "iDisplayLength": rowCount,
        "sAjaxSource": globAjax.UrlAppendToken("${envData.contextPath!''}/merchant/merchantSetting/merchantList"),
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
                { "name":"pmCode","value":$('#pmCode').val()},
                { "name":"pmShortName","value":$('#pmShortName').val()},
                { "name":"createTimeRange","value":$('#createTimeRange').val()},
                { "name":"pmState","value":optionStat}
            );
        },
        columns: [
            { "data": "pmCode",         "orderable": false ,"width":"50px","searchable": false},
            { "data": "pmName",         "orderable": false ,"width":"25%","searchable": false},
            { "data": "pmShortName",    "orderable": false ,"width":"50px","searchable": false},
            { "data": "pmCompanyPerson","orderable": false ,"width":"50px","searchable": false},
            { "data": "pmState",        "orderable": false ,"width":"50px","searchable": false},
            { "data": "createTime",     "orderable": false ,"width":"100px","searchable": false},
            { "data": "id",             "orderable": false ,"width":"200px","searchable": false}
        ],
        "oLanguage": dt.dataTableNormalSet("${envData.staticRoot!''}"),
        "columnDefs": [
            {
                "targets" : [3],
                "data" : "pmCompanyPerson",
                "render" : function(data,type,full)
                {
                    return GlobalOptionName.getPmCompanyPersonName(data);
                }
            },{
                "targets" : [4],
                "data" : "applyState",
                "render" : function(data, type,full)
                {
                    return MerchantSetting.getMerchantStateName(data);
                }
            },{
                "targets" : [5],
                "data" : "createTime",
                "render" : function(data, type,full)
                {
                    return globUtil.dateFtt('yyyy-MM-dd hh:mm',globUtil.long2Date(data));
                }
            },{
                "targets": [6],
                "render": function(data, type, full) {
                    if (full.pmState!=undefined && full.pmState!=null){
                        var buttons = [];
                        buttons.push({bgColor:'bg-blue',bgFunc:'merchantDetail',bgData:data,bgTitle:'详情'});
                        if (full.pmState==0){
                            buttons.push({bgColor:'bg-red',bgFunc:'merchantVerify',bgData:data,bgTitle:'认证'});
                        }else if (full.pmState==1){
                            buttons.push({bgColor:'bg-red',bgFunc:'merchantPause',bgData:data,bgTitle:'暂停'});
                        }else if (full.pmState==2){
                            buttons.push({bgColor:'bg-green',bgFunc:'merchantService',bgData:data,bgTitle:'服务'});
                            buttons.push({bgColor:'bg-red',bgFunc:'merchantOffLine',bgData:data,bgTitle:'下架'});
                        }
                        return dt.tableAppendOpeButton(buttons);
                    }
                    return "";
                }
            }]
    });

    function searchQuery(){
        merchantTable.ajax.reload();
    }
    function resetQuery() {
        $("#pmName").val("");
        $("#pmCompanyPerson").val(-1);
        $("#pmCode").val("");
        $('#pmShortName').val("");
        $('#createTimeRange').val("");
    }

    function merchantOffLine(id){
        msgbox.showComfireBox("确定要下架本商户信息吗？下架后本商户将不可恢复。",id,function (id) {
            var ajga=new AppJSGlobAjax();
            var url="/merchant/merchantSetting/merchantOffLine";
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

    function merchantDetail(id){
        var collect_data = {id: id,
            title:"商户详情",
            titleLogo:"glyphicon-list-alt",
            closeable:true,
            winSize:DialogGlobal.nomalSize(),
            instanceName:"MerchantSetting"
        };
        UrlDialog.staticContent(MerchantSetting.getTemplatePath(),collect_data,null);
    //    UrlDialog.queryContent("/merchant/merchantSetting/merchantDetailDialog",collect_data,null);
    }
</script>
</body>
</html>