<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_type_list" class="easyui-layout" fit="true">
	<div region="center" style="padding: 1px;">
		<t:datagrid name="dwIndicatorRelationCtlList" title="指标关系" actionUrl="dwIndicatorRelationCtlController.do?datagridTree" idField="Id" treegrid="true" fitColumns="false" pagination="false">
		    <t:dgCol title="ID" field="id" treefield="id" hidden="true" width="120"></t:dgCol> 
		    <t:dgCol title="RID"  field="fieldMap.rid" treefield="fieldMap.rid" hidden="true" width="60" ></t:dgCol>
		    <t:dgCol title="SRC"  field="src" treefield="src" hidden="true" width="60" ></t:dgCol>
		    <t:dgCol title="Code"  field="code" treefield="code" hidden="true" width="120" ></t:dgCol>
		    <t:dgCol title="指标"  field="text" treefield="text" width="220" dictionary="dw_indicator_ctl,id,name"></t:dgCol>
		    <t:dgCol title="是否强关联"  field="fieldMap.couplingFlg" treefield="fieldMap.couplingFlg" width="80"></t:dgCol>
		    <%-- <t:dgCol title="创建时间"  field="createDate" treefield="fieldMap.createDate" width="120" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol> --%>
   			<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
			
		  <t:dgDelOpt exp="fieldMap.allowDel#eq#Y" title="删除" url="dwIndicatorRelationCtlController.do?doDel&id={src}"/>
		  <t:dgOpenOpt title="查看指标" url="dwIndicatorCtlController.do?goUpdate&load=detail&id={code}" openModel="OpenWin" width="1080" height="800" ></t:dgOpenOpt>
		  <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorRelationCtlController.do?goAdd" funname="addRelation"></t:dgToolBar>
		  <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorRelationCtlController.do?goUpdate" funname="updateRelation"></t:dgToolBar>
		  <%-- <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorRelationCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar> --%>
		  <%-- <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorRelationCtlController.do?goUpdate" funname="detail"></t:dgToolBar> --%>
		 <%--  <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
		  <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
		  <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
		</t:datagrid>
	</div>
</div>

<script type="text/javascript">
	function addRelation(title, url, id) {
		var rowData = $('#' + id).datagrid('getSelected');
		if (rowData) {
			url += '&parentId=' + rowData.code;
		}
		add(title, url, 'dwIndicatorRelationCtlList', 840, 600);
	}

	function updateRelation(title, url, id) {
		var rowData = $('#' + id).datagrid('getSelected');
		var rid = rowData['fieldMap.rid'];
		var openUrl = url+'&rid='+rid;
		update(title, openUrl, 'dwIndicatorRelationCtlList', 840, 600);
	}
	
 	$(document).ready(function(){
 		//给时间控件加上样式
	 });
	
 	//导入
 	function ImportXls() {
 		openuploadwin('Excel导入', 'dwIndicatorRelationCtlController.do?upload', "dwIndicatorRelationCtlList");
 	}

 	//导出
 	function ExportXls() {
 		JeecgExcelExport("dwIndicatorRelationCtlController.do?exportXls","dwIndicatorRelationCtlList");
 	}

 	//模板下载
 	function ExportXlsByT() {
 		JeecgExcelExport("dwIndicatorRelationCtlController.do?exportXlsByT","dwIndicatorRelationCtlList");
 	}
</script>