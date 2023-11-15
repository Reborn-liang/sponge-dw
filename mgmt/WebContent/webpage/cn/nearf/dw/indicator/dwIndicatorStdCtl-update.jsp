<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>dw_indicator_std_ctl</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorStdCtlController.do?doUpdate" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorStdCtlPage.id }">
					<input id="createDate" name="createDate" type="hidden" value="${dwIndicatorStdCtlPage.createDate }">
					<input id="updateDate" name="updateDate" type="hidden" value="${dwIndicatorStdCtlPage.updateDate }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								原表名:
							</label>
						</td>
						<td class="value">
						     	 <input id="originalTable" name="originalTable" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorStdCtlPage.originalTable}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">原表名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								原字段名:
							</label>
						</td>
						<td class="value">
						     	 <input id="originalColumn" name="originalColumn" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorStdCtlPage.originalColumn}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">原字段名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								标准化类型:
							</label>
						</td>
						<td class="value">
						     	 <input id="stdType" name="stdType" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorStdCtlPage.stdType}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">标准化类型</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								标准化名:
							</label>
						</td>
						<td class="value">
						     	 <input id="stdName" name="stdName" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorStdCtlPage.stdName}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">标准化名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								标准化中文名:
							</label>
						</td>
						<td class="value">
						     	 <input id="chineseName" name="chineseName" type="text" style="width: 150px" class="inputxt"  value='${dwIndicatorStdCtlPage.chineseName}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">标准化中文名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								值域:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="valueRangeCode" type="list" dictTable="t_s_typegroup" dictField="typegroupcode" dictText="typegroupname" defaultVal="${dwIndicatorStdCtlPage.valueRangeCode}" hasLabel="false"  title="值域" extendJson="{style:'width:120px'}"></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">值域</label>
						</td>
					</tr>

			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/indicator/dwIndicatorStdCtl.js"></script>		