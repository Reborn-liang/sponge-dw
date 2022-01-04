var printTypesData = [
  	{"id":0, "name":"收银小票"},
	{"id":1, "name":"吧台制作"},
	{"id":2, "name":"甜品制作"},
	{"id":3, "name":"厨房制作"}
];

var SplitPrintType = [
	{"id":0, "name":"总单"},
	{"id":1, "name":"分单"},
	{"id":2, "name":"分单分数量"}
];

var couponTypesData = [
	{"id":0, "name":"代金券"},
	{"id":1, "name":"折扣券"},
	{"id":2, "name":"兑换券"}
];

var yesNoData = [
	{"id":0, "name":"否"},
	{"id":1, "name":"是"}
];

//var genderData = [
// 	{"id":1, "name":"男"},
// 	{"id":2, "name":"女"}
//];

//var marryStatusData = [
//	{"id":0, "name":"未婚"},
//	{"id":1, "name":"已婚"},
//	{"id":2, "name":"离异"},
//	{"id":3, "name":"丧偶"},
//];

var relativeData = [
	{"id":"配偶", "name":"配偶"},
	{"id":"父子", "name":"父子"},
	{"id":"父女", "name":"父女"},
	{"id":"母子", "name":"母子"},
	{"id":"母女", "name":"母女"},
	{"id":"兄弟", "name":"兄弟"},
	{"id":"兄妹", "name":"兄妹"},
	{"id":"姐弟", "name":"姐弟"},
	{"id":"姐妹", "name":"姐妹"},
];


var bankData = [
	{"id":"建设银行", "name":"建设银行"},
	{"id":"兴业银行", "name":"兴业银行"},
];




var weekdayData = [
	{"id":0, "name":"无限制"},
	{"id":1, "name":"星期一"},
	{"id":2, "name":"星期二"},
	{"id":3, "name":"星期三"},
	{"id":4, "name":"星期四"},
	{"id":5, "name":"星期五"},
	{"id":6, "name":"星期六"},
	{"id":7, "name":"星期日"}
];

var paymentTypesData = [
    {"id":1, "name":"现金"},
	{"id":2, "name":"支付宝"},
	{"id":3, "name":"微信"},
	{"id":4, "name":"外卖"},
	{"id":5, "name":"余额"},
	{"id":6, "name":"余额+支付宝"},
	{"id":7, "name":"余额+微信"},
];

var rechargePaymentTypesData = [
	{"id":1, "name":"现金"},
	{"id":2, "name":"支付宝"},
	{"id":3, "name":"微信"},
];

var openIdPlatformTypesData = [
	{"id":0, "name":""},
	{"id":1, "name":"微信"},
	{"id":2, "name":"支付宝"},
	{"id":3, "name":"其他"},
];


var statClassesData = [
	{"id":0, "name":"前厅其他"},
	{"id":1, "name":"后厨"},
	{"id":2, "name":"前厅酒水"},
];

var menuPackageTypeData = [
	{"id":0, "name":"普通套餐"},
	{"id":1, "name":"团购套餐"},
];

var posMsgStatusData = [
	{"id":0, "name":"未处理"},
	{"id":1, "name":"推送成功"},
	{"id":2, "name":"已收到"},
	{"id":-1, "name":"推送失败"}
];

var orderStatusData = [
//	{"id":0, "name":"新建"},
//	{"id":null, "name":"新建"},
//	{"id":"null", "name":"新建"},
	{"id":1, "name":"等待付款"},
	{"id":2, "name":"已付款"},
];


var refundStatusData = [
 	{"id":0, "name":"未退菜或退单"},
 	{"id":null, "name":"未退菜或退单"},
 	{"id":"null", "name":"未退菜或退单"},
 	{"id":1, "name":"等待退菜"},
 	{"id":2, "name":"已经退菜"},
 	{"id":3, "name":"等待退单"},
 	{"id":4, "name":"已经退单"},
];

var orderRefundStatusData = [
	{"id":0, "name":"未退单"},
	{"id":null, "name":"未退单"},
	{"id":"null", "name":"未退单"},
	{"id":3, "name":"等待退单"},
	{"id":4, "name":"已经退单"},
];

var orderDetailRefundStatusData = [
 	{"id":0, "name":"未退菜"},
 	{"id":null, "name":"未退菜"},
 	{"id":"null", "name":"未退菜"},
 	{"id":1, "name":"等待退菜"},
 	{"id":2, "name":"已经退菜"},
];


