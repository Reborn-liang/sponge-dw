<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>指标定义</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  $(document).ready(function(){
	
	
	$('#tt').tabs({
	   onSelect:function(title){
		    $('#tt .panel-body').css('width','auto');
		}
	});
	$(".tabs-wrap").css('width','100%');
  });
  
  function onModelChange(){
	  var modelCode = $("select[name='modelCode'] option:selected").val();
	  if(modelCode!=null && modelCode!='undefined' && modelCode!=""){
		  $("#modelCodeInd").val(modelCode);
	  }else{
		  $("#modelCodeInd").val("");
	  }
  }

  
  function disabledSftp(){
	  var sftpFlg = $("select[name='sftpFlg'] option:selected").val();
	  if(sftpFlg=="Y"){
		  $("#dwIndicatorSftpCtl_table").find("input").removeAttr("disabled","disabled");
	  }
	  if(sftpFlg=="N"){
		  $("#dwIndicatorSftpCtl_table").find("input").attr("disabled","disabled");
	  }
  }
  
  function parseSQL(ctlId) {
	  console.log(ctlId);
	 
	
				$.ajax({
					url : "dwIndicatorCtlController.do?parseSQL&id="+ctlId,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							alterTip(msg);
						}
					}
				});
	
	
 }
  
 </script>
 </head>
 <body style="overflow-x: hidden;">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="dwIndicatorCtlController.do?doUpdate">
					<input id="id" name="id" type="hidden" value="${dwIndicatorCtlPage.id }">
					<input id="createDate" name="createDate" type="hidden" value="${dwIndicatorCtlPage.createDate }">
					<input id="updateDate" name="updateDate" type="hidden" value="${dwIndicatorCtlPage.updateDate }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">业务分组:</label>
			</td>
			<td class="value">
		     	 <input id="bizGroup" name="bizGroup" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorCtlPage.bizGroup}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">业务分组</label>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">编码:</label>
			</td>
			<td class="value" width="100px">
		     	 <input id="code" name="code" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorCtlPage.code}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">编码</label>
			</td>
		</tr>
		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">指标表名:</label>
			</td>
			<td class="value" width="100px">
		     	<input id="targetTable" name="targetTable" type="text" style="width: 150px" class="inputxt" datatype="*" value='${dwIndicatorCtlPage.targetTable}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">指标表名</label>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">类型:</label>
			</td>
			<td class="value">
					<t:dictSelect field="type" type="list" noNeedBlank="true"
						typeGroupCode="DWIndType" defaultVal="${dwIndicatorCtlPage.type}" hasLabel="false"  title="类型"></t:dictSelect>     
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">类型</label>
			</td>
		</tr>
		
		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">数据源来源:</label>
			</td>
			<td class="value">
				<t:dictSelect field="sourceType" type="list" noNeedBlank="false"
						typeGroupCode="DWSourceType"  hasLabel="false"  title="来源" extendJson="{style:'width:80px'}"  defaultVal="${dwIndicatorCtlPage.sourceType}"></t:dictSelect>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">数据源来源类型:</label>
			</td>
			<td class="value">
				<t:dictSelect field="loadType" type="list" noNeedBlank="false"
						typeGroupCode="DWSourceLoadType"  hasLabel="false"  title="数据源来源类型" extendJson="{style:'width:80px'}"  defaultVal="${dwIndicatorCtlPage.loadType}"></t:dictSelect>
			</td>
		</tr>
		
		<tr>
			<td align="right",width="100px">
				<label class="Validform_label">数据源目录:</label>
			</td>
			<td class="value">
		     	<input id="sourcePath" name="sourcePath" type="text" style="width: 350px" class="inputxt" maxlength="200" value="${dwIndicatorCtlPage.sourcePath}">
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">数据源目录</label>
			</td>
			<td align="right",width="100px">
				<label class="Validform_label">分隔符:</label>
			</td>
			<td class="value">
		     	 <input id="delimiter" name="delimiter" type="text" style="width: 150px" class="inputxt" maxlength="1" value="${dwIndicatorCtlPage.delimiter}">
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">分隔符</label>
			</td>
		</tr>
		
		<tr>
