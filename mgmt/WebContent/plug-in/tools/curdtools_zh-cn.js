﻿//﻿var jq = jQuery.noConflict();
/**
 * 增删改工具栏
 */
/*window.onerror = function() {
	return true;
};*/
var iframe;// iframe操作对象
var win;//窗口对象
var gridname="";//操作datagrid对象名称
var windowapi;
var W;
try {
	windowapi = frameElement.api, W = windowapi.opener;//内容页中调用窗口实例对象接口
} catch (e) {
}
/**
 * 设置 window的 zIndex
 * @param flag true: 不增量(因为 tip提示经常使用 zIndex, 所以如果是 tip的话 ,则不增量)
 * @returns
 */
function getzIndex(flag){
	var zindexNumber = getCookie("ZINDEXNUMBER");
	if(zindexNumber == null){
		zindexNumber = 2010;
		setCookie("ZINDEXNUMBER",zindexNumber);
		//zindexNumber = 1980;
	}else{
		if(zindexNumber < 2030){
			zindexNumber = 2030;
		}
		var n = flag?zindexNumber:parseInt(zindexNumber) + parseInt(10);
		setCookie("ZINDEXNUMBER",n);
	}
	return zindexNumber;
} 
function upload(curform) {
	upload();
}
/**
 * 添加事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function add(title,addurl,gname,width,height) {
	gridname=gname;
	createwindow(title, addurl,width,height);
}
/**
 * 树列表添加事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function addTreeNode(title,addurl,gname) {
	if (rowid != '') {
		addurl += '&id='+rowid;
	}
	gridname=gname;
	createwindow(title, addurl);
}
/**
 * 更新事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function update(title,url, id,width,height,isRestful) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		tip('请选择编辑项目');
		return;
	}
	if (rowsData.length>1) {
		tip('请选择一条记录再编辑');
		return;
	}
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}

/**
 * 如果页面是详细查看页面，无效化所有表单元素，只能进行查看
 */
$(function(){
	if(location.href.indexOf("load=detail")!=-1){
		$(":input").attr("disabled","true");
		//$(":input").attr("style","border:0;border-bottom:1 solid black;background:white;");
	}
});

/**
 * 查看详细事件打开窗口
 * @param title 查看框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function detail(title,url, id,width,height) {
	var rowsData = $('#'+id).datagrid('getSelections');
//	if (rowData.id == '') {
//		tip('请选择查看项目');
//		return;
//	}
	
	if (!rowsData || rowsData.length == 0) {
		tip('请选择查看项目');
		return;
	}
	if (rowsData.length > 1) {
		tip('请选择一条记录再查看');
		return;
	}
    url += '&load=detail&id='+rowsData[0].id;
	createdetailwindow(title,url,width,height);
}

/**
 * 多记录刪除請求
 * @param title
 * @param url
 * @param gname
 * @return
 */
function deleteALLSelect(title,url,gname) {
	gridname=gname;
    var ids = [];
    var rows = $("#"+gname).datagrid('getSelections');
    if (rows.length > 0) {
    	$.dialog.setting.zIndex = getzIndex(true);
    	$.dialog.confirm('你确定永久删除该数据吗?', function(r) {
		   if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : url,
					type : 'post',
					data : {
						ids : ids.join(',')
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							reloadTable();
							$("#"+gname).datagrid('unselectAll');
							ids='';
						}
					}
				});
			}
		});
	} else {
		tip("请选择需要删除的数据");
	}
}


/**
 * 多记录处理請求
 * @param title
 * @param url
 * @param gname
 * @return
 */
function handleALLSelect(title,url,gname,comfirmMsg) {
	gridname=gname;
    var ids = [];
    var rows = $("#"+gname).datagrid('getSelections');
    if (rows.length > 0) {
    	$.dialog.confirm(comfirmMsg, function(r) {
		   if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : url,
					type : 'post',
					data : {
						ids : ids.join(',')
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							reloadTable();
							$("#"+gname).datagrid('unselectAll');
							ids='';
						}
					}
				});
			}
		});
	} else {
		tip("请选择需要操作的数据");
	}
}

