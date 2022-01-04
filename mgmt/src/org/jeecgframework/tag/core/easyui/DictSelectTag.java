package org.jeecgframework.tag.core.easyui;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.nearf.ggz.utils.ObjectUtils;

/**
 * 
 * 选择下拉框
 * 
 * @author: lianglaiyang
 * @date： 日期：2013-04-18
 * @version 1.0
 */
public class DictSelectTag extends TagSupport {
	private static final long serialVersionUID = 1;
	
	@Autowired
	private static SystemService systemService;
	
	private String typeGroupCode; // 数据字典类型
	private String field; // 选择表单的Name EAMPLE:<select name="selectName" id = ""
							// />
	private String id; // 选择表单ID EAMPLE:<select name="selectName" id = "" />
	private String defaultVal; // 默认值
	private String divClass; // DIV样式
	private String labelClass; // Label样式
	private String title; // label显示值
	private boolean hasLabel = true; // 是否显示label
	private String type;// 控件类型select|radio|checkbox
	private String dictTable;// 自定义字典表
	private String dictField;// 自定义字典表的匹配字段-字典的编码值
	private String dictText;// 自定义字典表的显示文本-字典的显示值
	private String extendJson;//扩展参数
	private String dictCondition;
	private boolean noNeedBlank;
	private boolean onlyShowDefault;

	//权限 
	//限制权限类型 stockroom|supplier
	private String permissionType;
	
	//权限附加
	private String permissionCondition;
	
	public String getPermissionType() {
		return permissionType;
	}

