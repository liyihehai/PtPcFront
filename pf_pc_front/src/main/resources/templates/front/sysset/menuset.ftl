<#include "../taglib.ftl">
<link rel="stylesheet" type="text/css" href="${envData.staticRoot}/resources/plugins/jquery-treetable/css/jquery.treetable.css"/>
<link rel="stylesheet" type="text/css" href="${envData.staticRoot}/resources/plugins/jquery-treetable/css/jquery.treetable.theme.default.css" />
<style>
    .menu-code{
        width: 80px;
    }
    .menu-name{
        width: 210px;
    }
    .menu-class{
        width: 300px;
    }
    .par-menu-code{
        width: 60px;
    }
    .menu-state{
        width: 80px;
    }
    .menu-ope{
        width: 312px;
    }
    .menu-ope-scrol{
        width: 295px;
    }
</style>
<div class="row level0">
	<div class="col-xs-12">
		<div class="box">
			<div class="box-body">
                <div class="nav-tabs-custom" style="cursor: move;">
					<!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-left ui-sortable-handle" style="width: 100%;padding-bottom: 7px;">
                        <li class="pull-left">
                            <button class="btn btn-warning btn-flat" onclick="expandMenus()">展开菜单</button>
                        </li>
                        <li class="pull-left">
                            <button class="btn btn-warning btn-flat" onclick="collapseMenus()">收起菜单</button>
                        </li>
                        <li class="pull-left">
                            <button class="btn btn-warning btn-flat" onclick="refreshMenus()">刷新菜单</button>
                        </li>
                        <li class="pull-right">
                            <button class="btn btn-danger btn-flat" onclick="newTopMenu()">新增菜单</button>
                        </li>
                    </ul>
				</div>
				<table id="menuTreeTableHead" class="table table-striped table-bordered table-hover">
					<thead>
					<tr role="row">
						<th class="menu-code">菜单代码</th>
						<th class="menu-name">菜单名称</th>
						<th>菜单等级/路径</th>
						<th class="par-menu-code">父菜单</th>
						<th class="menu-state">状态</th>
						<th class="menu-ope">操作</th>
					</tr>
					</thead>
                </table>
                <div style="overflow-y:scroll;height:calc(100vh - 62px);width:100%">
                <table id="menuTreeTable" class="table table-striped table-bordered table-hover" style="margin:0;">
					<tbody>
					${map.menuTreeRows!''}
					</tbody>
				</table>
                </div>
			</div><!-- /.box-body -->
		</div><!-- /.box -->
	</div><!-- /.col -->
</div><!-- /.row -->

