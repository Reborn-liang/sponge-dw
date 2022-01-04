<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>

<%
if (request.getAttribute("error") != null) {
	%>
<h1 align="center" style="width: 100%; color: red;">${error}</h1>
	<%
} else {
	String indId = request.getAttribute("indId").toString();
	%>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 1px;">
		<t:datagrid name="customQueryResult" checkbox="false" fit="true" fitColumns="false" title="预览" actionUrl="dwIndicatorCtlController.do?doPreview&id=${indId}" idField="id" queryMode="group">
			<%
				List<String> fields = (List<String>) request.getAttribute("fieldNames");
				if (fields != null) {
					for (String field : fields) {
						%><t:dgCol title="<%=field%>" field="<%=field%>" queryMode="single" width="100"></t:dgCol><%
					}
				}
			%>
		</t:datagrid>
	</div>
</div>


<script type="text/javascript">
	
</script>
<%
	}
%>
