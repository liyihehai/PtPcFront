<style>
    .operatorSelectTable-selected{
        background-color: blue;!important;
    }
</style>
<table id="operatorSelectTableHead" class="table table-bordered table-hover" style="width: 100%;margin:0;">
    <thead>
    <tr style="background-color: lightgray;text-align: center;">
        <th style="width: 60px;text-align: center;">选项</th>
        <th style="width: 210px;text-align: center;">操作员代码</th>
        <th style="width: 210px;text-align: center;">操作员名称</th>
    </tr>
    </thead>
</table>
<div style="overflow-y:scroll;height:calc(100vh - 252px);width:100%">
    <table id="operatorSelectTable" class="table table-bordered" style="width: 100%;margin:0;">
        <tbody>
        <#if map.operatorList??>
            <#list map.operatorList as operator>
                <tr data-id="${operator.opeCode!''}">
                <td style="width: 60px;text-align: center;">
                    <input type="radio" name="operatorSelectTable_radio" value="${operator.opeCode!''}">
                </td>
                <td style="width: 210px;">${operator.opeCode!''}</td>
                <td style="width: 194px;">${operator.opeName!''}</td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>
<script>
    $("#operatorSelectTable tbody").off("mousedown").on("mousedown", "tr", function() {
        $(".operatorSelectTable-selected").not(this).removeClass("operatorSelectTable-selected");
        $(this).toggleClass("operatorSelectTable-selected");
        var opeCode = $(this).attr("data-id");
        $(":radio[name='operatorSelectTable_radio'][value='"+opeCode+"']").prop('checked',true);
    });

    UrlDialog.pushDataFunction(function(){
        var ret = [];
        var opeCode = $(".operatorSelectTable-selected").attr("data-id");
        if (opeCode){
            var name=$("#operatorSelectTable tr[data-id='"+opeCode+"'] td:eq(2)").html();
            ret.push({"opeCode":opeCode,"opeName":name});
        }
        return ret;
    });
</script>