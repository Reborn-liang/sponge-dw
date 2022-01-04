

var existMenuUis = new Array();

var menus;

//if (!menus) {
//	menus = storageGet("menus_" + timeKey);
//}


var menuClasses;

//if (!menuClasses) {
//	menuClasses = storageGet("menuClasses_" + timeKey);
//}


//menu
function loadDataForMenuInputToCombbox(parentTagName, defaultMenuComponentName, supportMultiMenus, didMenuUisLoaded, didSelectMenuClass, didSelectMenu) {
	var menuUi = new MenuUi();
	menuUi.setParentTagName(parentTagName);
	menuUi.setDefaultMenuComponentName(defaultMenuComponentName);
	menuUi.setSupportMultiMenus(supportMultiMenus);
	
	if (didMenuUisLoaded) {
		menuUi.setDidMenuUisLoaded(didMenuUisLoaded);
	} else {
		menuUi.setDidMenuUisLoaded(eval("didMenuUisLoaded"));
	}
	if (didSelectMenuClass) {
		menuUi.setDidSelectMenuClass(didSelectMenuClass);
	} else {
		menuUi.setDidSelectMenuClass(eval("didSelectMenuClass"));
	}
	
	if (didSelectMenu) {
		menuUi.setDidSelectMenu(didSelectMenu);
	} else {
		menuUi.setDidSelectMenu(eval("didSelectMenu"));
	}
	
	menuUi.beginInit();
	
	existMenuUis.push(menuUi);
	
	return menuUi;
}

function resetAllMenuInputToCombbox() {
	$.each(existMenuUis, function(index, menuUi) {
		menuUi.reset();
	});
}





