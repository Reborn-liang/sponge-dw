<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="tSSmsTemplateList" checkbox="true" fitColumns="false" title="common.msgTemplateTable" actionUrl="tSSmsTemplateController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="common.isId"  field="id"  hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.createName"  field="createName"  hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.create.By"  field="createBy"  hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.createDate"  field="createDate" formatter="yyyy-MM-dd" hidden="false"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateName"  field="updateName"  hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateByName"  field="updateBy"  hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateDate"  field="updateDate" formatter="yyyy-MM-dd" hidden="true"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.templateName"  field="templateName"  query="true" queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.templateType"  field="templateType" query="true" queryMode="single" dictionary="msgTplType" ></t:dgCol>
   <t:dgCol title="common.templateContent"  field="templateContent"  queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.opt" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="common.deleteTo" url="tSSmsTemplateController.do?doDel&id={id}" />
   <t:dgToolBar title="common.icon.add" icon="icon-add" url="tSSmsTemplateController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="common.icon.edit" icon="icon-edit" url="tSSmsTemplateController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="common.icon.remove"  icon="icon-remove" url="tSSmsTemplateController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="common.icon.search" icon="icon-search" url="tSSmsTemplateController.do?goUpdate" funname="detail"></t:dgToolBar>
<%--   <t:dgToolBar title="??????" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
<%--   <t:dgToolBar title="??????" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
<%--   <t:dgToolBar title="????????????" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/system/sms/tSSmsTemplateList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//???????????????????????????
 			$("#tSSmsTemplateListtb").find("input[name='createDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#tSSmsTemplateListtb").find("input[name='updateDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//??????
function ImportXls() {
	openuploadwin('Excel??????', 'tSSmsTemplateController.do?upload', "tSSmsTemplateList");
}

//??????
function ExportXls() {
	JeecgExcelExport("tSSmsTemplateController.do?exportXls","tSSmsTemplateList");
}

//????????????
function ExportXlsByT() {
	JeecgExcelExport("tSSmsTemplateController.do?exportXlsByT","tSSmsTemplateList");
}
 </script>