<%--			<td align="right" width="100px">
				<label class="Validform_label">状态:</label>
			</td>
			<td class="value" width="100px">
					<t:dictSelect field="status" type="list"
						typeGroupCode="DWIndStatus" defaultVal="${dwIndicatorCtlPage.status}" hasLabel="false"  title="状态"></t:dictSelect>     
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">状态</label>
			</td>--%>
			<td align="right" width="100px">
				<label class="Validform_label">业务主键:</label>
			</td>
			<td class="value">
		     	 <input id="bizKey" name="bizKey" type="text" style="width: 150px" class="inputxt" value='${dwIndicatorCtlPage.bizKey}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">业务主键</label>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">加载类型:</label>
			</td>
			<td class="value">
				<t:dictSelect field="jobIncFlg" type="list" noNeedBlank="true"
							  typeGroupCode="DWLoadType" defaultVal="${dwIndicatorCtlPage.jobIncFlg}" hasLabel="false"  title="加载类型"></t:dictSelect>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">加载类型</label>
			</td>
		</tr>
		<tr>
<%--			<td align="right" width="100px">
				<label class="Validform_label">执行周期类型:</label>
			</td>
			<td class="value">
				<t:dictSelect field="jobType" type="list" noNeedBlank="true"
							  typeGroupCode="DWExecPeriod" defaultVal="${dwIndicatorCtlPage.jobType}" hasLabel="false"  title="执行周期类型"></t:dictSelect>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">执行周期类型</label>
			</td>--%>
			<td align="right" width="100px">
				<label class="Validform_label">名称:</label>
			</td>
			<td class="value">
				<input id="name" name="name" type="text" style="width: 150px" class="inputxt" value='${dwIndicatorCtlPage.name}' datatype="*">
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">名称</label>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">是否需要下发:</label>
			</td>
			<td class="value">
				<t:dictSelect field="sftpFlg" type="list" datatype="*" noNeedBlank="true"
							  typeGroupCode="sf_yn" defaultVal="${dwIndicatorCtlPage.sftpFlg}" hasLabel="false"  title="是否需要下发" extendJson="{onchange:'disabledSftp()'}"></t:dictSelect>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">是否需要下发</label>
			</td>
		</tr>
<%--		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">模型编码:</label>
			</td>
			<td class="value">
					<t:dictSelect id="modelCode" field="modelCode" type="list" noNeedBlank="false"  extendJson="{onchange:'onModelChange()'}"
						dictTable="dw_indicator_model_ctl" dictField="distinct(model_code)" dictCondition="where dim_active_flg=1" dictText="name" defaultVal="${dwIndicatorCtlPage.modelCode}" hasLabel="false"  title="模型编码"></t:dictSelect>     
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">模型编码</label>
			</td>
			<td align="right" width="100px">
				<label class="Validform_label">数据保留天数:</label>
			</td>
			<td class="value">
		     	 <input id="historyKeepDays" name="historyKeepDays" type="text" style="width: 150px" class="inputxt" value='${dwIndicatorCtlPage.historyKeepDays}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">数据保留天数</label>
			</td>
		</tr>--%>
<%--		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">任务开始时间点:</label>
			</td>
			<td class="value">
		     	 <input id="jobStartTime" name="jobStartTime" type="text" style="width: 150px" class="inputxt" value='${dwIndicatorCtlPage.jobStartTime}'>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">任务开始时间点</label>
			</td>
		</tr>--%>
<%--		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">是否需要除重处理:</label>
			</td>
			<td class="value">
					<t:dictSelect field="duplicationCheckFlg" type="list" datatype="*" noNeedBlank="true"
						typeGroupCode="sf_yn" defaultVal="${dwIndicatorCtlPage.duplicationCheckFlg}" hasLabel="false"  title="是否需要除重处理" extendJson="{onchange:'disabledSftp()'}"></t:dictSelect>     
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">是否需要除重处理</label>
			</td>
		</tr>--%>
		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">SQL:</label>
			</td>
			<td class="value" colspan="1">
				 <textarea id="sqls" style="width:100%;" class="inputxt" rows="8" name="sqls">${dwIndicatorCtlPage.sqls}</textarea>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">SQL</label>
			</td>
			<td class="value" colspan="2">
				<div style="width: 100px";align="center" >
<%--					<input type="button" value="填充全量模板" onclick="doFill(0);">
					<br>
					<br>
					<input type="button" value="填充增量模板" onclick="doFill(1);">
					<br>
					<br>
					<input type="button" value="解析SQL" onclick="parseSQL(${dwIndicatorCtlPage.id});">--%>
				</div>
			</td>
		</tr>
		<tr>
			<td align="right" width="100px">
				<label class="Validform_label">描述:</label>
			</td>
			<td class="value" colspan="3">
				 <textarea id="memo" style="width:60%;" class="inputxt" rows="6" name="memo">${dwIndicatorCtlPage.memo}</textarea>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">描述</label>
			</td>
		</tr>
			</table>
			<div style="width: auto;height: 200px;">
				<%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
				<div style="width:800px;height:1px;"></div>
				<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
