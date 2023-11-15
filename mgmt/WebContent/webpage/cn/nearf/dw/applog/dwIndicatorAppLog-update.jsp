<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>app日志记录</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorAppLogController.do?doUpdate" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorAppLogPage.id }">
					<input id="updateDate" name="updateDate" type="hidden" value="${dwIndicatorAppLogPage.updateDate }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								应用程序名:
							</label>
						</td>
						<td class="value">
						     	 <input id="app" name="app" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorAppLogPage.app}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">应用程序名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								发生时间:
							</label>
						</td>
						<td class="value">
									  <input id="createDate" name="createDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"datatype="*" value='<fmt:formatDate value='${dwIndicatorAppLogPage.createDate}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">发生时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								级别:
							</label>
						</td>
						<td class="value">
						     	 <input id="level" name="level" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorAppLogPage.level}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">级别</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								处理状态:
							</label>
						</td>
						<td class="value">
						     	 <input id="status" name="status" type="text" style="width: 150px" class="inputxt" datatype="*" <c:if test="${dwIndicatorAppLogPage.status==0}">value='未处理'</c:if>
						     	 <c:if test="${dwIndicatorAppLogPage.status==1}">value='已处理'</c:if>>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">处理状态</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								日志内容:
							</label>
						</td>
						<td class="value">
							<textarea id="content" name="content" rows="10" cols="20" style="width: 450px; height:200px" >${dwIndicatorAppLogPage.content}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">日志内容</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/applog/dwIndicatorAppLog.js"></script>		