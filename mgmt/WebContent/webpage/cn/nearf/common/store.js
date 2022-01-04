var hasAllPermission = false;
//查询有门店条件时，要求加省和城市
var defaultStoreComponentName = null;

//是否支持多选
var supportMultiStores = false;

var provinces;
var provinceCombobox;
//if (!provinces) {
//	provinces = storageGet("provices_" + timeKey);
//}

var cities;
var cityCombobox;
//if (!cities) {
//	cities = storageGet("cities_" + timeKey);
//}

var stores;
var storeCombobox;
//if (!stores) {
//	stores = storageGet("stores_" + timeKey);
//}

//store
function loadDataForStoreInputToCombbox(parentTagName, allPermission) {
	try {
		hasAllPermission = allPermission;
		dumpParentTagName = parentTagName;
		
		if (!stores || stores.length == 0) {
			storageClear();
			
			$.ajax({
				url : "erpStoreController.do?listAll&hasAllPermission=" + hasAllPermission,
				success : function(data) {
					stores = JSON.parse(data);
					storageSet("stores_" + timeKey);
					storeInputToCombboxForLoadedData(parentTagName, stores);
					//
					initProviceCityUi(parentTagName);
				}
			});
		} else {
			storeInputToCombboxForLoadedData(parentTagName, stores);
			//
			initProviceCityUi(parentTagName);
		}
	} catch (e) {
		alert("loadDataForStoreInputToCombbox:" + e);
	}
}

function storeInputToCombboxForLoadedData(parentTagName, storeData) {
	if (storeCombobox == null) {
		var mUiComp = getStoreUiComponet(parentTagName);
		if (mUiComp == null || mUiComp.length == 0) {
			alert("未找到store控件");
			return;
		}
		
		storeCombobox = mUiComp;
		storeCombobox.attr("style","width:150px;");
	}
	
	storeCombobox.combobox({ 
		data:storeData,
		valueField:"id", 
		textField:"name",
		onSelect:onSelectStore,
	});
}

function onSelectStore(store) {
	try {
		didSelectStore(store);
	} catch (e) {
	}
}

//ui
function getStoreUiComponet(parentTagName) {
	var storeUiComp = null;
	if (defaultStoreComponentName) {
		storeUiComp = getHtmlNodeObject(parentTagName).find("input[name='" + defaultStoreComponentName + "']");
	}
	if (storeUiComp == null || storeUiComp.length == 0) {
		storeUiComp = getHtmlNodeObject(parentTagName).find("input[name='storeId']");
	}
	if (storeUiComp == null || storeUiComp.length == 0) {
		storeUiComp = getHtmlNodeObject(parentTagName).find("input[name='belongStoreId']");
	}
	return storeUiComp;
}

function initProviceCityUi(parentTagName) {
	try {
		if (storeCombobox == null || storeCombobox.length == 0) {
			alert("未找到store控件");
			return;
		}
		
		var mUiComp = getHtmlNodeObject(parentTagName).find("input[name='cityId']");
		if (!mUiComp || mUiComp.length == 0) {
			var provinceNode = '<span style="display:-moz-inline-box;display:inline-block;"><span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; " title="省份">省份：</span><input onkeypress="EnterPress(event)" onkeydown="EnterPress()" type="text" name="provinceId" class="inuptxt" style="width: 100px"></span>';
			var cityNode = '<span style="display:-moz-inline-box;display:inline-block;"><span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; " title="城市">城市：</span><input onkeypress="EnterPress(event)" onkeydown="EnterPress()" type="text" name="cityId" class="inuptxt" style="width: 100px"></span>';
			
			var newNodeInsertUi = storeCombobox.parent();
			newNodeInsertUi.before(provinceNode + cityNode);
			
			if (supportMultiStores) {
				var multiStoreNode = '<button id="btnMultiSelStore" type="button" onclick="onMultStoreSelectClicked();" style="margin: 5px;">门店多选</button>';
				var multiStoreDiv = '<span id="divMultiStoreStatus" style="color: red;"></span>';
				var multiStoreInput = '<input id="storeIds" name="storeIds" type="hidden" />';
				
				newNodeInsertUi.after(multiStoreNode + multiStoreDiv + multiStoreInput);
			}
			
		}
		
		loadDataForProviceInputToCombbox(parentTagName);
	} catch (e) {
		alert("initProviceCityUi:" + e);
	}
}

