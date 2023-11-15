<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorModelCtlList" checkbox="true" fitColumns="false" title="模型定义" actionUrl="dwIndicatorModelCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="ID"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="模型编码"  field="modelCode" query="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="模型名称"  field="name" query="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="模型说明"  field="memo"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="表1 Schema"  field="factSchema"    queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="表1"  field="factTable"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="表2 Schema"  field="dimSchema"    queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="表2 "  field="dimTable"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="是否生效"  field="dimActiveFlg"  replace="是_1,否_0"   queryMode="group"  width="60"></t:dgCol>
   <t:dgCol title="关联类型"  field="joinType"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="关联条件"  field="joinCondition"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorModelCtlController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" funname="addModel"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" funname="editModel(id)"></t:dgToolBar>
   <t:dgToolBar title="克隆"  icon="icon-remove" url="dwIndicatorModelCtlController.do?clone" funname="clone" operationCode="Admin"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorModelCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorModelCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <%-- <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/indicatorModel/dwIndicatorModelCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		
 			$("#dwIndicatorModelCtlListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorModelCtlListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorModelCtlListtb").find("input[name='updateDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorModelCtlListtb").find("input[name='updateDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
 function addModel(id){
 	var url = 'dwIndicatorModelCtlController.do?goAdd';
 	
	
 	add('录入', url, 'dwIndicatorModelCtlList',900,600);
 }
 
 function editModel(id){
	var rowsData = $('#dwIndicatorModelCtlList').datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		tip('请选择一条记录');
		return;
	}
	if (rowsData.length > 1) {
		tip('请选择一条记录再操作');
		return;
	}
 	var url = 'dwIndicatorModelCtlController.do?goUpdate&id='+rowsData[0].id;
 	
	
 	add('编辑', url, 'dwIndicatorModelCtlList',900,600);
 }
 
 function clone(title,url,gname) {
		gridname=gname;
	    var ids = [];
	    var rows = $("#"+gname).datagrid('getSelections');
	    if (rows.length > 0) {
	    	$.dialog.confirm('你确定要克隆吗?', function(r) {
			   if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : url,
						type : 'post',
						data : {
							ids : ids.join(',')
						},
						cache : false,
						success : function(data) {
							var d = $.parseJSON(data);
							if (d.success) {
								var msg = d.msg;
								tip(msg);
								reloadTable();
								$("#"+gname).datagrid('unselectAll');
								ids='';
							}
						}
					});
				}
			});
		} else {
			tip("请选择需要克隆的数据");
		}
	}
 

function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorModelCtlController.do?upload', "dwIndicatorModelCtlList");
}


function ExportXls() {
	JeecgExcelExport("dwIndicatorModelCtlController.do?exportXls","dwIndicatorModelCtlList");
}


function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorModelCtlController.do?exportXlsByT","dwIndicatorModelCtlList");
}
 </script>