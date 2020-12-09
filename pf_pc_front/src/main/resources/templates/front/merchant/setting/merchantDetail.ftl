<div class="nav-tabs-custom" style="cursor: move;">
    <ul class="nav nav-tabs pull-left ui-sortable-handle">
        <li class="active"><a href="#" data-toggle="tab" aria-expanded="true" onclick="MerchantSetting.selectTab('detail_tab_1')">商户信息</a></li>
        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="MerchantSetting.selectTab('detail_tab_2')">基础信息</a></li>
        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="MerchantSetting.selectTab('detail_tab_3')">图文资料</a></li>
        <li><a href="#" data-toggle="tab" aria-expanded="true" onclick="MerchantSetting.selectTab('detail_tab_4')">法人信息</a></li>
    </ul>
    <div class="detail_tab_1 merchantDetail-tab">
        <table style="width: 100%;"></table>
        <p class="well st-well">
        <table style="width: 100%;">
            <tr class="st-tr">
                <td class="st3_td_label"><strong>商户名称：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmName" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>商户类型：</strong></td>
                <td>
                    <select id="merchantDetail-pmCompanyPerson" class="select2 form-control" disabled="disabled">
                        <option value="1">公司</option>
                        <option value="2">个人</option>
                    </select>
                </td>
            </tr>
        </table>
        </p>
    </div>
    <div class="detail_tab_2 merchantDetail-tab" style="display: none">
        <table style="width: 100%;"></table>
        <p class="well st-well">
        <table style="width: 100%;">
            <tr class="st-tr">
                <td class="st3_td_label"><strong>行业分类：</strong></td>
                <td colspan="3">
                    <select id="merchantDetail-pmBusiType" class="select2 form-control" disabled="disabled">
                    </select>
                </td>
                <td class="st3_td_label"><strong>国家地区：</strong></td>
                <td>
                    <select id="merchantDetail-pmCountry" class="select2 form-control" disabled="disabled">
                        <option value="china">中国</option>
                        <option value="other">其他国家或地区</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="st3_td_label"><strong>省份：</strong></td>
                <td>
                    <select id="merchantDetail-pmProvince" class="select2 form-control" disabled="disabled">
                    </select>
                </td>
                <td class="st3_td_label"><strong>城市：</strong></td>
                <td>
                    <select id="merchantDetail-pmCity" class="select2 form-control" disabled="disabled">
                    </select>
                </td>
                <td class="st3_td_label"><strong>县区：</strong></td>
                <td>
                    <select id="merchantDetail-pmArea" class="select2 form-control" disabled="disabled">
                    </select>
                </td>
            </tr>
            <tr class="st-tr">
                <td class="st3_td_label"><strong>地址：</strong></td>
                <td colspan="3">
                    <input type="text" id="merchantDetail-pmAddress" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>邮编：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmZipcode" class="form-control" value="" readonly="readonly">
                </td>
            </tr>
            <tr class="st-tr">
                <td class="st3_td_label"><strong>地理坐标：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmCoordinate" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>经度：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmLongitude" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>纬度：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmLatitude" class="form-control" value="" readonly="readonly">
                </td>
            </tr>
            <tr class="st-tr">
                <td class="st3_td_label"><strong>联系姓名：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmLinkName" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>联系电话：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmLinkPhone" class="form-control" value="" readonly="readonly">
                </td>
                <td class="st3_td_label"><strong>客服电话：</strong></td>
                <td>
                    <input type="text" id="merchantDetail-pmCsrPhone" class="form-control" value="" readonly="readonly">
                </td>
            </tr>
            <tr class="st-tr">
                <td class="st3_td_label"><strong>商户简介：</strong></td>
                <td colspan="5">
                    <textarea rows="4" id="merchantDetail-pmIntroduce" class="form-control" readonly="readonly"></textarea>
                </td>
            </tr>
        </table>
        </p>
    </div>
    <div class="detail_tab_3 merchantDetail-tab" style="display: none">
        <table style="width: 100%;"></table>
        <p class="well st-well">
        <table style="width: 100%;">
        <tr class="st-tr">
            <td class="st3_td_label"><strong>商户Logo</strong></td>
            <td colspan="5">
                <table style="margin: 5px;">
                    <tr>
                        <td style="padding: 5px;border: solid;">
                            <img id="merchantDetail-pm_logo_img" style="width:50px;height:50px;"/>
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
                <img id="merchantDetail-pmPic1" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
            </td>
            <td>
                <img id="merchantDetail-pmPic2" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
            </td>
            <td>
                <img id="merchantDetail-pmPic3" class="img-li dialog-view-img" style="width:250px;height:150px;padding-top: 5px;"/>
            </td>
        </tr>
    </table>
        </p>
    </div>
    <div class="detail_tab_4 merchantDetail-tab" style="display: none">
        <table style="width: 100%;"></table>
        <p class="well st-well">
        <table style="width: 100%;">
        <tr class="st-tr">
            <td class="st3_td_label"><strong id="merchantDetail-title-pmLegalName">法人姓名</strong></td>
            <td>
                <input type="text" id="merchantDetail-pmLegalName" class="form-control" value="" readonly="readonly">
            </td>
            <td class="st3_td_label"><strong id="merchantDetail-title-pmLegalIdNum">工商登记号</strong></td>
            <td>
                <input type="text" id="merchantDetail-pmLegalIdNum" class="form-control" value="" readonly="readonly">
            </td>
        </tr>
    </table>
        </p>
        <p class="well st-well">
        <table style="width: 100%;">
        <tr class="st-tr">
            <td>
                <strong id="merchantDetail-title-pmCertificatePic1">工商登记证1</strong>
            </td>
            <td>
                <strong id="merchantDetail-title-pmCertificatePic2">工商登记证2</strong>
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
                <img id="merchantDetail-pmCertificatePic1" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
            </td>
            <td>
                <img id="merchantDetail-pmCertificatePic2" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
            </td>
            <td>
                <img id="merchantDetail-pmCertificatePic3" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
            </td>
            <td>
                <img id="merchantDetail-pmCertificatePic4" class="img-li dialog-view-img" style="width:170px;height:200px;padding-top: 5px;"/>
            </td>
        </tr>
    </table>
        </p>
    </div>
</div>