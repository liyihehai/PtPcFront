<div class="modal" id="operatorRole">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-user tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">操作员角色</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员编号：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="operatorRole_opeCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>操作员姓名：</strong></td>
                        <td>
                            <input type="text" id="operatorRole_opeName" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <table id="operatorRole_userRoleTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
                    <tr style="background-color: lightgray;text-align: center;">
                        <td style="width: 45px"><input type="checkbox" id="operatorRole_userRoleAll"></td>
                        <td><strong>用户角色编号：</strong></td>
                        <td style="width: 420px"><strong>用户角色名称：</strong></td>
                    </tr>
                </table>
                <div style="overflow-y:scroll;height:calc(100% - 90px);width:100%;">
                    <table class="table table-bordered table-hover" style="width: 100%;margin:0;">
                        <tbody>
                        <#if map.roleList??>
                            <#list map.roleList as userRole>
                                <tr data-id="${userRole.roleCode!''}">
                            <td style="width: 45px;text-align: center;""><input type="checkbox" roleCode="${userRole.roleCode!''}" class="operatorRole_rowCheck"></td>
                                <td>${userRole.roleCode!''}</td>
                                <td style="width: 404px">${userRole.roleName!''}</td>
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
                            <button class="btn bg-maroon" data-toggle="button" onclick="operatorRoleInstance.saveOperatorRoles();">保存角色</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var OperatorRole=function (){
        this.operator;
        this.collect_data;
        this.initModal=function(operator,opeRoles){
            this.operator=operator;
            $("#operatorRole_opeCode").val(operator.opeCode);
            $("#operatorRole_opeName").val(operator.opeName);
            this.setSelectRoles(opeRoles);
            this.mountActions();
        };
        this.saveOperatorRoles=function(){
            var ajga=new AppJSGlobAjax();
            var url="/operator/saveOperatorRoles";
            this.collect_data = {
                opeCode:$("#operatorRole_opeCode").val(),
                opeName:$("#operatorRole_opeName").val(),
                userRoles:this.getSelectRoles()
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content)
                    msgbox.showMsgBox(content.msg);
            });
        };
        this.getSelectRoles=function(){
            var rows=$(".operatorRole_rowCheck");
            if (rows!=undefined && rows!=null && rows.length>0){
                var roleList="";
                var count=0;
                for(var i=0;i<rows.length;i++){
                    if ($(rows[i]).prop("checked")){
                        if (count>0){
                            roleList+=",";
                        }
                        roleList+="'"+$(rows[i]).attr("roleCode")+"'";
                        count++;
                    }
                }
                return roleList;
            }
            return "";
        };
        this.setSelectRoles=function(opeRoles){
            $(".operatorRole_rowCheck").prop("checked",false);
            if (opeRoles!=undefined && opeRoles!=null && opeRoles.length>0){
                for(var i=0;i<opeRoles.length;i++){
                    $("[roleCode='"+opeRoles[i].key+"']").prop("checked",true);
                }
            }
        };
        this.mountActions=function(){
            $("#operatorRole_userRoleAll").prop("checked",false);
            $("#operatorRole_userRoleAll").off("change").on("change",function(){
                var all_check = $("#operatorRole_userRoleAll").prop("checked");
                if (all_check){
                    $(".operatorRole_rowCheck").prop("checked",true);
                }else{
                    $(".operatorRole_rowCheck").prop("checked",false);
                }
            });
        }
    };
    var operatorRoleInstance = new OperatorRole();
</script>