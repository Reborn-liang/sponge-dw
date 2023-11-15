<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>自定义布局模板</title>
<t:base type="jquery,easyui,tools"></t:base>
<SCRIPT type="text/javascript">
  function test(data) {
	  $.messager.alert('提示信息', data.msg);
	}
  </SCRIPT>
</head>
<body>
<t:formvalid formid="formobj" beforeSubmit="setFtlContent()"  dialog="true" usePlugin="password" layout="table" action="cgformFtlController.do?saveEditor" >
	<input id="id" name="id" type="hidden" value="${cgformFtlPage.id}">
	<input id="cgformId" name="cgformId" type="hidden" value="${cgformFtlPage.cgformId}">
	<input id="ftlVersion" name="ftlVersion" type="hidden" value="${cgformFtlPage.ftlVersion}">
	<input id="ftlWordUrl" name="ftlWordUrl" type="hidden" value="${cgformFtlPage.ftlWordUrl}">
	<input id="createBy" name="createBy" type="hidden" value="${cgformFtlPage.createBy}">
	<input id="createName" name="createName" type="hidden" value="${cgformFtlPage.createName}">
	<input id="createDate" name="createDate" type="hidden" value="${cgformFtlPage.createDate}">
	<input id="editorType" name="editorType" type="hidden" value="02">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right"><label class="Validform_label">模板名称:</label></td>
			<td class="value"><input class="inputxt" id="cgformName" name="cgformName" value="${cgformFtlPage.cgformName}" datatype="*"> <span class="Validform_checktip"></span></td>
		</tr>
		<tr>
			<td class="value" colspan=2>
				<input type="hidden" id="ftl" name="ftlContent" >
				<script id="ftlContent" type="text/plain" style="width:100%;">${cgformFtlPage.ftlContent == NULL || cgformFtlPage.ftlContent == '' ? '' : cgformFtlPage.ftlContent}</script>
			</td>
		</tr>
	</table>
</t:formvalid>
<script>UEDITOR_HOME_URL='<%=path%>/plug-in/Formdesign/js/ueditor/';</script>
<script type="text/javascript" charset="utf-8" src="plug-in/Formdesign/js/ueditor/ueditor.config.js?2023"></script>
<script type="text/javascript" charset="utf-8" src="plug-in/Formdesign/js/ueditor/ueditor.all.js?2023"> </script>
<script type="text/javascript" charset="utf-8" src="plug-in/Formdesign/js/ueditor/lang/zh-cn/zh-cn.js?2023"></script>
<script type="text/javascript" charset="utf-8" src="plug-in/Formdesign/js/ueditor/formdesign/leipi.formdesign.v4.js?2023"></script>
<script type="text/javascript" charset="utf-8" src="plug-in/Formdesign/js/ueditor/formdesign/weixinplugs.js"></script>
<script type="text/javascript">

