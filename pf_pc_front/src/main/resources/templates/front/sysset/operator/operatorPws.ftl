<div class="modal" id="operatorPws">
    <div class="modal-dialog lmf-modal-dialog">
        <div class="modal-content lmf-modal-content">
            <div class="modal-header lmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:table-cell; vertical-align:middle">
                <#-- <span class="glyphicon glyphicon-user tmf-head" aria-hidden="true"></span> -->
                    <img src="${envData.staticRoot!''}/images/${envData.contextPath!''}/pwsset.png" style="width:22px;height:22px;"/>
                    <span style="width:100px;font-size:medium;font-weight:600;vertical-align: bottom;">密码设置</span>
                </div>
            </div>
            <div class="modal-body lmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="lmf_td_label"><strong>操作员编号：</strong></td>
                        <td>
                            <input type="text" id="operatorPws_opeCode" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="lmf_td_label"><strong>操作员姓名：</strong></td>
                        <td>
                            <input type="text" id="operatorPws_opeName" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="lmf_td_label"><strong>操作员密码：</strong></td>
                        <td>
                            <input type="password" id="operatorPws_opePassword" class="form-control" value="">
                        </td>
                    </tr>
                </table>
                </p>
            </div>
            <div class="modal-fooder lmf-modal-fooder">
                <table class="lmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button class="btn bg-maroon" data-toggle="button" onclick="operatorPwsInstance.saveOperatorPws();">设置密码</button>
                            <button type="button" id="operatorPws_Close" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="${envData.staticRoot}/js/utils/crypto-js-4.0.0/crypto-js.js"></script>
<script src="${envData.staticRoot}/js/utils/crypto-js-4.0.0/aes.js"></script>
<script src="${envData.staticRoot}/js/secret.js"></script>
<script src="${envData.staticRoot}/js/login.s.js?v=1.18"></script>
<script>
    var OperatorPws=function (){
        this.operator;
        this.collect_data;
        this.initModal=function(operator){
            this.operator=operator;
            $("#operatorPws_opeCode").val(operator.opeCode);
            $("#operatorPws_opeName").val(operator.opeName);
            $("#operatorPws_opePassword").val("");
        };
        this.saveOperatorPws=function(){
            var usercode=$("#operatorPws_opeCode").val();
            var password=$("#operatorPws_opePassword").val();
            loginModle.submitSetPws(usercode,password,this.onSetPwsFinished);
        };
        this.onSetPwsFinished=function(code,msg,username){
            msgbox.showMsgBox(msg);
            if (code==0)
                $("#operatorPws_Close").click();
        }
    };
    var operatorPwsInstance = new OperatorPws();
</script>