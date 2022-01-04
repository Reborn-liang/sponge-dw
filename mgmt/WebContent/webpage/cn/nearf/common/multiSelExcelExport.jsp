<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>请选择导出的字段</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
<script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>

<script type="text/javascript" src="webpage/cn/nearf/common/storage.js"></script>
<script type="text/javascript" src="webpage/cn/nearf/common/store.js"></script>
<script type="text/javascript" src="webpage/cn/nearf/common/menu.js"></script>
<script type="text/javascript">
	var entityName = '${entityName}'
	var selNames = '${selNames}';
	var url = '${hurl}'
	
	var names;
	
	$(window).load(function() {
		$.ajax({ url: "commonToolController.do?getExcelNamesByEntity&entityName=" + entityName, success: function(data) {
			names = JSON.parse(data);
			$.each(names, function(index, name) {
				if(isString(name.id)){
					name.id = "'" + name.id + "'";
				} 
			
				var checked = selNames.indexOf("," + name.id + ",") >= 0;
				$("#excelContainer").append(
				'<tr width="100%">'
					+ '<td colspan="5" width="100%">'
						+ '<input type="checkbox" id="excel_' + name.id + '" name="excelNames" ' + (checked ? '' : 'checked="checked"') + ' onchange="onCateWithNameSelected(Excel, ' + name.id + ', this);" value="' + name.id + '">' + "     " + name.name
					+ '</td>'
				+ '</tr>');
			});
	 	}});
	});

	function onSelMultiNames() {
		var names = "";
		
		var unchecked = $("input[name='excelNames']").not("input:checked");  
		
		unchecked.each(function() {
			names += "," + $(this).parent("td").text().trim(); 
		});
		
		if (names.length > 0) {
			names += ",";
		}
		
		if($('input[name="excelNames"]:checked').length == 0){
			alert('请至少选择一个字段导出');
			return false;
		}
		
		if (windowapi){
			windowapi.opener.didSelMultiExcel("${hurl}",names);
			windowapi.close();
		}
		
		return false;
	}
	
	function delayClose(){ 
		if(windowapi){
			windowapi.close();
		}
	}
	
	function isString(obj){
		return Object.prototype.toString.call(obj) == "[object String]";
	}
</script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${hurl}" tiptype="1" beforeSubmit="onSelMultiNames()">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="value">
					<table width="100%" id="excelContainer">
						<tr width="100%">
							<td colspan="5" width="100%" style="background-color: red;">
								<input type="checkbox" id="excel" checked="checked" onchange="onAllWithNameSelected(Excel, this);">全选
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</t:formvalid>
</body>