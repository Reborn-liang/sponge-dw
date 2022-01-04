<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorJobMonitorCtlList" checkbox="true" fitColumns="false" title="指标执行状态管理" actionUrl="dwIndicatorJobMonitorCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="ID"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="指标"  field="indicatorId"   query="true" queryMode="single"  width="220" dictionary="dw_indicator_ctl,id,name"></t:dgCol>
   <t:dgCol title="状态"  field="jobStatus"   query="true" queryMode="single" dictionary="DWJobStatus" width="60"></t:dgCol>
   <t:dgCol title="影响数据笔数"  field="processData"    queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="任务开始时间"  field="jobStartTime" formatter="yyyy-MM-dd hh:mm:ss"  query="false" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="任务结束时间"  field="jobEndTime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="执行时长(秒)"  field="jobRunTime"    queryMode="group"  width="90"></t:dgCol>
   <%-- <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorJobMonitorCtlController.do?doDel&id={id}"/>
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorJobMonitorCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorJobMonitorCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorJobMonitorCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorJobMonitorCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
   <t:dgToolBar title="重做" icon="icon-add" url="dwIndicatorJobMonitorCtlController.do?rerun" funname="update"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/mon/dwIndicatorJobMonitorCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#dwIndicatorJobMonitorCtlListtb").find("input[name='jobStartTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorJobMonitorCtlListtb").find("input[name='jobStartTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorJobMonitorCtlListtb").find("input[name='jobEndTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorJobMonitorCtlListtb").find("input[name='jobEndTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorJobMonitorCtlController.do?upload', "dwIndicatorJobMonitorCtlList");
}

//导出
function ExportXls() {
	JeecgExcelExport("dwIndicatorJobMonitorCtlController.do?exportXls","dwIndicatorJobMonitorCtlList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorJobMonitorCtlController.do?exportXlsByT","dwIndicatorJobMonitorCtlList");
}
 </script>