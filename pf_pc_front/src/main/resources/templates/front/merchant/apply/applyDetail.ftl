<div class="modal" id="applyDetail">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-list-alt tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">商户申请详情</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <div class="nav-tabs-custom" style="cursor: move;margin-bottom: 42px;">
                    <ul class="nav nav-tabs pull-left ui-sortable-handle">
                        <li class="active"><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyDetailInstance.selectTab('detail_tab_1')">申请信息</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyDetailInstance.selectTab('detail_tab_2')">基础信息</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyDetailInstance.selectTab('detail_tab_3')">图文资料</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyDetailInstance.selectTab('detail_tab_4')">法人信息</a></li>
                    </ul>
                </div>
                <div id="detail_tab_1" class="applyDetail-tab">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户名称：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_pmName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>商户类型：</strong></td>
                            <td>
                                <select id="applyDetail_pmCompanyPerson" class="select2 form-control" disabled="disabled">
                                    <option value="1">公司</option>
                                    <option value="2">个人</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>验证方式：</strong></td>
                            <td colspan="3">
                                <div class="radio-list">
                                    <label for="applyDetail_confirmType_email" class="radio-inline" disabled="disabled">
                                        <input type="radio" id="applyDetail_confirmType_email" name="applyDetail_confirmType" value="1" disabled="disabled">邮件验证
                                    </label>
                                    <label for="applyDetail_confirmType_sm" class="radio-inline" disabled="disabled">
                                        <input type="radio" id="applyDetail_confirmType_sm" name="applyDetail_confirmType" value="2" disabled="disabled">短信验证
                                    </label>
                                </div>
                            </td>
                        </tr>
                    </table>
                    </p>
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr apply-by-email">
                            <td class="st3_td_label"><strong>联系Email：</strong></td>
                            <td style="width: 400px;">
                                <input type="text" id="applyDetail_pmEmail" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr apply-by-sm">
                            <td class="st3_td_label"><strong>申请电话：</strong></td>
                            <td style="width: 200px;">
                                <input type="text" id="applyDetail_applyPhone" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                    </table>
                    </p>
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>申请方式：</strong></td>
                            <td>
                                <select id="applyDetail_applyWays" class="select2 form-control" disabled="disabled">
                                    <option value="1">操作员申请</option>
                                    <option value="2">网站自助申请</option>
                                    <option value="3">APP自助申请</option>
                                    <option value="4">业务员申请</option>
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>申请人：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_applyerName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>申请备注：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_applyMemo" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>申请状态：</strong></td>
                            <td>
                                <select id="applyDetail_applyState" class="select2 form-control" disabled="disabled">
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
                                <input type="text" id="applyDetail_opeName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>处理时间：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_lockTime" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>复核人代码：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_checkerCode" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>复核人姓名：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_checkerName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>复核时间：</strong></td>
                            <td>
                                <input type="text" id="applyDetail_checkTime" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="detail_tab_2" class="applyDetail-tab" style="display: none">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>行业分类：</strong></td>
                            <td colspan="3">
                                <select id="applyDetail-pmBusiType" class="select2 form-control" disabled="disabled">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>国家地区：</strong></td>
                            <td>
                                <select id="applyDetail-pmCountry" class="select2 form-control" disabled="disabled">
                                    <option value="china">中国</option>
                                    <option value="other">其他国家或地区</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="st3_td_label"><strong>省份：</strong></td>
                            <td>
                                <select id="applyDetail-pmProvince" class="select2 form-control" disabled="disabled">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>城市：</strong></td>
                            <td>
                                <select id="applyDetail-pmCity" class="select2 form-control" disabled="disabled">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>县区：</strong></td>
                            <td>
                                <select id="applyDetail-pmArea" class="select2 form-control" disabled="disabled">
                                </select>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>地址：</strong></td>
                            <td colspan="3">
                                <input type="text" id="applyDetail-pmAddress" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>邮编：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmZipcode" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>地理坐标：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmCoordinate" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>经度：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLongitude" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>纬度：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLatitude" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>联系姓名：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLinkName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>联系电话：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLinkPhone" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>客服电话：</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmCsrPhone" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户简介：</strong></td>
                            <td colspan="5">
                                <textarea rows="4" id="applyDetail-pmIntroduce" class="form-control" readonly="readonly"></textarea>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="detail_tab_3" class="applyDetail-tab" style="display: none">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户Logo</strong></td>
                            <td colspan="5">
                                <table style="margin: 5px;">
                                    <tr>
                                        <td style="padding: 5px;border: solid;">
                                            <img id="applyDetail-pm_logo_img" style="width:50px;height:50px;"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    </p>
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr>
                            <td>
                                <strong>图片1</strong>
                            </td>
                            <td>
                                <strong>图片2</strong>
                            </td>
                            <td>
                                <strong>图片3</strong>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <img id="applyDetail-pmPic1" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img id="applyDetail-pmPic2" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img id="applyDetail-pmPic3" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="detail_tab_4" class="applyDetail-tab" style="display: none">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong id="applyDetail-title-pmLegalName">法人姓名</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLegalName" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong id="applyDetail-title-pmLegalIdNum">工商登记号</strong></td>
                            <td>
                                <input type="text" id="applyDetail-pmLegalIdNum" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                    </table>
                    </p>
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td>
                                <strong id="applyDetail-title-pmCertificatePic1">工商登记证1</strong>
                            </td>
                            <td>
                                <strong id="applyDetail-title-pmCertificatePic2">工商登记证2</strong>
                            </td>
                            <td>
                                <strong>证件图片3</strong>
                            </td>
                            <td>
                                <strong>证件图片4</strong>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td>
                                <img id="applyDetail-pmCertificatePic1" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img id="applyDetail-pmCertificatePic2" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img id="applyDetail-pmCertificatePic3" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img id="applyDetail-pmCertificatePic4" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
            </div>
            <div class="modal-fooder tmf-modal-fooder">
                <table class="tmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var ApplyDetail=function (){
        this.apply;
        this.cityselutil = new citySelUtil();
        this.initModal=function(apply){
            this.apply=apply;
            $("#applyDetail_applyPhone").val(apply.applyPhone);
            $("#applyDetail_pmName").val(apply.pmName);
            $("#applyDetail_pmCompanyPerson").val(apply.pmCompanyPerson);
            $("#applyDetail_applyWays").val(apply.applyWays);
            $("#applyDetail_applyerName").val(apply.applyerName);
            $("#applyDetail_applyMemo").val(apply.applyMemo);
            $("#applyDetail_applyState").val(apply.applyState);
            $("#applyDetail_opeName").val(apply.opeName);
            $("#applyDetail_lockTime").val(globUtil.dateFtt('yyyy-MM-dd hh:mm',new Date(apply.lockTime)));
            $("#applyDetail_checkerCode").val(apply.checkerCode);
            $("#applyDetail_checkerName").val(apply.checkerName);
            $("#applyDetail_checkTime").val(globUtil.dateFtt('yyyy-MM-dd hh:mm',new Date(apply.checkTime)));
            $(":radio[name='applyDetail_confirmType'][value='"+apply.confirmType+"']").prop('checked',true);
            this.setVerifyInput(apply.confirmType);
            if (!apply.applyContent)
                apply.applyContent = new Object();
            else
                apply.applyContent = JSON.parse(apply.applyContent);
            $("#applyDetail_pmEmail").val((apply.applyContent.pmEmail)?apply.applyContent.pmEmail:"");
            this.mountActions();
            this.cityselutil.initSelUtil(2,cityData3,$("#applyDetail-pmProvince"),apply.applyContent.pmProvince,
                $("#applyDetail-pmCity"),apply.applyContent.pmCity,
                $("#applyDetail-pmArea"),apply.applyContent.pmArea);
            $("#applyDetail-pmBusiType").val((apply.applyContent.pmBusiType)?apply.applyContent.pmBusiType:"");
            $("#applyDetail-pmCountry").val((apply.applyContent.pmCountry)?apply.applyContent.pmCountry:"");
            $("#applyDetail-pmAddress").val((apply.applyContent.pmAddress)?apply.applyContent.pmAddress:"");
            $("#applyDetail-pmZipcode").val((apply.applyContent.pmZipcode)?apply.applyContent.pmZipcode:"");
            $("#applyDetail-pmCoordinate").val((apply.applyContent.pmCoordinate)?apply.applyContent.pmCoordinate:"");
            $("#applyDetail-pmLongitude").val((apply.applyContent.pmLongitude)?apply.applyContent.pmLongitude:"");
            $("#applyDetail-pmLatitude").val((apply.applyContent.pmLatitude)?apply.applyContent.pmLatitude:"");
            $("#applyDetail-pmLinkName").val((apply.applyContent.pmLinkName)?apply.applyContent.pmLinkName:"");
            $("#applyDetail-pmLinkPhone").val((apply.applyContent.pmLinkPhone)?apply.applyContent.pmLinkPhone:"");
            $("#applyDetail-pmCsrPhone").val((apply.applyContent.pmCsrPhone)?apply.applyContent.pmCsrPhone:"");
            $("#applyDetail-pmIntroduce").val((apply.applyContent.pmIntroduce)?apply.applyContent.pmIntroduce:"");
            $("#applyDetail-pm_logo_img").attr("src",(apply.applyContent.pmLogo)?apply.applyContent.pmLogo:"");
            $("#applyDetail-pmPic1").attr("src",(apply.applyContent.pmPic1)?apply.applyContent.pmPic1:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#applyDetail-pmPic2").attr("src",(apply.applyContent.pmPic2)?apply.applyContent.pmPic2:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#applyDetail-pmPic3").attr("src",(apply.applyContent.pmPic3)?apply.applyContent.pmPic3:"${envData.staticRoot!''}/images/custom/addImg.png");
            if (apply.pmCompanyPerson==1){
                $("#applyDetail-title-pmLegalName").html("法人姓名");
                $("#applyDetail-title-pmLegalIdNum").html("工商登记号");
                $("#applyDetail-title-pmCertificatePic1").html("工商登记证1");
                $("#applyDetail-title-pmCertificatePic2").html("工商登记证2");
            }else{
                $("#applyDetail-title-pmLegalName").html("个人姓名");
                $("#applyDetail-title-pmLegalIdNum").html("身份证号");
                $("#applyDetail-title-pmCertificatePic1").html("身份证正面");
                $("#applyDetail-title-pmCertificatePic2").html("身份证反面");
            }
            $("#applyDetail-pmLegalName").val((apply.applyContent.pmLegalName)?apply.applyContent.pmLegalName:"");
            $("#applyDetail-pmLegalIdNum").val((apply.applyContent.pmLegalIdNum)?apply.applyContent.pmLegalIdNum:"");
            $("#applyDetail-pmCertificatePic1").attr("src",(apply.applyContent.pmCertificatePic1)?apply.applyContent.pmCertificatePic1:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#applyDetail-pmCertificatePic2").attr("src",(apply.applyContent.pmCertificatePic2)?apply.applyContent.pmCertificatePic2:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#applyDetail-pmCertificatePic3").attr("src",(apply.applyContent.pmCertificatePic3)?apply.applyContent.pmCertificatePic3:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#applyDetail-pmCertificatePic4").attr("src",(apply.applyContent.pmCertificatePic4)?apply.applyContent.pmCertificatePic4:"${envData.staticRoot!''}/images/custom/addImg.png");
        };
        this.mountActions=function(){
            var tradeLib = new TradeLib();
            var globalCtrl = new GlobalCtrl();
            var optionHtml=globalCtrl.loadLibOption(tradeLib.libMap);
            $("#applyDetail-pmBusiType").html(optionHtml);
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
        this.selectTab=function(tabId){
            $(".applyDetail-tab").hide();
            $("#"+tabId).show();
        }
    };
    var applyDetailInstance = new ApplyDetail();
    ImgViewDialog.setImgClickView();
</script>