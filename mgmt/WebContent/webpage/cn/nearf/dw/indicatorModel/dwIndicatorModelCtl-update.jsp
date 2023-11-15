<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模型定义</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  	 var factTable = "${dwIndicatorModelCtlPage.factTable}";
  	 function onFactSchemaChange(){
  		 var schema = $("#factSchema").val();
		 $.ajax({url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema, success: function(data) {
				try {
					var factSelect = $("#factTable");
					factSelect.empty(); 
					var result = JSON.parse(data).obj;
					for(var i=0; i<result.length; i++){
				        
						if(factTable == result[i].id){
							
				        	factSelect.append("<option value='"+result[i].id+"'" +" selected='selected'>"+result[i].name+"</option>");				        	
				        }else{
				        	factSelect.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
				        }
				        
					}
				} catch (ex) {
				}
				
		  }, error:function(textStatus, errorThrown) {
			  alertTip("请求错误，状态："+textStatus);
		  }
		});
  	 }
  	
  	var dimTable = "${dwIndicatorModelCtlPage.dimTable}";
  	function onDimSchemaChange(){
  		var schema = $("#dimSchema").val();
		 $.ajax({url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema, success: function(data) {
				try {
					var dimSelect = $("#dimTable");
					dimSelect.empty(); 
					var result = JSON.parse(data).obj;
					for(var i=0; i<result.length; i++){
				        
				        if(dimTable == result[i].id){
				        	dimSelect.append("<option value='"+result[i].id+"'" +" selected='selected'>"+result[i].name+"</option>");
				        }else{
				        	dimSelect.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");	
				        }
				        
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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="dwIndicatorModelCtlController.do?doUpdate" tiptype="1">
		<input id="id" name="id" type="hidden" value="${dwIndicatorModelCtlPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							模型编码:
						</label>
					</td>
					<td class="value">
					     	 <input id="modelCode" name="modelCode" type="text" style="width: 150px" class="inputxt"  datatype="*" value='${dwIndicatorModelCtlPage.modelCode}'>
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
					     	 <input id="name" name="name" type="text" style="width: 150px" class="inputxt" value='${dwIndicatorModelCtlPage.name}'>
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
					     	<textarea id="memo" name="memo" rows="3" cols="50" style="width: 350px; height: 90px">${dwIndicatorModelCtlPage.memo}</textarea>
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
								<option value="data" <c:if test="${dwIndicatorModelCtlPage.factSchema=='data'}">selected="selected"</c:if>>data</option>
								<option value="view" <c:if test="${dwIndicatorModelCtlPage.factSchema=='view'}">selected="selected"</c:if>>view</option>
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
					     	 <select id="factTable" name="factTable" datatype="*" style="width: 150px" value='${dwIndicatorModelCtlPage.factTable}'>
							</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表1</label>
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
								<option value="data" <c:if test="${dwIndicatorModelCtlPage.dimSchema=='data'}">selected="selected"</c:if>>data</option>
								<option value="view" <c:if test="${dwIndicatorModelCtlPage.dimSchema=='view'}">selected="selected"</c:if>>view</option>
						</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表2 Schema</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							表2:
						</label>
					</td>
					<td class="value">
							<select id="dimTable" name="dimTable" datatype="*" style="width: 150px" value='${dwIndicatorModelCtlPage.dimTable}'>
							</select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">表2</label>
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
								<option value="1" <c:if test="${dwIndicatorModelCtlPage.dimActiveFlg=='1'}">selected="selected"</c:if>>是</option>
								<option value="0" <c:if test="${dwIndicatorModelCtlPage.dimActiveFlg=='0'}">selected="selected"</c:if>>否</option>
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
							<option value="inner join" <c:if test="${dwIndicatorModelCtlPage.joinType=='inner join'}">selected="selected"</c:if>>inner join</option>
							<option value="left join" <c:if test="${dwIndicatorModelCtlPage.joinType=='left join'}">selected="selected"</c:if>>left join</option>
							<option value="right join" <c:if test="${dwIndicatorModelCtlPage.joinType=='right join'}">selected="selected"</c:if>>right join</option>
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
					     	 <%-- <input id="joinCondition" name="joinCondition" type="text" style="width: 350px" class="inputxt"  datatype="*" value='${dwIndicatorModelCtlPage.joinCondition}'>--%>
					     	 <textarea id="joinCondition" style="width:100%;" rows="8" name="joinCondition" datatype="*">${dwIndicatorModelCtlPage.joinCondition}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">关联条件</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>	