<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>修改首页样式</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?savestyle" layout="table">
	<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
		<tbody><th>首页风格</th>
			<tr>
				<td class="value"><input type="radio" value="default" name="indexStyle" /> <span>经典风格</span></td>
			</tr>
			
			<tr>
				<td class="value"><input type="radio" value="shortcut" name="indexStyle" /> <span>ShortCut风格</span></td>
			</tr>
			<tr>
				<td class="value"><input type="radio" value="sliding" name="indexStyle"  /><span>Sliding云桌面</span></td>
			</tr>
			<tr>
				<td class="value"><input type="radio" value="ace" name="indexStyle"  /><span>ACE平面风格</span></td>
			</tr>
				
		</tbody>
	</table>
</t:formvalid>
</body>
<script type="text/javascript">
		$(function(){
			var val = "${indexStyle}";
			$("input[name='indexStyle']").each(function(){
				if($(this).val()==val){
					$(this).attr("checked",true);
					return false;
				}
			});
		
		});
	</script>
</html>