<%--				 <t:tab href="dwIndicatorCtlController.do?dwIndicatorColumnCtlList&id=${dwIndicatorCtlPage.id}" icon="icon-search" title="指标明细" id="dwIndicatorColumnCtl"></t:tab>--%>
				 <t:tab href="dwIndicatorCtlController.do?dwIndicatorSftpCtlList&id=${dwIndicatorCtlPage.id}&sftpFlg=${dwIndicatorCtlPage.sftpFlg}" icon="icon-search" title="下发配置" id="dwIndicatorSftpCtl"></t:tab>
				</t:tabs>
			</div>
			</t:formvalid>
			
		<table style="display:none">
		<tbody id="add_dwIndicatorSftpCtl_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
				  <td align="left">
					  	<input name="dwIndicatorSftpCtlList[#index#].id" maxlength="11" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		datatype="*">
					  <label class="Validform_label" style="display: none;">ID</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorSftpCtlList[#index#].indicatorId" maxlength="10" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		datatype="*">
					  <label class="Validform_label" style="display: none;">指标ID</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorSftpCtlList[#index#].sftpServer" maxlength="100" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">SFTP服务器地址</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorSftpCtlList[#index#].sourceFolder" maxlength="300" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">原目录</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorSftpCtlList[#index#].targetFolder" maxlength="300" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">目标目录</label>
				  </td>
			</tr>
		 </tbody>
		<tbody id="add_dwIndicatorColumnCtl_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
				  <td align="left">
							<t:dictSelect field="dwIndicatorColumnCtlList[#index#].type" type="list" extendJson="{style:'width:80px'}"
										noNeedBlank="true" typeGroupCode="DWFileType" defaultVal="" hasLabel="false"  title="字段类型"></t:dictSelect>     
					  <label class="Validform_label" style="display: none;">字段类型</label>
				  </td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[#index#].fromSchema" name="dwIndicatorColumnCtlList[#index#].fromSchema" style="width: 120px" onchange="onFactSchemaChange(#index#);onFromColumnSchemaChange(#index#)"><option value=''>---请选择---</option>
					  		<option value="data">data</option>
							<option value="view">view</option>
					  	</select>	
					 	<span class="Validform_checktip"></span>
					  <label class="Validform_label" style="display: none;">源schema</label>
					</td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[#index#].fromTable" name="dwIndicatorColumnCtlList[#index#].fromTable" style="width: 150px" onchange="onFromColumnSchemaChange(#index#)">
						</select>
						<span class="Validform_checktip"></span>	
					   <label class="Validform_label" style="display: none;">源表</label>
				  </td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[#index#].fromColumn" name="dwIndicatorColumnCtlList[#index#].fromColumn" style="width: 150px" onchange="onFromColumnChange(#index#)">
						</select>
					  <label class="Validform_label" style="display: none;">源字段</label>
				 </td>
				 <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].formula" maxlength="255" datatype="*" type="text" class="inputxt"  style="width:157px" value="${poVal.formula}">
					    <label class="Validform_label" style="display: none;">公式</label>
				  </td>	
				  <td align="left">
					  <button  style="width:40px;" type="button" onclick="formula(#index#)">扩展</button>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].indicatorColumn" maxlength="40" 
					  		type="text" class="inputxt"  style="width:100px;"
					  		datatype="*">
					  <label class="Validform_label" style="display: none;">指标字段</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].columnType" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;" datatype="*">
					  <label class="Validform_label" style="display: none;">字段类型</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].columnLength" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">字段长度</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].exportName" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">导出字段名</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].exportOrder" maxlength="10" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		>
					  <label class="Validform_label" style="display: none;">导出排序位置</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[#index#].filters" maxlength="2000" 
					  		type="text" class="inputxt"  style="width:300px;">
					  <label class="Validform_label" style="display: none;">过滤条件</label>
				  </td>
				  <td align="left">
					  <button  style="width:40px;" type="button" onclick="a(#index#)">扩展</button>
				  </td>
			</tr>
		 </tbody>
		</table>
 </body>
 <script src = "webpage/cn/nearf/dw/indicator/dwIndicatorCtl.js"></script>	