/**
 * 查看时的弹出窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function createdetailwindow(title, addurl,width,height,needPrint,printOptions) {
	width = width?width:700;
	height = height?height:400;
	if(width=="100%" || height=="100%"){
		width = window.top.document.body.offsetWidth;
		height =window.top.document.body.offsetHeight-100;
	}
	if(typeof(needPrint) != 'undefined' && needPrint != undefined && needPrint != 'undefined'){
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+addurl,
				zIndex: getzIndex(),
				lock : true,
				width:width,
				height: height,
				title:title,
				opacity : 0.3,
				cache:false, 
				okVal: '打印',
				ok: function(){
					iframe = this.iframe.contentWindow;
			    		print(printOptions);
			    		return false;
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			W.$.dialog({
				content: 'url:'+addurl,
				zIndex: getzIndex(),
				lock : true,
				width:width,
				height: height,
				parent:windowapi,
				title:title,
				opacity : 0.3,
				cache:false, 
				okVal: '打印',
				ok: function(){
			    		iframe = this.iframe.contentWindow;
			    		print(printOptions);
			    		return false;
				},
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
	}else{
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+addurl,
				zIndex: getzIndex(),
				lock : true,
				width:width,
				height: height,
				title:title,
				opacity : 0.3,
				cache:false, 
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			W.$.dialog({
				content: 'url:'+addurl,
				zIndex: getzIndex(),
				lock : true,
				width:width,
				height: height,
				parent:windowapi,
				title:title,
				opacity : 0.3,
				cache:false, 
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
	}
}

//打印函数
	function print(printOptions) {
		// Default options
		var defaultOps = {
			prepend:null,
			append:null,
			needPre:true,
			needApp:true,
		    brContent: null, // 条码内容 内容为null则不显示条码
		    codeType:"qr",//br 一维码 qr 二维码
		    receiptName: "****单"// 打印单据名称
		};
		// Merge with user-options
		printOptions = $.extend({}, defaultOps, (printOptions || {}));
		
		console.log("printOptions.prepend      = " + printOptions.prepend     );
		console.log("printOptions.append       = " + printOptions.append      );
		console.log("printOptions.needPre      = " + printOptions.needPre     );
		console.log("printOptions.needApp      = " + printOptions.needApp     );
		console.log("printOptions.brContent    = " + printOptions.brContent   );
		console.log("printOptions.codeType     = " + printOptions.codeType    );
		console.log("printOptions.receiptName  = " + printOptions.receiptName );
		
		var defPre = "";
		var url = "";
		var width = 80;
		if(printOptions.brContent){
			if(printOptions.codeType == 'br'){
				url = "/ggz/br.jsp?content=" + printOptions.brContent;
				width = 240;
			}else{
				url = "/ggz/qr.jsp?content=" + printOptions.brContent;
				width = 100;
			}
			var sw = width + 20;
			defPre = "<div style='float: left;width: 100%; height:100px; margin-right: -"+sw+"px;'> <h1 align='center' style='color: black;font-size:18px;'>武汉餐厨配供应链管理有限公司</h1><h2 align='center' style='color: black;font-size:16px;'>"+printOptions.receiptName+"</h2></div><div style='float: right;margin-right:20px;width: "+width+"px;'><img style='width:"+width+"px;height:100px' src='" + url + "'></div><br/>";
		}else{
			defPre = "<div style='width: 100%; height:80px;'> <h1 align='center' style='color: black;font-size:18px;'>武汉餐厨配供应链管理有限公司</h1><h2 align='center' style='color: black;font-size:16px;'>"+printOptions.receiptName+"</h2></div><br/>";
		}	
		
		var defApp = '<br/><table width="100%"><tr><td width="25%">第一联(白联):财务联</td><td width="25%"> 第二联(红联):客户联</td> <td width="25%">第三联(蓝联):客户签收联</td> <td width="25%">第四联(黄联):仓库联</td></tr></table>';
		
		$("#formobj",iframe.document).print({
			title: null,
			stylesheet : "plug-in/jquery/ggzPrint.css",
	        //Don't print this
	        noPrintSelector : ".no-print",
	        //Add this at top
	        prepend : printOptions.needPre ? (printOptions.prepend ? printOptions.prepend : defPre) : null,
	        //Add this on bottom
	        append : printOptions.needApp ? (printOptions.append ? printOptions.append : defApp) : null,
	        //Log to console when printing is done via a deffered callback
	        deferred: $.Deferred().done(function() { console.log('Printing done', arguments); })
	    });
	}

/**
 * 全屏编辑
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function editfs(title,url) {
	var name=gridname;
	 if (rowid == '') {
		tip('请选择编辑项目');
		return;
	}
	url += '&id='+rowid;
	openwindow(title,url,name,800,500);
}
// 删除调用函数
function delObj(url,name, msg) {
	gridname=name;
	createdialog('删除确认 ', msg==null?'确定删除该记录吗 ?':msg, url,name);
}
// 删除调用函数
function confuploadify(url, id) {
		$.dialog.setting.zIndex = getzIndex(true);
	$.dialog.confirm('确定删除吗', function(){
		deluploadify(url, id);
	}, function(){
	});
}
/**
 * 执行删除附件
 * 
 * @param url
 * @param index
 */