//province
function loadDataForProviceInputToCombbox(parentTagName) {
	if (!provinces || provinces.length == 0) {
		$.ajax({
			url : "erpProvinceController.do?listAll&hasAllPermission=" + hasAllPermission,
			success : function(data) {
				try {
					provinces = JSON.parse(data);
					storageSet("provices_" + timeKey);
					provinceInputToCombboxForLoadedData(parentTagName);
				} catch (e) {
					alert("loadDataForProviceInputToCombbox:" + e);
				}
			}
		});
	} else {
		provinceInputToCombboxForLoadedData(parentTagName);
	}
}

function provinceInputToCombboxForLoadedData(parentTagName) {
	if (provinces == null || provinces.length <= 0) {
		return;
	}
	
	if (provinceCombobox == null) {
		var mUiComp = getHtmlNodeObject(parentTagName).find("input[name='provinceId']");
		if (mUiComp == null || mUiComp.length == 0) {
			return;
		}
		provinceCombobox = mUiComp;
		provinceCombobox.attr("style","width:100px;");
	}
	
	provinceCombobox.combobox({ 
		data:provinces,
		valueField:"id", 
		textField:"name",
		onSelect:onSelectProvince,
	});
	
	loadDataForCityInputToCombbox(parentTagName);
}

function onSelectProvince(province) {
	var proCities = new Array();
	try {
		var provinceId = province.id;
		for (var i = 0; i < cities.length; i ++) {
			var city = cities[i];
			if (city.provinceId == provinceId) {
				proCities.push(city);
			}
		}
		
		cityInputToCombboxForLoadedData(dumpParentTagName, proCities);
	} catch (e) {
		alert("onSelectCity:" + e);
	}
	
	var proCityStores = new Array();
	try {
		for (var i = 0; i < proCities.length; i ++) {
			var city = proCities[i];
			for (var j = 0; j < stores.length; j ++) {
				var store = stores[j];
				if (store.belongCityId == city.id) {
					proCityStores.push(store);
				}
			}
		}
		
		storeInputToCombboxForLoadedData(dumpParentTagName, proCityStores);
	} catch (e) {
		alert("onSelectCity:" + e);
	}
	
	try {
		didSelectProvince(province, proCities, proCityStores);
	} catch (e) {
	}
}


//city
function loadDataForCityInputToCombbox(parentTagName) {
	if (!cities || cities.length == 0) {
		$.ajax({
			url : "erpCityController.do?listAll&hasAllPermission=" + hasAllPermission,
			success : function(data) {
				cities = JSON.parse(data);
				storageSet("cities_" + timeKey);
				cityInputToCombboxForLoadedData(parentTagName, cities);
			}
		});
	} else {
		cityInputToCombboxForLoadedData(parentTagName, cities);
	}
}

function cityInputToCombboxForLoadedData(parentTagName, cityData) {
	try {
		if (cityData == null || cityData.length <= 0) {
			alert("cityInputToCombboxForLoadedData:no data");
			return;
		}
		
		if (cityCombobox == null) {
			var mUiComp = getHtmlNodeObject(parentTagName).find("input[name='cityId']");
			if (mUiComp == null || mUiComp.length == 0) {
				alert("cityInputToCombboxForLoadedData:no ui");
				return;
			}
			
			cityCombobox = mUiComp;
			cityCombobox.attr("style","width:100px;");
		}
		
		cityCombobox.combobox({ 
			data:cityData,
			valueField:"id", 
			textField:"name",
			onSelect:onSelectCity,
		});
		
	} catch (e) {
		alert("cityInputToCombboxForLoadedData:" + e);
	}
	
	try {
		didStoreUisLoaded();
	} catch (e) {
	}
}


function onSelectCity(city) {
	var cityStores = new Array();
	try {
		var cityId = city.id;
		for (var i = 0; i < stores.length; i ++) {
			var store = stores[i];
			if (store.belongCityId == cityId) {
				cityStores.push(store);
			}
		}
		
		storeInputToCombboxForLoadedData(dumpParentTagName, cityStores);
	} catch (e) {
		alert("onSelectCity:" + e);
	}
	
	try {
		didSelectCity(city, cityStores);
	} catch (e) {
	}
}


//for multi-select
function onMultStoreSelectClicked() {
	var height = parseInt(window.innerHeight - 20);
	createwindow("选择门店", "erpStoreController.do?multiSelStores&selStoreIds=" + encodeURIComponent($("#storeIds").val()) + "&hasAllPermission=" + hasAllPermission, 620, height);
}

function didSelMultiStores(storeIds) {
	$("#storeIds").val(storeIds);
	if (!storeIds || storeIds.length == 0) {
		$("#divMultiStoreStatus").text("");
	} else {
		storeCombobox.combobox('clear');
		$("#divMultiStoreStatus").text("已选" + (storeIds.split(",").length - 2) + "个门店");
	}
}


