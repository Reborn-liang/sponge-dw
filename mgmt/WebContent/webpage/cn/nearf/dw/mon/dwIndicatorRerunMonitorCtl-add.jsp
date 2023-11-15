<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>指标重做管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorRerunMonitorCtlController.do?doAdd" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorRerunMonitorCtlPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							指标:
						</label>
					</td>
					<td class="value">
					     	 <input id="indicatorId" name="indicatorId" type="text" style="width: 150px" class="inputxt"  datatype="*">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">指标</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="jobType" name="jobType" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务类型</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							加载类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="jobIncFlg" name="jobIncFlg" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">加载类型</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务状态:
						</label>
					</td>
					<td class="value">
					     	 <input id="jobStatus" name="jobStatus" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务状态</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							刷新时间:
						</label>
					</td>
					<td class="value">
							   <input id="refreshDate" name="refreshDate" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">刷新时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							处理数据量:
						</label>
					</td>
					<td class="value">
					     	 <input id="processData" name="processData" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">处理数据量</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="jobStartTime" name="jobStartTime" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">任务开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							任务结束时间:
						</label>
					</td>
					<td class="value">
							   <input id="jobEndTime" name="jobEndTime" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
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
					     	 <input id="jobRunTime" name="jobRunTime" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">执行时长</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="createDate" name="createDate" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							更新时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="updateDate" name="updateDate" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">更新时间</label>
						</td>
				<td align="right">
					<label class="Validform_label">
					</label>
				</td>
				<td class="value">
				</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/mon/dwIndicatorRerunMonitorCtl.js"></script>		