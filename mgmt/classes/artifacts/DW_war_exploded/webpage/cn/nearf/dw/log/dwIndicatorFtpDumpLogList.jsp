<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorFtpDumpLogList" checkbox="true" fitColumns="false" title="指标导出日志" actionUrl="dwIndicatorFtpDumpLogController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="指标"  field="indicatorId"   query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="SFTP状态"  field="sftpStatus"   query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="错误信息"  field="errorInfo"    queryMode="single"  width="300"></t:dgCol>
   <t:dgCol title="创建时间"  field="createDate" formatter="yyyy-MM-dd"  query="true" queryMode="group"  width="120"></t:dgCol>
<%--    <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorFtpDumpLogController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorFtpDumpLogController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorFtpDumpLogController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorFtpDumpLogController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar> --%>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorFtpDumpLogController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
<%--    <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/log/dwIndicatorFtpDumpLogList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#dwIndicatorFtpDumpLogListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorFtpDumpLogListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorFtpDumpLogController.do?upload', "dwIndicatorFtpDumpLogList");
}

//导出
function ExportXls() {
	JeecgExcelExport("dwIndicatorFtpDumpLogController.do?exportXls","dwIndicatorFtpDumpLogList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorFtpDumpLogController.do?exportXlsByT","dwIndicatorFtpDumpLogList");
}
 </script>