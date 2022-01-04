<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>指标执行状态管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorJobMonitorCtlController.do?doUpdate" tiptype="1">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								指标:
							</label>
						</td>
						<td class="value">
						     	 <input id="indicatorId" name="indicatorId" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorJobMonitorCtlPage.indicatorId}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">指标</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								状态:
							</label>
						</td>
						<td class="value">
						     	 <input id="jobStatus" name="jobStatus" type="text" style="width: 150px" class="inputxt"  value='${dwIndicatorJobMonitorCtlPage.jobStatus}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">状态</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								影响数据笔数:
							</label>
						</td>
						<td class="value">
						     	 <input id="processData" name="processData" type="text" style="width: 150px" class="inputxt"  value='${dwIndicatorJobMonitorCtlPage.processData}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">影响数据笔数</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务开始时间:
							</label>
						</td>
						<td class="value">
									  <input id="jobStartTime" name="jobStartTime" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"datatype="*" value='<fmt:formatDate value='${dwIndicatorJobMonitorCtlPage.jobStartTime}' type="date" pattern="yyyy-MM-dd"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务开始时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								任务结束时间:
							</label>
						</td>
						<td class="value">
									  <input id="jobEndTime" name="jobEndTime" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"datatype="*" value='<fmt:formatDate value='${dwIndicatorJobMonitorCtlPage.jobEndTime}' type="date" pattern="yyyy-MM-dd"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务结束时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								执行时长:
							</label>
						</td>
						<td class="value">
						     	 <input id="jobRunTime" name="jobRunTime" type="text" style="width: 150px" class="inputxt"  value='${dwIndicatorJobMonitorCtlPage.jobRunTime}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">执行时长</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/mon/dwIndicatorJobMonitorCtl.js"></script>		