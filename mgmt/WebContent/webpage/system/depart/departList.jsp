<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_depart_list" class="easyui-layout" fit="true">
    <div region="center" style="padding: 1px;">
        <t:datagrid name="departList" title="common.department.list"
                    actionUrl="departController.do?departgrid"
                    treegrid="true" idField="departid" pagination="false">
            <t:dgCol title="common.id" field="id" treefield="id" hidden="true"></t:dgCol>
            <t:dgCol title="common.department.name" field="departname" treefield="text"></t:dgCol>
            <t:dgCol title="position.desc" field="description" treefield="src"></t:dgCol>
            <t:dgCol title="common.org.code" field="orgCode" treefield="fieldMap.orgCode"></t:dgCol>
            <t:dgCol title="common.org.type" field="orgType" dictionary="orgtype" treefield="fieldMap.orgType"></t:dgCol>
            <t:dgCol title="忽略健康证" field="ignoreHealthCert" treefield="fieldMap.ignoreHealthCert" formatterjs="function(value, row, index){return formatOtherUiCompoentForData(yesNoData, 'id', 'name', value, row, index);}  "></t:dgCol>
            <t:dgCol title="禁止入职" field="noEntry" treefield="fieldMap.noEntry" formatterjs="function(value, row, index){return formatOtherUiCompoentForData(yesNoData, 'id', 'name', value, row, index);}  "></t:dgCol>
            <t:dgCol title="人员标配"  field="staffing"  treefield="fieldMap.staffing" ></t:dgCol>
            <t:dgCol title="排序" field="sort" treefield="fieldMap.sort"></t:dgCol>
            <t:dgCol title="common.operation" field="opt"></t:dgCol>
            <t:dgDelOpt url="departController.do?del&id={id}" title="common.delete"></t:dgDelOpt>
            <t:dgFunOpt funname="queryUsersByDepart(id)" title="view.member"></t:dgFunOpt>
            <t:dgFunOpt funname="setRoleByDepart(id,text)" title="role.set"></t:dgFunOpt>
        </t:datagrid>
        <div id="departListtb" style="padding: 3px; height: 25px">
            <div style="float: left;">
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-add" onclick="addOrg()"><t:mutiLang langKey="common.add.param" langArg="common.department"/></a>
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit" onclick="update('<t:mutiLang langKey="common.edit.param" langArg="common.department"/>','departController.do?update','departList')"><t:mutiLang langKey="common.edit.param" langArg="common.department"/></a>
            </div>
        </div>
    </div>
</div>
<div data-options="region:'east',
	title:'<t:mutiLang langKey="member.list"/>',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
	style="width: 400px; overflow: hidden;" id="eastPanel">
    <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="userListpanel"></div>
</div>

<script type="text/javascript">

</script>