<script src="${envData.staticRoot}/resources/plugins/jquery-treetable/jquery.treetable.js"></script>
<#include "./menuModify.ftl">
<#include "./functionModify.ftl">
<script>
    $("#menuTreeTableHead").treetable({});
    $("#menuTreeTable").treetable({
        expandable: true,
        column:1,
        initialState:"expanded"
	});
    var functionEnters= new Array();

    function mountActions(){
        // Highlight selected row
        $("#menuTreeTable tbody").off("mousedown").on("mousedown", "tr", function() {
            $(".selected").not(this).removeClass("selected");
            $(this).toggleClass("selected");
        });
        $(".menuEdit").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            menuEdit(data);
        });
        $(".menuDel").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            menuDelete(data);
        });
        $(".addSubMenu").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            addSubMenu(data)
        });
        $(".addFunction").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            addFunction(data);
        });
        $(".menuFunctionEdit").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            menuFunctionEdit(data);
        });
        $(".menuFunctionDel").off("click").on("click",function(){
            var data=$(event.target).attr("bData");
            menuFunctionDel(data);
        });
	};
    function getStateValByName(stateName){
        return (stateName=="可用")?1:0;
    }
    function getStateNameByVal(stateValue){
        return (stateValue==1)?"可用":"不可用";
    }
    function getMenuItem(menuCode){
        var code=$("[data-tt-id='"+menuCode+"'] td:eq(0)").html();
        if (code!=undefined && code!=null && code==menuCode){
            var menuItem = new Object();
            menuItem.menuCode=menuCode;
            menuItem.menuName=$("[data-tt-id='"+menuCode+"'] td:eq(1) t").html();
            menuItem.menuClass=$("[data-tt-id='"+menuCode+"'] td:eq(2)").html();
            menuItem.parentMenuCode=$("[data-tt-id='"+menuCode+"'] td:eq(3)").html();
            menuItem.menuState=getStateValByName($("[data-tt-id='"+menuCode+"'] td:eq(4)").html());
            return menuItem;
		}
        return null;
    }
    function resetOperatorButtons(menuCode) {
        var childs=$("[data-tt-parent-id='"+menuCode+"']");
        if (childs!=undefined && childs.length>0){
            var buttons=$("[data-tt-parent-id='"+menuCode+"'] td:eq(5) button");
            if ($(buttons[0]).hasClass("menuFunctionEdit")){
                var buttonsHtml = makeButtonHtml(menuCode,"menuEdit")+
                    makeButtonHtml(menuCode,"addFunction");
                $("[data-tt-id='"+menuCode+"'] td:eq(5)").html(buttonsHtml);
            }else if ($(buttons[0]).hasClass("menuEdit")){
                var buttonsHtml = makeButtonHtml(menuCode,"menuEdit")+
                    makeButtonHtml(menuCode,"addSubMenu");
                $("[data-tt-id='"+menuCode+"'] td:eq(5)").html(buttonsHtml);
            }
        }else{
            var buttonsHtml = makeButtonHtml(menuCode,"menuEdit")+
                makeButtonHtml(menuCode,"menuDel")+
                makeButtonHtml(menuCode,"addSubMenu")+
                makeButtonHtml(menuCode,"addFunction");
            $("[data-tt-id='"+menuCode+"'] td:eq(5)").html(buttonsHtml);
        }
    }
    function onMenuChanged(menuItem) {
        $("[data-tt-id='"+menuItem.menuCode+"'] td:eq(1) t").html(menuItem.menuName);
        $("[data-tt-id='"+menuItem.menuCode+"'] td:eq(2)").html(menuItem.menuClass);
        $("[data-tt-id='"+menuItem.menuCode+"'] td:eq(3)").html(menuItem.parentMenuCode);
        $("[data-tt-id='"+menuItem.menuCode+"'] td:eq(4)").html(getStateNameByVal(menuItem.menuState));
    }

    function onFuncChanged(funcItem){
        $("[data-tt-id='"+funcItem.funCode+"'] td:eq(1) t").html(funcItem.funName);
        $("[data-tt-id='"+funcItem.funCode+"'] td:eq(2)").html(funcItem.funPath);
        $("[data-tt-id='"+funcItem.funCode+"'] td:eq(3)").html(funcItem.menuCode);
        $("[data-tt-id='"+funcItem.funCode+"'] td:eq(4)").html(getStateNameByVal(funcItem.funState));
    }
    function makeButtonHtml(menuCode,type){
        if (type=="menuEdit")
            return "<button class=\"btn bg-green btn-in-row menuEdit\" data-toggle=\"button\" bdata=\""+menuCode+"\">编辑</button> \n";
        if (type=="menuDel")
            return "<button class=\"btn bg-maroon btn-in-row menuDel\" data-toggle=\"button\" bdata=\""+menuCode+"\">删除</button> \n";
        else if (type=="addSubMenu")
            return "<button class=\"btn bg-teal btn-in-row addSubMenu\" data-toggle=\"button\" bdata=\""+menuCode+"\">增加菜单</button> \n";
        else if (type=="addFunction")
            return "<button class=\"btn bg-purple btn-in-row addFunction\" data-toggle=\"button\" bdata=\""+menuCode+"\">增加功能</button> \n";
    }

    function onMenuAdded(menuItem){
        var menuHtml="";
        if (menuItem.menuClass==1)
            menuHtml="<tr data-tt-id='"+menuItem.menuCode+"'>";
        else
            menuHtml="<tr data-tt-id='"+menuItem.menuCode+"' data-tt-parent-id='"+menuItem.parentMenuCode+"'>";
        menuHtml+=
			"<td class=\"menu-code\">"+menuItem.menuCode+"</td>" +
            "<td class=\"menu-name\">" +
            "<img src=\"${envData.staticRoot}/images/${envData.contextPath}/menu.png\"><t>"+menuItem.menuName+"</t>" +
            "</td>" +
            "<td>"+menuItem.menuClass+"</td>" +
            "<td class=\"par-menu-code\">"+menuItem.parentMenuCode+"</td>" +
            "<td class=\"menu-state\">"+getStateNameByVal(menuItem.menuState)+"</td>" +
            "<td class=\"menu-ope-scrol\">" +
            makeButtonHtml(menuItem.menuCode,"menuEdit")+
            makeButtonHtml(menuItem.menuCode,"menuDel")+
            makeButtonHtml(menuItem.menuCode,"addSubMenu")+
            makeButtonHtml(menuItem.menuCode,"addFunction")+
            "</td>" +
            "</tr>";
        var parentNode=$("#menuTreeTable").treetable("node",menuItem.parentMenuCode);
        $("#menuTreeTable").treetable("loadBranch", parentNode, menuHtml);
        resetOperatorButtons(menuItem.parentMenuCode);
        mountActions();
        <#-- 新增成功，要将状态改为更改 -->
        menuModifyInstance.initModal(getMenuItem(menuItem.menuCode),getMenuItem(menuItem.parentMenuCode),2);
	}

    function onFuncAdded(funcItem){
        var menuHtml=
            "<tr data-tt-id='"+funcItem.funCode+"' data-tt-parent-id='"+funcItem.menuCode+"'>"+
            "<td class=\"menu-code\">"+funcItem.funCode+"</td>" +
            "<td class=\"menu-name\">" +
            "<img src=\"${envData.staticRoot}/images/${envData.contextPath}/function.png\"><t>"+funcItem.funName+"</t>" +
            "</td>" +
            "<td>"+funcItem.funPath+"</td>" +
            "<td class=\"par-menu-code\">"+funcItem.menuCode+"</td>" +
            "<td class=\"menu-state\">"+getStateNameByVal(funcItem.funState)+"</td>" +
            "<td class=\"menu-ope-scrol\">" +
            "<button class=\"btn bg-green btn-in-row menuFunctionEdit\" data-toggle=\"button\" bdata=\""+funcItem.funCode+"\">编辑</button> \n" +
            "<button class=\"btn bg-maroon btn-in-row menuFunctionDel\" data-toggle=\"button\" bdata=\""+funcItem.funCode+"\">删除</button> \n" +
            "</td>" +
            "</tr>";
        var parentNode=$("#menuTreeTable").treetable("node",funcItem.menuCode);
        $("#menuTreeTable").treetable("loadBranch", parentNode, menuHtml);
        resetOperatorButtons(funcItem.menuCode);
        mountActions();
        var func = getFuncItem(funcItem.funCode);
        var parMenu=getMenuItem(func.menuCode);
        functionModifyInstance.initModal(func,parMenu,2,functionEnters);
    }

    function menuEdit(menuCode){
        var menu=getMenuItem(menuCode);
        var parMenu=getMenuItem(menu.parentMenuCode);
        menuModifyInstance.getMenuItem = getMenuItem;
        menuModifyInstance.initModal(menu,parMenu,2);
        menuModifyInstance.onMenuChanged = onMenuChanged;
        $('#menuModify').modal({backdrop: 'static', keyboard: false});
    }

    function getFuncItem(funCode){
        var menu=getMenuItem(funCode);
        var func=new Object();
        func.funCode = menu.menuCode;
        func.funName = menu.menuName;
        func.funPath = menu.menuClass;
        func.menuCode = menu.parentMenuCode;
        func.funState = menu.menuState;
        return func;
    }
    function menuFunctionEdit(funCode){
        var func = getFuncItem(funCode);
        var parMenu=getMenuItem(func.menuCode);
        functionModifyInstance.getMenuItem = getMenuItem;
        functionModifyInstance.initModal(func,parMenu,2,functionEnters);
        functionModifyInstance.onFuncChanged = onFuncChanged;
        $('#functionModify').modal({backdrop: 'static', keyboard: false});
    }

    function expandMenus(){
        $("#menuTreeTable").treetable("expandAll");
    }

    function collapseMenus() {
        $("#menuTreeTable").treetable("collapseAll");
    }

    function refreshMenus() {
        var ajga=new AppJSGlobAjax();
        var url="/sysset/refreshMenus";
        this.collect_data = {};
        var data = JSON.stringify(this.collect_data);
        ajga.AjaxApplicationJson(url,data,function (content){
            if (content.code==0){
                var rows=content.menuTreeRows;
            //    $("#menuTreeTable").treetable("removeNode", content.menuCode);
                $("#menuTreeTable tbody").empty();
                $("#menuTreeTable").treetable("loadBranch", null, rows);
                mountActions();
            }
            msgbox.showMsgBox(content.msg);
        });
    }

    function newTopMenu(){
        menuModifyInstance.getMenuItem = getMenuItem;
        menuModifyInstance.initModal(null,null,1);
        menuModifyInstance.onMenuChanged = onMenuChanged;
        menuModifyInstance.onMenuAdded = onMenuAdded;
        $('#menuModify').modal({backdrop: 'static', keyboard: false});
	}
    function addSubMenu(parMenuCode) {
        var parMenu=getMenuItem(parMenuCode);
        menuModifyInstance.getMenuItem = getMenuItem;
        menuModifyInstance.initModal(null,parMenu,1);
        menuModifyInstance.onMenuChanged = onMenuChanged;
        menuModifyInstance.onMenuAdded = onMenuAdded;
        $('#menuModify').modal({backdrop: 'static', keyboard: false});
    }

    function addFunction(parMenuCode){
        var parMenu=getMenuItem(parMenuCode);
        functionModifyInstance.getMenuItem = getMenuItem;
        functionModifyInstance.onFuncAdded = onFuncAdded;
        functionModifyInstance.onFuncChanged = onFuncChanged;
        functionModifyInstance.initModal(null,parMenu,1,functionEnters);
        $('#functionModify').modal({backdrop: 'static', keyboard: false});
    }

    function menuDelete(menuCode){
        msgbox.showComfireBox("确定要删除本菜单吗？",menuCode,function (menuCode){
            var ajga=new AppJSGlobAjax();
            var url="/sysset/deleteMenuByCode";
            this.collect_data = {
                menuCode: menuCode
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    $("#menuTreeTable").treetable("removeNode", content.menuCode);
                    resetOperatorButtons(content.parentMenuCode);
                    mountActions();
                }
                msgbox.showMsgBox(content.msg);
            });
        });
	}

	function menuFunctionDel(funcCode){
        msgbox.showComfireBox("确定要删除本功能菜单吗？",funcCode,function (funcCode){
            var ajga=new AppJSGlobAjax();
            var url="/sysset/deleteFuncByCode";
            this.collect_data = {
                funCode: funcCode
            };
            var data = JSON.stringify(this.collect_data);
            ajga.AjaxApplicationJson(url,data,function (content){
                if (content.code==0){
                    $("#menuTreeTable").treetable("removeNode", content.funCode);
                    resetOperatorButtons(content.parentMenuCode);
                    mountActions();
                }
                msgbox.showMsgBox(content.msg);
            });
        });
    }

    $(document).ready(function(){
        mountActions();
        <#if (map.FEnterList??)>
            <#list map.FEnterList as FEnter >
        functionEnters[${FEnter_index}]={path:"${FEnter.path!''}",name:"${FEnter.name!''}",params:"${FEnter.params!''}",desc:"${FEnter.desc!''}"};
            </#list>
        </#if>
    });

</script>