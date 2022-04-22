<div class="modal" id="applyModifyExpand">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-list-alt tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">申请详情</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <div class="nav-tabs-custom" style="cursor: move;margin-bottom: 42px;">
                    <ul class="nav nav-tabs pull-left ui-sortable-handle">
                        <li class="active"><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyModifyExpandInstance.selectTab('tab_1')">基础信息</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyModifyExpandInstance.selectTab('tab_2')">图文资料</a></li>
                        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="applyModifyExpandInstance.selectTab('tab_3')">法人信息</a></li>
                    </ul>
                </div>
                <div id="tab_1" class="applyModifyExpand-tab">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>行业分类：</strong></td>
                            <td colspan="3">
                                <select id="applyModifyExpand-pmBusiType" class="select2 form-control">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>国家地区：</strong></td>
                            <td>
                                <select id="applyModifyExpand-pmCountry" class="select2 form-control">
                                    <option value="china">中国</option>
                                    <option value="other">其他国家或地区</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="st3_td_label"><strong>省份：</strong></td>
                            <td>
                                <select id="applyModifyExpand-pmProvince" class="select2 form-control">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>城市：</strong></td>
                            <td>
                                <select id="applyModifyExpand-pmCity" class="select2 form-control">
                                </select>
                            </td>
                            <td class="st3_td_label"><strong>县区：</strong></td>
                            <td>
                                <select id="applyModifyExpand-pmArea" class="select2 form-control">
                                </select>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>地址：</strong></td>
                            <td colspan="3">
                                <input type="text" id="applyModifyExpand-pmAddress" class="form-control" value=""
                                       onblur="applyModifyExpandInstance.onAddressBlur();">
                            </td>
                            <td class="st3_td_label"><strong>邮编：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmZipcode" class="form-control" value="">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>地理坐标：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmCoordinate" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>经度：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLongitude" class="form-control" value="" readonly="readonly">
                            </td>
                            <td class="st3_td_label"><strong>纬度：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLatitude" class="form-control" value="" readonly="readonly">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>联系姓名：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLinkName" class="form-control" value="">
                            </td>
                            <td class="st3_td_label"><strong>联系电话：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLinkPhone" class="form-control" value="">
                            </td>
                            <td class="st3_td_label"><strong>客服电话：</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmCsrPhone" class="form-control" value="">
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户简介：</strong></td>
                            <td colspan="5">
                                <textarea rows="4" id="applyModifyExpand-pmIntroduce" class="form-control"></textarea>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="tab_2" class="applyModifyExpand-tab" style="display: none">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong>商户Logo</strong></td>
                            <td colspan="5">
                                <table style="margin: 5px;">
                                    <tr>
                                        <td style="padding: 5px;border: solid;">
                                            <img id="pm_logo_img" src="${envData.staticRoot!''}/images${envData.contextPath!''}/inputpic.png" style="width:50px;height:50px;"/>
                                        </td>
                                        <td style="padding: 5px;">
                                            <button type="button" class="btn btn-default" onclick="applyModifyExpandInstance.uploadLogo();">上传LOGO</button>
                                            <button type="button" class="btn btn-default" onclick="applyModifyExpandInstance.cropperLogo();">裁剪LOGO</button>
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
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmImg('pmPic1')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmImg('pmPic1')">裁剪</button>
                            </td>
                            <td>
                                <strong>图片2</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmImg('pmPic2')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmImg('pmPic2')">裁剪</button>
                            </td>
                            <td>
                                <strong>图片3</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmImg('pmPic3')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmImg('pmPic3')">裁剪</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <img class="img-li dialog-view-img" id="pmPic1" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img class="img-li dialog-view-img" id="pmPic2" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img class="img-li dialog-view-img" id="pmPic3" style="width:250px;height:150px;padding-top: 5px;"/>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="tab_3" class="applyModifyExpand-tab" style="display: none">
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td class="st3_td_label"><strong id="title-pmLegalName">法人姓名</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLegalName" class="form-control" value="">
                            </td>
                            <td class="st3_td_label"><strong id="title-pmLegalIdNum">工商登记号</strong></td>
                            <td>
                                <input type="text" id="applyModifyExpand-pmLegalIdNum" class="form-control" value="">
                            </td>
                        </tr>
                    </table>
                    </p>
                    <p class="well st-well">
                    <table style="width: 100%;">
                        <tr class="st-tr">
                            <td>
                                <strong id="title-pmCertificatePic1">工商登记证1</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmCertificatePic('pmCertificatePic1')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmCertificatePic('pmCertificatePic1')">裁剪</button>
                            </td>
                            <td>
                                <strong id="title-pmCertificatePic2">工商登记证2</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmCertificatePic('pmCertificatePic2')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmCertificatePic('pmCertificatePic2')">裁剪</button>
                            </td>
                            <td>
                                <strong>证件图片3</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmCertificatePic('pmCertificatePic3')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmCertificatePic('pmCertificatePic3')">裁剪</button>
                            </td>
                            <td>
                                <strong>证件图片4</strong>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.uploadPmCertificatePic('pmCertificatePic4')">上传</button>
                                <button class='btn bg-maroon btn-in-row' data-toggle='button' onclick="applyModifyExpandInstance.cropperPmCertificatePic('pmCertificatePic4')">裁剪</button>
                            </td>
                        </tr>
                        <tr class="st-tr">
                            <td>
                                <img class="img-li dialog-view-img" id="pmCertificatePic1" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img class="img-li dialog-view-img" id="pmCertificatePic2" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img class="img-li dialog-view-img" id="pmCertificatePic3" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                            <td>
                                <img class="img-li dialog-view-img" id="pmCertificatePic4" style="width:170px;height:200px;padding-top: 5px;"/>
                            </td>
                        </tr>
                    </table>
                    </p>
                </div>
                <div id="imgViewer" style="display: none">
                    <img id="imageV" src="" alt="图片浏览">
                </div>
            </div>
            <div class="modal-fooder tmf-modal-fooder">
                <table class="tmf-modal-fooder">
                    <tr>
                        <td style="text-align:center">
                            <button type="button" class="btn btn-default" onclick="applyModifyExpandInstance.saveApplyContent();" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="${envData.staticRoot!''}/js/dataLib/tradelib.js?v=1.1.2"></script>
