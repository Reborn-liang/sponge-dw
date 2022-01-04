<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorStdCtlList" checkbox="true" fitColumns="false" title="标准化管理" actionUrl="dwIndicatorStdCtlController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
   <t:dgDelOpt title="删除" url="dwIndicatorStdCtlController.do?doDel&id={id}" />
   <t:dgFunOpt exp="stdType#eq#T" title="生成视图" funname="genView(id)"></t:dgFunOpt>
   <t:dgCol title="ID"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="原表名"  field="originalTable" query="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="原字段名"  field="originalColumn"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="标准化类型"  field="stdType"  query="true"  queryMode="single" dictionary="DWStdType" width="120"></t:dgCol>
   <t:dgCol title="标准化名"  field="stdName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="标准化中文名"  field="chineseName" query="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="值域"  field="valueRangeCode" dictionary="t_s_typegroup,typegroupcode,typegroupname" query="true" formatterjs="valueRangeFormatter" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorStdCtlController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorStdCtlController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorStdCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorStdCtlController.do?goUpdate" funname="detail"></t:dgToolBar>
   <%-- <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar> --%>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <%-- <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/cn/nearf/dw/indicator/dwIndicatorStdCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#dwIndicatorStdCtlListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorStdCtlListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorStdCtlListtb").find("input[name='updateDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorStdCtlListtb").find("input[name='updateDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
	function valueRangeFormatter(value, rec, index) {
		var title = "值域";
		var url = "systemController.do?goTypeGridByCode&typegroupCode=" + rec.valueRangeCode;
		var href = "<a style='color:black;text-decoration:underline;' title='" + value + "' href='#' onclick=viewInfo('" + title + "','" + url + "')>";
		return href + value + '</a>';
	}
	
	function viewInfo(title, url) {
		openwindow(title, url, title, 350, 500);
	}
 
 function genView(id) {
		showProgress();
		$.ajax({url:"dwIndicatorStdCtlController.do?genView&id="+id, success: function(data) {
			try {
				var result = JSON.parse(data);
				hideProgress();
				alertTip(result.msg);
			} catch (ex) {
				hideProgress();
				alertTip(ex);
			}
	    }, error:function(textStatus, errorThrown) {
	    	hideProgress();
	    	alertTip(errorThrown);
	    }
	    });
}
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'dwIndicatorStdCtlController.do?upload', "dwIndicatorStdCtlList");
}

//导出
function ExportXls() {
	JeecgExcelExport("dwIndicatorStdCtlController.do?exportXls","dwIndicatorStdCtlList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorStdCtlController.do?exportXlsByT","dwIndicatorStdCtlList");
}
 </script>