var leipiEditor = UE.getEditor('ftlContent',{
            
            toolleipi:true,
            textarea: 'design_content',   
            
            toolbars: [[
            'fullscreen', 'source', '|', 'undo', 'redo', '|',
            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
            'directionalityltr', 'directionalityrtl', 'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo', 'music', 'attachment', 'map',  'insertframe', 'insertcode', 'webapp', 'pagebreak', 'template', 'background', '|',
            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
            'print', 'preview', 'searchreplace', 'help', 'drafts'
        ]],
            
            
            wordCount:false,
            
            elementPathEnabled:false,
            
            initialFrameHeight:400
            
            
        });

 var leipiFormDesign = {
    
    exec : function (method) {
        leipiEditor.execCommand(method);
    },
    
   parse_form:function(template,fields)
    {
        
        var preg =  /(\|-<span(((?!<span).)*leipiplugins=\"(radios|checkboxs|select|popup)\".*?)>(.*?)<\/span>-\||<(img|input|textarea|select).*?(<\/select>|<\/textarea>|\/>))/gi,
        preg_attr =/(\w+)=\"(.?|.+?)\"/gi,
        preg_group =/<input.*?\/>/gi;
        if(!fields) fields = 0;

        var template_parse = template,template_data = new Array(),add_fields=new Object(),checkboxs=0;

        var pno = 0;
        template.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
            var parse_attr = new Array(),attr_arr_all = new Object(),name = '', select_dot = '' , is_new=false;
            var p0 = plugin;
            var tag = p6 ? p6 : p4;
            

            if(tag == 'radios' || tag == 'checkboxs')
            {
                plugin = p2;
            }else if(tag == 'select')
            {
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
            }
            plugin.replace(preg_attr, function(str0,attr,val) {
                    if(attr=='name')
                    {
                        if(val=='leipiNewField')
                        {
                            is_new=true;
                            fields++;
                            val = 'data_'+fields;
                        }
                        name = val;
                    }
                    
                    if(tag=='select' && attr=='value')
                    {
                        if(!attr_arr_all[attr]) attr_arr_all[attr] = '';
                        attr_arr_all[attr] += select_dot + val;
                        select_dot = ',';
                    }else
                    {
                        attr_arr_all[attr] = val;
                    }
                    var oField = new Object();
                    oField[attr] = val;
                    parse_attr.push(oField);
            }) 
            
             if(tag =='checkboxs') 
             {
                plugin = p0;
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
                var name = 'checkboxs_'+checkboxs;
                attr_arr_all['parse_name'] = name;
                attr_arr_all['name'] = '';
                attr_arr_all['value'] = '';
                
                attr_arr_all['content'] = '<span leipiplugins="checkboxs"  title="'+attr_arr_all['title']+'">';
                var dot_name ='', dot_value = '';
                p5.replace(preg_group, function(parse_group) {
                    var is_new=false,option = new Object();
                    parse_group.replace(preg_attr, function(str0,k,val) {
                        if(k=='name')
                        {
                            if(val=='leipiNewField')
                            {
                                is_new=true;
                                fields++;
                                val = 'data_'+fields;
                            }

                            attr_arr_all['name'] += dot_name + val;
                            dot_name = ',';

                        }
                        else if(k=='value')
                        {
                            attr_arr_all['value'] += dot_value + val;
                            dot_value = ',';

                        }
                        option[k] = val;    
                    });
                    
                    if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                    attr_arr_all['options'].push(option);
                    
                    var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
                    attr_arr_all['content'] +='<input type="checkbox" name="'+option['name']+'" value="'+option['value']+'"  '+checked+'/>'+option['value']+'&nbsp;';

                    if(is_new)
                    {
                        var arr = new Object();
                        arr['name'] = option['name'];
                        arr['leipiplugins'] = attr_arr_all['leipiplugins'];
                        add_fields[option['name']] = arr;

                    }

                });
                attr_arr_all['content'] += '</span>';

                
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                template_data[pno] = attr_arr_all;
                checkboxs++;

             }else if(name)
            {
                if(tag =='radios') 
                {
                    plugin = p0;
                    plugin = plugin.replace('|-','');
                    plugin = plugin.replace('-|','');
                    attr_arr_all['value'] = '';
                    attr_arr_all['content'] = '<span leipiplugins="radios" name="'+attr_arr_all['name']+'" title="'+attr_arr_all['title']+'">';
                    var dot='';
                    p5.replace(preg_group, function(parse_group) {
                        var option = new Object();
                        parse_group.replace(preg_attr, function(str0,k,val) {
                            if(k=='value')
                            {
                                attr_arr_all['value'] += dot + val;
                                dot = ',';
                            }
                            option[k] = val;    
                        });
                        option['name'] = attr_arr_all['name'];
                        if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                        attr_arr_all['options'].push(option);
                        
                        var checked = option['checked'] !=undefined ? 'checked="checked"' : '';
                        attr_arr_all['content'] +='<input type="radio" name="'+attr_arr_all['name']+'" value="'+option['value']+'"  '+checked+'/>'+option['value']+'&nbsp;';

                    });
                    attr_arr_all['content'] += '</span>';

                }else
                {
                    attr_arr_all['content'] = is_new ? plugin.replace(/leipiNewField/,name) : plugin;
                }
                
                
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                if(is_new)
                {
                    var arr = new Object();
                    arr['name'] = name;
                    arr['leipiplugins'] = attr_arr_all['leipiplugins'];
                    add_fields[arr['name']] = arr;
                }
                template_data[pno] = attr_arr_all;

               
            }
            pno++;
        })
        var parse_form = new Object({
            'fields':fields,
            'template':template,
            'parse':template_parse,
            'data':template_data,
            'add_fields':add_fields
        });
        return JSON.stringify(parse_form);
    },
    
    fnCheckForm : function ( type ) {
        if(leipiEditor.queryCommandState( 'source' ))
            leipiEditor.execCommand('source');
            
        if(leipiEditor.hasContents()){
            leipiEditor.sync();
            
            alert("你点击了保存,这里可以异步提交，请自行处理....");
			$("#ftl").val(leipiEditor.getContent());
            return false;
            
            var type_value='',formid=0,fields=$("#fields").val(),formeditor='';

            if( typeof type!=='undefined' ){
                type_value = type;
            }
            
            formeditor=leipiEditor.getContent();
            
            var parse_form = this.parse_form(formeditor,fields);
            
            
             
             $.ajax({
                type: 'POST',
                url : 'cgformFtlController.do?saveEditor',
                
                data : {'type' : type_value,'formid':formid,'parse_form':parse_form},
                success : function(data){
                    if(confirm('查看js解析后，提交到服务器的数据，请临时允许弹窗'))
                    {
                        win_parse=window.open('','','width=800,height=600');
                        
                        data  = data.replace(/<\/+textarea/,'&lt;textarea');
                        win_parse.document.write('<textarea style="width:100%;height:100%">'+data+'</textarea>');
                        win_parse.focus();
                    }
                    
                    
                }
            });
            
        } else {
            alert('表单内容不能为空！')
            $('#submitbtn').button('reset');
            return false;
        }
    } ,
   
    fnReview : function (){
        if(leipiEditor.queryCommandState( 'source' ))
            leipiEditor.execCommand('source');
            
        if(leipiEditor.hasContents()){
            leipiEditor.sync();       
            
            var type_value='',formid=0,fields=$("#fields").val(),formeditor='';
            formeditor=leipiEditor.getContent();
            
            var parse_form = this.parse_form(formeditor,fields);    
           var style= '<link rel="stylesheet" href="plug-in/Formdesign/js/ueditor/formdesign/bootstrap/css/bootstrap.css">'+
           ' <link rel="stylesheet" href="plug-in/Formdesign/js/ueditor/formdesign/leipi.style.css">';
           $.ajax({
                type: 'POST',
                url : 'cgformFtlController.do?parseUeditorOld',
                dataType : 'json',
                data : {'action' :'edit','parseForm':parse_form},
                success : function(msg){
						
                    	 W.$.dialog({
                 			content:style+msg.msg,
                 			lock : true,
                 			title:"模板预览 [${cgformFtlPage.cgformName}]",
                 			opacity : 0.5,
                 			width:500,
                 			height:400,
                 			cache:false,
                 			modal:false,
                 		    ok: function(){
                 		    	iframe = this.iframe.contentWindow;
                 		    	iframe.goForm();
                 				return false;
                 		    },
                 		    cancelVal: '关闭',
                 		    cancel: true 
                 		});
          
                }
            });
           
        } else {
            alert('表单内容不能为空！');
            return false;
        }
    }
};
function setFtlContent(){
    if(leipiEditor.queryCommandState( 'source' ))
            leipiEditor.execCommand('source');
            
    if(leipiEditor.hasContents()){
        leipiEditor.sync();
	    $("#ftl").val(leipiEditor.getContent());
	}
}
</script>
</body>