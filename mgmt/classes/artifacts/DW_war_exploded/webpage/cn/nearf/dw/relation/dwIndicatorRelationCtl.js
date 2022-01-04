  var allIndicators = null;
  var comboBox = null;
  var comboBoxForParent = null;
  
  var typeComboBox = null;
  var typeComboBoxForParent = null;
  
  function initIndicatorsUI(parentTagName, tagName, defValue){
		try {
			$.ajax({
				url : "dwIndicatorRelationCtlController.do?indicatorList",
				success : function(data) {
					var indicators = JSON.parse(data);
					allIndicators = indicators;
					indicatorRelationInputToCombboxForLoadedData(parentTagName, tagName, indicators, defValue);
				}
			});
		} catch (e) {
			alert("loadDataForIndicatorRelationInputToCombbox:" + e);
		}
	}

	//将indicatorRelation的数据拼接到combobox中
	function indicatorRelationInputToCombboxForLoadedData(parentTagName, tagName, relationData, defautValue) {
		//debugger;
		var cb = getHtmlNodeObject(parentTagName).find("input[name='"+tagName+"']");
		if(tagName=='indicatorId'){
			comboBox = cb;
		}else{
			comboBoxForParent = cb
		}
		
		var typeOfInd = null;
		for (var i = 0; i < allIndicators.length; i ++) {
			var relation = allIndicators[i];
			if (relation.id == defautValue) {
				typeOfInd = relation.type;
				break;
			}
		}
		
		var sameTypeIndicators = new Array();
		for (var i = 0; i < allIndicators.length; i ++) {
			var relation = allIndicators[i];
			if (relation.type == typeOfInd) {
				sameTypeIndicators.push(relation);
			}
		}
		
		
		cb.combobox({ 
			data: sameTypeIndicators,
			valueField:"id", 
			textField:"name",
			onLoadSuccess:function(){
				if(defautValue!=null){
					$(this).combobox('setValue', defautValue);
				}else{
	     			$(this).combobox('setValue', '');
	     		}
	     	}
		});
		
		var relationTypeNode = '<input onkeypress="EnterPress(event)" onkeydown="EnterPress()" type="text" name="'+tagName+'type" class="inuptxt" style="width: 100px;">';
		var newNodeInsertUi = cb.parent();
		newNodeInsertUi.prepend(relationTypeNode);
		var typeCB = getHtmlNodeObject(parentTagName).find("input[name='"+tagName+"type']");
		if(tagName=='indicatorId'){
			typeComboBox = typeCB;
		}else{
			typeComboBoxForParent = typeCB;
		}
		$.ajax({
			url : "dwIndicatorRelationCtlController.do?relationTypeList",
			success : function(data) {
				try {
					var types = JSON.parse(data);
					typeCB.combobox({ 
						data: types,
						valueField: "id", 
						textField: "name",
						onSelect: (tagName=='indicatorId')?onSelectIndicatorType:onSelectIndicatorTypeForParent,
						onLoadSuccess:function(){
							if(defautValue!=null){
								$(this).combobox('setValue', typeOfInd);
							}else{
				     			$(this).combobox('setValue', '');
				     		}
				     	}
					});
				} catch (e) {
				}
			}
		});

	}
	
	//加载相应的relation
	function onSelectIndicatorType(relationType) {
		//debugger;
		var proRelations = new Array();
		try {
			var relationTypeCode = relationType.id;
			for (var i = 0; i < allIndicators.length; i ++) {
				var relation = allIndicators[i];
				if (relation.type == relationTypeCode) {
					proRelations.push(relation);
				}
			}
			comboBox.combobox({ 
				data: proRelations,
				valueField:"id", 
				textField:"name",
				onLoadSuccess:function(){
		     		$(this).combobox('setValue', '');
		     	}
			});
		} catch (e) {
		}
	}
	
	function onSelectIndicatorTypeForParent(relationType) {
		//debugger;
		var proRelations = new Array();
		try {
			var relationTypeCode = relationType.id;
			for (var i = 0; i < allIndicators.length; i ++) {
				var relation = allIndicators[i];
				if (relation.type == relationTypeCode) {
					proRelations.push(relation);
				}
			}
			comboBoxForParent.combobox({ 
				data: proRelations,
				valueField:"id", 
				textField:"name",
				onLoadSuccess:function(){
		     		$(this).combobox('setValue', '');
		     	}
			});
		} catch (e) {
		}
	}
