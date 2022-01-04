var seqBeginUi = new Array();
var seqEndUi = new Array();

function upgradeSeqUi(parentTagName, tagName) {
	if (!tagName) {
		tagName = "seq";
	}
	var parentTag = getHtmlNodeObject(parentTagName);
	seqBeginUi[tagName] = parentTag.find("input[name='" + tagName + "_begin']");
	seqEndUi[tagName] = parentTag.find("input[name='" + tagName + "_end']");
	
	var seqActionUi = '<button type="button" onclick="onSeqYesterdayClicked(\'' + tagName + '\');">昨天</button>'
		+ '<button type="button" onclick="onSeqThisWeekClicked(\'' + tagName + '\');">本周</button>'
		+ '<button type="button" onclick="onSeqThisMonthClicked(\'' + tagName + '\');">本月</button>'
		+ '<button type="button" onclick="onSeqLastMonthClicked(\'' + tagName + '\');">上月</button>';
	seqEndUi[tagName].after(seqActionUi);
}

function onSeqYesterdayClicked(tagName) {
	var now = new Date();
	var yesterdayTime = now.getTime() - 1 * 24 * 3600 * 1000;
	
	var yesterdayDate = new Date();
	yesterdayDate.setTime(yesterdayTime);
	
	seqBeginUi[tagName].val(moment(yesterdayDate).format('YYYYMMDD'));
	seqEndUi[tagName].val(moment(yesterdayDate).format('YYYYMMDD'));
}

function onSeqThisWeekClicked(tagName) {
	var now = new Date();
	var weekDay = now.getDay();
	var beginTime = 0;
	if (weekDay == 0) {
		beginTime = now.getTime() - 6 * 24 * 3600 * 1000;
	} else {
		beginTime = now.getTime() - (weekDay - 1) * 24 * 3600 * 1000;
	}
	var beginDate = new Date();
	beginDate.setTime(beginTime);
	
	seqBeginUi[tagName].val(moment(beginDate).format('YYYYMMDD'));
	seqEndUi[tagName].val(moment(now).format('YYYYMMDD'));
}

function onSeqThisMonthClicked(tagName) {
	var now = new Date();
	var monthDay = now.getDate();
	var beginTime = 0;
	beginTime = now.getTime() - (monthDay - 1) * 24 * 3600 * 1000;
	var beginDate = new Date();
	beginDate.setTime(beginTime);
	
	seqBeginUi[tagName].val(moment(beginDate).format('YYYYMMDD'));
	seqEndUi[tagName].val(moment(now).format('YYYYMMDD'));
}

function onSeqLastMonthClicked(tagName) {
	var now = new Date();
	var monthDay = now.getDate();
	var lastEndTime = 0;
	lastEndTime = now.getTime() - monthDay * 24 * 3600 * 1000;
	var lastEndDate = new Date();
	lastEndDate.setTime(lastEndTime);
	
	seqBeginUi[tagName].val(moment(lastEndDate).format('YYYYMM') + "01");
	seqEndUi[tagName].val(moment(lastEndDate).format('YYYYMMDD'));
}



