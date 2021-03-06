function doFill(type) {
	  var full = "insert into data.target_table\n" + 
		"select * from\n" + 
		"(SELECT  \n" + 
		"'D',\n" + 
		"t.fiscal_period,\n" + 
		"t.fiscal_year,\n" + 
		"t.posting_period,\n" + 
		"gxrq,\n" + 
		"column1,\n" + 
		"…\n" + 
		"columnn,\n" + 
		"CURRENT_TIMESTAMP,\n" + 
		"CURRENT_TIMESTAMP \n" + 
		"from stage.dshqzwj,data.dw_time_period_dim AS t\n" + 
		"where create_date >= t.period_from_date and create_date <= t.period_end_date) ss\n" + 
		"[where ]";
	  
	  var inc="insert into data.target_table_tmp\n" + 
		"select * from\n" + 
		"(SELECT  \n" + 
		"'D',\n" + 
		"t.fiscal_period,\n" + 
		"t.fiscal_year,\n" + 
		"t.posting_period,\n" + 
		"gxrq,\n" + 
		"column1,\n" + 
		"…\n" + 
		"columnn,\n" + 
		"CURRENT_TIMESTAMP,\n" + 
		"CURRENT_TIMESTAMP \n" + 
		"from stage.dshqzwj,data.dw_time_period_dim AS t\n" + 
		"where create_date >= t.period_from_date and create_date <= t.period_end_date) ss\n" + 
		"[where ];\n" + 
		"\n" + 
		"delete from data.target_table where PK in (select PK from data.target_table_tmp);\n" + 
		"\n" + 
		"insert into data.target_table select * from data.target_table_tmp;\n" + 
		"\n" + 
		"truncate data.target_table_tmp;";
	 //全量
	if(0 == type){
		$("#sqls").val(full);
	}else{
		$("#sqls").val(inc);
	}
}

//初始化下标
function resetTrNum(tableId) {
	$tbody = $("#"+tableId+"");
	$tbody.find('>tr').each(function(i){
		$(':input, select,button,a', this).each(function(){
			var $this = $(this), name = $this.attr('name'),id=$this.attr('id'),onclick_str=$this.attr('onclick'), val = $this.val();
			if(name!=null){
				if (name.indexOf("#index#") >= 0){
					$this.attr("name",name.replace('#index#',i));
				}else{
					var s = name.indexOf("[");
					var e = name.indexOf("]");
					var new_name = name.substring(s+1,e);
					$this.attr("name",name.replace(new_name,i));
				}
			}
			if(id!=null){
				if (id.indexOf("#index#") >= 0){
					$this.attr("id",id.replace('#index#',i));
				}else{
					var s = id.indexOf("[");
					var e = id.indexOf("]");
					var new_id = id.substring(s+1,e);
					$this.attr("id",id.replace(new_id,i));
				}
			}
			if(onclick_str!=null){
				if (onclick_str.indexOf("#index#") >= 0){
					$this.attr("onclick",onclick_str.replace(/#index#/g,i));
				}else{
				}
			}
		});
		$(this).find('div[name=\'xh\']').html(i+1);
	});
}
//通用弹出式文件上传
function commonUpload(callback,inputId){
    $.dialog({
           content: "url:systemController.do?commonUpload",
           lock : true,
           title:"文件上传",
           zIndex:2100,
           width:700,
           height: 200,
           parent:windowapi,
           cache:false,
       ok: function(){
               var iframe = this.iframe.contentWindow;
               iframe.uploadCallback(callback,inputId);
               return true;
       },
       cancelVal: '关闭',
       cancel: function(){
       } 
   });
}
//通用弹出式文件上传-回调
function commonUploadDefaultCallBack(url,name,inputId){
	$("#"+inputId+"_href").attr('href',url).html('下载');
	$("#"+inputId).val(url);
}
function browseImages(inputId, Img) {// 图片管理器，可多个上传共用
		var finder = new CKFinder();
		finder.selectActionFunction = function(fileUrl, data) {//设置文件被选中时的函数 
			$("#" + Img).attr("src", fileUrl);
			$("#" + inputId).attr("value", fileUrl);
		};
		finder.resourceType = 'Images';// 指定ckfinder只为图片进行管理
		finder.selectActionData = inputId; //接收地址的input ID
		finder.removePlugins = 'help';// 移除帮助(只有英文)
		finder.defaultLanguage = 'zh-cn';
		finder.popup();
	}
function browseFiles(inputId, file) {// 文件管理器，可多个上传共用
	var finder = new CKFinder();
	finder.selectActionFunction = function(fileUrl, data) {//设置文件被选中时的函数 
		$("#" + file).attr("href", fileUrl);
		$("#" + inputId).attr("value", fileUrl);
		decode(fileUrl, file);
	};
	finder.resourceType = 'Files';// 指定ckfinder只为文件进行管理
	finder.selectActionData = inputId; //接收地址的input ID
	finder.removePlugins = 'help';// 移除帮助(只有英文)
	finder.defaultLanguage = 'zh-cn';
	finder.popup();
}
function decode(value, id) {//value传入值,id接受值
	var last = value.lastIndexOf("/");
	var filename = value.substring(last + 1, value.length);
	$("#" + id).text(decodeURIComponent(filename));
}