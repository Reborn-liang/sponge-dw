<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title></title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  $(window).load(function(){
	  initIndicatorsUI("#formobj", "indicatorId", ${dwIndicatorRelationCtlPage.indicatorId});
	  initIndicatorsUI("#formobj", "parentIndicatorId", ${dwIndicatorRelationCtlPage.parentIndicatorId});
  });
  </script>
 </head>
 <body>
   <c:if test="${error != null}">
	<h1 align="center" style="color: red; font-size:18px">${error}</h1>
</c:if>
<c:if test="${error == null}">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorRelationCtlController.do?doUpdate" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorRelationCtlPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
					<td align="right">
						<label class="Validform_label">
							指标:
						</label>
					</td>
					<td class="value">
					     	<input id="indicatorId" name="indicatorId" type="text"  value='${dwIndicatorRelationCtlPage.indicatorId}' style="width: 150px" class="inputxt"  datatype="*">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">指标</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								依赖指标:
							</label>
						</td>
						<td class="value">
						     	<input id="parentIndicatorId" name="parentIndicatorId" type="text"  value='${dwIndicatorRelationCtlPage.parentIndicatorId}' style="width: 150px" class="inputxt"  datatype="*">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">依赖指标</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								是否强关联:
							</label>
						</td>
						<td class="value">
							<t:dictSelect field="couplingFlg" type="list" typeGroupCode="sf_yn" defaultVal="${dwIndicatorRelationCtlPage.couplingFlg}" hasLabel="false" title="是否强关联" datatype="*" noNeedBlank="true"></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">是否强关联</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
	</c:if>
 </body>
  <script src = "webpage/cn/nearf/dw/relation/dwIndicatorRelationCtl.js"></script>		