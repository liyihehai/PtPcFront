<p class="well st-well">
<table style="width: 100%;">
    <tr class="st-tr">
        <td><strong>商户名称：</strong></td>
        <td>
            <input type="text" id="applyRefuseDialog-pmName" class="form-control"
                   value="${map.apply.pmName!''}" readonly="readonly">
        </td>
    </tr>
</table>
</p>
<strong>拒绝原因说明：</strong>
<div style="height:calc(100vh - 275px);width:100%">
    <textarea rows="10" id="applyRefuseDialog-refuseReason" class="form-control"></textarea>
</div>
<script>
    UrlDialog.pushDataFunction(function(){
        var applyId = ${map.apply.id!'0'};
        var pmName = "${map.apply.pmName!''}";
        var refuseReason = $("#applyRefuseDialog-refuseReason").val();
        return {"applyId":applyId,"pmName":pmName,"refuseReason":refuseReason};
    });
</script>