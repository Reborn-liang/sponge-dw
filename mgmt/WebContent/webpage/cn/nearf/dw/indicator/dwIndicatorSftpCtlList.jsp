<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
<%
	String sfyn=(String)request.getAttribute("sfyn");
	if("N".equals(sfyn)||sfyn==null){
%>
	$("#dwIndicatorSftpCtl_table").find("input").attr("disabled","disabled");
<%
	}
%>
$(document).ready(function(){
    	if(location.href.indexOf("load=detail")!=-1){
			$(":input").attr("disabled","true");
		}
    });
    
</script>
<div style="width: auto;height: 300px;overflow-y:auto;overflow-x:auto;">
<table cellpadding="0" cellspacing="1" class="formtable" id="dwIndicatorSftpCtl_table" >
	<tbody id="add_dwIndicatorSftpCtl_table" >	
	<c:if test="${fn:length(dwIndicatorSftpCtlList)  <= 0 }">
			<tr>
					<input name="dwIndicatorSftpCtlList[0].id" type="hidden"  value="${poVal.id}"/>
					<input name="dwIndicatorSftpCtlList[0].indicatorId" type="hidden"  value="${poVal.indicatorId}"/>
					<%-- <input name="dwIndicatorSftpCtlList[0].createDate" type="hidden"  value="${poVal.createDate}"/>
					<input name="dwIndicatorSftpCtlList[0].updateDate" type="hidden"  value="${poVal.updateDate}"/> --%>
			</tr>
			<tr>
			  <td align="right">
				<label class="Validform_label">SFTP_服务器地址:</label>
				</td>
			  <td class="value" colspan="3">
				  	<input name="dwIndicatorSftpCtlList[0].sftpServer" maxlength="300" id="sftpServer" type="text" class="inputxt" style="width: 300px">
				  <label class="Validform_label" style="display: none;">SFTP_服务器地址</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label">
						sftp用户名:
					</label>
				</td>
				<td class="value">
				     	 <input name="dwIndicatorSftpCtlList[0].sftpUser" type="text" style="width: 200px" class="inputxt" >
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">sftp用户名</label>
				</td>
				<td align="right">
					<label class="Validform_label">
						sftp用户名密码:
					</label>
				</td>
				<td class="value">
				     	 <input name="dwIndicatorSftpCtlList[0].sftpUserPasswd" type="text" style="width: 200px" class="inputxt" >
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">sftp用户名密码</label>
				</td>
			</tr>
			<tr>
			  <td align="right">
				<label class="Validform_label">原目录:</label>
				</td>
			  <td class="value">
				  	<input name="dwIndicatorSftpCtlList[0].sourceFolder" maxlength="300" id="sourceFolder" type="text" class="inputxt" style="width: 200px">
				  <label class="Validform_label" style="display: none;">原目录</label>
				</td>
			  <td align="right">
				<label class="Validform_label">目标目录:</label>
			  </td>
			  <td class="value">
				  	<input name="dwIndicatorSftpCtlList[0].targetFolder" maxlength="300" id="targetFolder" type="text" class="inputxt" style="width: 200px">
				  <label class="Validform_label" style="display: none;">目标目录</label>
				</td>
				</tr>
			<tr>