var rechargeEnterSourceData = [
	{"id":1, "name":"线下扫描"},
	{"id":2, "name":"线上扫描"},
];

var rechargeStatusData = [
	{"id":1, "name":"新建"},
	{"id":2, "name":"已支付"},
];


var posPayModeData = [
	{"id":1, "name":"先付"},
	{"id":2, "name":"后付"},
];

var oldMemberStatusData = [
	{"id":0, "name":"未绑定"},
	{"id":1, "name":"已绑定"},
 ];


var leaveJobReasonData = [
	{"id":"试用期不合格", "name":"试用期不合格"},
	{"id":"正常离职", "name":"正常离职"},
	{"id":"非正常离职", "name":"非正常离职"},
	{"id":"自动离职", "name":"自动离职"},
	{"id":"协商劝退", "name":"协商劝退"},
	{"id":"违纪辞退", "name":"违纪辞退"},
	{"id":"重大违纪", "name":"重大违纪"},
	{"id":"违法", "name":"违法"},
];


function filteData(data, besides, key) {
	var fData = [];
	var fStr = besides.split(",");
	if (!key) {
		key = "id";
	}
	$.each(data, function(index, content) {
		var has = true;
		$.each(fStr, function(ind, bes) {
			if (bes && bes.length > 0) {
				if (content[key] == bes) {
					has = false;
					return false;
				}
			}
		});
		
		if (has) {
			fData.push(content);
		}
	});
	return fData;
}

//




//for page select
var dataForList;
var hasCheckedUi = false;
var dataForUiComponent;
var dataForListId;
var dataForListValue;

function loadListForData(urlStr, uiComponent, idName, valueName) {
	dataForUiComponent = uiComponent;
	dataForListId = idName;
	dataForListValue = valueName;
	$.ajax({ url: urlStr, success: function(data) {
		dataForList = JSON.parse(data);
		checkUiComponentForData();
	 }});
}

function checkUiComponentForData() {
	if (hasCheckedUi) {
		return;
	}

	if (dataForList == null || dataForList.length <= 0) {
		return;
	}

	if (dataForUiComponent == null || dataForUiComponent.length == 0) {
		return;
	}
	hasCheckedUi = true;
	dataForUiComponent.attr("style","width:150px;").combobox({
		data:dataForList,
		valueField:dataForListId,
		textField:dataForListValue
	});
}

function formatUiCompoentForData(value, row, index) {
	if (dataForList != null && dataForList.length > 0) {
		try {
			for (var i = 0; i < dataForList.length; i ++) {
				var dataL = dataForList[i];
				if (dataL[dataForListId] == value) {
					value = dataL[dataForListValue];
					break;
				}
			}
		} catch (exception) {
		}
	}
	return value;
}

function formatUiCompoentForDataMultiSelect(value, row, index) {
	return formatOtherUiCompoentForDataMultiSelect(dataForList, dataForListId, dataForListValue, value, row, index);
}


//for some case
function initCity(selectedId) {
	var data = $('#belongCityId').combobox("getData");
	if (data && data.length > 0) {
		if (selectedId == null) {
			$('#belongCityId').combobox('select', data[0].id);
			//$('#belongCityId').combobox('setValue', data[0].name);
		} else {
			$('#belongCityId').combobox('select', selectedId);
		}
	}
}

function initStore(selectedId) {
	var data = $('#belongStoreId').combobox("getData");
	if (data && data.length > 0) {
		if (selectedId == null) {
			$('#belongStoreId').combobox('select', data[0].id);
		} else {
			$('#belongStoreId').combobox('select', selectedId);
		}
	}
}


//
//multi select store
//
var Menu = "Menu";
var Pack = "Pack";
var Store = "Store";
var Stockroom = "Stockroom";
//for excel export
var Excel = "Excel";
//for cloumn hiding
var Cloumn = "Cloumn";
//for app role
var AppRole = "AppRole";

function initChechBoxUiWithCategory(majorName, objWithCate) {
	var container = $("#" + majorName.toLowerCase() + "Container");
	$.each(objWithCate, function(key, value) {
		var cateKey = JSON.parse(key);
		container.append('<tr width="100%">'
			+ '<td colspan="5" width="100%" style="background-color: #ff9999; ">'
				+ '<input type="checkbox" id="' + majorName.toLowerCase() + '_' + cateKey.id + '" onchange="onCateWithNameSelected(' + majorName + ', ' + cateKey.id + ', this);">' + cateKey.name
			+ '</td>'
		+ '</tr>');

		initCheckBoxiUi(majorName, cateKey.id, value);
	});
}

