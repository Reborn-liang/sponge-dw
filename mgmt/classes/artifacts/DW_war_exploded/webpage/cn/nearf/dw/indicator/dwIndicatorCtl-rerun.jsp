<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>立即执行</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
 <c:if test="${rerunError == 0}">
     <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorCtlController.do?doRerun&id=${dwIndicatorCtlPage.id}" tiptype="1">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务开始时间:
						</label>
					</td>
					<td class="value">
						<input id="jobStartTime" name="jobStartTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()" datatype="*">    
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">任务开始时间</label>
					</td>
				</tr>
			</table>
		</t:formvalid>
</c:if>
<c:if test="${rerunError == 1}">
	源表不能立即执行
</c:if>
 </body>