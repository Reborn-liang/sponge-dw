<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorRerunMonitorCtlList" checkbox="true" fitColumns="false" title="指标重做管理" actionUrl="dwIndicatorRerunMonitorCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="指标"  field="indicatorId"   query="true" queryMode="single"  width="120" dictionary="dw_indicator_ctl,id,name"></t:dgCol>
   <t:dgCol title="执行周期"  field="jobType"   query="true" queryMode="single" dictionary="DWExecPeriod"  width="120"></t:dgCol>
   <t:dgCol title="加载类型"  field="jobIncFlg"    queryMode="single" dictionary="DWLoadType" width="120"></t:dgCol>
   <t:dgCol title="任务状态"  field="jobStatus"   query="true" queryMode="single" dictionary="DWJobStatus" width="120"></t:dgCol>
   <t:dgCol title="重做开始时间"  field="refreshDate" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="处理数据量"  field="processData"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="任务开始时间"  field="jobStartTime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="任务结束时间"  field="jobEndTime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="执行时长(秒)"  field="jobRunTime"    queryMode="single"  width="90"></t:dgCol>
   <t:dgCol title="创建时间"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorRerunMonitorCtlController.do?doDel&id={id}" />
   <t:dgToolBar title="批量激活"  icon="icon-edit" url="dwIndicatorRerunMonitorCtlController.do?active" funname="active" operationCode="Admin"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorRerunMonitorCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
  <%--  <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorRerunMonitorCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorRerunMonitorCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorRerunMonitorCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/mon/dwIndicatorRerunMonitorCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='refreshDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='refreshDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='jobStartTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='jobStartTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='jobEndTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorRerunMonitorCtlListtb").find("input[name='jobEndTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
 function active(title,url,gname) {
		gridname=gname;
	    var ids = [];
	    var rows = $("#"+gname).datagrid('getSelections');
	    if (rows.length > 0) {
	    	$.dialog.confirm('你确定要激活这些任务吗?', function(r) {
			   if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : url,
						type : 'post',
						data : {
							ids : ids.join(',')
						},
						cache : false,
						success : function(data) {
							var d = $.parseJSON(data);
							if (d.success) {
								var msg = d.msg;
								tip(msg);
								reloadTable();
								$("#"+gname).datagrid('unselectAll');
								ids='';
							}
						}
					});
				}
			});
		} else {
			tip("请选择需要激活的任务");
		}
	}
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorRerunMonitorCtlController.do?upload', "dwIndicatorRerunMonitorCtlList");
}

//导出
function ExportXls() {
	JeecgExcelExport("dwIndicatorRerunMonitorCtlController.do?exportXls","dwIndicatorRerunMonitorCtlList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorRerunMonitorCtlController.do?exportXlsByT","dwIndicatorRerunMonitorCtlList");
}
 </script>