function MenuUi() {
	var dumpId = generateUId();
	
	var parentTagName = null;
	var defaultMenuComponentName = null;
	var supportMultiMenus = false;
	
	var didMenuUisLoaded = null;
	var didSelectMenuClass = null;
	var didSelectMenu = null;
	
	var menuCombobox = null;
	
	var menuClassCombobox = null;
	
	this.setParentTagName = function (parentTagNameValue) {
		parentTagName = parentTagNameValue;
	};
	
	this.setDefaultMenuComponentName = function (defaultMenuComponentNameValue) {
		defaultMenuComponentName = defaultMenuComponentNameValue;
	};
	
	this.setSupportMultiMenus = function (supportMultiMenusValue) {
		supportMultiMenus = supportMultiMenusValue;
	};
	
	this.setDidMenuUisLoaded = function (didMenuUisLoadedFunc) {
		didMenuUisLoaded = didMenuUisLoadedFunc;
	};
	
	this.setDidSelectMenuClass = function (didSelectMenuClassFunc) {
		didSelectMenuClass = didSelectMenuClassFunc;
	};
	
	this.setDidSelectMenu = function (didSelectMenuFunc) {
		didSelectMenu = didSelectMenuFunc;
	};
	
	
	this.getMenuCombobox = function () {
		return menuCombobox;
	};
	
	this.getMenuClassCombobox = function () {
		return menuClassCombobox;
	};
	
	
	this.reset = function () {
		try {
			menuClassCombobox.combobox('clear');
		} catch (ex) {
		}
		try {
			menuCombobox.combobox('clear');
		} catch (ex) {
		}
	}
	
	this.beginInit = function () {
		if (!menuClasses || menuClasses.length == 0) {
			storageClear();
			
			$.ajax({
				url : "erpMenuController.do?listAll",
				success : function(data) {
					menus = JSON.parse(data);
					storageSet("menus_" + timeKey);
					menuInputToCombboxForLoadedData(parentTagName, menus);
					//
					initMenuClassUi(parentTagName);
				}
			});
		} else {
			menuInputToCombboxForLoadedData(parentTagName, menus);
			//
			initMenuClassUi(parentTagName);
		}
	};
	
	
	function menuInputToCombboxForLoadedData(parentTagName, menuData) {
		if (menuCombobox == null) {
			var mUiComp = null;
			if (defaultMenuComponentName) {
				mUiComp = getHtmlNodeObject(parentTagName).find("input[name='" + defaultMenuComponentName + "']");
			}
			if (mUiComp == null || mUiComp.length == 0) {
				mUiComp = getHtmlNodeObject(parentTagName).find("input[name='menuId']");
			}
			if (mUiComp == null || mUiComp.length == 0) {
				mUiComp = getHtmlNodeObject(parentTagName).find("input[name='belongMenuId']");
			}
			if (mUiComp == null || mUiComp.length == 0) {
				return;
			}
			menuCombobox = mUiComp;
			menuCombobox.attr("style","width:150px;");
		}
		
		menuCombobox.combobox({ 
			data:menuData,
			valueField:"id", 
			textField:"name",
			onSelect:onSelectMenu,
		});
	}

	function onSelectMenu(menu) {
		try {
			didSelectMenu(menu);
		} catch (e) {
		}
	}

	//ui
	function initMenuClassUi(parentTagName) {
		try {
			if (menuCombobox == null || menuCombobox.length == 0) {
				//alert("未找到menu控件");
				return;
			}
			var mUiComp = $("#menuClassId_" + dumpId);
			if (!mUiComp || mUiComp.length == 0) {
				var menuClassNode = '<span style="display:-moz-inline-box;display:inline-block;"><span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; " title="菜品分类">菜品分类：</span><input onkeypress="EnterPress(event)" onkeydown="EnterPress()" type="text" id="menuClassId_' + dumpId + '" name="menuClassId" class="inuptxt" style="width: 100px"></span>';
				var newNodeInsertUi = null;
				newNodeInsertUi = menuCombobox.parent();
				newNodeInsertUi.before(menuClassNode);
				
				if (supportMultiMenus) {
					var multiMenuNode = '<button id="btnMultiSelMenu" type="button" onclick="onMultMenuSelectClicked(' + dumpId + ');" style="margin: 5px;">菜品多选</button>';
					var multiMenuDiv = '<span id="divMultiMenuStatus" style="color: red;"></span>';
					var multiMenuInput = '<input id="menuIds" name="menuIds" type="hidden" />';
					
					newNodeInsertUi.after(multiMenuNode + multiMenuDiv + multiMenuInput);
				}
			}
			
			loadDataForMenuClassInputToCombbox(parentTagName);
		} catch (e) {
			alert("initMenuClassUi:" + e);
		}
	}

	//menu class
	function loadDataForMenuClassInputToCombbox(parentTagName) {
		if (!menuClasses || menuClasses.length == 0) {
			$.ajax({
				url : "erpMenuClassController.do?listAll",
				success : function(data) {
					try {
						menuClasses = JSON.parse(data);
						storageSet("menuClasses_" + timeKey);
						menuClassInputToCombboxForLoadedData(parentTagName);
					} catch (e) {
						alert("loadDataForProviceInputToCombbox:" + e);
					}
				}
			});
		} else {
			menuClassInputToCombboxForLoadedData(parentTagName);
		}
	}

	function menuClassInputToCombboxForLoadedData(parentTagName) {
		if (menuClasses == null || menuClasses.length <= 0) {
			return;
		}
		
		if (menuClassCombobox == null) {
			var mUiComp = $("#menuClassId_" + dumpId);
			if (mUiComp == null || mUiComp.length == 0) {
				return;
			}
			menuClassCombobox = mUiComp;
			menuClassCombobox.attr("style","width:100px;");
		}
		
		menuClassCombobox.combobox({ 
			data:menuClasses,
			valueField:"id", 
			textField:"name",
			onSelect:onSelectMenuClass,
		});
		
		
		try {
			didMenuUisLoaded();
		} catch (e) {
			debug(e);
		}
	}

	function onSelectMenuClass(data) {
		var menuClassMenus = new Array();
		try {
			var menuClassId = data.id;
			for (var i = 0; i < menus.length; i ++) {
				var menu = menus[i];
				if (menu.menuClassId == menuClassId) {
					menuClassMenus.push(menu);
				}
			}
			
			menuInputToCombboxForLoadedData(parentTagName, menuClassMenus);
		} catch (e) {
			alert("onSelectMenuClass:" + e);
		}
		
		try {
			didSelectMenuClass(data, menuClassMenus);
		} catch (e) {
		}
	}
}

//TODO, 如果这个也存在多个实例，需要重构
//for multi-select
function onMultMenuSelectClicked(dumpId) {
	var height = parseInt(window.innerHeight - 20);
	createwindow("选择菜品", "erpMenuController.do?multiSelMenus&selMenuIds=" + encodeURIComponent($("#menuIds").val()) + "&dumpId=" + encodeURIComponent(dumpId), 620, height);
}

function didSelMultiMenus(menuIds) {
	$("#menuIds").val(menuIds);
	if (!menuIds || menuIds.length == 0) {
		$("#divMultiMenuStatus").text("");
	} else {
		resetAllMenuInputToCombbox();
		$("#divMultiMenuStatus").text("已选" + (menuIds.split(",").length - 2) + "个菜品");
	}
}

