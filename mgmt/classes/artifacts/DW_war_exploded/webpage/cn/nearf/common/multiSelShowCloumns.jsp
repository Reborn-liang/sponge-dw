<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>请选择导出的字段</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>

<script type="text/javascript" src="webpage/cn/nearf/common/common.js"></script>

<script type="text/javascript">
	var fields = '${fields}'
	var titles = '${titles}'
	var selectFields = '${selectFields}'
	
	
	$(window).load(function() {
		var f =  fields.split(',');
		var t =  titles.split(',');
		var allChecked = true;
		$.each(f, function(index, field) {
			var title = t[index];
			if(field != ''){
				var checked = selectFields.indexOf("," + field + ",") < 0;
				if(!checked){
					allChecked = false;
				}
				if(isString(field)){
					field = "'" + field + "'";
				}
				$("#cloumnContainer").append(
				'<tr width="100%">'
					+ '<td colspan="5" width="100%">'
						+ '<input type="checkbox" id="cloumn_' + field + '" name="filedTitle" ' + (checked ? 'checked="checked"' : '') + ' onchange="onCateWithNameSelected(Cloumn, ' + field + ', this);" value="' + field + '">' + "     " + title
					+ '</td>'
				+ '</tr>');
			}
		});
		if(!allChecked){
			$("#cloumn").removeAttr("checked");
		}
	});

	function onSelMultiUnCheckFiled() {
		var unCheckFileds = "";
		
		var unchecked = $("input[name='filedTitle']").not("input:checked");  
		
		unchecked.each(function() {
			unCheckFileds += "," + $(this).val(); 
		});
		
		if (unCheckFileds.length > 0) {
			unCheckFileds += ",";
		}
		var checkFileds = "";
		
		$('input[name="filedTitle"]:checked').each(function() {
			checkFileds += "," + $(this).val(); 
		});
		
		if (checkFileds.length > 0) {
			checkFileds += ",";
		}
		
		if($('input[name="filedTitle"]:checked').length == 0){
			alert('请至少选择一个字段显示');
			return false;
		}
		
		if (windowapi){
			windowapi.opener.didSelMultiCloumn(unCheckFileds,checkFileds);
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
	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${hurl}" tiptype="1" beforeSubmit="onSelMultiUnCheckFiled()">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td class="value">
					<table width="100%" id="cloumnContainer">
						<tr width="100%">
							<td colspan="5" width="100%" style="background-color: red;">
								<input type="checkbox" id="cloumn" checked="checked" onchange="onAllWithNameSelected(Cloumn, this);">全选
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</t:formvalid>
</body>