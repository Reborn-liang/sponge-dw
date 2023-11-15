<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>部门信息</title>
<t:base type="jquery,easyui,tools"></t:base>
<script type="text/javascript">
	$(window).load(function(){
	});
	
	$(function() {
		$('#cc').combotree({
			url : 'departController.do?setPFunction&selfId=${depart.id}',
            width: 155,
            onSelect : function(node) {

                changeOrgType();
            }
        });
        if(!$('#cc').val()) { 
            var orgTypeSelect = $("#orgType");
            var companyOrgType = '<option value="1" <c:if test="${orgType=='1'}">selected="selected"</c:if>><t:mutiLang langKey="common.company"/></option>';
            orgTypeSelect.empty();
            orgTypeSelect.append(companyOrgType);
        } else { 
            $("#orgType option:first").remove();
        }
        
        if('${empty pid}' == 'false') { 
            $('#cc').combotree('setValue', '${pid}');
        }
	});
    function changeOrgType() { 
        var orgTypeSelect = $("#orgType");
        var optionNum = orgTypeSelect.get(0).options.length;
        
        if(optionNum == 1) {
            $("#orgType option:first").remove();
            var bumen = '<option value="2" <c:if test="${orgType=='2'}">selected="selected"</c:if>><t:mutiLang langKey="common.department"/></option>';
            var gangwei = '<option value="3" <c:if test="${orgType=='3'}">selected="selected"</c:if>><t:mutiLang langKey="common.position"/></option>';
            orgTypeSelect.append(bumen).append(gangwei);
        }
    }
    
    function onChangeOrgType() {
    	var orgType = $("#orgType").val();
        
    	if (orgType == 4) {
    		$("#departname").val("");
    		$("#departname").attr("readonly", true);
    		
        } else {
        	$("#departname").val("");
    		$("#departname").removeAttr("readonly");
        }
    }
</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" action="systemController.do?saveDepart">
	<input id="id" name="id" type="hidden" value="${depart.id }">
	<fieldset class="step">
        <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="common.department.name"/>: </label>
            <input id="departname" name="departname" class="inputxt" value="${depart.departname }"  datatype="s1-20">
            <span class="Validform_checktip"><t:mutiLang langKey="departmentname.rang1to20"/></span>
        </div>
        <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="position.desc"/>: </label>
            <input name="description" class="inputxt" value="${depart.description }">
        </div>
        <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="parent.depart"/>: </label>
            <input id="cc" name="TSPDepart.id" value="${depart.TSPDepart.id}">
        </div>
        <div class="form">
			<label class="Validform_label"> 人员标配: </label>
			<input id="staffing" name="staffing" type="text" style="width: 150px" class="easyui-validatebox" value='${depart.staffing}' required="true" datatype="*"> <span class="Validform_checktip"></span> <label class="Validform_label" style="display: none;">标配</label>
		</div>
        <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="common.org.type"/>: </label>
            <select name="orgType" id="orgType" onchange="onChangeOrgType();">
                <option value="1" <c:if test="${orgType=='1'}">selected="selected"</c:if>><t:mutiLang langKey="common.company"/></option>
                <option value="2" <c:if test="${orgType=='2'}">selected="selected"</c:if>><t:mutiLang langKey="common.department"/></option>
                <option value="3" <c:if test="${orgType=='3'}">selected="selected"</c:if>><t:mutiLang langKey="common.position"/></option>
                <option value="4" <c:if test="${orgType=='4'}">selected="selected"</c:if>>门店</option>
            </select>
        </div>
        
        <c:if test="${depart == null}">
 			<input type="hidden" name="orgCode" value="${depart.orgCode }">
        </c:if>
        <c:if test="${depart != null}">
         <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="机构编码"/>: </label>
            <input id="orgCode" name="orgCode" class="inputxt" value="${depart.orgCode }">
        </div>
        </c:if>
        
        <div class="form">
            <label class="Validform_label"> <t:mutiLang langKey="排序"/>: </label>
            <input name="sort" class="inputxt" value="${depart.sort }">
        </div>
	</fieldset>
</t:formvalid>
</body>
</html>
