<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%
  	String filter = (String)request.getAttribute("filter");
	String row = (String)request.getAttribute("row");
	String column = (String)request.getAttribute("column");
	String columnName = (String)request.getAttribute("columnName");
%>
<!DOCTYPE html>
<html>
 <head>
  <title>过滤条件</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  
<script type="text/javascript">
  
function onEditFilter() {
		var editValue = $('#editValue').val();
		var row = $('#row').val();
		var column = $('#column').val();

		try {
			if (window.opener && window.opener.didFinishEdit) {
				window.opener.didFinishEdit(editValue,row,column);
				window.opener.close();
			} else if (windowapi && windowapi.opener.didFinishEdit){
				windowapi.opener.didFinishEdit(editValue,row,column);
				windowapi.opener.close();
			} else {
				parent.didFinishEdit(editValue,row,column);
			}
		} catch (e) {
			alert(e);
		}
		
		return false;
	}

</script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="" tiptype="1" beforeSubmit="onEditFilter()">
		<table cellpadding="0" cellspacing="1" class="formtable">
		<input id="row" name="row" type="hidden" value="${row}">
		<input id="column" name="column" type="hidden" value="${column}">
			<tr>
			<td align="right" width="80px">
				<label class="Validform_label">${columnName}:</label>
			</td>
			<td class="value">
		     	 <textarea id="editValue" name="editValue" type="text" style="width: 600px;height: 300px;" class="inputxt">${filter}</textarea>
			</td>
		</tr>	 
		</table>
  </t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/indicator/dwIndicatorStdCtl.js"></script>		