

var defaultEmpoyeeComponentName = "employeeIds";

var employeeIdUi;


function initEmployeeIdSelectUi(parentTagName) {
	if (defaultEmpoyeeComponentName) {
		employeeIdUi = getHtmlNodeObject(parentTagName).find("input[name='" + defaultEmpoyeeComponentName + "']");
	}
	if (employeeIdUi == null || employeeIdUi.length == 0) {
		employeeIdUi = getHtmlNodeObject(parentTagName).find("input[name='storeId']");
	}
	if (employeeIdUi == null || employeeIdUi.length == 0) {
		employeeIdUi = getHtmlNodeObject(parentTagName).find("input[name='belongStoreId']");
	}
	
	if (employeeIdUi && employeeIdUi.length > 0) {
		employeeIdUi.attr("style","width:200px;");
		
		employeeIdUi.bind("input", function() {
			didSelMultiEmployeeIds(employeeIdUi.val());
		});
		
		var newNodeInsertUi = employeeIdUi.parent();
		
		var multiEmployeeNode = '<button id="btnMultiSelEmployee" type="button" onclick="onMultEmployeeSelectClicked();" style="margin: 5px;">员工编号多选</button>';
		var multiEmployeeDiv = '<span id="divMultiEmployeeStatus" style="color: red;"></span>';
		
		newNodeInsertUi.after(multiEmployeeNode + multiEmployeeDiv);
	}
	
	
}



//for multi-select
function onMultEmployeeSelectClicked() {
	createwindow("员工编号多选", "erpEmployeeController.do?multiSelEmployeeIds&selEmployeeIds=" + encodeURIComponent(employeeIdUi.val()), 620);
}

function didSelMultiEmployeeIds(employeeIds) {
	employeeIdUi.val(employeeIds);
	if (!employeeIds || employeeIds.length == 0) {
		$("#divMultiEmployeeStatus").text("");
	} else {
		$("#divMultiEmployeeStatus").text("已选" + (employeeIds.split(",").length) + "个员工");
	}
}


function onSearchReseted() {
	$("#divMultiEmployeeStatus").text("");
}











