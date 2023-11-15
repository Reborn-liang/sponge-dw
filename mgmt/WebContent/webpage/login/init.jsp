<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	 var browserversion = "";
	 
	 if ($.browser.msie) {
		 browserversion = "IE" + $.browser.version;
	 }
	 
	 if ($.browser.webkit) {
		 browserversion = "Chrome" + $.browser.version; 
	 }
	 
	 if ($.browser.mozilla) {
		 browserversion = "Mozilla Firefox" + $.browser.version;
	 }
	 
	 if ($.browser.opera) {
		 browserversion = "Opera" + $.browser.version;
	 }
	 
	 window.location.href = "loginController.do?login";
	 
 });
</script>
</head>
<body>
</body>
</html>