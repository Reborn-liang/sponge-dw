<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="dwIndicatorCtlList" checkbox="true" fitColumns="false" title="指标管理" actionUrl="dwIndicatorCtlController.do?datagrid" idField="id" fit="true" queryMode="group"><t:dgCol title="ID"  field="id"  queryMode="single"  width="120"></t:dgCol><t:dgCol title="操作" field="opt" width="270"></t:dgCol>
   <t:dgFunOpt exp="type#ne#SRC,CALC" title="生效" funname="effect(id)"></t:dgFunOpt>
   <t:dgFunOpt exp="type#eq#SRC" title="生成标准化配置" funname="genStd(id)"></t:dgFunOpt>
   <t:dgFunOpt title="创建表" exp="type#ne#CALC" funname="createTable(id)"></t:dgFunOpt>
   <t:dgFunOpt title="运行指标" funname="runIndicator(id)"></t:dgFunOpt>
   <t:dgFunOpt title="删除指标" funname="deleteIndicator(id)"></t:dgFunOpt>
   <t:dgCol title="业务分组"  field="bizGroup"    queryMode="single"  width="120" query="true"></t:dgCol>
   <t:dgCol title="编码"  field="code"    queryMode="single"  width="120" query="true"></t:dgCol>
   <t:dgCol title="指标表名"  field="targetTable"    queryMode="single"  width="120" query="true"></t:dgCol>
   <t:dgCol title="类型"  field="type"    queryMode="single" dictionary="DWIndType" width="80" query="true"></t:dgCol>
   <t:dgCol title="数据源来源"  field="sourceType"  queryMode="single" dictionary="DWSourceType" width="80" query="true"></t:dgCol>
   <t:dgCol title="数据源加载类型"  field="loadType"  queryMode="single" dictionary="DWSourceLoadType" width="80" query="true"></t:dgCol>
   <t:dgCol title="状态"  field="status"    queryMode="single" dictionary="DWIndStatus" width="50" query="true"></t:dgCol>
   <t:dgCol title="业务主键"  field="bizKey"    queryMode="group"  width="120" ></t:dgCol>
   <t:dgCol title="模型编码"  field="modelCode"    queryMode="single" dictionary="dw_indicator_model_ctl,model_code,name"  width="120"></t:dgCol>
   <t:dgCol title="数据保留天数"  field="historyKeepDays"    queryMode="group"  width="60"></t:dgCol>
   <t:dgCol title="执行周期"  field="jobType"  query="true" queryMode="single" dictionary="DWExecPeriod" width="60"></t:dgCol>
   <t:dgCol title="加载类型"  field="jobIncFlg"    queryMode="single" dictionary="DWLoadType" width="60" query="true"></t:dgCol>
   <t:dgCol title="任务开始时间点"  field="jobStartTime"    queryMode="group"  width="80"></t:dgCol>
   <t:dgCol title="是否需要下发"  field="sftpFlg"    queryMode="single" dictionary="sf_yn" width="60" query="true"></t:dgCol>
   <t:dgCol title="是否需要除重处理"  field="duplicationCheckFlg"    queryMode="single"  dictionary="sf_yn" width="120" query="true"></t:dgCol>
   <t:dgCol title="名称"  field="name"    queryMode="group"  width="120" ></t:dgCol>
   <t:dgCol title="描述"  field="memo"    queryMode="group"  width="160"></t:dgCol>
   <t:dgCol title="SQL"  field="sqls"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <%-- <t:dgDelOpt title="删除" url="dwIndicatorCtlController.do?doDel&id={id}" /> --%>
   <t:dgToolBar title="录入" icon="icon-add" url="dwIndicatorCtlController.do?goAdd" funname="add" width="100%" height="100%"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="dwIndicatorCtlController.do?goUpdate" funname="update" width="100%" height="100%"></t:dgToolBar>
   <t:dgToolBar title="预览" icon="icon-add" url="dwIndicatorCtlController.do?goPreview" funname="update" width="100%" height="100%"></t:dgToolBar>
<%--   <t:dgToolBar title="立即运行" icon="icon-add" url="dwIndicatorCtlController.do?goRerun" funname="update"></t:dgToolBar>--%>
   <%-- <t:dgToolBar title="批量删除"  icon="icon-remove" url="dwIndicatorCtlController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar> --%>
   <t:dgToolBar title="查看" icon="icon-search" url="dwIndicatorCtlController.do?goUpdate" funname="detail" width="100%" height="100%"></t:dgToolBar>
<%--   <t:dgToolBar title="导出指标" icon="icon-putout" url="dwIndicatorCtlController.do?goExport&load=detail" funname="detail"></t:dgToolBar>--%>
<%--   <t:dgToolBar title="导入指标" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%-- 
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
<<style>

</style>
 
 <script src = "webpage/cn/nearf/dw/indicator/dwIndicatorCtlList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		
 			$("#dwIndicatorCtlListtb").find("input[name='createDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorCtlListtb").find("input[name='createDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorCtlListtb").find("input[name='updateDate_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#dwIndicatorCtlListtb").find("input[name='updateDate_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });

 function createTable(id) {
  sendFlaskAPI(id, "createTable")
 }

 function runIndicator(id) {
  sendFlaskAPI(id, "runIndicator")
 }

 function deleteIndicator(id) {
  sendFlaskAPI(id, "deleteIndicator")
 }

 function sendFlaskAPI(id, method) {
  showProgress();
  $.ajax({
   url: "dwIndicatorCtlController.do?" + method + "&id=" + id, success: function (data) {
    try {
     var result = JSON.parse(data);
     hideProgress();
     alertTip(result.msg);
    } catch (ex) {
     hideProgress();
     alertTip(ex);
    }
   }, error: function (textStatus, errorThrown) {
    hideProgress();
    alertTip(errorThrown);
   }
  });
 }


function ImportXls() {
	openuploadwin('导入', 'dwIndicatorCtlController.do?upload', "dwIndicatorCtlList");
}


function ExportXls() {
	JeecgExcelExport("dwIndicatorCtlController.do?exportXls","dwIndicatorCtlList");
}


function ExportXlsByT() {
	JeecgExcelExport("dwIndicatorCtlController.do?exportXlsByT","dwIndicatorCtlList");
}

function goRun(id) {
		var width = parseInt(window.innerWidth);
		var height = parseInt(window.innerHeight + 50);

		var rowsData = $('#dwIndicatorCtlList').datagrid('getSelections');
		if (!rowsData || rowsData.length == 0) {
			tip('请选择一条记录');
			return;
		}
		if (rowsData.length > 1) {
			tip('请选择一条记录再操作');
			return;
		}
		$.dialog.confirm('你确定要立即运行吗?', function(r) {
		   if (r) {
				$.ajax({
					url : "dwIndicatorCtlController.do?goRerun&id="+rowsData[0].id,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							reloadTable();
							$("#dwIndicatorCtlList").datagrid('unselectAll');
						}
					}
				});
			}
		});
}

function effect(id) {
	showProgress();
	$.ajax({url:"dwIndicatorCtlController.do?effectIndicator&id="+id, success: function(data) {
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

function genStd(id) {
	showProgress();
	$.ajax({url:"dwIndicatorCtlController.do?genStd&id="+id, success: function(data) {
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
 </script>