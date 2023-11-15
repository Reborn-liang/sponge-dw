<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>

<% 
String upload_method = (String)request.getAttribute("upload_method");
if(upload_method == null || upload_method.length() == 0){
	upload_method = "importExcel";
	request.setAttribute("upload_method", upload_method);
}
%>

<head>
<title>通用Excel导入${controller_name}</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" beforeSubmit="upload">
	<fieldset class="step">
	<div class="form"><t:upload name="fiels" buttonText="选择要导入的文件" uploader="${controller_name}.do?${upload_method}" extend="*.xls;*.xlsx;*.zip" id="file_upload" formData="documentTitle"
	dialog="false"></t:upload></div>
	<div class="form" id="filediv" style="height: 50px"></div>
	</fieldset>
</t:formvalid>
<script type="text/javascript">

	function showStartHandingMask(){    
    
    	$("<div class=\"datagrid-mask\" style=\"background:#666666;\"></div>").css({display:"block",width:$("body")[0].offsetWidth+10,height:$(window).height()}).appendTo("body");     
    	$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候……").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});    
	}    
	
	    
	function releaseMaskPage(){    
	    $(".datagrid-mask,.datagrid-mask-msg").remove();    
	}    
	 
	   
    function alertShowInfoTip(msg,title) {  
    	releaseMaskPage();
        $.dialog.setting.zIndex = 19990;  
        title = title?title:"提示信息";  
        $.dialog({  
                title:title,  
                
                lock:true,  
                content: msg,  
                width:400,  
                height:300,  
                ok:function(){  
                    var wind = frameElement.api.opener;   
                    frameElement.api.close();  
                    wind.reloadTable();  
                }  
            });  
    }  

</script>
</body>
</html>
