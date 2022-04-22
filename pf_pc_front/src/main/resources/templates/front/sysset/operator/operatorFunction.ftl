<div class="modal" id="operatorFunction">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-th-list tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">操作员功能</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员编号：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="operatorFunction_opeCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>操作员姓名：</strong></td>
                        <td>
                            <input type="text" id="operatorFunction_opeName" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <table id="operatorFunction_functionTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
                    <tr style="background-color: lightgray;text-align: center;">
                        <td style="width:60px"><input type="checkbox" id="operatorFunction_functionAll"><label for="operatorFunction_functionAll">全部</label></td>
                        <td style="width:45px"><strong>角色</strong></td>
                        <td style="width:45px"><strong>系统</strong></td>
                        <td><strong>功能路径</strong></td>
                        <td style="width: 320px"><strong>功能名称</strong></td>
                    </tr>
                </table>
                <div style="overflow-y:scroll;height:calc(100% - 90px);width:100%;">
                    <table class="table table-bordered table-hover" style="width: 100%;margin:0;">
                        <tbody>
                        <#if map.functionList??>
                            <#list map.functionList as func>
                                <tr data-id="${func.funCode!''}">
                                    <td style="width: 60px;text-align: center;""><input type="checkbox" funCode="${func.funCode!''}" class="operatorFunction_rowCheck"></td>
                                    <td style="width: 45px;text-align: center;""><input type="checkbox" roleFunCode="${func.funCode!''}" class="roleFunction_rowCheck" disabled="true"></td>
                                    <td style="width: 45px;text-align: center;""><input type="checkbox" sysFunCode="${func.funCode!''}" class="sysRoleFunction_rowCheck" disabled="true"></td>
                                    <td>${func.funPath!''}</td>
                                    <td style="width: 304px">${func.funName!''}</td>
                                </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-fooder tmf-modal-fooder">
                <table class="tmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button class="btn bg-maroon" data-toggle="button" onclick="operatorFunctionInstance.saveOperatorFunctions();">保存功能</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var OperatorFunction=function (){
        this.operator;
        this.collect_data;
        this.initModal=function(operator,opeFunctions){
            this.operator=operator;
            $("#operatorFunction_opeCode").val(operator.opeCode);
            $("#operatorFunction_opeName").val(operator.opeName);
            this.setSelectFunctions(opeFunctions);
            this.mountActions();
        };
        this.saveOperatorFunctions=function(){
            var ajga=new AppJSGlobAjax();
            var url="/operator/saveOperatorFunctions";
            this.collect_data = {
                opeCode:$("#operatorFunction_opeCode").val(),
                opeName:$("#operatorFunction_opeName").val(),
                functions:this.getSelectFunctions()
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content)
                    msgbox.showMsgBox(content.msg);
            });
        };
        this.getSelectFunctions=function(){
            var rows=$(".operatorFunction_rowCheck");
            if (rows!=undefined && rows!=null && rows.length>0){
                var functionList="";
                var count=0;
                for(var i=0;i<rows.length;i++){
                    if ($(rows[i]).prop("checked")){
                        if (count>0){
                            functionList+=",";
                        }
                        functionList+="'"+$(rows[i]).attr("funCode")+"'";
                        count++;
                    }
                }
                return functionList;
            }
            return "";
        };
        this.setSelectFunctions=function(opeFunctions){
            $(".operatorFunction_rowCheck").prop("checked",false);
            $(".roleFunction_rowCheck").prop("checked",false);
            $(".sysRoleFunction_rowCheck").prop("checked",false);
            if (opeFunctions!=undefined && opeFunctions!=null && opeFunctions.length>0){
                for(var i=0;i<opeFunctions.length;i++){
                    if (opeFunctions[i].opeFunction==1)
                        $("[funCode='"+opeFunctions[i].funCode+"']").prop("checked",true);
                    if (opeFunctions[i].roleFunction==1)
                        $("[roleFunCode='"+opeFunctions[i].funCode+"']").prop("checked",true);
                    if (opeFunctions[i].sysRoleFunction==1)
                        $("[sysFunCode='"+opeFunctions[i].funCode+"']").prop("checked",true);
                }
            }
        };
        this.mountActions=function(){
            $("#operatorFunction_functionAll").prop("checked",false);
            $("#operatorFunction_functionAll").off("change").on("change",function(){
                var all_check = $("#operatorFunction_functionAll").prop("checked");
                if (all_check){
                    $(".operatorFunction_rowCheck").prop("checked",true);
                }else{
                    $(".operatorFunction_rowCheck").prop("checked",false);
                }
            });
        }
    };
    var operatorFunctionInstance = new OperatorFunction();
</script>