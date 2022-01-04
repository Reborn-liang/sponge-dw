<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorAppLogList" checkbox="true" fitColumns="false" title="app日志记录" actionUrl="dwIndicatorAppLogController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt exp="status#eq#0" title="标记为已处理" funname="changeStatus(id)"></t:dgFunOpt>
   <t:dgFunOpt exp="status#eq#1" title="恢复为未处理" funname="changeStatus(id)"></t:dgFunOpt>
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="应用程序名"  field="app"  query="true"  queryMode="single"  width="150"></t:dgCol>
   <t:dgCol title="发生时间"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss"  query="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="级别"  field="level"  query="true"  queryMode="single"  width="70"></t:dgCol>
   <t:dgCol title="处理状态"  field="status"  replace="已处理_1,未处理_0" query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="日志内容"  field="content"    queryMode="single"  width="420"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true"  queryMode="single"  width="120"></t:dgCol>
  <%--  <t:dgDelOpt title="删除" url="dwIndicatorAppLogController.do?doDel&id={id}" /> --%>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorAppLogController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorAppLogController.do?goUpdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/applog/dwIndicatorAppLogList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
		$("#dwIndicatorAppLogListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("#dwIndicatorAppLogListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
 });
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorAppLogController.do?upload', "dwIndicatorAppLogList");
}

//导出
function ExportXls() {
	JeecgExcelExport("dwIndicatorAppLogController.do?exportXls","dwIndicatorAppLogList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorAppLogController.do?exportXlsByT","dwIndicatorAppLogList");
}
//点击切换状态
function changeStatus(id){
	$.ajax({
		url:'dwIndicatorAppLogController.do?changeStatus&id='+id,
		success:function(data){
			try{
				var result=JSON.parse(data);
				alertTip(result.msg);
				$('#dwIndicatorAppLogList').datagrid('reload');
			}catch (e) {
				alertTip(e);
			}
		},error:function(textStatus,errorThrown){
			alertTip(errorThrown);
		}
	});
}
 </script>