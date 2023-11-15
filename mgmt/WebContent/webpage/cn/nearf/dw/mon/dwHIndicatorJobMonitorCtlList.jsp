<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwHIndicatorJobMonitorCtlList" checkbox="true" fitColumns="false" title="指标执行状态历史" actionUrl="dwHIndicatorJobMonitorCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="ID"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="指标"  field="indicatorId"   query="true" queryMode="single"  width="120" dictionary="dw_indicator_ctl,id,name"></t:dgCol>
   <t:dgCol title="任务类型"  field="jobType"   query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="加载类型"  field="jobIncFlg"    queryMode="group" dictionary="DWLoadType" width="60"></t:dgCol>
   <t:dgCol title="任务状态"  field="jobStatus"    queryMode="group" dictionary="DWJobStatus" width="60"></t:dgCol>
   <t:dgCol title="影响数据笔数"  field="processData"    queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="任务开始时间"  field="jobStartTime" formatter="yyyy-MM-dd hh:mm:ss" query="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="任务结束时间"  field="jobEndTime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="执行时长(秒)"  field="jobRunTime"    queryMode="group"  width="90"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwHIndicatorJobMonitorCtlController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="dwHIndicatorJobMonitorCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwHIndicatorJobMonitorCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwHIndicatorJobMonitorCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwHIndicatorJobMonitorCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/mon/dwHIndicatorJobMonitorCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		
 			$("#dwHIndicatorJobMonitorCtlListtb").find("input[name='jobStartTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwHIndicatorJobMonitorCtlListtb").find("input[name='jobStartTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwHIndicatorJobMonitorCtlListtb").find("input[name='jobEndTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwHIndicatorJobMonitorCtlListtb").find("input[name='jobEndTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 

function ImportXls() {
	openuploadwin('Excel导入', 'dwHIndicatorJobMonitorCtlController.do?upload', "dwHIndicatorJobMonitorCtlList");
}


function ExportXls() {
	JeecgExcelExport("dwHIndicatorJobMonitorCtlController.do?exportXls","dwHIndicatorJobMonitorCtlList");
}


function ExportXlsByT() {
	JeecgExcelExport("dwHIndicatorJobMonitorCtlController.do?exportXlsByT","dwHIndicatorJobMonitorCtlList");
}
 </script>