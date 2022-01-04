
var reportStatisticsFields = [
    {"field":"field", "name":"名称", "type":"int/double", "unit":"单位", "sumFields":[]},
];


function onReportDataLoadSuccess(data) {
	try {
		var statisticsDatas = new Array();
		for (var i = 0; i < data.rows.length; i ++) {
			var row = data.rows[i];

			for (var j = 0; j < reportStatisticsFields.length; j ++) {
				var reportField = reportStatisticsFields[j];
				addForStatistics(statisticsDatas, reportField, row);
			}
		}
			
		var statisticsComponent = $("#statisticsDiv");
		if (!statisticsComponent || statisticsComponent.length == 0) {
			var component = $("[class='datagrid-pager pagination']");
			var componentHtml = '<div id="statisticsDiv" style="padding:10px;">';
			componentHtml += '本页小计，总记录数<span id="report_stat_total_results"></span>（';
			componentHtml += reportStatisticsFields[0].name + '：<span id="report_stat_' + reportStatisticsFields[0].field + '"></span>' + reportStatisticsFields[0].unit;
			for (var i = 1; i < reportStatisticsFields.length; i ++) {
				var reportField = reportStatisticsFields[i];
				componentHtml += '，' + reportField.name + '：<span id="report_stat_' + reportField.field + '"></span>' + reportField.unit;
			}
			componentHtml += '）';
			componentHtml += '</div>';
			component.prepend(componentHtml);
		}
		
		$("#report_stat_total_results").text(data.rows.length);
		for (var i = 0; i < reportStatisticsFields.length; i ++) {
			var reportField = reportStatisticsFields[i];
			if (reportField.sumFields && reportField.sumFields.length > 0) {
				if (reportField.type == "int") {
					var fieldValue = 0;
					for (var j = 0; j < reportField.sumFields.length; j ++) {
						fieldValue += getIntValue(statisticsDatas[reportField.sumFields[j]]);
					}
					$('#report_stat_' + reportField.field).text(fieldValue);
				} else {
					var fieldValue = 0.0;
					for (var j = 0; j < reportField.sumFields.length; j ++) {
						fieldValue += getDoubleValue(statisticsDatas[reportField.sumFields[j]]);
					}
					$('#report_stat_' + reportField.field).text(fieldValue);
				}
			} else {
				if (reportField.type == "int") {
					$('#report_stat_' + reportField.field).text(getIntValue(statisticsDatas[reportField.field]));
				} else {
					$('#report_stat_' + reportField.field).text(getDoubleValue(statisticsDatas[reportField.field]).toFixed(2));
				}
			}
		}
		 
	} catch (e) {
		alert(e);
	}
}



function addForStatistics(statisticsDatas, reportField, row) {
	try {
		if (reportField.type == "int") {
			statisticsDatas[reportField.field] = getIntValue(statisticsDatas[reportField.field]) + getIntValue(row[reportField.field]);
		} else {
			statisticsDatas[reportField.field] = getDoubleValue(statisticsDatas[reportField.field]) + getDoubleValue(row[reportField.field]);
		}
	} catch (e) {
	}
}