function initCheckBoxiUi(majorName, cateKeyId, value) {
	var container = $("#" + majorName.toLowerCase() + "Container");
	var subHtml = '<tr width="100%">';
	$.each(value, function(n, subValue) {
		var id = subValue.id;
		if(isString(id)){
			id = "'" + id + "'";
		} 
		subHtml += '<td><input type="checkbox" id="' + majorName.toLowerCase() + '_' + cateKeyId + '_' + subValue.id + '" name="' + majorName.toLowerCase() +'Ids" onchange="onSubWithNameSelected(' + majorName + ', ' + cateKeyId + ', ' + id + ', this);" value="' + subValue.id + '">' + subValue.name + '</td>';
		if (n > 0 && (n + 1) % 5 == 0) {
			subHtml += '</tr><tr>';
		}
	});
	subHtml += '</tr>';
	container.append(subHtml);
}

function onAllWithNameSelected(majorName, uiObject) {
	$('input[id^="' + majorName.toLowerCase() + '_"]').each(function(){
		this.checked = uiObject.checked;
	});
}

function onCateWithNameSelected(majorName, menuClassId, uiObject) {
	try {
		$('input[id^="' + majorName.toLowerCase() + '_' + menuClassId + '_"]').each(function(){
			this.checked = uiObject.checked;
		});

		if (!uiObject.checked) {
			$("#" + majorName.toLowerCase())[0].checked = uiObject.checked;
		} else {
			var allChecked = true;
			$('input[id^="' + majorName.toLowerCase() + '_"]').each(function(){
				if (!this.checked) {
					allChecked = false;
					return false;
				}
			});

			$("#" + majorName.toLowerCase())[0].checked = allChecked;
		}
	} catch (e) {
		alert(e);
	}
}

function onSubWithNameSelected(majorName, menuClassId, menuId, uiObject) {
	try {
		var menuClassChecked = true;
		$('input[id^="' + majorName.toLowerCase() + '_' + menuClassId + '_"]').each(function(){
			if (!this.checked) {
				menuClassChecked = false;
				return false;
			}
		});

		$("#" + majorName.toLowerCase() + "_" + menuClassId)[0].checked = menuClassChecked;
		if (!menuClassChecked) {
			$("#" + majorName.toLowerCase())[0].checked = menuClassChecked;
		} else {
			var allChecked = true;
			$('input[id^="' + majorName.toLowerCase() + '_"]').each(function(){
				if (!this.checked) {
					allChecked = false;
					return false;
				}
			});
			$("#" + majorName.toLowerCase())[0].checked = allChecked;
		}
	} catch (e) {
		//alert(e);
	}
}

function initSelectIds(majorName, selectIdsStr) {
	var selIds = selectIdsStr.split(",");
	$.each(selIds, function(n, subValue) {
		if (subValue != null && subValue.length > 0) {
			//alert("xxx:" + subValue + "," + $('input[id^="' + majorName.toLowerCase() + '_"]').length + "," + $('input[value="' + subValue + '"]').length);
			var subView = $('input[id^="' + majorName.toLowerCase() + '_"][value="' + subValue + '"]');
			if (subView && subView.length > 0 && subView.attr("id")) {
				try {
					subView[0].checked = true;
				} catch (e) {
				}
				try {
					var uiId = subView.attr("id").split("_");
					var cateId = uiId[uiId.length - 2];
					onSubWithNameSelected(majorName, cateId, subValue);
				} catch (e) {
				}
			}
			//onSubWithNameSelected(majorName, subValue.menuClassId, $());
			//alert(subView.attr("id"));
		}
	});
}









//for common case
function formatOtherUiCompoentForData(datas, idName, valueName, value, row, index, allowEmpty) {
	if (datas != null && datas.length > 0) {
		try {
			if (allowEmpty && (value == null || value == "")) {
				return value;
			}
			for (var i = 0; i < datas.length; i ++) {
				var dataL = datas[i];
				if (dataL[idName] == value) {
					value = dataL[valueName];
					break;
				}
			}
		} catch (exception) {
		}
	}
	return value;
}

