<div class="modal" id="roleFunction">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-th-list tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">角色功能</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>角色编号：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="roleFunction_roleCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>角色名称：</strong></td>
                        <td>
                            <input type="text" id="roleFunction_roleName" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <table id="roleFunction_functionTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
                    <tr style="background-color: lightgray;text-align: center;">
                        <td style="width:60px"><input type="checkbox" id="roleFunction_functionAll"><label for="roleFunction_functionAll">全部</label></td>
                        <td style="width:45px"><strong>系统</strong></td>
                        <td><strong>功能路径</strong></td>
                        <td style="width: 290px"><strong>功能名称</strong></td>
                    </tr>
                </table>
                <div style="overflow-y:scroll;height:calc(100vh - 248px);width:100%;margin-bottom: 10px;">
                    <table class="table table-bordered table-hover" style="width: 100%;margin:0;">
                        <tbody>
                        <#if map.functionList??>
                            <#list map.functionList as func>
                                <tr data-id="${func.funCode!''}">
                                    <td style="width: 60px;text-align: center;""><input type="checkbox" funCode="${func.funCode!''}" class="roleFunction_rowCheck"></td>
                                    <td style="width: 45px;text-align: center;""><input type="checkbox" sysFunCode="${func.funCode!''}" class="sysRoleFunction_rowCheck" disabled="true"></td>
                                    <td>${func.funPath!''}</td>
                                    <td style="width: 274px">${func.funName!''}</td>
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
                            <button class="btn bg-maroon" data-toggle="button" onclick="roleFunctionInstance.saveRoleFunctions();">保存功能</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var RoleFunction=function (){
        this.role;
        this.collect_data;
        this.initModal=function(role,roleFunctions){
            this.role=role;
            $("#roleFunction_roleCode").val(role.roleCode);
            $("#roleFunction_roleName").val(role.roleName);
            this.setSelectFunctions(roleFunctions);
            this.mountActions();
        }
        this.saveRoleFunctions=function(){
            var ajga=new AppJSGlobAjax();
            var url="/role/saveRoleFunctions";
            this.collect_data = {
                roleCode:$("#roleFunction_roleCode").val(),
                roleName:$("#roleFunction_roleCode").val(),
                functions:this.getSelectFunctions()
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content)
                    msgbox.showMsgBox(content.msg);
            });
        }
        this.getSelectFunctions=function(){
            var rows=$(".roleFunction_rowCheck");
            if (rows!=undefined && rows!=null && rows.length>0){
                var functionList="";
                var count=0;
                for(var i=0;i<rows.length;i++){
                    if ($(rows[i]).prop("checked")){
                        if (count>0){
                            functionList+=",";
                        }
                        functionList+=$(rows[i]).attr("funCode");
                        count++;
                    }
                }
                return functionList;
            }
            return "";
        }
        this.setSelectFunctions=function(roleFunctions){
            $(".roleFunction_rowCheck").prop("checked",false);
            $(".sysRoleFunction_rowCheck").prop("checked",false);
            if (roleFunctions!=undefined && roleFunctions!=null && roleFunctions.length>0){
                for(var i=0;i<roleFunctions.length;i++){
                    if (roleFunctions[i].roleFunction==1)
                        $("[funCode='"+roleFunctions[i].funCode+"']").prop("checked",true);
                    if (roleFunctions[i].sysRoleFunction==1)
                        $("[sysFunCode='"+roleFunctions[i].funCode+"']").prop("checked",true);
                }
            }
        }
        this.mountActions=function(){
            $("#roleFunction_functionAll").prop("checked",false);
            $("#roleFunction_functionAll").off("change").on("change",function(){
                var all_check = $("#roleFunction_functionAll").prop("checked");
                if (all_check){
                    $(".roleFunction_rowCheck").prop("checked",true);
                }else{
                    $(".roleFunction_rowCheck").prop("checked",false);
                }
            });
        }
    };
    var roleFunctionInstance = new RoleFunction();
</script>