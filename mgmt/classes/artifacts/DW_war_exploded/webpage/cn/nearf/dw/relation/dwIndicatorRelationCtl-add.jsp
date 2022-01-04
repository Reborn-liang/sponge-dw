<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
<title>指标关系</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>

<script type="text/javascript">
$(window).load(function(){
	  initIndicatorsUI("#formobj", "indicatorId");
	  initIndicatorsUI("#formobj", "parentIndicatorId");
});
</script>
</head>
<body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorRelationCtlController.do?doAdd" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorRelationCtlPage.id }">
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
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							依赖指标:
						</label>
					</td>
					<td class="value">
					     	<input id="parentIndicatorId" name="parentIndicatorId" type="text" style="width: 150px" class="inputxt"  datatype="*">
							<!-- <input id="relationTree" style="width: 150px">
							<button type="button" onclick="clearRelation();">清除</button> -->
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
					<t:dictSelect field="couplingFlg" type="list" typeGroupCode="sf_yn" defaultVal="Y" hasLabel="false" title="是否强关联" datatype="*"></t:dictSelect>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">是否强关联</label>
					</td>
				</tr>
			</table>
		</t:formvalid>
</body>
<script src = "webpage/cn/nearf/dw/relation/dwIndicatorRelationCtl.js"></script>		