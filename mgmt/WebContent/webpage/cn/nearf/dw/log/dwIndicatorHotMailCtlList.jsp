<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorHotMailCtlList" checkbox="true" fitColumns="false" title="告警邮件管理" actionUrl="dwIndicatorHotMailCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="分组标记"  field="groupName"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="收件人名"  field="userName"    queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="收件人邮箱地址"  field="userEmailAddr"    queryMode="single"  width="160"></t:dgCol>
   <t:dgCol title="紧急标志"  field="urgentFlg"    queryMode="single" dictionary="sf_yn" width="40"></t:dgCol>
   <t:dgCol title="记录创建时间"  field="createDate" formatter="yyyy-MM-dd"   queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="记录更新时间"  field="updateDate" formatter="yyyy-MM-dd"   queryMode="group"  width="100"></t:dgCol>
<%--    <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorHotMailCtlController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorHotMailCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorHotMailCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorHotMailCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar> --%>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorHotMailCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
<%--    <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/log/dwIndicatorHotMailCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		
 			$("#dwIndicatorHotMailCtlListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorHotMailCtlListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorHotMailCtlListtb").find("input[name='updateDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorHotMailCtlListtb").find("input[name='updateDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 

function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorHotMailCtlController.do?upload', "dwIndicatorHotMailCtlList");
}


function ExportXls() {
	JeecgExcelExport("dwIndicatorHotMailCtlController.do?exportXls","dwIndicatorHotMailCtlList");
}


function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorHotMailCtlController.do?exportXlsByT","dwIndicatorHotMailCtlList");
}
 </script>