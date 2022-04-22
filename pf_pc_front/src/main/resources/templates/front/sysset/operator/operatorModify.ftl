<div class="modal" id="operatorModify">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-user tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">操作员编辑</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员编号：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="operatorModify_opeCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>操作员姓名：</strong></td>
                        <td>
                            <input type="text" id="operatorModify_opeName" class="form-control" value="">
                        </td>
                        <td class="st3_td_label"><strong>操作员类型：</strong></td>
                        <td>
                            <select id="operatorModify_opeType" class="select2 form-control">
                                <option value="1">超级管理员</option>
                                <option value="2">普通操作员</option>
                                <option value="3">自动操作员</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员电话：</strong></td>
                        <td colspan="5">
                            <input type="text" id="operatorModify_opeMobile" class="form-control" value="">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>操作员状态：</strong></td>
                        <td colspan="5">
                            <div class="radio-list">
                                <label for="operatorModify_unValid" class="radio-inline">
                                    <input type="radio" id="operatorModify_unValid" name="operatorModify_opeState" value="0">无效
                                </label>
                                <label for="operatorModify_valid" class="radio-inline">
                                    <input type="radio" id="operatorModify_valid" name="operatorModify_opeState" value="1">有效
                                </label>
                                <label for="operatorModify_pause" class="radio-inline">
                                    <input type="radio" id="operatorModify_pause" name="operatorModify_opeState" value="2">暂停
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
                            <button class="btn bg-maroon" data-toggle="button" onclick="operatorModifyInstance.saveOperatorModify();">保存操作员</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var OperatorModify=function (){
        this.operator;
        this.actionType;<#-- 操作类型：1新增，2编辑 -->
        this.collect_data;
        this.onOpeChanged=function(updateRole){};
        this.onOpeAdded=function(role){};
        this.getItemFromTable=function(code){return null;};
        this.initModal=function(operator,type){
            this.operator=operator;
            this.actionType=type;
            if (type==2){
                $("#operatorModify_opeCode").attr("readonly",true);
                $("#operatorModify_opeCode").val(operator.opeCode);
                $("#operatorModify_opeName").val(operator.opeName);
                $("#operatorModify_opeType").val(operator.opeType);
                $("#operatorModify_opeMobile").val(operator.opeMobile);
                $(":radio[name='operatorModify_opeState'][value='"+operator.opeState+"']").prop('checked',true);
            }else{
                $("#operatorModify_opeCode").attr("readonly",false);
                $("#operatorModify_opeCode").val("");
                $("#operatorModify_opeName").val("");
                $("#operatorModify_opeType").val("1");
                $("#operatorModify_opeMobile").val("");
                $(":radio[name='operatorModify_opeState'][value='0']").prop('checked',true);
            }
        };
        this.saveOperatorModify=function(){
            var ajga=new AppJSGlobAjax();
            var url="/operator/saveOperatorModify";
            if (this.actionType==1){<#-- 如果操作是增加操作,需要判断当前的代码是否重复 -->
                var opeItem=this.getItemFromTable($("#operatorModify_opeCode").val());
                if (opeItem){
                    msgbox.showMsgBox("操作员代码重复，不能新增该代码的操作员");
                    return;
                }
            }
            var state = $(':radio[name=operatorModify_opeState]:checked').val();
            this.collect_data = {
                opeCode: $("#operatorModify_opeCode").val(),
                opeName:$("#operatorModify_opeName").val(),
                opeType:$("#operatorModify_opeType").val(),
                opeTypeName:$("#operatorModify_opeType").find("option:selected").text(),
                opeMobile:$("#operatorModify_opeMobile").val(),
                opeState:state,
                opeStateName:this.getStateName(state),
                actionType:this.actionType
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    if (operatorModifyInstance.actionType==1){
                        <#-- 如果是新增 -->
                        if (operatorModifyInstance.onOpeAdded!=null){
                            operatorModifyInstance.onOpeAdded(operatorModifyInstance.collect_data);
                        }
                    }else if (operatorModifyInstance.actionType==2){
                        <#-- 如果是更改 -->
                        if (operatorModifyInstance.onOpeChanged!=null){
                            operatorModifyInstance.onOpeChanged(operatorModifyInstance.collect_data);
                        }
                    }
                }
                msgbox.showMsgBox(content.msg);
            });
        };
        this.getStateName=function (state) {
            if (state==0) return "无效";
            if (state==1) return "有效";
            if (state==2) return "暂停";
            if (state==3) return "已删除";
            return "未知";
        }
    };
    var operatorModifyInstance = new OperatorModify();
</script>