<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模型定义</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  	 function onFactSchemaChange(){
  		 var schema = $("#factSchema").val();
		 $.ajax({url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema, success: function(data) {
				try {
					var factSelect = $("#factTable");
					factSelect.empty(); //clean last data first
					var result = JSON.parse(data).obj;
					factSelect.append("<option value=''>请选择</option>");
					for(var i=0; i<result.length; i++){
				        //alert("name:"+result[i].name+", id:"+result[i].id);
				        factSelect.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
					}
				} catch (ex) {
				}
				
		  }, error:function(textStatus, errorThrown) {
			  alertTip("请求错误，状态："+textStatus);
		  }
		});
  	 }
  	 
  	function onDimSchemaChange(){
  		var schema = $("#dimSchema").val();
		 $.ajax({url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema, success: function(data) {
				try {
					var factSelect = $("#dimTable");
					factSelect.empty(); //clean last data first
					var result = JSON.parse(data).obj;
					factSelect.append("<option value=''>请选择</option>");
					for(var i=0; i<result.length; i++){
				        //alert("name:"+result[i].name+", id:"+result[i].id);
				        factSelect.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
					}
				} catch (ex) {
				}
				
		  }, error:function(textStatus, errorThrown) {
			  alertTip("请求错误，状态："+textStatus);
		  }
		});
	 }
  </script>
 </head>
 <body onload="onFactSchemaChange();onDimSchemaChange();">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorModelCtlController.do?doAdd" tiptype="1">
					<input id="id" name="id" type="hidden" value="${dwIndicatorModelCtlPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							模型编码:
						</label>
					</td>
					<td class="value">
					     	 <input id="modelCode" name="modelCode" type="text" style="width: 150px" class="inputxt"  datatype="*">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">模型编码</label>
						</td>
				<tr>
					<td align="right">
						<label class="Validform_label">
							模型名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="name" name="name" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">模型名称</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							模型说明:
						</label>
					</td>
					<td class="value">
					     	<textarea id="memo" name="memo" rows="3" cols="50" style="width: 350px; height: 90px"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">模型说明</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							表1 Schema:
						</label>
					</td>
					<td class="value">
						<select id="factSchema" name="factSchema" datatype="*" style="width: 150px" onchange="onFactSchemaChange();">
								<option value="data">data</option>
								<option value="view">view</option>
						</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表1 Schema</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							表1:
						</label>
					</td>
					<td class="value">
					     	 <select id="factTable" name="factTable" datatype="*" style="width: 150px">
							</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表1 </label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							表2 Schema:
						</label>
					</td>
					<td class="value">
						<select id="dimSchema" name="dimSchema" datatype="*" style="width: 150px" onchange="onDimSchemaChange();">
								<option value="data">data</option>
								<option value="view">view</option>
						</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表2 Schema</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							表2 :
						</label>
					</td>
					<td class="value">
							<select id="dimTable" name="dimTable" datatype="*" style="width: 150px">
							</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表2 </label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							是否生效:
						</label>
					</td>
					<td class="value">
						<select id="dimActiveFlg" name="dimActiveFlg" datatype="*" style="width: 150px">
								<option value="1">是</option>
								<option value="0">否</option>
						</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">维表是否生效</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							关联类型:
						</label>
					</td>
					<td class="value">
						<select id="joinType" name="joinType" datatype="*" style="width: 150px">
							<option value="inner join">inner join</option>
							<option value="left join">left join</option>
							<option value="right join">right join</option>
						</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">关联类型</label>
						</td>
				<tr>
					<td align="right">
						<label class="Validform_label">
							关联条件:
						</label>
					</td>
					<td class="value">
					     	 <%-- <input id="joinCondition" name="joinCondition" type="text" style="width: 350px" class="inputxt"  datatype="*"> --%>
					     	 <textarea id="joinCondition" style="width:100%;" rows="8" name="joinCondition" datatype="*"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">关联条件</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/cn/nearf/dw/indicatorModel/dwIndicatorModelCtl.js"></script>		