function deluploadify(url, id) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				$("#" + id).remove();// 移除SPAN
				m.remove(id);// 移除MAP对象内字符串
			}

		}
	});
}
// 普通询问操作调用函数
function confirm(url, content,name) {
	createdialog('提示信息 ', content, url,name);
}
/**
 * 提示信息
 */
function tip_old(msg) {
	$.dialog.setting.zIndex = getzIndex(true);
	$.dialog.tips(msg, 1);
}
/**
 * 提示信息
 */
function tip(msg) {
	try{
		$.dialog.setting.zIndex = getzIndex(true);
		$.messager.show({
			title : '提示信息',
			msg : msg,
			timeout : 1000 * 6
		});
	}catch(e){
		alertTipTop(msg,'10%');
	}
}

function alertTipTop(msg,top,title) {
	$.dialog.setting.zIndex = getzIndex(true);
	title = title?title:"提示信息";
	$.dialog({
			title:title,
			zIndex: getzIndex(),
			icon:'tips.gif',
			top:top,
			content: msg
		});
}

/**
 * 提示信息像alert一样
 */
function alertTip(msg,title) {
	$.dialog.setting.zIndex = getzIndex(true);
	title = title?title:"提示信息";
	$.dialog({
			title:title,
			zIndex: getzIndex(),
			icon:'tips.gif',
			content: msg
		});
}
/**
 * 创建添加或编辑窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function createwindow(title, addurl,width,height) {
	width = width?width:700;
	height = height?height:400;
	if(width=="100%" || height=="100%"){
		width = window.top.document.body.offsetWidth;
		height =window.top.document.body.offsetHeight-100;
	}
    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
	if(typeof(windowapi) == 'undefined'){
		$.dialog({
			content: 'url:'+addurl,
			lock : true,
			zIndex: getzIndex(),
			width:width,
			height:height,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}else{
		W.$.dialog({
			content: 'url:'+addurl,
			lock : true,
			width:width,
			zIndex:getzIndex(),
			height:height,
			parent:windowapi,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
	
}
/**
 * 创建添加或编辑子弹出窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function createsubwindow(title, addurl,width,height) {
	width = width?width:700;
	height = height?height:400;
	if(width=="100%" || height=="100%"){
		width = window.top.document.body.offsetWidth;
		height =window.top.document.body.offsetHeight-100;
	}
    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
	if(typeof(windowapi) == 'undefined'){
		$.dialog({
			content: 'url:'+addurl,
			lock : true,
			zIndex: getzIndex(),
			width:width,
			height:height,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return true;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}else{
		W.$.dialog({
			content: 'url:'+addurl,
			lock : true,
			width:width,
			zIndex:getzIndex(),
			height:height,
			parent:windowapi,
			title:title,
			opacity : 0.3,
			cache:false,
		    ok: function(){
		    	iframe = this.iframe.contentWindow;
				saveObj();
				return true;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
	
}
/**
 * 创建上传页面窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openuploadwin(title, url,name,width, height) {
	gridname=name;
	$.dialog({
	    content: 'url:'+url,
		zIndex: getzIndex(),
	    cache:false,
	    button: [
	        {
	            name: "开始上传",
	            callback: function(){
	            	iframe = this.iframe.contentWindow;
					iframe.upload();
					return false;
	            },
	            focus: true
	        },
	        {
	            name: "取消上传",
	            callback: function(){
	            	iframe = this.iframe.contentWindow;
					iframe.cancel();
	            }
	        }
	    ]
	});
}
/**
 * 创建查询页面窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function opensearchdwin(title, url, width, height) {
	$.dialog({
		content: 'url:'+url,
		zIndex: getzIndex(),
		title : title,
		lock : true,
		height : height,
		cache:false,
		width : width,
		opacity : 0.3,
		button : [ {
			name : '查询',
			callback : function() {
				iframe = this.iframe.contentWindow;
				iframe.searchs();
			},
			focus : true
		}, {
			name : '取消',
			callback : function() {

			}
		} ]
	});
}
/**
 * 创建不带按钮的窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openwindow(title, url,name, width, height) {
	gridname=name;
	if (typeof (width) == 'undefined'&&typeof (height) != 'undefined')
	{
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				cache:false,
				lock : true,
				width: 'auto',
			    height: height
			});
		}else{
			$.dialog({
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				cache:false,
				parent:windowapi,
				lock : true,
				width: 'auto',
			    height: height
			});
		}
	}
	if (typeof (height) == 'undefined'&&typeof (width) != 'undefined')
	{
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+url,
				title : title,
				zIndex: getzIndex(),
				lock : true,
				width: width,
				cache:false,
			    height: 'auto'
			});
		}else{
			$.dialog({
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				lock : true,
				parent:windowapi,
				width: width,
				cache:false,
			    height: 'auto'
			});
		}
	}
	if (typeof (width) == 'undefined'&&typeof (height) == 'undefined')
	{
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				lock : true,
				width: 'auto',
				cache:false,
			    height: 'auto'
			});
		}else{
			$.dialog({
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				lock : true,
				parent:windowapi,
				width: 'auto',
				cache:false,
			    height: 'auto'
			});
		}
	}
	
	if (typeof (width) != 'undefined'&&typeof (height) != 'undefined')
	{
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				width: width,
			    height:height,
				content: 'url:'+url,
				zIndex: getzIndex(),
				title : title,
				cache:false,
				lock : true
			});
		}else{
			$.dialog({
				width: width,
			    height:height,
				content: 'url:'+url,
				zIndex: getzIndex(),
				parent:windowapi,
				title : title,
				cache:false,
				lock : true
			});
		}
	}
}

/**
 * 创建询问窗口
 * 
 * @param title
 * @param content
 * @param url
 */
