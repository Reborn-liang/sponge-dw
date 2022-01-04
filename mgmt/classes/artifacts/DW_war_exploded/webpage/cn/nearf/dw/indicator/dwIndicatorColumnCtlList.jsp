<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<script type="text/javascript" src = "webpage/cn/nearf/common/resetTrNum.js"></script>
<script type="text/javascript">
	$('#addDwIndicatorColumnCtlBtn').linkbutton({   
	    iconCls: 'icon-add'
	});  
	$('#delDwIndicatorColumnCtlBtn').linkbutton({   
	    iconCls: 'icon-remove'  
	}); 
	
	$('#addDwIndicatorColumnCtlBtn').bind('click', function(){   
 		 var tr =  $("#add_dwIndicatorColumnCtl_table_template tr").clone();
	 	 $("#add_dwIndicatorColumnCtl_table").append(tr);
	 	 resetTrNum('add_dwIndicatorColumnCtl_table');
	 	 return false;
    });  
	
	$('#delDwIndicatorColumnCtlBtn').bind('click', function(){   
      	$("#add_dwIndicatorColumnCtl_table").find("input:checked").parent().parent().remove();   
        resetTrNum('add_dwIndicatorColumnCtl_table'); 
        return false;
    }); 
	
    $(document).ready(function(){
    	var modelCode = $("#modelCode").val();
    	if(modelCode!=null && modelCode!='undefined' && modelCode!=""){
    		  $("#modelCodeInd").val(modelCode);
   	    }else{
   		    $("#modelCodeInd").val("");
   	    }

    	i=0;
    	$(".datagrid-toolbar").parent().css("width","auto");
    	if(location.href.indexOf("load=detail")!=-1){
			$(":input").attr("disabled","true");
			$(".datagrid-toolbar").hide();
		}
		//将表格的表头固定
	    $("#dwIndicatorColumnCtl_table").createhftable({
	    	height:'300px',
			width:'auto',
			fixFooter:false
		});
		
	    onLoadFromTableList(); 
	    onLoadFromColumnList();
    }); 
    
    //schema改变,table值随之改变
    function onFactSchemaChange(index){
    	var modelCode = $("#modelCodeInd").val();
    	console.log("onFactSchemaChange, model code:" + modelCode)
    	if(modelCode==null || modelCode=='undefined'){
    		console.log("aaa:"+modelCode);
    		modelCode = "";
    	}
		var schema = $("select[name='dwIndicatorColumnCtlList["+index+"].fromSchema']").val();
		var TableVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromTable']").val();
		var ColumnVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromColumn']").val();
		ColumnVal=TableVal="";
		$.ajax({
			url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema+'&modelCode='+modelCode,
			success:function(data){
				try{
					var fromTable = $("select[name='dwIndicatorColumnCtlList["+index+"].fromTable']");
					fromTable.empty();
					fromTable.append("<option value=''>---请选择---</option>");
					var result=JSON.parse(data).obj;
					for(var i= 0; i<result.length ; i++){
						fromTable.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
					}
				}catch (e) {
				}
			},error:function(textStatus,errorThrown){
				 alertTip("请求错误，状态："+textStatus);
			}
		});
	}
    
    //编辑时,带出table下拉列表的值
    function onLoadFromTableList(){
    	var modelCode = $("#modelCodeInd").val();
    	if(modelCode==null || modelCode=='undefined'){
    		console.log("bbb:"+modelCode);
    		modelCode = "";
    	}
    	var Schema = $("select:not([name^='dwIndicatorColumnCtlList[#'])[name$='].fromSchema']");
    	var Table = $("select:not([name^='dwIndicatorColumnCtlList[#'])[name$='].fromTable']"); 
    	$.each(Schema, function(index, value) {
    		var i = index;
    		var $this = $(value);
    		var schemaVal = $this.val();
    		$.each(Table, function(index, value) {
    			var x = index;
        		var $this = $(value);
        		var tableVal = $this.val();
        		if(i==x){
        			$.ajax({
            			url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schemaVal+'&modelCode='+modelCode+"&i="+i,
            			success:function(data){
            				try{
            					var fromTable = $("select[name='dwIndicatorColumnCtlList["+index+"].fromTable']");
            					fromTable.empty();
            					//fromTable.append("<option value=''>---请选择---</option>");
            					var result=JSON.parse(data).obj;
            					 for(var i= 0; i<result.length ; i++){
            						 if(result[i].name!=tableVal){
            							fromTable.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
            						 }else{
            							fromTable.append("<option selected='selected' value='"+result[i].id+"'>"+result[i].name+"</option>");
            						 }
            					}
            				}catch (e) {
            				}
            			},error:function(textStatus,errorThrown){
            				 alertTip("请求错误，状态："+textStatus);
            			}
            		 });
        		}
    		});
     });
   } 
    //schema改变和table改变,column随之改变
    function onFromColumnSchemaChange(index){
    	var schema = $("select[name='dwIndicatorColumnCtlList["+index+"].fromSchema']").val();
    	var tableVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromTable']").val();
    	var seColumnVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromColumn']").val();
    	var inColumnVal = $("input[name='dwIndicatorColumnCtlList["+index+"].fromColumn']").val();
    	inColumnVal=seColumnVal="";
    	if(tableVal!="" &&schema != ""){
    		$.ajax({
    			url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schema+'&tableName='+tableVal,
    			success:function(data){
    				try{
    					var fromColumn = $("select[name='dwIndicatorColumnCtlList["+index+"].fromColumn']");
    					fromColumn.empty();
    					fromColumn.append("<option value=''>---请选择---</option>");
    					var result=JSON.parse(data).obj;
    					for(var i= 0; i<result.length ; i++){
    						fromColumn.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
    					}
    				}catch (e) {
    				}
    			},error:function(textStatus,errorThrown){
    				 alertTip("请求错误，状态："+textStatus);
    			}
    		});
    	}
    }
    
    //编辑时带出column列表的值
      function onLoadFromColumnList(){
      	var Table = $("select:not([name^='dwIndicatorColumnCtlList[#'])[name$='].fromTable']"); 
    	var Schema = $("select:not([name^='dwIndicatorColumnCtlList[#'])[name$='].fromSchema']");
    	var Column = $("select:not([name^='dwIndicatorColumnCtlList[#'])[name$='].fromColumn']");
    	 $.each(Schema, function(index, value) {
    		var x = index;
    		var $this = $(value);
    		var schemaVal = $this.val();
    		$.each(Table, function(index, value) {
    			var y = index;
        		var $this = $(value);
        		var tableVal = $this.val();
        		$.each(Column, function(index, value) {
        			var z = index;
            		var $this = $(value);
            		var columnVal = $this.val();
            		if(x==y && x==z && y==z){
            			console.log("333 x="+x+", y="+y+", schema="+schemaVal+", tableValue="+tableVal);
            			$.ajax({
                			url:'dwIndicatorCtlController.do?getTableNameOrColumnFromInfomation&schema='+schemaVal+'&tableName='+tableVal,
                			success:function(data){
                				try{
                					var fromColumn = $("select[name='dwIndicatorColumnCtlList["+index+"].fromColumn']");
                					
                			    		var columnType = $("input[name='dwIndicatorColumnCtlList["+index+"].columnType']");
                			    		var columnLength = $("input[name='dwIndicatorColumnCtlList["+index+"].columnLength']");
               
                					var result=JSON.parse(data).obj;
                					for(var i= 0; i<result.length ; i++){
                						if(result[i].name!=columnVal){
                							fromColumn.append("<option value='"+result[i].id+"'>"+result[i].name+"</option>");
                						}else{
                							fromColumn.append("<option selected='selected' value='"+result[i].id+"'>"+result[i].name+"</option>");
                							console.log("4444 result[i].t="+result[i].t+", result[i].l="+result[i].l+", schema="+schemaVal+", tableValue="+tableVal);
                							columnType.val(result[i].t);
                    	 					columnLength.val(result[i].l);
                						}
                					}
                				}catch (e) {
                				}
                			},error:function(textStatus,errorThrown){
                				 alertTip("请求错误，状态："+textStatus);
                			}
                		});
            		}
        		});
         });
     });
   }
    
    function onFromColumnChange(index){
	    	console.log("onFromColumnChange:"+index)
	    	var fromSchemaVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromSchema']").val();
	    	var fromTableVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromTable']").val();
	    	var fromColVal = $("select[name='dwIndicatorColumnCtlList["+index+"].fromColumn']").val();
	    	var indInput =  $("input[name='dwIndicatorColumnCtlList["+index+"].indicatorColumn']");
	    	var formulaInput =  $("input[name='dwIndicatorColumnCtlList["+index+"].formula']");
		var indVal = indInput.val();
		var formulaVal = formulaInput.val();
		console.log("indVal:"+indVal+", fromColVal:"+fromColVal);
		if(formulaVal==null || formulaVal==""){
			formulaInput.val(fromTableVal+"."+fromColVal);
		}
		if(indVal==null || indVal==""){
			indInput.val(fromColVal);
		}
		$.ajax({
			url:'dwIndicatorCtlController.do?getColumnInfomation&schema='+fromSchemaVal+'&tableName='+fromTableVal+'&columnName='+fromColVal,
			success:function(data){
				try{
			    		var columnType = $("input[name='dwIndicatorColumnCtlList["+index+"].columnType']");
			    		var columnLength = $("input[name='dwIndicatorColumnCtlList["+index+"].columnLength']");
			    		var result=JSON.parse(data).obj;
					columnType.val(result.t);
			 	 	columnLength.val(result.l);
				}catch (e) {
				}
			},error:function(textStatus,errorThrown){
				 alertTip("请求错误，状态："+textStatus);
			}
		});
    }
    
    function a(index){
		var filter = $("input[name='dwIndicatorColumnCtlList["+index+"].filters']").val();
	    	title="过滤条件";
	    	url="dwIndicatorCtlController.do?goFilter&filter="+filter+"&row="+index+"&column=filters"+"&columnName=" + encodeURIComponent(title);
	    	W = this;
	    	createsubwindow(title,url);
	}
    
    function formula(index){
		var formula = $("input[name='dwIndicatorColumnCtlList["+index+"].formula']").val();
	    	title="公式";
	    	url="dwIndicatorCtlController.do?goFilter&filter="+formula+"&row="+index+"&column=formula"+"&columnName=" + encodeURIComponent(title);
	    	W = this;
	    	createsubwindow(title,url);
	}
	    
	function didFinishEdit(value,row,column) {
	    	var obj = $("input[name='dwIndicatorColumnCtlList["+row+"]."+column+"']");
	    	obj.val(value);
	}
    
