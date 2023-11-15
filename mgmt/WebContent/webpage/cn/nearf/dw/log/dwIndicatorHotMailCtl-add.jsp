<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>告警邮件管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorHotMailCtlController.do?doAdd" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorHotMailCtlPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							分组标记:
						</label>
					</td>
					<td class="value">
					     	 <input id="groupName" name="groupName" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">分组标记</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							收件人名:
						</label>
					</td>
					<td class="value">
					     	 <input id="userName" name="userName" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">收件人名</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							收件人邮箱地址:
						</label>
					</td>
					<td class="value">
					     	 <input id="userEmailAddr" name="userEmailAddr" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">收件人邮箱地址</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							紧急标志:
						</label>
					</td>
					<td class="value">
							  <t:dictSelect field="urgentFlg" type="list"
									typeGroupCode="sf_yn" defaultVal="${dwIndicatorHotMailCtlPage.urgentFlg}" hasLabel="false"  title="紧急标志"></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">紧急标志</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							记录创建时间:
						</label>
					</td>
					<td class="value">
							   <input id="createDate" name="createDate" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">记录创建时间</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							记录更新时间:
						</label>
					</td>
					<td class="value">
							   <input id="updateDate" name="updateDate" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">记录更新时间</label>
						</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/log/dwIndicatorHotMailCtl.js"></script>		