function createdialog(title, content, url,name) {
	$.dialog.setting.zIndex = getzIndex(true);
	$.dialog.confirm(content, function(){
		doSubmit(url,name);
		rowid = '';
	}, function(){
	});
}
/**
 * 执行保存
 * 
 * @param url
 * @param gridname
 */
function saveObj() {
	$('#btn_sub', iframe.document).click();
}

/**
 * 执行AJAX提交FORM
 * 
 * @param url
 * @param gridname
 */
function ajaxSubForm(url) {
	$('#myform', iframe.document).form('submit', {
		url : url,
		onSubmit : function() {
			iframe.editor.sync();
		},
		success : function(r) {
			tip('操作成功');
			reloadTable();
		}
	});
}
/**
 * 执行查询
 * 
 * @param url
 * @param gridname
 */
function search() {

	$('#btn_sub', iframe.document).click();
	iframe.search();
}

/**
 * 执行操作
 * 
 * @param url
 * @param index
 */
function doSubmit(url,name,data) {
	gridname=name;
	//--author：JueYue ---------date：20140227---------for：把URL转换成POST参数防止URL参数超出范围的问题
	var paramsData = data;
	if(!paramsData){
		paramsData = new Object();
		if (url.indexOf("&") != -1) {
			var str = url.substr(url.indexOf("&")+1);
			url = url.substr(0,url.indexOf("&"));
			var strs = str.split("&");
			for(var i = 0; i < strs.length; i ++) {
				paramsData[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
			}
		}      
	}
	//--author：JueYue ---------date：20140227---------for：把URL转换成POST参数防止URL参数超出范围的问题
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data : paramsData,
		url : url,// 请求的action路径
		error : function(textStatus, errorThrown) {// 请求失败处理函数
			tip(textStatus + ", " + errorThrown);
		},
		success : function(data) {
			var d = $.parseJSON(data);
			var msg = d.msg;
			if (d.success) {
				tip(msg);
				reloadTable();
			} else {
				tip(msg);
			}
		}
	});
	
	
}
/**
 * 退出确认框
 * 
 * @param url
 * @param content
 * @param index
 */
