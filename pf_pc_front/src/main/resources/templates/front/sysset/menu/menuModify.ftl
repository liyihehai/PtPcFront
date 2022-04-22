<div class="modal" id="menuModify">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-home tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">菜单编辑</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>父菜单号：</strong></td>
                        <td>
                            <input type="text" id="menuModify_parentMenuCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>父菜单名：</strong></td>
                        <td>
                            <input type="text" id="menuModify_parentMenuName" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>父菜单等级：</strong></td>
                        <td>
                            <input type="text" id="menuModify_parentMenuClass" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>菜单编号：</strong></td>
                        <td>
                            <input type="text" id="menuModify_menuCode" class="form-control" value="">
                        </td>
                        <td class="st3_td_label"><strong>菜单名称：</strong></td>
                        <td>
                            <input type="text" id="menuModify_menuName" class="form-control" value="">
                        </td>
                        <td class="st3_td_label"><strong>菜单等级：</strong></td>
                        <td>
                            <input type="text" id="menuModify_menuClass" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>菜单状态：</strong></td>
                        <td colspan="5">
                            <div class="radio-list">
                                <label for="menuModify_unValid" class="radio-inline">
                                    <input type="radio" id="menuModify_unValid" name="menuModify_menuState" value="0">不可用
                                </label>
                                <label for="menuModify_valid" class="radio-inline">
                                    <input type="radio" id="menuModify_valid" name="menuModify_menuState" value="1">可用
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
                            <button class="btn bg-maroon" data-toggle="button" id="genBatchRoom" onclick="menuModifyInstance.saveMenuModify();">保存菜单</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var MenuModify=function (){
        this.menu;
        this.parMenu;
        this.actionType;<#-- 操作类型：1新增菜单，2编辑菜单 -->
        this.collect_data;
        this.onMenuChanged=function(updateMenu){};
        this.getMenuItem=function (menuCode) {};
        this.onMenuAdded=function(menu){};
        this.initModal=function(menu,parMenu,type){
            this.menu=menu;
            this.parMenu=parMenu;
            this.actionType=type;
            if (parMenu!=null){
                $("#menuModify_parentMenuCode").val(parMenu.menuCode);
                $("#menuModify_parentMenuName").val(parMenu.menuName);
                $("#menuModify_parentMenuClass").val(parMenu.menuClass);
            }else{
                $("#menuModify_parentMenuCode").val("");
                $("#menuModify_parentMenuName").val("");
                $("#menuModify_parentMenuClass").val(1);
            }
            if (menu){
                $("#menuModify_menuCode").val(menu.menuCode);
                $("#menuModify_menuName").val(menu.menuName);
                $("#menuModify_menuClass").val(menu.menuClass);
                $(":radio[name='menuModify_menuState'][value='"+menu.menuState+"']").prop('checked',true);
            }else{
                $("#menuModify_menuCode").val("");
                $("#menuModify_menuName").val("");
                if (parMenu!=null)
                    $("#menuModify_menuClass").val(Number(parMenu.menuClass)+1);
                else
                    $("#menuModify_menuClass").val(1);
                $(":radio[name='menuModify_menuState'][value='0']").prop('checked',true);
            }
            if (menu!=null && menu.menuCode!=null)
                $("#menuModify_menuCode").attr("readonly",true);
            else
                $("#menuModify_menuCode").attr("readonly",false);
        };
        this.saveMenuModify=function(){
            var ajga=new AppJSGlobAjax();
            var url="/sysset/saveMenuModify";
            if (this.actionType==1){<#-- 如果操作是增加菜单操作,需要判断当前的菜单代码是否重复 -->
                var menuItem=this.getMenuItem($("#menuModify_menuCode").val());
                if (menuItem){
                    msgbox.showMsgBox("菜单代码重复，不能新增该代码的菜单");
                    return;
                }
            }
            var mClass = $("#menuModify_menuClass").val();
            var parMCode = $("#menuModify_parentMenuCode").val();
            if (mClass==1)
                parMCode = $("#menuModify_menuCode").val();
            this.collect_data = {
                menuCode: $("#menuModify_menuCode").val(),
                menuName:$("#menuModify_menuName").val(),
                menuClass:mClass,
                parentMenuCode:parMCode,
                menuState:$(':radio[name=menuModify_menuState]:checked').val()
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    if (menuModifyInstance.actionType==1){
                        <#-- 如果是新增 -->
                        if (menuModifyInstance.onMenuAdded!=null){
                            menuModifyInstance.onMenuAdded(menuModifyInstance.collect_data);
                        }
                    }else if (menuModifyInstance.actionType==2){
                        <#-- 如果是更改 -->
                        if (menuModifyInstance.onMenuChanged!=null){
                            menuModifyInstance.onMenuChanged(menuModifyInstance.collect_data);
                        }
                    }
                }
                msgbox.showMsgBox(content.msg);
            });
        }
    };
    var menuModifyInstance = new MenuModify();
</script>