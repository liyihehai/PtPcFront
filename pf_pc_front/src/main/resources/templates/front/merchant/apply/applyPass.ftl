<p class="well st-well">
<table style="width: 100%;">
    <tr class="st-tr">
        <td><strong>商户名称：</strong></td>
        <td colspan="3">
            <input type="text" id="applyRefuseDialog-pmName" class="form-control"
                   value="${map.apply.pmName!''}" readonly="readonly">
        </td>
    </tr>
    <tr class="st-tr">
        <td><strong>商户简称：</strong></td>
        <td>
            <input type="text" id="applyRefuseDialog-pmShortName" class="form-control"
                   value="">
        </td>
        <td><strong>商户代码：</strong></td>
        <td>
            <input type="text" id="applyRefuseDialog-pmCode" class="form-control"
                   value="${map.apply.pmCode!''}">
        </td>
    </tr>
</table>
</p>
<strong>审核说明：</strong>
<div style="height:calc(100vh - 330px);width:100%">
    <textarea rows="8" id="applyRefuseDialog-checkDesc" class="form-control">审核通过</textarea>
</div>
<script>
    UrlDialog.pushDataFunction(function(){
        var applyId = ${map.apply.id!'0'};
        var pmName = "${map.apply.pmName!''}";
        var pmCode = $("#applyRefuseDialog-pmCode").val();
        var pmShortName = $("#applyRefuseDialog-pmShortName").val();
        var checkDesc = $("#applyRefuseDialog-checkDesc").val();
        return {"applyId":applyId,"pmName":pmName,"pmCode":pmCode,
            "pmShortName":pmShortName,"checkDesc":checkDesc};
    });
</script>