function exit(url, content) {
	$.dialog.setting.zIndex = getzIndex(true);
	$.dialog.confirm(content, function(){
		window.location = url;
	}, function(){
	});
}
/**
 * 模板页面ajax提交
 * 
 * @param url
 * @param gridname
 */
function ajaxdoSub(url, formname) {
	$('#' + formname).form('submit', {
		url : url,
		onSubmit : function() {
			editor.sync();
		},
		success : function(r) {
			tip('操作成功');
		}
	});
}
/**
 * ajax提交FORM
 * 
 * @param url
 * @param gridname
 */
function ajaxdoForm(url, formname) {
	$('#' + formname).form('submit', {
		url : url,
		onSubmit : function() {
		},
		success : function(r) {
			tip('操作成功');
		}
	});
}

function opensubwin(title, url, saveurl, okbutton, closebutton) {
	$.dialog({
		content: 'url:'+url,
		zIndex: getzIndex(),
		title : title,
		lock : true,
		opacity : 0.3,
		button : [ {
			name : okbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = frameElement.api.opener;// 来源页面
				$('#btn_sub', iframe.document).click();
				return false;
			}
		}, {
			name : closebutton,
			callback : function() {
			}
		} ]

	});
}

function openauditwin(title, url, saveurl, okbutton, backbutton, closebutton) {
	$.dialog({
		content: 'url:'+url,
		zIndex: getzIndex(),
		title : title,
		lock : true,
		opacity : 0.3,
		button : [ {
			name : okbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = $.dialog.open.origin;// 来源页面
				$('#btn_sub', iframe.document).click();
				return false;
			}
		}, {
			name : backbutton,
			callback : function() {
				iframe = this.iframe.contentWindow;
				win = frameElement.api.opener;// 来源页面
				$('#formobj', iframe.document).form('submit', {
					url : saveurl + "&code=exit",
					onSubmit : function() {
						$('#code').val('exit');
					},
					success : function(r) {
						$.dialog.tips('操作成功', 2);
						win.location.reload();
					}
				});

			}
		}, {
			name : closebutton,
			callback : function() {
			}
		} ]

	});
}
/*获取Cookie值*/
function getCookie(c_name)
{
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=")
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}
/* 设置 cookie  */
function setCookie(c_name, value, expiredays){
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}
// 添加标签
function addOneTab(subtitle, url, icon) {
	var indexStyle = getCookie("JEECGINDEXSTYLE");
	if(indexStyle=='sliding'||indexStyle=='bootstrap'||indexStyle=='ace'){
		//shortcut和bootstrap风格的tab跳转改为直接跳转
		window.location.href=url;
	}else{
		if (icon == '') {
			icon = 'icon folder';
		}
		window.top.$.messager.progress({
			text : '页面加载中....',
			interval : 300
		});
		window.top.$('#maintabs').tabs({
			onClose : function(subtitle, index) {
				window.top.$.messager.progress('close');
			}
		});
		if (window.top.$('#maintabs').tabs('exists', subtitle)) {
			window.top.$('#maintabs').tabs('select', subtitle);
			if (url.indexOf('isHref') != -1) {
				window.top.$('#maintabs').tabs('update', {
					tab : window.top.$('#maintabs').tabs('getSelected'),
					options : {
						title : subtitle,
						href:url,
						//content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
						closable : true,
						icon : icon
					}
				});
			}else {
				window.top.$('#maintabs').tabs('update', {
					tab : window.top.$('#maintabs').tabs('getSelected'),
					options : {
						title : subtitle,
						content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
						//content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
						closable : true,
						icon : icon
					}
				});
			}
		} else {
			if (url.indexOf('isHref') != -1) {
				window.top.$('#maintabs').tabs('add', {
					title : subtitle,
					href:url,
					closable : true,
					icon : icon
				});
			}else {
				window.top.$('#maintabs').tabs('add', {
					title : subtitle,
					content : '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
					closable : true,
					icon : icon
				});
			}
		}
	}
}
// 关闭自身TAB刷新父TABgrid
function closetab(title) {
	//暂时先不刷新
	//window.top.document.getElementById('tabiframe').contentWindow.reloadTable();
	//window.top.document.getElementById('maintabs').contentWindow.reloadTable();
	window.top.$('#maintabs').tabs('close', title);
	//tip("添加成功");
}

