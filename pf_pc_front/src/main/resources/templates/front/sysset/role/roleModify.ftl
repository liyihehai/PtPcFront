<div class="modal" id="roleModify">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-th-large tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">角色编辑</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>角色编号：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="roleModify_roleCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>角色名称：</strong></td>
                        <td>
                            <input type="text" id="roleModify_roleName" class="form-control" value="">
                        </td>
                    </tr>
                </table>
                </p>
                <table id="roleModify_sysRoleTable" class="table table-bordered table-hover" style="width: 100%;margin:0;">
                    <tr style="background-color: lightgray;text-align: center;">
                        <td style="width: 45px"><input type="checkbox" id="roleModify_sysRoleAll"></td>
                        <td><strong>系统角色编号：</strong></td>
                        <td style="width: 420px"><strong>系统角色名称：</strong></td>
                    </tr>
                </table>
                <div style="overflow-y:scroll;height:calc(100vh - 320px);width:100%;margin-bottom: 10px;">
                <table class="table table-bordered table-hover" style="width: 100%;margin:0;">
                    <tbody>
                    <#if map.sysRoleList??>
                    <#list map.sysRoleList as sysRole>
                        <tr data-id="${sysRole.key!''}">
                            <td style="width: 45px;text-align: center;""><input type="checkbox" id="${sysRole.key!''}" class="Role_rowCheck"></td>
                            <td><strong>${sysRole.key!''}</strong></td>
                            <td style="width: 404px"><strong>${sysRole.value!''}</strong></td>
                        </tr>
                    </#list>
                    </#if>
                    </tbody>
                </table>
                </div>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>角色状态：</strong></td>
                        <td colspan="5">
                            <div class="radio-list">
                                <label for="roleModify_unValid" class="radio-inline">
                                    <input type="radio" id="roleModify_unValid" name="roleModify_roleState" value="0">不可用
                                </label>
                                <label for="roleModify_valid" class="radio-inline">
                                    <input type="radio" id="roleModify_valid" name="roleModify_roleState" value="1">可用
                                </label>
                            </div>
                        </td>
                    </tr>
                </table>
                </p>
            </div>
            <div class="modal-fooder tmf-modal-fooder">
                <table class="tmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button class="btn bg-maroon" data-toggle="button" onclick="roleModifyInstance.saveRoleModify();">保存角色</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var RoleModify=function (){
        this.role;
        this.actionType;<#-- 操作类型：1新增，2编辑 -->
        this.collect_data;
        this.onRoleChanged=function(updateRole){}
        this.onRoleAdded=function(role){}
        this.getItemFromTable=function(code){}
        this.initModal=function(role,type){
            this.role=role;
            this.actionType=type;
            if (type==2){
                $("#roleModify_roleCode").attr("readonly",true);
                $("#roleModify_roleCode").val(role.roleCode);
                $("#roleModify_roleName").val(role.roleName);
                this.setSysRoleCheck(role.sysroleList);
                $(":radio[name='roleModify_roleState'][value='"+role.roleState+"']").prop('checked',true);
            }else{
                $("#roleModify_roleCode").attr("readonly",false);
                $("#roleModify_roleCode").val("");
                $("#roleModify_roleName").val("");
                $(":radio[name='roleModify_roleState'][value='0']").prop('checked',true);
                $(".Role_rowCheck").prop("checked",false);
            }
            this.mountActions();
        }
        this.setSysRoleCheck=function(sysRoleList){
            $(".Role_rowCheck").prop("checked",false);
            var roles=sysRoleList.split(',');
            if (roles.length>0){
                for(var i=0;i<roles.length;i++){
                    var role=roles[i];
                    if (role.length>=3){
                        role = role.substring(1,role.length-1);
                        $("#"+role).prop("checked",true);
                    }
                }
            }
        }
        this.getSysRoleCheck=function(){
            var rows=$(".Role_rowCheck");
            var retObj = new Object();
            if (rows!=undefined && rows!=null && rows.length>0){
                retObj.sysroleList="";
                retObj.sysroleNameList="";
                var count=0;
                for(var i=0;i<rows.length;i++){
                    if ($(rows[i]).prop("checked")){
                        if (count>0){
                            retObj.sysroleList+=",";
                            retObj.sysroleNameList+=",";
                        }
                        retObj.sysroleList+="["+rows[i].id+"]";
                        retObj.sysroleNameList+=$("[data-id='"+rows[i].id+"'] td:eq(2) strong").html();
                        count++;
                    }
                }
                return retObj;
            }
            return "";
        }
        this.saveRoleModify=function(){
            var ajga=new AppJSGlobAjax();
            var url="/role/saveRoleModify";
            if (this.actionType==1){<#-- 如果操作是增加操作,需要判断当前的代码是否重复 -->
                var roleItem=this.getItemFromTable($("#roleModify_roleCode").val());
                if (roleItem){
                    msgbox.showMsgBox("角色代码重复，不能新增该代码的角色");
                    return;
                }
            }
            var src=this.getSysRoleCheck();
            this.collect_data = {
                roleCode: $("#roleModify_roleCode").val(),
                roleName:$("#roleModify_roleName").val(),
                sysroleList:src.sysroleList,
                sysroleNameList:src.sysroleNameList,
                roleState:$(':radio[name=roleModify_roleState]:checked').val(),
                actionType:this.actionType
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    if (roleModifyInstance.actionType==1){
                        <#-- 如果是新增 -->
                        if (roleModifyInstance.onRoleAdded!=null){
                            roleModifyInstance.onRoleAdded(roleModifyInstance.collect_data);
                        }
                    }else if (roleModifyInstance.actionType==2){
                        <#-- 如果是更改 -->
                        if (roleModifyInstance.onRoleChanged!=null){
                            roleModifyInstance.onRoleChanged(roleModifyInstance.collect_data);
                        }
                    }
                }
                msgbox.showMsgBox(content.msg);
            });
        }
        this.mountActions=function(){
            $("#roleModify_sysRoleAll").off("change").on("change",function(){
                var all_check = $("#roleModify_sysRoleAll").prop("checked");
                if (all_check){
                    $(".Role_rowCheck").prop("checked",true);
                }else{
                    $(".Role_rowCheck").prop("checked",false);
                }
            });
        }
    };
    var roleModifyInstance = new RoleModify();
</script>