<%-- 			   <td align="right">
				<label class="Validform_label">文件类型:</label>
			  </td>
			  <td class="value">
				  <t:dictSelect field="dwIndicatorSftpCtlList[0].fileType" type="radio" typeGroupCode="SftpFileType"  defaultVal="CSV" hasLabel="false" datatype="*"/>
				  <label class="Validform_label" style="display: none;">文件类型</label>
				</td> --%>
				<td align="right">
					<label class="Validform_label">
						导出文件格式:
					</label>
				</td>
				<td class="value" colspan="3">
				     	 <input name="dwIndicatorSftpCtlList[0].dumpFileType" type="text" style="width: 200px" class="inputxt" >
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">导出文件格式</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label">
						导出字段是否定长:
					</label>
				</td>
				<td class="value">
						<t:dictSelect  field="dwIndicatorSftpCtlList[0].fixedColumnLength" type="radio" typeGroupCode="sf_yn"  defaultVal="N" hasLabel="false" datatype="*"/>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">导出字段是否定长</label>
				</td>
				<td align="right">
					<label class="Validform_label">
						字段分隔符:
					</label>
				</td>
				<td class="value">
				     	<input name="dwIndicatorSftpCtlList[0].splitChar" type="text" style="width: 200px" class="inputxt" >
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">字段分隔符</label>
				</td>
			</tr>
	</c:if>
	<c:if test="${fn:length(dwIndicatorSftpCtlList)  > 0 }">
		<c:forEach items="${dwIndicatorSftpCtlList}" var="poVal" varStatus="stuts" begin="0" end="0">
			<tr>
					<input name="dwIndicatorSftpCtlList[0].id" type="hidden" value="${poVal.id}"/>
					<input name="dwIndicatorSftpCtlList[0].indicatorId" type="hidden" value="${poVal.indicatorId}"/>
					<input name="dwIndicatorSftpCtlList[0].createDate" type="hidden" value="${poVal.createDate}"/>
					<input name="dwIndicatorSftpCtlList[0].updateDate" type="hidden" value="${poVal.updateDate}"/>
			</tr>
			<tr>
			  <td align="right">
				<label class="Validform_label">SFTP_服务器地址:</label>
				</td>
			  <td class="value" colspan="3">
				  	<input name="dwIndicatorSftpCtlList[0].sftpServer" maxlength="300" id="sftpServer" type="text" class="inputxt" style="width: 300px" value='${poVal.sftpServer}'>
				  <label class="Validform_label" style="display: none;">SFTP_服务器地址</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label">
						sftp用户名:
					</label>
				</td>
				<td class="value">
				     	 <input name="dwIndicatorSftpCtlList[0].sftpUser" type="text" style="width: 200px" class="inputxt"  value='${poVal.sftpUser}'>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">sftp用户名</label>
				</td>
				<td align="right">
					<label class="Validform_label">
						sftp用户名密码:
					</label>
				</td>
				<td class="value">
				     	 <input name="dwIndicatorSftpCtlList[0].sftpUserPasswd" type="text" style="width: 200px" class="inputxt"  value='${poVal.sftpUserPasswd}'>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">sftp用户名密码</label>
				</td>
			</tr>
			<tr>
			  <td align="right">
				<label class="Validform_label">原目录:</label>
				</td>
			  <td class="value">
				  	<input name="dwIndicatorSftpCtlList[0].sourceFolder" maxlength="300" id="sourceFolder" type="text" class="inputxt" style="width: 200px" value='${poVal.sourceFolder}'>
				  <label class="Validform_label" style="display: none;">原目录</label>
				</td>
			  <td align="right">
				<label class="Validform_label">目标目录:</label>
			  </td>
			  <td class="value">
				  	<input name="dwIndicatorSftpCtlList[0].targetFolder" maxlength="300" id="targetFolder" type="text" class="inputxt" style="width: 200px" value='${poVal.targetFolder}'>
				  <label class="Validform_label" style="display: none;">目标目录</label>
				</td>
				</tr>
			<tr>
<%-- 			   <td align="right">
				<label class="Validform_label">文件类型:</label>
			  </td>
			  <td class="value" >
				  <t:dictSelect  field="dwIndicatorSftpCtlList[0].fileType" type="radio" typeGroupCode="SftpFileType"  defaultVal="${poVal.fileType}" hasLabel="false" datatype="*"/>
				  <label class="Validform_label" style="display: none;">文件类型</label>
				</td> --%>
				<td align="right">
					<label class="Validform_label">
						导出文件格式:
					</label>
				</td>
				<td class="value" colspan="3">
				     	 <input name="dwIndicatorSftpCtlList[0].dumpFileType" type="text" style="width: 200px" class="inputxt"  value='${poVal.dumpFileType}'>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">导出文件格式</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label">
						导出字段是否定长:
					</label>
				</td>
				<td class="value">
						<t:dictSelect  field="dwIndicatorSftpCtlList[0].fixedColumnLength" type="radio" typeGroupCode="sf_yn"  defaultVal="N" hasLabel="false" datatype="*"/>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">导出字段是否定长</label>
				</td>
				<td align="right">
					<label class="Validform_label">
						字段分隔符:
					</label>
				</td>
				<td class="value">
				     	<input name="dwIndicatorSftpCtlList[0].splitChar" type="text" style="width: 200px" class="inputxt"  value='${poVal.splitChar}'>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">字段分隔符</label>
				</td>
			</tr>
		</c:forEach>
	</c:if>	
	</tbody>
</table>
</div>