	public String getPermissionCondition() {
		return permissionCondition;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public void setPermissionCondition(String permissionCondition) {
		this.permissionCondition = permissionCondition;
	}
	
	public boolean isOnlyShowDefault() {
		return onlyShowDefault;
	}

	public void setOnlyShowDefault(boolean onlyShowDefault) {
		this.onlyShowDefault = onlyShowDefault;
	}

	public boolean isNoNeedBlank() {
		return noNeedBlank;
	}

	public void setNoNeedBlank(boolean noNeedBlank) {
		this.noNeedBlank = noNeedBlank;
	}
	
	public String getDictCondition() {
		return dictCondition;
	}

	public void setDictCondition(String dicCondition) {
		this.dictCondition = dicCondition;
	}
	private String datatype;
	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	private String readonly;// 只读属性
    public String getReadonly() {
		return readonly;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	
	

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			out.print(end().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public StringBuffer end() {
		StringBuffer sb = new StringBuffer();
		
		Gson gson = new Gson();
		
		if (StringUtils.isBlank(divClass)) {
			divClass = "form"; // 默认form样式
		}
		if (StringUtils.isBlank(labelClass)) {
			labelClass = "Validform_label"; // 默认label样式
		}
		if (dictTable != null) {
			List<Map<String, Object>> list = queryDic();
			if ("radio".equals(type)) {
				for (Map<String, Object> map : list) {
					radio(map.get("text").toString(), map.get("field")
							.toString(), sb);
				}
			} else if ("checkbox".equals(type)) {
				for (Map<String, Object> map : list) {
					checkbox(map.get("text").toString(), map.get("field")
							.toString(), sb);
				}
			} else if("text".equals(type)){
				for (Map<String, Object> map : list) {
					text(map.get("text").toString(), map.get("field")
							.toString(), sb);
				}
			}else {
				sb.append("<select name=\"" + field + "\"");
				
				this.readonly(sb);
				
				//增加扩展属性
				if (!StringUtils.isBlank(this.extendJson)) {
					Map<String, String> mp = gson.fromJson(extendJson, Map.class);
					for(Map.Entry<String, String> entry: mp.entrySet()) { 
						sb.append(entry.getKey()+"=\"" + entry.getValue() + "\"");
						} 
				}
				if (!StringUtils.isBlank(this.id)) {
					sb.append(" id=\"" + id + "\"");
				}
				this.datatype(sb);
				sb.append(">");

				if(!noNeedBlank){
					select("common.please.select", "", sb);
				}
				
				
				for (Map<String, Object> map : list) {
					select(ObjectUtils.getString(map.get("text"), ""), ObjectUtils.getString(map.get("field"), ""), sb);
				}
				sb.append("</select>");
			}
		} else if (StringUtil.isNotEmpty(this.typeGroupCode)) {
			if (hasLabel) {
				sb.append("<div class=\"" + divClass + "\">");
				sb.append("<label class=\"" + labelClass + "\" >");
			}
			
			if (this.typeGroupCode.indexOf("json:") == 0) {
				try {
					String clazzName = this.typeGroupCode.substring("json:".length());
					Class clazz = Class.forName(clazzName);
					Method method = clazz.getDeclaredMethod("toJson");
					String json = method.invoke(clazz).toString();
					if (StringUtil.isEmpty(dictField)) {
						dictField = "id";
					}
					if (StringUtil.isEmpty(dictText)) {
						dictText = "name";
					}
					generateClassTypegroup(json, sb);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				TSTypegroup typeGroup = null;
				try {
					if (StringUtil.isNotEmpty(this.typeGroupCode)) {
						typeGroup = TSTypegroup.allTypeGroups.get(this.typeGroupCode.toLowerCase());
					}
				} catch (Exception e) {
				}
				if (typeGroup != null) {
					List<TSType> types = TSTypegroup.allTypes.get(this.typeGroupCode.toLowerCase());
					if (hasLabel) {
						if (StringUtils.isBlank(this.title)) {
							this.title = MutiLangUtil.getMutiLangInstance().getLang(typeGroup.getTypegroupname());
						}
						sb.append(this.title + ":");
						sb.append("</label>");
					}
					if ("radio".equals(type)) {
						for (TSType type : types) {
							radio(type.getTypename(), type.getTypecode(), sb);
						}
					} else if ("checkbox".equals(type)) {
						for (TSType type : types) {
							checkbox(type.getTypename(), type.getTypecode(), sb);
						}
					}else if ("text".equals(type)) {
						for (TSType type : types) {
							text(type.getTypename(), type.getTypecode(), sb);
						}
					} else {
						sb.append("<select name=\"" + field + "\"");
						
						this.readonly(sb);
						
						//增加扩展属性
						if (!StringUtils.isBlank(this.extendJson)) {
							Map<String, String> mp = gson.fromJson(extendJson, Map.class);
							for(Map.Entry<String, String> entry: mp.entrySet()) { 
								sb.append(" "+entry.getKey()+"=\"" + entry.getValue() + "\"");
							} 
						}
						if (!StringUtils.isBlank(this.id)) {
							sb.append(" id=\"" + id + "\"");
						}
						this.readonly(sb);
						this.datatype(sb);
						sb.append(">");
						if(!noNeedBlank){
							select("common.please.select", "", sb);
						}
						for (TSType type : types) {
							select(type.getTypename(), type.getTypecode(), sb);
						}
						sb.append("</select>");
					}
					if (hasLabel) {
						sb.append("</div>");
					}
				} else if (StringUtil.isNotEmpty(this.extendJson)) {
					generateClassTypegroup(this.extendJson, sb);
				}
			}
			
		} else {
			if (StringUtil.isNotEmpty(this.extendJson)) {
				generateClassTypegroup(this.extendJson, sb);
			}
		}

		return sb;
	}
	
	private void generateClassTypegroup(String json, StringBuffer sb) {
		Gson gson = new Gson();
		
		List<Map<String, String>> extendValues = null;
		try {
			extendValues = gson.fromJson(json, new TypeToken<List<Map<String, String>>>(){}.getType());
		} catch (Exception e) {
			extendValues = new ArrayList<Map<String, String>>();
			try {
				List<Map<String, Object>> dumpValues = gson.fromJson(json, new TypeToken<List<Map<String, Object>>>(){}.getType());
				for (Map<String, Object> dumpMap : dumpValues) {
					Map<String, String> map = new HashMap<String, String>();
					extendValues.add(map);
					for (Map.Entry<String, Object> dumpMapEntry : dumpMap.entrySet()) {
						map.put(dumpMapEntry.getKey(), ObjectUtils.getString(dumpMapEntry.getValue()));
					}
				}
			} catch (Exception ex) {
			}
		}
		
		if (hasLabel) {
			if (StringUtils.isNotEmpty(this.title)) {
				sb.append(this.title + ":");
				sb.append("</label>");
			}
		}
		if ("radio".equals(type)) {
			for (Map<String, String> type : extendValues) {
				radio(type.get(dictText), type.get(dictField), sb);
			}
		} else if ("checkbox".equals(type)) {
			for (Map<String, String> type : extendValues) {
				checkbox(type.get(dictText), type.get(dictField), sb);
			}
		}else if ("text".equals(type)) {
			for (Map<String, String> type : extendValues) {
				text(type.get(dictText), type.get(dictField), sb);
			}
		} else {
			sb.append("<select name=\"" + field + "\"");
			
			//增加扩展属性
			if (!StringUtils.isBlank(this.id)) {
				sb.append(" id=\"" + id + "\"");
			}
			this.readonly(sb);
			this.datatype(sb);
			sb.append(">");
			if(!noNeedBlank){
				select("common.please.select", "", sb);
			}
			for (Map<String, String> type : extendValues) {
				select(type.get(dictText), type.get(dictField), sb);
			}
			sb.append("</select>");
		}
		if (hasLabel) {
			sb.append("</div>");
		}
	}
	
	/**
	 * 文本框方法
	 * @param name
	 * @param code
	 * @param sb
	 */
	private void text(Object nameObj, Object codeObj, StringBuffer sb) {
		String name = "";
		try {
			name = nameObj.toString();
		} catch (Exception e) {
		}
		String code = "";
		try {
			code = codeObj.toString();
		} catch (Exception e) {
		}
		if (code.equals(this.defaultVal)) {
			sb.append("<input name='"+field+"'"+" id='"+id+"' value='" + MutiLangUtil.getMutiLangInstance().getLang(name) + "' readOnly = 'readOnly' ");
			//增加扩展属性
			if (!StringUtils.isBlank(this.extendJson)) {
				Gson gson = new Gson();
				Map<String, String> mp = gson.fromJson(extendJson, Map.class);
				for(Map.Entry<String, String> entry: mp.entrySet()) { 
					sb.append(entry.getKey()+"=\"" + entry.getValue() + "\"");
				} 
			}
			sb.append(" />");
		} else {
		}
	}


	/**
	 * 单选框方法
	 * 
	 * @作者：Alexander
	 * 
	 * @param name
	 * @param code
	 * @param sb
	 */
	private void radio(Object nameObj, Object codeObj, StringBuffer sb) {
		String name = "";
		try {
			name = nameObj.toString();
		} catch (Exception e) {
		}
		String code = "";
		try {
			code = codeObj.toString();
		} catch (Exception e) {
		}
		if (code.equals(this.defaultVal)) {
			sb.append("<input type=\"radio\" name=\"" + field
					+ "\" checked=\"checked\" value=\"" + code + "\"");
			if (!StringUtils.isBlank(this.id)) {
				sb.append(" id=\"" + id + "\"");
			}
			
			this.readonly(sb);
			//增加扩展属性
			if (!StringUtils.isBlank(this.extendJson)) {
				Gson gson = new Gson();
				Map<String, String> mp = gson.fromJson(extendJson, Map.class);
				for(Map.Entry<String, String> entry: mp.entrySet()) { 
					sb.append(entry.getKey()+"=\"" + entry.getValue() + "\"");
				} 
			}
			this.datatype(sb);
			sb.append(" />");
		} else {
			sb.append("<input type=\"radio\" name=\"" + field + "\" value=\""
					+ code + "\"");
			if (!StringUtils.isBlank(this.id)) {
				sb.append(" id=\"" + id + "\"");
			}
			this.readonly(sb);
			this.datatype(sb);
			sb.append(" />");
		}
		sb.append(MutiLangUtil.getMutiLangInstance().getLang(name));
	}

	/**
	 * 复选框方法
	 * 
	 * @作者：Alexander
	 * 
	 * @param name
	 * @param code
	 * @param sb
	 */
	private void checkbox(Object nameObj, Object codeObj, StringBuffer sb) {
		String name = "";
		try {
			name = nameObj.toString();
		} catch (Exception e) {
		}
		String code = "";
		try {
			code = codeObj.toString();
		} catch (Exception e) {
		}
		String[] values = this.defaultVal.split(",");
		Boolean checked = false;
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (code.equals(value)) {
				checked = true;
				break;
			}
			checked = false;
		}
		if(checked){
			sb.append("<input type=\"checkbox\" name=\"" + field
					+ "\" checked=\"checked\" value=\"" + code + "\"");
			if (!StringUtils.isBlank(this.id)) {
				sb.append(" id=\"" + id + "\"");
			}
			this.readonly(sb);
			this.datatype(sb);
			sb.append(" />");
		} else {
			sb.append("<input type=\"checkbox\" name=\"" + field
					+ "\" value=\"" + code + "\"");
			if (!StringUtils.isBlank(this.id)) {
				sb.append(" id=\"" + id + "\"");
			}
			this.readonly(sb);
			this.datatype(sb);
			sb.append(" />");
		}
		sb.append(MutiLangUtil.getMutiLangInstance().getLang(name));
	}

	/**
	 * 选择框方法
	 * 
	 * @作者：Alexander
	 * 
	 * @param name
	 * @param code
	 * @param sb
	 */
	private void select(Object nameObj, Object codeObj, StringBuffer sb) {
		String name = "";
		try {
			name = nameObj.toString();
		} catch (Exception e) {
		}
		String code = "";
		try {
			code = codeObj.toString();
		} catch (Exception e) {
		}
		if (code.equals(this.defaultVal)) {
			sb.append(" <option value=\"" + code + "\" selected=\"selected\">");
		} else if(!onlyShowDefault) {
			sb.append(" <option value=\"" + code + "\">");
		}
		sb.append(MutiLangUtil.getMutiLangInstance().getLang(name));
		sb.append(" </option>");
	}

	/**
	 * 查询自定义数据字典
	 * 
	 * @作者：Alexander
	 */
	private List<Map<String, Object>> queryDic() {
		systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
		String sql = "select " + dictField + " as field," + dictText + " as text from " + dictTable;
	       if(dictCondition!=null){
	           sql = sql + " " + dictCondition;
	       }
		List<Map<String, Object>> list = systemService.findForJdbc(sql);
		return list;
	}
	
	/**
	 * 加入datatype属性,并加入非空验证作为默认值
	 * @param sb
	 * @return
	 */
	private StringBuffer datatype(StringBuffer sb){
		if (!StringUtils.isBlank(this.datatype)) {
			sb.append(" datatype=\"" + datatype + "\"");
		}
		return sb;
	}
	
	/**
	 * 加入readonly 属性,当此属性值为 readonly时，设置控件只读
	 * @author jg_xugj
	 * @param sb
	 * @return sb
	 */
	private StringBuffer readonly(StringBuffer sb){
		if(!StringUtils.isBlank(readonly) && (readonly.equalsIgnoreCase("readonly") || readonly.equalsIgnoreCase("true"))){
			if ("radio".equals(type)) {
				sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
			}
			else if ("checkbox".equals(type)) {
				sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
			}
			else if ("text".equals(type)) {
				
			} 
			else {
				sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
			}
		}
		return sb;
	}

	public String getTypeGroupCode() {
		return typeGroupCode;
	}

	public void setTypeGroupCode(String typeGroupCode) {
		this.typeGroupCode = typeGroupCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getDivClass() {
		return divClass;
	}

	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}

	public String getLabelClass() {
		return labelClass;
	}

	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isHasLabel() {
		return hasLabel;
	}

	public void setHasLabel(boolean hasLabel) {
		this.hasLabel = hasLabel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDictTable() {
		return dictTable;
	}

	public void setDictTable(String dictTable) {
		this.dictTable = dictTable;
	}

	public String getDictField() {
		return dictField;
	}

	public void setDictField(String dictField) {
		this.dictField = dictField;
	}

	public String getDictText() {
		return dictText;
	}

	public void setDictText(String dictText) {
		this.dictText = dictText;
	}
	public String getExtendJson() {
		return extendJson;
	}

	public void setExtendJson(String extendJson) {
		this.extendJson = extendJson;
	}
	
	
	public static void main(String[] args) throws Exception {
		String clazzName = "cn.nearf.ggz.query.CustomQueryType$CustomQueryParamType";
		Class clazz = Class.forName(clazzName);
		Method method = clazz.getDeclaredMethod("toJson");
		String json = method.invoke(clazz).toString();
		System.err.println(json);
		List<Map<String, String>> extendValues = new Gson().fromJson(json, new TypeToken<List<Map<String, String>>>(){}.getType());
		System.err.println(extendValues);
		
		StringBuilder sb = new StringBuilder();
		for (Map<String, String> type : extendValues) {
			Object codeObj = type.get("id");
			Object nameObj = type.get("name");
			
			String name = "";
			try {
				name = nameObj.toString();
			} catch (Exception e) {
			}
			String code = "";
			try {
				code = codeObj.toString();
			} catch (Exception e) {
			}
			sb.append(" <option value=\"" + code + "\">");
			sb.append(name);
			sb.append(" </option>");
		}
		
		System.err.println(sb);
	}
}
