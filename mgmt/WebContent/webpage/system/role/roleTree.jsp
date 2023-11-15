<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$(function() {
        
		$('#orgRoleTree').tree({
			checkbox : true,
			url : 'roleController.do?getRoleTree&orgId=${orgId}',
			onLoadSuccess : function(node) {
				expandAll();
			}
		});
		$("#functionListPanel").panel(
				{
					title :'<t:mutiLang langKey="common.role.list"/>',
					tools:[{iconCls:'icon-save',handler:function(){mysubmit();}}]
				}
		);
	});
	function mysubmit() { 
		var orgId = $("#orgId").val();
		var ids = GetNode();
		doSubmit("roleController.do?updateOrgRole&roleIds=" + ids + "&orgId=" + orgId);
	}
    
    function GetNode() {
		var node = $('#orgRoleTree').tree('getChecked');
		var cnodes = '';
		var pnodes = '';
		var pnode = null; 
		for ( var i = 0; i < node.length; i++) {
			if ($('#orgRoleTree').tree('isLeaf', node[i].target)) {
				cnodes += node[i].id + ',';
				pnode = $('#orgRoleTree').tree('getParent', node[i].target); 
				while (pnode!=null) {
					pnodes += pnode.id + ',';
					pnode = $('#orgRoleTree').tree('getParent', pnode.target); 
				}
			}
		}
		cnodes = cnodes.substring(0, cnodes.length - 1);
		pnodes = pnodes.substring(0, pnodes.length - 1);
		return cnodes + "," + pnodes;
	}
	
	function expandAll() {
		var node = $('#orgRoleTree').tree('getSelected');
		if (node) {
			$('#orgRoleTree').tree('expandAll', node.target);
		} else {
			$('#orgRoleTree').tree('expandAll');
		}
	}
    
	function selecrAll() {
		var node = $('#orgRoleTree').tree('getRoots');
		for ( var i = 0; i < node.length; i++) {
            $('#orgRoleTree').tree("check",node[i].target);
	    }
	}
    
	function reset() {
		$('#orgRoleTree').tree('reload');
	}

	$('#selecrAllBtn').linkbutton({
	});
	$('#resetBtn').linkbutton({   
	});   
</script>
<div class="easyui-layout" fit="true">
    <div region="center" style="padding: 1px;">
        <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="functionListPanel">
            <input type="hidden" name="orgId" value="${orgId}" id="orgId">
            <a id="selecrAllBtn" onclick="selecrAll();"><t:mutiLang langKey="select.all"/></a>
            <a id="resetBtn" onclick="reset();"><t:mutiLang langKey="common.reset"/></a>
            <ul id="orgRoleTree"></ul>
        </div>
    </div>
</div>