function formatOtherUiCompoentForDataMultiSelect(datas, idName, valueName, value, row, index) {
	var eValue = "";
	if (datas != null && datas.length > 0) {
		try {
			var values = value.split(",");
			for (var i = 0; i < datas.length; i ++) {
				var dataL = datas[i];
				for (var j = 0; j < values.length; j ++) {
					var valueL = values[j].trim();
					if (valueL && valueL.length > 0) {
						if (dataL[idName] == valueL) {
							if (eValue.length > 0) {
								eValue += ",";
							}
							eValue += dataL[valueName];
							break;
						}
					}

				}
			}
			return eValue;
		} catch (exception) {
		}
	}
	return value;
}

function initUiComponetForData(compoentId, idName, selectedId, noDefault) {
	try{
		var comp = compoentId;
		if (isString(compoentId)) {
			compoentId = compoentId.charAt(0) == '#' ? compoentId : "#" + compoentId;
			comp = $(compoentId);
		}
		if(!(comp instanceof jQuery)) {
			comp = $(compoentId);
		}
		var data = comp.combobox("getData");
		if (data && data.length > 0) {
			if (selectedId == null || selectedId == '') {
				if (!noDefault) {
					comp.combobox('select', data[0][idName]);
				} else {
					comp.combobox('clear');
				}
			} else {
				comp.combobox('select', selectedId);
			}
		}
	} catch (exp) {
		alert(exp);
	}
}

function initUiComponetForDataMultiSelect(compoentId, idName, selectedIds, noDefault) {
	var comp = compoentId;
	if (isString(compoentId)) {
		compoentId = compoentId.charAt(0) == '#' ? compoentId : "#" + compoentId;
		comp = $(compoentId);
	}
	
	if (selectedIds != null) {
		selectedIds = selectedIds + "";
	}
	
	var data = comp.combobox("getData");
	if (data != null && data.length > 0) {
		if (selectedIds == null || selectedIds.length == 0) {
			if (!noDefault) {
				comp.combobox('select', data[0][idName]);
			} else {
				comp.combobox('clear');
			}
		} else {
			var ids = selectedIds.split(",");
			for (var i = 0; i < ids.length; i ++) {
				var id = ids[i].trim();
				if (id != null && id.length > 0) {
					comp.combobox('select', id);
				}
			}
		}
	}
}

//for combobox sel/unsel function
function onComboboxSelectSingle(data, compoentId, idName) {
	try {
		var comp = compoentId;
		if (isString(compoentId)) {
			compoentId = compoentId.charAt(0) == '#' ? compoentId : "#" + compoentId;
			comp = $(compoentId);
		}

		if (data) {
			if (data.isSelected) {
				data.isSelected = false;
				comp.combobox('unselect', data[idName]);
			} else {
				data.isSelected = true;
			}
		}

		var datas = comp.combobox("getData");
		if (datas && datas.length > 0) {
			for (var i = 0; i < datas.length; i ++) {
				var dataL = datas[i];
				if (dataL != data) {
					dataL.isSelected = false;
				}
			}
		}
	} catch(exp) {
		alert(exp);
	}
}

function inputToCombobox(datas, compoentObj, idName, valueName, selData) {
	try {
		var comp = compoentObj;
		if (isString(compoentObj)) {
			compoentObj = compoentObj.charAt(0) == '#' ? compoentObj : "#" + compoentObj;
			comp = $(compoentObj);
		}

		comp.attr("style","width:150px;").combobox({
			data:datas,
			valueField:idName,
			textField:valueName
		});

		if (selData) {
			comp.combobox('select', selData);
		} else {
			comp.combobox('clear');
		}
	} catch(exp) {
		alert(exp);
	}


}


function inputToCombotree(datas, compoentObj, idName, valueName, selData) {
	try {
		var comp = getHtmlNodeObject(compoentObj);

		if (isString(datas)) {
			comp.attr("style","width:150px;").combotree({
				url:datas,
				valueField:idName,
				textField:valueName
			});
		} else {
			comp.attr("style","width:150px;").combotree({
				data:datas,
				valueField:idName,
				textField:valueName
			});
		}

		if (selData) {
			comp.combotree('select', selData);
		} else {
			comp.combotree('clear');
		}
	} catch(exp) {
		alert(exp);
	}


}


//new utils for input to combbox
function loadDataForInputToCombbox(returnData, requestUrl, parentTagName, inputId, width) {
	$.ajax({
		url : requestUrl,
		success : function(data) {
			returnData.data = JSON.parse(data);
			inputToCombboxForLoadedData(returnData, parentTagName, inputId, width);
		}
	});
}