//popup  
//object: this  name:需要选择的列表的字段  code:动态报表的code
function inputClick(obj,name,code) {
	 $.dialog.setting.zIndex = getzIndex(true);
	 if(name==""||code==""){
		 alert("popup参数配置不全");
		 return;
	 }
	 if(typeof(windowapi) == 'undefined'){
		 $.dialog({
				content: "url:cgReportController.do?popup&id="+code,
				zIndex: getzIndex(),
				lock : true,
				title:"选择",
				width:800,
				height: 400,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
			    	var selected = iframe.getSelectRows();
			    	if (selected == '' || selected == null ){
				    	alert("请选择");
			    		return false;
				    }else {
					    var str = "";
				    	$.each( selected, function(i, n){
					    	if (i==0)
					    	str+= n[name];
					    	else
				    		str+= ","+n[name];
				    	});
				    	$(obj).val("");
				    	//$('#myText').searchbox('setValue', str);
					    $(obj).val(str);
				    	return true;
				    }
					
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			$.dialog({
				content: "url:cgReportController.do?popup&id="+code,
				zIndex: getzIndex(),
				lock : true,
				title:"选择",
				width:800,
				height: 400,
				parent:windowapi,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
			    	var selected = iframe.getSelectRows();
			    	if (selected == '' || selected == null ){
				    	alert("请选择");
			    		return false;
				    }else {
					    var str = "";
				    	$.each( selected, function(i, n){
					    	if (i==0)
					    	str+= n[name];
					    	else
				    		str+= ","+n[name];
				    	});
				    	$(obj).val("");
				    	//$('#myText').searchbox('setValue', str);
					    $(obj).val(str);
				    	return true;
				    }
					
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
}
/*
	自定义url的弹出
	obj:要填充的控件,可以为多个，以逗号分隔
	name:列表中对应的字段,可以为多个，以逗号分隔（与obj要对应）
	url：弹出页面的Url
*/
function popClick(obj,name,url) {
	 $.dialog.setting.zIndex = getzIndex(true);
	var names = name.split(",");
	var objs = obj.split(",");
	 if(typeof(windowapi) == 'undefined'){
		 $.dialog({
				content: "url:"+url,
				zIndex: getzIndex(),
				lock : true,
				title:"选择",
				width:700,
				height: 400,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
			    	var selected = iframe.getSelectRows();
			    	if (selected == '' || selected == null ){
				    	alert("请选择");
			    		return false;
				    }else {
				    	for(var i1=0;i1<names.length;i1++){
						    var str = "";
					    	$.each( selected, function(i, n){
						    	if (i==0)
						    	str+= n[names[i1]];
						    	else{
									str+= ",";
									str+=n[names[i1]];
								}
					    	});
							if($("#"+objs[i1]).length>=1){
								$("#"+objs[i1]).val("");
								$("#"+objs[i1]).val(str);
							}else{
								$("input[name='"+objs[i1]+"']").val("");
								$("input[name='"+objs[i1]+"']").val(str);
							}
						 }
				    	return true;
				    }
					 
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			$.dialog({
				content: "url:"+url,
				zIndex: getzIndex(),
				lock : true,
				title:"选择",
				width:700,
				height: 400,
				parent:windowapi,
				cache:false,
			     ok: function(){
			    	iframe = this.iframe.contentWindow;
			    	var selected = iframe.getSelectRows();
			    	if (selected == '' || selected == null ){
				    	alert("请选择");
			    		return false;
				    }else {
				    	for(var i1=0;i1<names.length;i1++){
						    var str = "";
					    	$.each( selected, function(i, n){
						    	if (i==0)
						    	str+= n[names[i1]];
						    	else{
									str+= ",";
									str+=n[names[i1]];
								}
					    	});
					    	if($("#"+objs[i1]).length>=1){
								$("#"+objs[i1]).val("");
								$("#"+objs[i1]).val(str);
							}else{
								$("[name='"+objs[i1]+"']").val("");
								$("[name='"+objs[i1]+"']").val(str);
							}
						 }
				    	return true;
				    }
					
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
}


/**
 * Jeecg Excel 带栏位选择条件导出
 * 代入查询条件 带入导出对象名称
 */
function OptionalExcelExport(url,datagridId,entityName){
	var queryParams = $('#'+datagridId).datagrid('options').queryParams;
	$('#'+datagridId+'tb').find('*').each(function() {
	    queryParams[$(this).attr('name')] = $(this).val();
	});
	var params = '&';
	$.each(queryParams, function(key, val){
		params+='&'+key+'='+val;
	}); 
	var fields = '&field=';
	$.each($('#'+ datagridId).datagrid('options').columns[0], function(i, val){
		if(val.field != 'opt'){
			fields+=val.field+',';
		}
	}); 
	var href = url + encodeURI(fields + params);
	
	var height = parseInt(window.innerHeight - 20);
	var windowUrl = "commonToolController.do?multiSelExport&selNames=" + "" + "&entityName=" + encodeURIComponent(entityName) + "&hurl=" + encodeURIComponent(href);
	createwindow("请选择您要导出的字段", windowUrl, 620, height);
}


function didSelMultiExcel(hurl,names){
	var url = hurl + "&exclusions=" + encodeURI(names);
	window.location.href = url;
}

/**
 * Jeecg Excel 导出
 * 代入查询条件
 */
function JeecgExcelExport(url,datagridId){
	var queryParams = $('#'+datagridId).datagrid('options').queryParams;
	$('#'+datagridId+'tb').find('*').each(function() {
	    queryParams[$(this).attr('name')] = $(this).val();
	});
	var params = '&';
	$.each(queryParams, function(key, val){
		params+='&'+key+'='+val;
	}); 
	var fields = '&field=';
	$.each($('#'+ datagridId).datagrid('options').columns[0], function(i, val){
		if(val.field != 'opt'){
			fields+=val.field+',';
		}
	}); 
	window.location.href = url + encodeURI(fields+params);
}
/**
 * 自动完成的解析函数
 * @param data
 * @returns {Array}
 */
function jeecgAutoParse(data){
	var parsed = [];
    	$.each(data.rows,function(index,row){
    		parsed.push({data:row,result:row,value:row.id});
    	});
			return parsed;
}

function dump(arr,level) {
    var dumped_text = "";
    if(!level) level = 0;
     
    //The padding given at the beginning of the line.
    var level_padding = "";
    for(var j=0;j<level+1;j++) level_padding += "    ";
     
    if(typeof(arr) == 'object') { //Array/Hashes/Objects 
        for(var item in arr) {
            var value = arr[item];
             
            if(typeof(value) == 'object') { //If it is an array,
                dumped_text += level_padding + "'" + item + "' ...\n";
                dumped_text += dump(value,level+1);
            } else {
                dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
            }
        }
    } else { //Stings/Chars/Numbers etc.
        dumped_text = "===>"+arr+"<===("+typeof(arr)+")";
    }
    return dumped_text;
}