<script src="${envData.staticRoot!''}/js/dataLib/china.city.data.js?v=1.1.2"></script>
<script src="${envData.staticRoot!''}/js/utils/cityselutil.js?v=1.1.3"></script>
<script src="${envData.staticRoot!''}/js/utils/upload/js/messenger.js"></script>
<script src="${envData.staticRoot!''}/js/utils/fileuploadutil.js?v=1.1.7"></script>
<script>
    var ApplyModifyExpand=function (){
        this.applyContent;
        this.pmCompanyPerson=1;
        this.cityselutil = new citySelUtil();
        this.initModal=function(apply){
            if (!apply.applyContent)
                apply.applyContent=new Object();
            this.pmCompanyPerson = apply.pmCompanyPerson;
            this.applyContent=apply.applyContent;
            this.cityselutil.initSelUtil(2,cityData3,$("#applyModifyExpand-pmProvince"),this.applyContent.pmProvince,
                    $("#applyModifyExpand-pmCity"),this.applyContent.pmCity,
                    $("#applyModifyExpand-pmArea"),this.applyContent.pmArea);
            this.mountActions();
            $("#applyModifyExpand-pmBusiType").val((this.applyContent.pmBusiType)?this.applyContent.pmBusiType:"");
            $("#applyModifyExpand-pmCountry").val((this.applyContent.pmCountry)?this.applyContent.pmCountry:"");
            $("#applyModifyExpand-pmAddress").val((this.applyContent.pmAddress)?this.applyContent.pmAddress:"");
            $("#applyModifyExpand-pmZipcode").val((this.applyContent.pmZipcode)?this.applyContent.pmZipcode:"");
            $("#applyModifyExpand-pmCoordinate").val((this.applyContent.pmCoordinate)?this.applyContent.pmCoordinate:"");
            $("#applyModifyExpand-pmLongitude").val((this.applyContent.pmLongitude)?this.applyContent.pmLongitude:"");
            $("#applyModifyExpand-pmLatitude").val((this.applyContent.pmLatitude)?this.applyContent.pmLatitude:"");
            $("#applyModifyExpand-pmLinkName").val((this.applyContent.pmLinkName)?this.applyContent.pmLinkName:"");
            $("#applyModifyExpand-pmLinkPhone").val((this.applyContent.pmLinkPhone)?this.applyContent.pmLinkPhone:"");
            $("#applyModifyExpand-pmCsrPhone").val((this.applyContent.pmCsrPhone)?this.applyContent.pmCsrPhone:"");
            $("#applyModifyExpand-pmIntroduce").val((this.applyContent.pmIntroduce)?this.applyContent.pmIntroduce:"");
            $("#pm_logo_img").attr("src",(this.applyContent.pmLogo)?this.applyContent.pmLogo:"");
            $("#pmPic1").attr("src",(this.applyContent.pmPic1)?this.applyContent.pmPic1:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#pmPic2").attr("src",(this.applyContent.pmPic2)?this.applyContent.pmPic2:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#pmPic3").attr("src",(this.applyContent.pmPic3)?this.applyContent.pmPic3:"${envData.staticRoot!''}/images/custom/addImg.png");
            if (this.pmCompanyPerson==1){
                $("#title-pmLegalName").html("法人姓名");
                $("#title-pmLegalIdNum").html("工商登记号");
                $("#title-pmCertificatePic1").html("工商登记证1");
                $("#title-pmCertificatePic2").html("工商登记证2");
            }else{
                $("#title-pmLegalName").html("个人姓名");
                $("#title-pmLegalIdNum").html("身份证号");
                $("#title-pmCertificatePic1").html("身份证正面");
                $("#title-pmCertificatePic2").html("身份证反面");
            }
            $("#applyModifyExpand-pmLegalName").val((this.applyContent.pmLegalName)?this.applyContent.pmLegalName:"");
            $("#applyModifyExpand-pmLegalIdNum").val((this.applyContent.pmLegalIdNum)?this.applyContent.pmLegalIdNum:"");
            $("#pmCertificatePic1").attr("src",(this.applyContent.pmCertificatePic1)?this.applyContent.pmCertificatePic1:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#pmCertificatePic2").attr("src",(this.applyContent.pmCertificatePic2)?this.applyContent.pmCertificatePic2:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#pmCertificatePic3").attr("src",(this.applyContent.pmCertificatePic3)?this.applyContent.pmCertificatePic3:"${envData.staticRoot!''}/images/custom/addImg.png");
            $("#pmCertificatePic4").attr("src",(this.applyContent.pmCertificatePic4)?this.applyContent.pmCertificatePic4:"${envData.staticRoot!''}/images/custom/addImg.png");
        };
        this.mountActions=function(){
            var tradeLib = new TradeLib();
            var globalCtrl = new GlobalCtrl();
            var optionHtml=globalCtrl.loadLibOption(tradeLib.libMap);
            $("#applyModifyExpand-pmBusiType").html(optionHtml);
        };
        this.saveApplyContent=function(){
            this.applyContent.pmBusiType= $("#applyModifyExpand-pmBusiType").val();
            this.applyContent.pmProvince= this.cityselutil.getProvice();
            this.applyContent.pmCity = this.cityselutil.getCity();
            this.applyContent.pmArea = this.cityselutil.getArea();
            this.applyContent.pmCountry=$("#applyModifyExpand-pmCountry").val();
            this.applyContent.pmAddress=$("#applyModifyExpand-pmAddress").val();
            this.applyContent.pmZipcode=$("#applyModifyExpand-pmZipcode").val();
            this.applyContent.pmCoordinate=$("#applyModifyExpand-pmCoordinate").val();
            this.applyContent.pmLongitude=$("#applyModifyExpand-pmLongitude").val();
            this.applyContent.pmLatitude=$("#applyModifyExpand-pmLatitude").val();
            this.applyContent.pmLinkName=$("#applyModifyExpand-pmLinkName").val();
            this.applyContent.pmLinkPhone=$("#applyModifyExpand-pmLinkPhone").val();
            this.applyContent.pmCsrPhone=$("#applyModifyExpand-pmCsrPhone").val();
            this.applyContent.pmIntroduce=$("#applyModifyExpand-pmIntroduce").val();
            this.applyContent.pmLogo=$("#pm_logo_img").attr("src");
            this.applyContent.pmPic1=$("#pmPic1").attr("src");
            this.applyContent.pmPic2=$("#pmPic2").attr("src");
            this.applyContent.pmPic3=$("#pmPic3").attr("src");
            this.applyContent.pmLegalName=$("#applyModifyExpand-pmLegalName").val();
            this.applyContent.pmLegalIdNum=$("#applyModifyExpand-pmLegalIdNum").val();
            this.applyContent.pmCertificatePic1=$("#pmCertificatePic1").attr("src");
            this.applyContent.pmCertificatePic2=$("#pmCertificatePic2").attr("src");
            this.applyContent.pmCertificatePic3=$("#pmCertificatePic3").attr("src");
            this.applyContent.pmCertificatePic4=$("#pmCertificatePic4").attr("src");
        };
        this.onAddressBlur=function(){
            var p=this.cityselutil.getProvice();
            var c=this.cityselutil.getCity();
            var a=this.cityselutil.getArea();
            if (p==c)
                c="";
            var addr = p+c+a+$("#applyModifyExpand-pmAddress").val().trim();
            var ajgx=new AppJSGlobAjax();
            var url="/main/queryAddressGeocode";
            this.collect_data = {addr:addr};
            if (this.actionType==2)
                this.collect_data.id=this.apply.id;
            var data = JSON.stringify(this.collect_data);
            ajgx.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    $("#applyModifyExpand-pmCoordinate").val(content.geocode);
                    $("#applyModifyExpand-pmLongitude").val(content.lat);
                    $("#applyModifyExpand-pmLatitude").val(content.lng);
                }else
                    msgbox.showMsgBox(content.msg);
            });
        }
        this.selectTab=function(tabId){
            $(".applyModifyExpand-tab").hide();
            $("#"+tabId).show();
        }
        this.uploadLogo=function(){
            var setting = {
                eventId: 'pm_logo_img',
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.jsonLogo
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showFileSelector("image/*");
        }
        this.cropperLogo=function () {
            var setting = {
                eventId: 'pm_logo_img',
                cropperUrl:'${envData.contextPath!''}/merchant/merchantApply/cropperLogo',
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.jsonLogo
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showImageCropper();
        }
        this.jsonLogo=function(logoImg){
            return encodeURIComponent(JSON.stringify({srcUrl:$("#"+logoImg).attr('src')}));
        }
        this.uploadPmImg=function(pmImg){
            var setting = {
                eventId: pmImg,
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.pmImgJson
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showFileSelector("image/*");
        }
        this.cropperPmImg=function (pmImg) {
            var setting = {
                eventId: pmImg,
                cropperUrl:'${envData.contextPath!''}/merchant/merchantApply/cropperPmImg',
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.pmImgJson
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showImageCropper();
        }
        this.pmImgJson=function (pmImg) {
            return encodeURIComponent(JSON.stringify({srcUrl:$("#"+pmImg).attr('src')}));
        }
        this.onImageUpload=function(data){
            if (data.code==0){
                if (data!=undefined && data.fileUrl!=undefined && data.fileUrl!=""){
                    $("#"+data.eventId).attr('src',data.fileUrl);
                }
            }else{
                msgbox.showMsgBox(content.msg);
            }
        }
        this.cropperPmCertificatePic=function (pic) {
            var setting = {
                eventId: pic,
                cropperUrl:'${envData.contextPath!''}/merchant/merchantApply/cropperPmCertificatePic',
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.pmImgJson
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showImageCropper();
        }
        this.uploadPmCertificatePic=function (pic) {
            var setting = {
                eventId: pic,
                upUrl:  '${envData.contextPath!''}/main/uploadImage',
                callback:this.onImageUpload,
                jsonFunction:this.pmImgJson
            }
            var fileUploadUtil = new FileUploadUtil(setting);
            fileUploadUtil.showFileSelector("image/*");
        }
        this.onViewImage=function (image) {
            var imgsrc = $("#"+image).attr("src");
            ImgViewDialog.showDialog(imgsrc,"图片浏览");
        }
    };
    var applyModifyExpandInstance = new ApplyModifyExpand();
    ImgViewDialog.setImgClickView();
</script>