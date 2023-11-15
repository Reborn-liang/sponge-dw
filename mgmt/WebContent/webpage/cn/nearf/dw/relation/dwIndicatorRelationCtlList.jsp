<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorRelationCtlList" checkbox="true" fitColumns="false" title="指标关系" actionUrl="dwIndicatorRelationCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="ID"  field="id"  hidden="false"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="指标"  field="indicatorId"    queryMode="group"  width="320"></t:dgCol>
   <t:dgCol title="依赖指标"  field="parentIndicatorId"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="是否强关联"  field="couplingFlg"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorRelationCtlController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorRelationCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorRelationCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorRelationCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorRelationCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/relation/dwIndicatorRelationCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		
 			$("#dwIndicatorRelationCtlListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRelationCtlListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRelationCtlListtb").find("input[name='updateDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRelationCtlListtb").find("input[name='updateDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 

function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorRelationCtlController.do?upload', "dwIndicatorRelationCtlList");
}


function ExportXls() {
	JeecgExcelExport("dwIndicatorRelationCtlController.do?exportXls","dwIndicatorRelationCtlList");
}


function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorRelationCtlController.do?exportXlsByT","dwIndicatorRelationCtlList");
}
 </script>