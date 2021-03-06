<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="tSSmsTemplateSqlList" checkbox="true" fitColumns="false" title="common.sqlDataTable" actionUrl="tSSmsTemplateSqlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="common.esId"  field="id"  hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.createName"  field="createName"  hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.createLoginName"  field="createBy"  hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.createDate"  field="createDate" formatter="yyyy-MM-dd" hidden="false"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateName"  field="updateName"  hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateByName"  field="updateBy"  hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.updateDate"  field="updateDate" formatter="yyyy-MM-dd" hidden="true"   queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.configurationCODE"  field="code" query="true" queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.configurationName"  field="name"  query="true" queryMode="single"  ></t:dgCol>
   <t:dgCol title="common.sqlBusinessId"  field="sqlId"  queryMode="single" dictionary="t_s_sms_sql,id,sql_name" ></t:dgCol>
   <t:dgCol title="common.msgModeliD"  field="templateId"  queryMode="single" dictionary="t_s_sms_template,id,template_name" ></t:dgCol>
   <t:dgCol title="common.opt" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="common.delete" url="tSSmsTemplateSqlController.do?doDel&id={id}" />
   <t:dgFunOpt funname="pushTest(id,code)" title="common.pushTest"></t:dgFunOpt>
   <t:dgToolBar title="common.add" icon="icon-add" url="tSSmsTemplateSqlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="common.edit" icon="icon-edit" url="tSSmsTemplateSqlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="common.batch.delete"  icon="icon-remove" url="tSSmsTemplateSqlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="common.search" icon="icon-search" url="tSSmsTemplateSqlController.do?goUpdate" funname="detail"></t:dgToolBar>
<%--   <t:dgToolBar title="??????" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
<%--   <t:dgToolBar title="??????" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
<%--   <t:dgToolBar title="????????????" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/system/sms/tSSmsTemplateSqlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//???????????????????????????
 			$("#tSSmsTemplateSqlListtb").find("input[name='createDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#tSSmsTemplateSqlListtb").find("input[name='updateDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 function pushTest(id,code){
	 $.getJSON("tSSmsTemplateSqlController.do?pushMsg&code="+code,
		function(result){
		 if (result.success){
			 alert("????????????");
		 }else {
			 alert("????????????:"+result.msg);
	     }
	  });
 }
//??????
function ImportXls() {
	openuploadwin('Excel??????', 'tSSmsTemplateSqlController.do?upload', "tSSmsTemplateSqlList");
}

//??????
function ExportXls() {
	JeecgExcelExport("tSSmsTemplateSqlController.do?exportXls","tSSmsTemplateSqlList");
}

//????????????
function ExportXlsByT() {
	JeecgExcelExport("tSSmsTemplateSqlController.do?exportXlsByT","tSSmsTemplateSqlList");
}
 </script>