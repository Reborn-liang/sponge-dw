<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>


<script type="text/javascript" src="webpage/cn/nearf/common/storage.js"></script>
<script type="text/javascript" src="webpage/cn/nearf/common/store.js"></script>

<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	var selDpt = null;

	function onSelectDpt(data) {
		try {
			selDpt = data;
			checkAndUpdateEmployee();
		} catch (err) {
		}
	}

	function checkAndUpdateEmployee() {
		if (selDpt == null) {
			return;
		}
		
		$("#id").combobox({
			url : "erpEmployeeController.do?listAll" + "&dptId=" + selDpt.id
		});
	}
</script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userController.do?doAddStoreMgr">
		<table style="width: 680px;" cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td align="right" nowrap>
					员工ID<div style="color: red;">(二选一)</div> 
				</td>
				<td class="value">
					<input id="employeeId" name="employeeId" type="text" style="width: 150px" class="inputxt" >
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">
					员工<div style="color: red;">(二选一)</div> 
				</td>
				<td class="value">
					部门: <input id="dptId" name="dptId" type="text" style="width: 150px" class="easyui-combobox" data-options="editable:true, url:'erpDepartmentController.do?listAll', valueField:'id', textField:'name', onSelect:onSelectDpt," >
					员工: <input id="id" name="id" type="text" style="width: 150px" class="easyui-combobox" data-options="valueField:'id', textField:'name', " > <span class="Validform_checktip"></span> <label class="Validform_label" style="display: none;">员工</label>
				</td>
			</tr>
		</table>
	</t:formvalid>
</body>