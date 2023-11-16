<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="cn.nearf.ggz.utils.SequenceUtils"%>
<%@page import="cn.nearf.dw.indicator.filter.CsrfTokenManager"%>

<%@ taglib prefix="t" uri="/easyui-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags"%>

<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String CSRFToken = CsrfTokenManager.createTokenForSession(request.getSession());
session.setAttribute(CsrfTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME, CSRFToken);
%>

<c:set var="webRoot" value="<%=basePath%>" />
<c:set var="pageUrl" value="<%=request.getServletPath()%>" />

<script type="text/javascript">
	document.addEventListener('DOMContentLoaded', function() {
		var forms = document.getElementsByTagName('form');
		for (var i = 0; i < forms.length; i++) {
			var form = forms[i];
			if (form.method.toLowerCase() === 'post') {
				var csrfInput = document.createElement('input');
				csrfInput.type = 'hidden';
				csrfInput.name = 'CSRFToken';
				csrfInput.id = 'CSRFToken';
				csrfInput.value = '<%=CSRFToken%>';
				form.appendChild(csrfInput);
			}
		}
	});
<c:if test='${pageUrl.indexOf("/h5/") < 0}'>
	
	function setCookie(c_name, value, expiredays){
		var exdate=new Date();
		exdate.setDate(exdate.getDate() + expiredays);
		document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
	}

	function getzIndex(flag){
		var zindexNumber = getCookie("ZINDEXNUMBER");
		if(zindexNumber == null){
			zindexNumber = 2010;
			setCookie("ZINDEXNUMBER",zindexNumber);
		}else{
			if(zindexNumber < 2030){
				zindexNumber = 2030;
			}
			var n = flag?zindexNumber:parseInt(zindexNumber) + parseInt(10);
			setCookie("ZINDEXNUMBER",n);
		}
		return zindexNumber;
	}
	
	function doAction(title, url, gname, width, height) {
		gridname = gname;
		var ids = [];
		var rows = $("#" + gname).datagrid('getSelections');
		if (rows.length > 0) {
			$.dialog.setting.zIndex = getzIndex(true);
			$.dialog.confirm('确定' + title + '吗?', function(r) {
				if (r) {
					showProgress();
					
					for (var i = 0; i < rows.length; i++) {
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
							hideProgress();
							
							var d = $.parseJSON(data);
							if (d.success) {
								var msg = d.msg;
								tip(msg);
								reloadTable();
								$("#" + gname).datagrid('unselectAll');
								ids = '';
							}
						},
						
						error:function(textStatus, errorThrown) {
							hideProgress();
							
							tip(errorThrown);
					    	}
					});
				}
			});
		} else {
			createdialog('确认' + title, '确定' + title + '吗 ?', url, gname);
		}
	}
	
	function doActionWithOne(title, url, gname, width, height) {
		gridname = gname;
		var ids = "";
		var rows = $("#" + gname).datagrid('getSelections');
		if (rows.length == 1) {
			$.dialog.setting.zIndex = getzIndex(true);
			$.dialog.confirm('确定' + title + '吗?', function(r) {
				if (r) {
					showProgress();
					
					ids = rows[0].id;
					$.ajax({
						url : url,
						type : 'post',
						data : {
							id : ids
						},
						cache : false,
						success : function(data) {
							hideProgress();
							
							var d = $.parseJSON(data);
							var msg = d.msg;
							tip(msg);
							if (d.success) {
								reloadTable();
								$("#" + gname).datagrid('unselectAll');
								ids = '';
							}
						},
						error:function(textStatus, errorThrown) {
							hideProgress();
							
							tip(errorThrown);
					    	}
					});
				}
			});
		} else if (rows.length == 0) {
			tip("请选择" + title + "的数据");
		} else if (rows.length > 1) {
			tip("请选择一条" + title + "的数据");
		}
	}
	
	function doBatchAction(title, url, gname, width, height) {
		gridname = gname;
		var ids = [];
		var rows = $("#" + gname).datagrid('getSelections');
		if (rows.length > 0) {
			$.dialog.setting.zIndex = getzIndex(true);
			$.dialog.confirm('确定' + title + '吗?', function(r) {
				if (r) {
					showProgress();
					
					for (var i = 0; i < rows.length; i++) {
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
							hideProgress();
							
							var d = $.parseJSON(data);
							if (d.success) {
								var msg = d.msg;
								tip(msg);
								reloadTable();
								$("#" + gname).datagrid('unselectAll');
								ids = '';
							}
						},
						
						error:function(textStatus, errorThrown) {
							hideProgress();
							
							tip(errorThrown);
					    	}
					});
				}
			});
		} else {
			tip("请选择" + title + "的数据");
		}
	}
	
	
	function addOrUpdate(title, url, id, width, height, isRestful) {
		gridname = id;
		var rowsData = $('#' + id).datagrid('getSelections');
		if (!rowsData || rowsData.length == 0) {
		} else if (rowsData.length > 1) {
			tip('请选择一条记录');
			return;
		} else {
			if (isRestful != 'undefined' && isRestful) {
				url += '/' + rowsData[0].id;
			} else {
				url += '&id=' + rowsData[0].id;
			}
		}
		
		createwindow(title, url, width, height);
	}
</c:if>
	
	
</script>

<c:if test='<%=request.getServletPath().indexOf("/h5/") < 0%>'>
	<script type="text/javascript" src="webpage/cn/nearf/common/common.js?v=1.1.5"></script>
</c:if>

