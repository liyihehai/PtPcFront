<div class="modal" id="applyModify">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-list-alt tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">商户申请</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户名称：</strong></td>
                            <td>
                                <input type="text" id="applyModify_pmName" class="form-control" value="">
                            </td>
                            <td class="st3_td_label"><strong>商户类型：</strong></td>
                            <td>
                                <select id="applyModify_pmCompanyPerson" class="select2 form-control">
                                    <option value="1">公司</option>
                                    <option value="2">个人</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>验证方式：</strong></td>
                            <td colspan="3">
                                <div class="radio-list">
                                    <label for="applyModify_confirmType_email" class="radio-inline">
                                        <input type="radio" id="applyModify_confirmType_email" name="applyModify_confirmType" value="1">邮件验证
                                    </label>
                                    <label for="applyModify_confirmType_sm" class="radio-inline">
                                        <input type="radio" id="applyModify_confirmType_sm" name="applyModify_confirmType" value="2">短信验证
                                    </label>
                                </div>
                            </td>
                        </tr>
                    </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr apply-by-email">
                        <td class="st3_td_label"><strong>验证Email：</strong></td>
                        <td colspan="3">
                            <input type="text" id="applyModify_applyEmail" class="form-control" value="">
                        </td>
                        <td>
                            <button class="btn bg-maroon" data-toggle="button" onclick="applyModifyInstance.sendApplyVerifyEmail();">发送验证邮件</button>
                        </td>
                    </tr>
                    <tr class="st-tr apply-by-sm">
                        <td class="st3_td_label"><strong>验证电话：</strong></td>
                        <td style="width: 200px;">
                            <input type="text" id="applyModify_applyPhone" class="form-control" value="">
                        </td>
                        <td>
                            <button class="btn bg-maroon" data-toggle="button" onclick="applyModifyInstance.sendApplyVerifySM();">发送验证短信</button>
                        </td>
                        <td class="st3_td_label"><strong>验证号码：</strong></td>
                        <td>
                            <input type="text" id="applyModify_smRandomCode" class="form-control" value="">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>联系Email：</strong></td>
                        <td colspan="3">
                            <input type="text" id="applyModify_pmEmail" class="form-control" value=""/>
                            <input type="hidden" id="applyModify_applyContent" class="form-control" value=""/>
                        </td>
                        <td>
                            <button class="btn bg-maroon" data-toggle="button" onclick="applyModifyInstance.showApplyContent();">设置详情</button>
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>申请方式：</strong></td>
                        <td>
                            <select id="applyModify_applyWays" class="select2 form-control" disabled="disabled">
                                <option value="1">操作员申请</option>
                                <option value="2">网站自助申请</option>
                                <option value="3">APP自助申请</option>
                                <option value="4">业务员申请</option>
                            </select>
                        </td>
                        <td class="st3_td_label"><strong>申请人：</strong></td>
                        <td>
                            <input type="text" id="applyModify_applyerName" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>申请备注：</strong></td>
                        <td>
                            <input type="text" id="applyModify_applyMemo" class="form-control" value="">
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>申请状态：</strong></td>
                        <td>
                            <select id="applyModify_applyState" class="select2 form-control" disabled="disabled">
                                <option value="-1">已删除</option>
                                <option value="0">申请编辑</option>
                                <option value="1">申请通过</option>
                                <option value="2">待审核</option>
                                <option value="3">申请未通过</option>
                                <option value="4">待锁定</option>
                            </select>
                        </td>
                        <td class="st3_td_label"><strong>处理人：</strong></td>
                        <td>
                            <input type="text" id="applyModify_opeName" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>处理时间：</strong></td>
                        <td>
                            <input type="text" id="applyModify_lockTime" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>复核人代码：</strong></td>
                        <td>
                            <input type="text" id="applyModify_checkerCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>复核人姓名：</strong></td>
                        <td>
                            <input type="text" id="applyModify_checkerName" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>复核时间：</strong></td>
                        <td>
                            <input type="text" id="applyModify_checkTime" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
            </div>
            <div class="modal-fooder tmf-modal-fooder">
                <table class="tmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button class="btn bg-maroon" data-toggle="button" onclick="applyModifyInstance.saveApplyModify();">保存申请</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<#include "./applyModifyExpand.ftl">