function inputToCombboxForLoadedData(returnData, parentTagName, inputId, width) {
	if (returnData.hasChecked) {
		return;
	}

	if (returnData.data == null || returnData.data.length <= 0) {
		return;
	}

	var mUiComp = returnData.uiComponent;
	if (!mUiComp) {
		mUiComp = getHtmlNodeObject(parentTagName).find("input[name='" + inputId + "']");
		if (mUiComp == null || mUiComp.length == 0) {
			return;
		}

		returnData.uiComponent = mUiComp;
	}

	returnData.hasChecked = true;

	if (width) {
		mUiComp.attr("style","width:" + width + "px;");
	}

	mUiComp.combobox({
		data:returnData.data,
		valueField:"id",
		textField:"name",
		onSelect:onSelectCombboxForLoaded,
	});

	mUiComp.combobox('clear');

	onLoadSuccessCombboxForLoaded();
}

function onSelectCombboxForLoaded(data) {
	try {
		didSelectCombboxForLoaded(data);
	} catch (e) {
	}
}

function onLoadSuccessCombboxForLoaded() {
	try {
		didLoadSuccessCombboxForLoaded();
	} catch (e) {
	}
}


//input to combobox
function inputToCombboxForData(uiData, parentTagName, inputId, width, selValue) {
	if (uiData == null || uiData.length <= 0) {
		return;
	}

	var mUiComp = getHtmlNodeObject(parentTagName).find("input[name='" + inputId + "']");
	if (mUiComp == null || mUiComp.length == 0) {
		return;
	}

	if (width) {
		mUiComp.attr("style","width:" + width + "px;");
	}

	mUiComp.combobox({
		data:uiData,
		valueField:"id",
		textField:"name"
	});

	if (selValue != null) {
		mUiComp.combobox('setValue', selValue);
	} else {
		mUiComp.combobox('clear');
	}
}




//other ui
function showProgress() {
	$.messager.progress({
		title:'请稍后',
		msg:'处理中...'
	});
}

function hideProgress() {
	$.messager.progress('close');
}

function showDialog(title, url, name, width, height, zindex) {
	return $.dialog({
		content: 'url:' + url,
		title : title,
		cache : false,
		parent : windowapi,
		zIndex : zindex,
		lock : true,
		width : width,
	    height : height
	});
}

//utils
function getHtmlNodeObject(compoentObj) {
	var comp = compoentObj;
	if (isString(compoentObj)) {
		compoentObj = compoentObj.charAt(0) == '#' ? compoentObj : "#" + compoentObj;
		comp = $(compoentObj);
	}
	return comp;
}


function isString(obj){
	return Object.prototype.toString.call(obj) == "[object String]";
}


function isNumber(obj) {
    return typeof obj === 'number';
}


function intToTime(value, row, index) {
	try {
		var vlen = value.length;
		return value.substring(0, vlen - 4) + ":" + value.substring(vlen - 4, vlen - 2) + ":" + value.substring(vlen - 2, vlen) ;
	} catch (err) {
		return value;
	}
}

//statistics utils
function getIntValue(obj) {
	try {
		if (obj == null) {
			return 0;
		}
		var intObj = parseInt(obj);
		if (!intObj) {
			return 0;
		}
		return intObj;
	} catch (e) {
	}
}

function getDoubleValue(obj) {
	try {
		if (obj == null) {
			return 0.00;
		}
		var doubleObj = parseFloat(obj);
		if (!doubleObj) {
			return 0.00;
		}
		return doubleObj;
	} catch (e) {
	}
}



function applyHref(tabname,href,value,rec,index){
	var hrefnew = href;
	var re = "";
	var p1 = /\#\{(\w+)\}/g;
	try{
		var vars =hrefnew.match(p1); 
		for(var i=0;i<vars.length;i++){
			var keyt = vars[i];
			var p2 = /\#\{(\w+)\}/g;
			var key = p2.exec(keyt);
			
			hrefnew =  hrefnew.replace(keyt,rec[key[1]]);
		}
	}catch(ex){
	}
		re += "<a href = '#' onclick=\"addOneTab('"+tabname + "_" + value +"','"+ hrefnew+"')\" ><u>"+value+"</u></a>";
	return re;
}




var currentIdPos = 0;
var lastGeneratedId = 0;
function generateUId() {
	var timeId = new Date().getTime();
	if (timeId != lastGeneratedId) {
		currentIdPos = 0;
		lastGeneratedId = timeId * 10 + currentIdPos;
		return lastGeneratedId;
	} else {
		currentIdPos ++;
		lastGeneratedId = timeId * 10 + currentIdPos;
		return lastGeneratedId;
	}
}
