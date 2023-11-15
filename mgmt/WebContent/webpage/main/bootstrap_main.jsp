<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
<title><t:mutiLang langKey="jeect.platform"/></title>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<link rel="stylesheet" href="plug-in/bootstrap/css/bootstrap.css" type="text/css"></link>
<style type="text/css">
.bootstrap-menu i {
	display: inline-block;
	width: 14px;
	height: 14px;
	margin-top: 1px;
	*margin-right: .3em;
	line-height: 14px;
	vertical-align: text-top;
	background-image: url("plug-in/bootstrap/img/glyphicons-halflings.png");
	background-repeat: no-repeat;
}

.bootstrap-center {
	height: 530px;
	overflow-y: auto;
	margin-top: -20px;
	margin-left: 2px;
	margin-right: 2px;
}

.bootstrap-icon {
	display: inline-block;
	width: 16px;
	height: 16px;
	line-height: 16px;
	vertical-align: text-top;
	background-repeat: no-repeat;
	background-image: url("plug-in/accordion/images/pictures.png");
}

.footer {
	margin-top: 10px;
}
</style>
</head>
<body>

<div id="header">
<div class="navbar">
<div class="navbar-inner">
<div class="container-fluid"><a class="brand" href="http:
<div class="nav-no-collapse bootstrap-menu">

<ul class="nav pull-right usernav">
	<li style="line-height: 43px;"><span style="color: #CC33FF"><t:mutiLang langKey="common.user"/>:</span><span style="color: #666633">(${userName })</span> <span style="color: #CC33FF"><t:mutiLang langKey="common.role"/></span>:<span style="color: #666633">${roleName
	}</span></li>
	<li class="dropdown"><a href="#" class="dropdown-toggle avatar" data-toggle="dropdown"> <i class="icon-wrench"></i> <span class="txt"><t:mutiLang langKey="common.control.panel"/></span> <b class="caret"></b> </a>
	<ul class="dropdown-menu">
		<li onclick="openwindow('<t:mutiLang langKey="common.profile"/>','userController.do?userinfo')"><a href="javascript:;"><i class="icon-user"></i> <t:mutiLang langKey="common.profile"/></a></li>
		<li onclick="add('<t:mutiLang langKey="common.change.password"/>','userController.do?changepassword')"><a href="javascript:;"><i class="icon-pencil"></i> <t:mutiLang langKey="common.change.password"/></a></li>
		<li onclick="add('<t:mutiLang langKey="common.change.style"/>','userController.do?changestyle')"><a href="javascript:;"><i class="icon-retweet"></i> <t:mutiLang langKey="common.change.style"/></a></li>
	</ul>
	</li>
	<li class="dropdown"><a href="#" class="dropdown-toggle avatar" data-toggle="dropdown"> <i class="icon-arrow-left"></i> <span class="txt"><t:mutiLang langKey="common.logout"/></span> <b class="caret"></b> </a>
	<ul class="dropdown-menu">
		<li onclick="exit('loginController.do?logout','<t:mutiLang langKey="common.exit.confirm"/>',1);"><a href="javascript:;"><i class="icon-off"></i><t:mutiLang langKey="common.exit"/></a></li>
	</ul>
	</li>
</ul>
</div>
</div>
</div>
</div>
</div>



<div id="wrapper" class="bootstrap-center"></div>


<footer class="footer">
<div class="container">
<ul class="footer-links">
	<li style="display: inline;">&copy; <t:mutiLang langKey="common.copyright"/></li>
	<li style="display: inline;">&nbsp;<t:mutiLang langKey="common.copyright"/><t:mutiLang langKey="common.browser"/></li>
</ul>
</div>
</footer>
<script type="text/javascript" src="plug-in/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plug-in/accordion/js/bootstrap_main.js"></script>
</body>
</html>