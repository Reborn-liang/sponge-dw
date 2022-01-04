var menuClassCompUil;

var menuClassesValueId;

var menuClassesShowSelName;

var hasClose = false;

//ui
function updateMenuClassUi(parentTagName, menuClassInputId, menuClassValueId, showSelName, selClose) {
	try {
		var mUiComp = getHtmlNodeObject(parentTagName).find("input[name='" + menuClassInputId + "']");
		if (mUiComp == null || mUiComp.length == 0) {
			//alert("未找到menuClass控件");
			return;
		}
		
		
		if (!menuClassValueId) {
			menuClassValueId = "menuClassIds";
		}
		menuClassesValueId = menuClassValueId;
		
		menuClassesShowSelName = showSelName;
		if (menuClassesShowSelName) {
			mUiComp.attr("readonly", "readonly");
		}
		hasClose = selClose;
		
		var multiMenuClassNode = '<button id="btnMultiSelMenuClass" type="button" onclick="onMultMenuClassSelectClicked();" style="margin: 5px;">分类多选</button>';
		var multiMenuClassDiv = '<span id="divMultiMenuClassStatus" style="color: red;"></span>';
		var multiMenuClassInput = '<input id="' + menuClassesValueId + '" name="' + menuClassesValueId + '" type="hidden" />';
		
		mUiComp.after(multiMenuClassNode + multiMenuClassDiv + multiMenuClassInput);
		
		menuClassCompUil = mUiComp;
	} catch (e) {
		alert("updateMenuClassUi:" + e);
	}
}




//for multi-select
function onMultMenuClassSelectClicked() {
	var height = parseInt(window.innerHeight - 20);
	
	if (hasClose) {
		window.open("erpMenuClassController.do?hasClose=1&multiSelMenuClasses&selMenuClassIds=" + encodeURIComponent($("#" + menuClassesValueId).val()), "选择菜品分类", 'width=620,height=' + height);
	} else {
		createwindow("选择菜品分类", "erpMenuClassController.do?multiSelMenuClasses&selMenuClassIds=" + encodeURIComponent($("#" + menuClassesValueId).val()), 620, height);
	}
}

function didSelMultiMenuClasses(menuClassIds, menuClassNames) {
	$("#" + menuClassesValueId).val(menuClassIds);
	
	if (menuClassesShowSelName) {
		menuClassCompUil.val(menuClassNames);
	}
	
	try {
		if (!menuClassIds || menuClassIds.length == 0) {
			$("#divMultiMenuClassStatus").text("");
		} else {
			menuClassCompUil.combobox('clear');
			$("#divMultiMenuClassStatus").text("已选" + (menuClassIds.split(",").length - 2) + "个分类");
		}
	} catch (e) {
	}
	
	
}