</script>
<div style="padding: 3px; height: 25px;width:auto;" class="datagrid-toolbar">
	<a id="addDwIndicatorColumnCtlBtn" href="#">添加</a> <a id="delDwIndicatorColumnCtlBtn" href="#">删除</a> 
</div>
<input id="modelCodeInd" name="modelCodeInd" type="hidden" value=""/>
<table border="0" cellpadding="2" cellspacing="0" id="dwIndicatorColumnCtl_table">
	<tr bgcolor="#E6E6E6">
		<td align="center" bgcolor="#EEEEEE">序号</td>
		<td align="center" bgcolor="#EEEEEE">操作</td>
				  <td align="left" bgcolor="#EEEEEE">
						字段类型
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						源schema
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						源表
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						源字段
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						公式
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						指标字段
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						字段类型
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						字段长度
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						导出字段名
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						导出排序位置
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						过滤条件
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
				  </td>
	</tr>
	<tbody id="add_dwIndicatorColumnCtl_table">	
	<c:if test="${fn:length(dwIndicatorColumnCtlList)  <= 0 }">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">1</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck"/></td>
					<input name="dwIndicatorColumnCtlList[0].id" type="hidden"/>
					<input name="dwIndicatorColumnCtlList[0].indicatorId" type="hidden"/>
					<input name="dwIndicatorColumnCtlList[0].createDate" type="hidden"/>
					<input name="dwIndicatorColumnCtlList[0].updateDate" type="hidden"/>
				  <td align="left">
					 <t:dictSelect field="dwIndicatorColumnCtlList[0].type" type="list" noNeedBlank="true"
						typeGroupCode="DWFileType" defaultVal="${dwIndicatorColumnCtlPage.type}" hasLabel="false"  title="字段类型" extendJson="{style:'width:80px'}"></t:dictSelect>     
					  <label class="Validform_label" style="display: none;">字段类型</label>
					</td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[0].fromSchema" name="dwIndicatorColumnCtlList[0].fromSchema" style="width: 120px" onchange="onFactSchemaChange(0)"><option value=''>---请选择---</option>
					  		<option value="data">data</option>
							<option value="view">view</option>
					  	</select>	
					 	<span class="Validform_checktip"></span>
					  <label class="Validform_label" style="display: none;">源schema</label>
					</td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[0].fromTable" name="dwIndicatorColumnCtlList[0].fromTable" datatype="*" style="width: 150px" onchange="onFromColumnSchemaChange(0)">
						</select>
						<span class="Validform_checktip"></span>	
					   <label class="Validform_label" style="display: none;">源表</label>
					</td>
				  <td align="left">
					  	<select id="dwIndicatorColumnCtlList[0].fromColumn" name="dwIndicatorColumnCtlList[0].fromColumn"  style="width: 150px"" onchange="onFromColumnChange(0)">
						</select>
						<span class="Validform_checktip"></span>
					  <label class="Validform_label" style="display: none;">源字段</label>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].formula" maxlength="255" datatype="*" type="text" class="inputxt"  style="width:157px">
					    <label class="Validform_label" style="display: none;">公式</label>
				  </td>
				  <td align="left">
					  <button style="width:40px;" type="button" onclick="formula(0)">扩展</button>
				  </td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].indicatorColumn" maxlength="255" 
					  		type="text" class="inputxt"  style="width:100px;" datatype="*">
					  <label class="Validform_label" style="display: none;">指标字段</label>
					</td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].columnType" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;" datatype="*">
					  <label class="Validform_label" style="display: none;">字段类型</label>
					</td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].columnLength" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;" >
					  <label class="Validform_label" style="display: none;">字段长度</label>
					</td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].exportName" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;" >
					  <label class="Validform_label" style="display: none;">导出字段名</label>
					</td>
				  <td align="left">
					  	<input name="dwIndicatorColumnCtlList[0].exportOrder" maxlength="10" 
					  		type="text" class="inputxt"  style="width:120px;" >
					  <label class="Validform_label" style="display: none;">导出排序位置</label>
					</td>
				  <td align="left">
				  	<input name="dwIndicatorColumnCtlList[0].filters" maxlength="2000"
				  		type="text" class="inputxt"  style="width:300px;" >
					  <label class="Validform_label" style="display: none;">过滤条件</label>
				  </td>
				   <td align="left">
					  <button style="width:40px;" type="button" onclick="a(0)">扩展</button>
				  </td>
   			</tr>
	</c:if>
	<c:if test="${fn:length(dwIndicatorColumnCtlList)  > 0 }">
		<c:forEach items="${dwIndicatorColumnCtlList}" var="poVal" varStatus="stuts">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">${stuts.index+1 }</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck" /></td>
					<input name="dwIndicatorColumnCtlList[${stuts.index }].id" type="hidden" value="${poVal.id }"/>
					<input name="dwIndicatorColumnCtlList[${stuts.index }].indicatorId" type="hidden" value="${poVal.indicatorId }"/>
					<input name="dwIndicatorColumnCtlList[${stuts.index }].createDate" type="hidden" value="${poVal.createDate }"/>
					<input name="dwIndicatorColumnCtlList[${stuts.index }].updateDate" type="hidden" value="${poVal.updateDate }"/>
				   <td align="left">
							<t:dictSelect field="dwIndicatorColumnCtlList[${stuts.index }].type" type="list"
										noNeedBlank="true" typeGroupCode="DWFileType" defaultVal="${poVal.type}" hasLabel="false"  title="字段类型" extendJson="{style:'width:80px'}"></t:dictSelect>     
					  <label class="Validform_label" style="display: none;">字段类型</label>
				   </td>
				   <td align="left">
					  	<select id="dwIndicatorColumnCtlList[${stuts.index }].fromSchema" name="dwIndicatorColumnCtlList[${stuts.index }].fromSchema" style="width: 120px" onchange="onFactSchemaChange(${stuts.index })">
					  		<option value='${poVal.fromSchema }' selected="selected">${poVal.fromSchema }</option>
					  		<c:if test="${poVal.fromSchema == 'data'}">
					  			<option value="view">view</option> 
					  		</c:if>
					  		<c:if test="${poVal.fromSchema == 'view'}">
					  			<option value="data">data</option>
					  		</c:if>
							<c:if test="${poVal.fromSchema == ''}">
								<option value="data">data</option>
					  			<option value="view">view</option>
					  		</c:if>
					  	</select>	
					 	<span class="Validform_checktip"></span>	
					  <label class="Validform_label" style="display: none;">源schema</label>
				   </td>
				    <td align="left">
					  	<select id="dwIndicatorColumnCtlList[${stuts.index }].fromTable" name="dwIndicatorColumnCtlList[${stuts.index }].fromTable" style="width: 150px" onchange="onFromColumnSchemaChange(${stuts.index })">
					  		<option value="${poVal.fromTable }" selected="selected">${poVal.fromTable }</option>
						</select>
						<span class="Validform_checktip"></span>	
					    <label class="Validform_label" style="display: none;">源表</label>
					</td>
				   <td align="left">
					  	<select name="dwIndicatorColumnCtlList[${stuts.index }].fromColumn" style="width: 150px" onchange="onFromColumnChange(${stuts.index})" >
							<option value="${poVal.fromColumn }" selected="selected">${poVal.fromColumn}</option>
						</select>
						<span class="Validform_checktip"></span>
					    <label class="Validform_label" style="display: none;">源字段</label>
					</td>
					<td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].formula" maxlength="255" datatype="*"
					  		type="text" class="inputxt"  style="width:157px;" value="${poVal.formula}">
					    <label class="Validform_label" style="display: none;">公式</label>
					</td>
				   <td align="left">
					  <button style="width:40px;" type="button" onclick="formula(${stuts.index})">扩展</button>
				  </td>
					<td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].indicatorColumn" maxlength="40" 
					  		type="text" class="inputxt"  style="width:100px;" datatype="*" value="${poVal.indicatorColumn }">
					  <label class="Validform_label" style="display: none;">指标字段</label>
				   </td>
				   <td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].columnType" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;"  value="${poVal.columnType }" datatype="*">
					  <label class="Validform_label" style="display: none;">字段类型</label>
				   </td>
				   <td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].columnLength" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;"  value="${poVal.columnLength }">
					  <label class="Validform_label" style="display: none;">字段长度</label>
				   </td>
				   <td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].exportName" maxlength="255" 
					  		type="text" class="inputxt"  style="width:120px;"  value="${poVal.exportName }">
					  <label class="Validform_label" style="display: none;">导出字段名</label>
				   </td>
				   <td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].exportOrder" maxlength="10" 
					  		type="text" class="inputxt"  style="width:120px;"  value="${poVal.exportOrder }">
					  <label class="Validform_label" style="display: none;">导出排序位置</label>
				   </td>
				   <td align="left">
					  	<input name="dwIndicatorColumnCtlList[${stuts.index }].filters" maxlength="2000" 
					  		type="text" class="inputxt"  style="width:300px;"  value="${poVal.filters }">
					  <label class="Validform_label" style="display: none;">过滤条件</label>
				   </td>
				   <td align="left">
					  <button style="width:40px;" type="button" onclick="a(${stuts.index})">扩展</button>
				  </td>
   			</tr>
		</c:forEach>
	</c:if>	
	</tbody>
</table>
