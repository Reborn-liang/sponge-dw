<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>指标导出日志</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorFtpDumpLogController.do?doAdd" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorFtpDumpLogPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							指标:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect field="indicatorId" type="list"
									typeGroupCode="" defaultVal="${dwIndicatorFtpDumpLogPage.indicatorId}" hasLabel="false"  title="指标"></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">指标</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							SFTP状态:
						</label>
					</td>
					<td class="value">
					     	 <input id="sftpStatus" name="sftpStatus" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">SFTP状态</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							错误信息:
						</label>
					</td>
					<td class="value">
						  	 <textarea style="width:600px;" class="inputxt" rows="6" id="errorInfo" name="errorInfo"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">错误信息</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
							   <input id="createDate" name="createDate" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建时间</label>
						</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/log/dwIndicatorFtpDumpLog.js"></script>		