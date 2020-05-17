<div class="modal" id="functionModify">
    <div class="modal-dialog tmf-modal-dialog">
        <div class="modal-content tmf-modal-content">
            <div class="modal-header tmf-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div style="display:inline-block;">
                    <span class="glyphicon glyphicon-home tmf-head" aria-hidden="true"></span>
                    <span style="width:100px;font-size:medium;font-weight:600">功能编辑</span>
                </div>
            </div>
            <div class="modal-body tmf-modal-body">
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>父菜单号：</strong></td>
                        <td>
                            <input type="text" id="functionModify_parentMenuCode" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>父菜单名：</strong></td>
                        <td>
                            <input type="text" id="functionModify_parentMenuName" class="form-control" value="" readonly="readonly">
                        </td>
                        <td class="st3_td_label"><strong>父菜单等级：</strong></td>
                        <td>
                            <input type="text" id="functionModify_parentMenuClass" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>功能编号：</strong></td>
                        <td>
                            <input type="text" id="functionModify_funcCode" class="form-control" value="">
                        </td>
                        <td class="st3_td_label"><strong>功能名称：</strong></td>
                        <td>
                            <input type="text" id="functionModify_funcName" class="form-control" value="">
                        </td>
                        <td class="st3_td_label"><strong>菜单等级：</strong></td>
                        <td>
                            <input type="text" id="functionModify_menuClass" class="form-control" value="" readonly="readonly">
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>功能路径：</strong></td>
                        <td colspan="5">
                            <select id="functionModify_funcPath" class="select2 form-control" style="width: 100%"
                                onchange="functionModifyInstance.onPathSelChanged();">
                            </select>
                        </td>
                    </tr>
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>功能描述：</strong></td>
                        <td colspan="5">
                            <input type="text" id="functionModify_funcDesc" class="form-control" value="" readonly=true style="width: 100%">
                        </td>
                    </tr>
                </table>
                </p>
                <p class="well st-well">
                <table style="width: 100%;">
                    <tr class="st-tr">
                        <td class="st3_td_label"><strong>功能状态：</strong></td>
                        <td colspan="5">
                            <div class="radio-list">
                                <label for="functionModify_unValid" class="radio-inline">
                                    <input type="radio" id="functionModify_unValid" name="functionModify_funState" value="0">不可用
                                </label>
                                <label for="functionModify_valid" class="radio-inline">
                                    <input type="radio" id="functionModify_valid" name="functionModify_funState" value="1">可用
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
                            <button class="btn bg-maroon" data-toggle="button" onclick="functionModifyInstance.saveFunctionModify();">保存功能</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var FunctionModify=function (){
        this.func;
        this.parMenu;
        this.actionType;//操作类型：1新增功能，2编辑功能
        this.collect_data;
        this.srcFuncArray;
        this.onFuncChanged=function(updateFunc){};
        this.getMenuItem=function (menuCode) {};
        this.onFuncAdded=function(func){};
        this.initModal=function(func,parMenu,type,srcFuncArray){
            this.func=func;
            this.parMenu=parMenu;
            this.actionType=type;
            this.srcFuncArray=srcFuncArray;
            $("#functionModify_parentMenuCode").val(parMenu.menuCode);
            $("#functionModify_parentMenuName").val(parMenu.menuName);
            $("#functionModify_parentMenuClass").val(parMenu.menuClass);
            var selPath = $("#functionModify_funcPath");
            if (selPath[0].options.length<=0){
                if (srcFuncArray && srcFuncArray.length>0){
                    for (var index=0;index<srcFuncArray.length;index++){
                        var f = srcFuncArray[index];
                        var option = "<option value="+f.path+">"+f.name+"("+f.path+")</option>";
                        selPath.append(option);
                    }
                }
            }
            $("#functionModify_menuClass").val(Number(parMenu.menuClass)+1);
            if (func!=null){
                $("#functionModify_funcCode").val(func.funCode);
                $("#functionModify_funcName").val(func.funName);
                $("#functionModify_funcPath").val(func.funPath);
                $(":radio[name='functionModify_funState'][value='"+func.funState+"']").prop('checked',true);
            }else{
                $("#functionModify_funcCode").val("");
                $("#functionModify_funcName").val("");
                $("#functionModify_funcPath").val("");
                $(":radio[name='functionModify_funState'][value='0']").prop('checked',true);
            }
            if (type!=1)
                $("#functionModify_funcCode").attr("readonly",true);
            else
                $("#functionModify_funcCode").attr("readonly",false);
            this.onPathSelChanged();
        };
        this.saveFunctionModify=function(){
            var ajga=new AppJSGlobAjax();
            var url="/sysset/saveFunctionModify";
            if (this.actionType==1){//如果操作是增加菜单操作,需要判断当前的菜单代码是否重复
                var menuItem=this.getMenuItem($("#functionModify_funcCode").val());
                if (menuItem){
                    msgbox.showMsgBox("菜单代码重复，不能新增该代码的菜单");
                    return;
                }
            }
            this.collect_data = {
                menuCode: $("#functionModify_parentMenuCode").val(),
                funCode:$("#functionModify_funcCode").val(),
                funName:$("#functionModify_funcName").val(),
                funPath:$("#functionModify_funcPath").val(),
                funParam:"",
                funState:$(':radio[name=functionModify_funState]:checked').val()
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    if (functionModifyInstance.actionType==1){
                        //如果是新增
                        if (functionModifyInstance.onFuncAdded!=null){
                            functionModifyInstance.onFuncAdded(functionModifyInstance.collect_data);
                        }
                    }else if (functionModifyInstance.actionType==2){
                        //如果是更改
                        if (functionModifyInstance.onFuncChanged!=null){
                            functionModifyInstance.onFuncChanged(functionModifyInstance.collect_data);
                        }
                    }
                }
                msgbox.showMsgBox(content.msg);
            });
        };
        this.onPathSelChanged=function () {
            var checkIndex=$("#functionModify_funcPath").get(0).selectedIndex;
            if (checkIndex!=undefined && checkIndex!=null && checkIndex>=0
                && this.srcFuncArray!=null && this.srcFuncArray.length>0){
                var func=this.srcFuncArray[checkIndex];
                $("#functionModify_funcDesc").val(func.desc);
                var fname = $("#functionModify_funcName").val();
                if (fname==undefined||fname==null||fname=='')
                    $("#functionModify_funcName").val(func.name);
                return;
            }
            $("#functionModify_funcDesc").val("");
        }
    };
    var functionModifyInstance = new FunctionModify();
</script>