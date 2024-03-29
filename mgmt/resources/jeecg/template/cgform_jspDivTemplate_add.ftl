<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>${ftl_description}</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="${entityName?uncap_first}Controller.do?doAdd" tiptype="1">
		<#list columns as po>
			<#if po.isShow == 'N'>
				<input id="${po.fieldName}" name="${po.fieldName}" type="hidden" value="${'$'}{${entityName?uncap_first}Page.${po.fieldName} }">
			</#if>
		</#list>
		<fieldset class="step">
		<#list columns as po>
			<#if po.isShow == 'Y'>
			<div class="form">
		      <label class="Validform_label">${po.content}:</label>
		      <#if po.showType=='text'>
		     	 <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="inputxt" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='long'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
			  <#elseif po.showType=='popup'>
			<input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="searchbox-inputtext" <#if po.fieldValidType?if_exists?html != ''>datatype="${po.fieldValidType?if_exists?html}" <#else><#if po.type == 'int'>datatype="n" <#elseif po.type=='long'>datatype="n" <#elseif po.type=='double'>datatype="/^(-?\d+)(\.\d+)?$/" <#else><#if po.isNull != 'Y'>datatype="*" </#if></#if></#if><#if po.dictTable?if_exists?html!=""> onclick="inputClick(this,'${po.dictField}','${po.dictTable}')"</#if>>			 
			  <#elseif po.showType=='textarea'>
				 <textarea id="${po.fieldName}" style="width:600px;" class="inputxt" rows="6" name="${po.fieldName}" ></textarea>
		      <#elseif po.showType=='password'>
		      	<input id="${po.fieldName}" name="${po.fieldName}" type="password" style="width: 150px" class="inputxt" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='long'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
				<#elseif po.showType=='radio' || po.showType=='select' || po.showType=='checkbox' || po.showType=='list'>	 
					<t:dictSelect field="${po.fieldName}" type="${po.showType?if_exists?html}"
										<#if po.dictTable?if_exists?html != ''>dictTable="${po.dictTable?if_exists?html}" dictField="${po.dictField?if_exists?html}" dictText="${po.dictText?if_exists?html}"<#else>typeGroupCode="${po.dictField}"</#if> defaultVal="${'$'}{${entityName?uncap_first}Page.${po.fieldName}}" hasLabel="false"  title="${po.content}"></t:dictSelect>     
				<#elseif po.showType=='date'>
					  <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()"<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if>>
		      	<#elseif po.showType=='datetime'>
					  <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if>>
				<#elseif po.showType=='file'>
					  <input type="hidden" id="${po.fieldName}" name="${po.fieldName}" />
						 <a  target="_blank" id="${po.fieldName}_href">暂时未上传文件</a>
					     <input class="ui-button" type="button" value="上传附件"
									onclick="commonUpload(function(url,name){$("#${po.fieldName}_href").attr('src',url).html('下载');$("#${po.fieldName}").val(url);})"/>
		      	<#else>
		      		<input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="inputxt"<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='long'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
				</#if>	   
		      <span class="Validform_checktip"></span>
		    </div>
			</#if>
			</#list>
	    </fieldset>
  </t:formvalid>
 </body>
  <script src = "webpage/${bussiPackage?replace('.','/')}/${entityPackage}/${entityName?uncap_first}.js"></script>		