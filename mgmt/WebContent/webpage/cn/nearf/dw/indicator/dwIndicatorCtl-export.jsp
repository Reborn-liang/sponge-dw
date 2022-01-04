<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>下载导出文件</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
<script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>

<script type="text/javascript">
	
</script>
</head>
<body>
 	<c:if test="${errorMsg != null}">
    <p style="align-content: center;">${errorMsg}</p>
    </c:if>
    <c:if test="${errorMsg == null}">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td align="right"><label class="Validform_label"> 文件: </label></td>
				<td class="value">
					<a href="${uri}" target="_blank">点击下载文件</a>
				</td>
			</tr>
		</table>
	</c:if>
</body>
