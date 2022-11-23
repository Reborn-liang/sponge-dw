<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?savenewpwd" usePlugin="password" layout="table" beforeSubmit="encryptPassword()">
	<input id="id" type="hidden" value="${user.id }">
	<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
			<tr>
				<td align="right" width="10%"><span class="filedzt">原密码:</span></td>
				<td class="value"><input id="password" type="password" value="" name="password" class="inputxt" datatype="*" errormsg="请输入原密码" /> <span class="Validform_checktip"> 请输入原密码 </span></td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">新密码:</span></td>
				<td class="value"><input id="newpassword1" type="password" value="" name="newpassword" class="inputxt" plugin="passwordStrength" datatype="*6-18" errormsg="密码至少6个字符,最多18个字符！" /> <span
					class="Validform_checktip"> 密码至少6个字符,最多18个字符！ </span> <span class="passwordStrength" style="display: none;"> <b>密码强度：</b> <span>弱</span><span>中</span><span class="last">强</span> </span></td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">重复密码:</span></td>
				<td class="value"><input id="newpassword" type="password" recheck="newpassword" class="inputxt" datatype="*6-18" errormsg="两次输入的密码不一致！"> <span class="Validform_checktip"></span></td>
			</tr>
		</tbody>
	</table>
</t:formvalid>
<script type="text/javascript" src="plug-in/login/js/crypto-js.min.js"></script>
<script type="text/javascript">
	// 默认的 KEY 与 iv 如果没有给
	const KEY = CryptoJS.enc.Utf8.parse("9qJhN9A5jytWA4*a");
	const IV = CryptoJS.enc.Utf8.parse('9qJhN9A5jytWA4*a');
	/**
	 * AES加密 ：字符串 key iv  返回base64
	 */
	function Encrypt(word, keyStr, ivStr) {
		let key = KEY;
		let iv = IV;
		if (keyStr) {
			key = CryptoJS.enc.Utf8.parse(keyStr);
			iv = CryptoJS.enc.Utf8.parse(ivStr);
		}
		let srcs = CryptoJS.enc.Utf8.parse(word);
		var encrypted = CryptoJS.AES.encrypt(srcs, key, {
			iv: iv,
			mode: CryptoJS.mode.CBC,
			padding: CryptoJS.pad.ZeroPadding
		});
		return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);

	}
	/**
	 * AES 解密 ：字符串 key iv  返回base64
	 *
	 * @return {string}
	 */
	function Decrypt(word, keyStr, ivStr) {
		let key  = KEY;
		let iv = IV;

		if (keyStr) {
			key = CryptoJS.enc.Utf8.parse(keyStr);
			iv = CryptoJS.enc.Utf8.parse(ivStr);
		}

		let base64 = CryptoJS.enc.Base64.parse(word);
		let src = CryptoJS.enc.Base64.stringify(base64);

		let decrypt = CryptoJS.AES.decrypt(src, key, {
			iv: iv,
			mode: CryptoJS.mode.CBC,
			padding: CryptoJS.pad.ZeroPadding
		});

		let decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
		return decryptedStr.toString();
	}
	function encryptPassword() {
		$("#password").val(Encrypt($("#password").val()))
		$("#newpassword1").val(Encrypt($("#newpassword1").val()))
		$("#newpassword").val(Encrypt($("#newpassword").val()))
	}
</script>
</body>