<script>
    var ApplyModify=function (){
        this.apply;
        this.actionType;<#-- 操作类型：1新增，2编辑 -->
        this.collect_data;
        this.refreshList=function(){};
        this.initModal=function(apply,type){
            this.apply=apply;
            if (this.apply==null)
                this.apply=new Object();
            this.actionType=type;
            if (type==2){
                $("#applyModify_applyPhone").val(apply.applyPhone);
                $("#applyModify_applyEmail").val(apply.applyEmail);
                $("#applyModify_pmName").val(apply.pmName);
                $("#applyModify_pmCompanyPerson").val(apply.pmCompanyPerson);
                $("#applyModify_applyWays").val(apply.applyWays);
                $("#applyModify_applyerName").val(apply.applyerName);
                $("#applyModify_applyMemo").val(apply.applyMemo);
                $("#applyModify_applyState").val(apply.applyState);
                $("#applyModify_opeName").val(apply.opeName);
                $("#applyModify_lockTime").val(apply.lockTime);
                $("#applyModify_checkerCode").val(apply.checkerCode);
                $("#applyModify_checkerName").val(apply.checkerName);
                $("#applyModify_checkTime").val(apply.checkTime);
                $(":radio[name='applyModify_confirmType'][value='"+apply.confirmType+"']").prop('checked',true);
                this.setVerifyInput(apply.confirmType);
                this.apply.applyContent=JSON.parse(apply.applyContent);
                $("#applyModify_pmEmail").val(this.apply.applyContent.pmEmail);
            }else{
                $("#applyModify_applyPhone").val("");
                $("#applyModify_smRandomCode").val("");
                $("#applyModify_applyEmail").val("");
                $("#applyModify_pmName").val("");
                $("#applyModify_pmCompanyPerson").val(1);
                $("#applyModify_applyWays").val(1);
                $("#applyModify_applyerName").val("${map.OperatorInfo.operatorName!''}");
                $("#applyModify_applyMemo").val("");
                $("#applyModify_applyState").val(0);
                $("#applyModify_opeName").val("${map.OperatorInfo.operatorName!''}");
                $("#applyModify_lockTime").val("");
                $("#applyModify_checkerCode").val("");
                $("#applyModify_checkerName").val("");
                $("#applyModify_checkTime").val("");
                $(":radio[name='applyModify_confirmType'][value='1']").prop('checked',true);
                this.setVerifyInput(1);
                this.apply.applyContent={};
                $("#applyModify_pmEmail").val("");
            }
            if ($("#applyModify_applyWays").val()==4)
                $("#applyModify_applyerName").attr("readonly",false);
            else
                $("#applyModify_applyerName").attr("readonly",true);
            this.mountActions();
        };
        this.mountActions=function(){
            $(':radio[name=applyModify_confirmType]').click(function(){
                var checkValue = $(this).val();
                applyModifyInstance.setVerifyInput(checkValue);
            });
        };
        this.setVerifyInput=function(verifyType){
            if (verifyType==1){
                $(".apply-by-email").show();
                $(".apply-by-sm").hide();
            }else{
                $(".apply-by-email").hide();
                $(".apply-by-sm").show();
            }
        };
        this.saveApplyModify=function(){
            var ajgx=new AppJSGlobAjax();
            var url="/merchant/merchantApply/saveApplyModify";
            var confirmType = $(':radio[name=applyModify_confirmType]:checked').val();
            this.apply.applyContent.pmEmail=$("#applyModify_pmEmail").val();
            this.collect_data = {
                confirmType:confirmType,
                applyEmail: $("#applyModify_applyEmail").val(),
                applyPhone:$("#applyModify_applyPhone").val(),
                smRandomCode:$("#applyModify_smRandomCode").val(),
                pmName:$("#applyModify_pmName").val(),
                pmCompanyPerson:$("#applyModify_pmCompanyPerson").val(),
                applyWays:$("#applyModify_applyWays").val(),
                applyerName:$("#applyModify_applyerName").val(),
                applyContent:JSON.stringify((this.apply && this.apply.applyContent)?this.apply.applyContent:""),
                applyMemo:$("#applyModify_applyMemo").val(),
                applyState:$("#applyModify_applyState").val(),
                actionType:this.actionType
            };
            if (this.actionType==2)
                this.collect_data.id=this.apply.id;
            var data = JSON.stringify(this.collect_data);
            ajgx.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    applyModifyInstance.initModal(content.apply,2);
                    if (applyModifyInstance.refreshList!=null){
                        applyModifyInstance.refreshList();
                    }
                }
                msgbox.showMsgBox(content.msg);
            });
        };
        this.sendApplyVerifyEmail=function(){
            if (!this.apply||!this.apply.id){
                msgbox.showMsgBox("商户信息尚未保存");
                return;
            }
            var ajga=new AppJSGlobAjax();
            var url="/merchant/merchantApply/sendApplyVerifyEmail";
            var collect_data = {id: this.apply.id};
            var data = JSON.stringify(collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                msgbox.showMsgBox(content.msg);
            });
        }
        this.sendApplyVerifySM=function () {
            var ajga=new AppJSGlobAjax();
            var url="/merchant/merchantApply/sendApplyVerifySM";
            var collect_data = {applyPhone: $("#applyModify_applyPhone").val()};
            var data = JSON.stringify(collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                msgbox.showMsgBox(content.msg);
            });
        }
        this.showApplyContent=function(){
            this.apply.pmCompanyPerson=$("#applyModify_pmCompanyPerson").val();
            applyModifyExpandInstance.initModal(this.apply);
            $('#applyModifyExpand').modal({backdrop: 'static', keyboard: false});
        }
    };
    var applyModifyInstance = new ApplyModify();
</script>