var timeKey = "";
if (timeKey == "") {
	var expired = 24 * 3600 * 1000;
	var now = new Date();
	timeKey = parseInt(now.getTime() / expired) * expired;
}

var dumpParentTagName;


function onSearchReseted() {
	try {
		loadDataForStoreInputToCombbox(dumpParentTagName);
	} catch (e) {
	}
	try {
		resetAllMenuInputToCombbox();
	} catch (e) {
	}
	try {
		didSearchReseted();
	} catch (e) {
	}
}

//storage
var usingCookieOnly = false;
var gStorage;

function getStorage() {
	try {
		if (!gStorage) {
			if (window.sessionStorage) {
				gStorage = window.sessionStorage;
			} else if (window.localStorage) {
				gStorage = window.localStorage;
			}
		}
	} catch (exp) {
		alert("getStorage:" + exp);
	}
	return gStorage;
}

function storageSet(key, value) {
	try {
		getStorage().removeItem(key);
		getStorage().setItem(key, JSON.stringify(value));
	} catch (exp) {
		alert("storageSet:" + exp);
	}
}

function storageGet(key) {
	try {
		return JSON.parse(getStorage().getItem(key));
	} catch (exp) {
		//alert("storageGet:" + exp);
		return null;
	}
}

function storageRemove(key) {
	try {
		getStorage().removeItem(key);
	} catch (exp) {
		alert("storageRemove:" + exp);
	}
}

function storageClear() {
	try {
		getStorage().clear();
	} catch (exp) {
		alert("storageClear:" + exp);
	}
}