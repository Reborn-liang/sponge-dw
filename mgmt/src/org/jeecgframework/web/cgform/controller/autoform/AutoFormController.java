package  org.jeecgframework.web.cgform.controller.autoform;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DynamicDBUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.autoform.*;
import org.jeecgframework.web.cgform.service.autoform.AutoFormDbServiceI;
import org.jeecgframework.web.cgform.service.autoform.AutoFormServiceI;
import org.jeecgframework.web.cgform.util.AutoFormCommUtil;
import org.jeecgframework.web.cgform.util.AutoFormTemplateParseUtil;
import org.jeecgframework.web.cgform.util.TemplateUtil;
import org.jeecgframework.web.system.pojo.base.DynamicDataSourceEntity;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.service.DynamicDataSourceServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**   
 * @Title: Controller
 * @Description: ?????????
 * @author onlineGenerator
 * @date 2015-06-15 20:29:59
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/autoFormController")
public class AutoFormController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AutoFormController.class);

	@Autowired
	private AutoFormServiceI autoFormService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AutoFormDbServiceI autoFormDbService;
	@Autowired
	private DynamicDataSourceServiceI dynamicDataSourceServiceI;
	

	/**
	 * ??????????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "autoForm")
	public ModelAndView autoForm(HttpServletRequest request) {
		return new ModelAndView("jeecg/cgform/autoform/autoFormList");
	}

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(AutoFormEntity autoForm,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AutoFormEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, autoForm, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.autoFormService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ???????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(AutoFormEntity autoForm, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		autoForm = systemService.getEntity(AutoFormEntity.class, autoForm.getId());
		String message = "?????????????????????";
		try{
			autoFormService.delete(autoForm);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			delFormDb(autoForm.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "?????????????????????";
		try{
			for(String id:ids.split(",")){
				AutoFormEntity autoForm = systemService.getEntity(AutoFormEntity.class, 
				id
				);
				autoFormService.delete(autoForm);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
				delFormDb(id);
			}
		} catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ???????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(AutoFormEntity autoForm, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "?????????????????????";
		try{
//			TemplateUtil tool = new TemplateUtil();
			if(StringUtils.isNotBlank(autoForm.getFormContent())){
//				Map<String,Object> map = tool.processor(autoForm.getFormContent());
//				autoForm.setFormContent(map.get("template").toString());
//				autoForm.setFormParse(map.get("parseHtml").toString());
				JSONObject jsonObj  = JSONObject.fromObject(autoForm.getFormContent());
				String html  = (String)jsonObj.get("template");
				autoForm.setFormContent(html);
				autoForm.setFormParse(autoForm.getFormContent());
			}
			autoFormService.save(autoForm);			
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(autoForm.getId());
		return j;
	}
	
	/**
	 * ???????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(AutoFormEntity autoForm, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message ="";
		try {
//			TemplateUtil tool = new TemplateUtil();
			Map<String, Object> attributes = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(autoForm.getId())){
				AutoFormEntity t = autoFormService.get(AutoFormEntity.class, autoForm.getId());
				MyBeanUtils.copyBeanNotNull2Bean(autoForm, t);
				if(StringUtils.isNotBlank(autoForm.getFormContent())){
//					Map<String,Object> map = tool.processor(autoForm.getFormContent());
//					t.setFormContent(map.get("template").toString());
//					t.setFormParse(map.get("parseHtml").toString());
					JSONObject jsonObj  = JSONObject.fromObject(autoForm.getFormContent());
					String html  = (String)jsonObj.get("template");
					//????????????id??????
					validateId(html);
					t.setFormContent(html);
					t.setFormParse(autoForm.getFormContent());
				}
				autoFormService.saveOrUpdate(t);
				attributes.put("id", t.getId());
				j.setAttributes(attributes);
				message = "??????????????????";
				if(StringUtils.isBlank(autoForm.getMainTableSource()))
					message+=",?????????????????????????????????????????????";
			} else {
				if(StringUtils.isNotBlank(autoForm.getFormContent())){
//					Map<String,Object> map = tool.processor(autoForm.getFormContent());
//					autoForm.setFormContent(map.get("template").toString());
//					autoForm.setFormParse(map.get("parseHtml").toString());
					JSONObject jsonObj  = JSONObject.fromObject(autoForm.getFormContent());
					String html  = (String)jsonObj.get("template");
					//????????????id??????
					validateId(html);
					autoForm.setFormContent(html);
					autoForm.setFormParse(autoForm.getFormContent());
				}
				//TODO ?????????
				autoFormService.save(autoForm);
				attributes.put("id", autoForm.getId());
				j.setAttributes(attributes);
				message = "??????????????????";
				if(StringUtils.isBlank(autoForm.getMainTableSource()))
					message+=",?????????????????????????????????????????????";
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			j.setSuccess(false);
			message = "??????????????????!"+e.getMessage();
		}
		j.setMsg(message);
		return j;
	}
	
	private void validateId(String html){
		if(StringUtil.isEmpty(html)){
			return;
		}
		Document doc = Jsoup.parse(html);
		Map<String,String> map = new HashMap<String, String>();
		Elements IDs = doc.select("input[name$=.ID]");
		for (Element el: IDs) {
			String name = el.attr("name");
			System.out.println(name);
			if(map.get(name)!=null){
				throw new BusinessException("??????ID????????????"+name+"???,???????????????HTML???????????????????????????");
			}else{
				map.put(name, name);
			}
		}
		Elements ids = doc.select("input[name$=.id]");
		for (Element el: ids) {
			String name = el.attr("name");
			System.out.println(name);
			if(map.get(name)!=null){
				throw new BusinessException("??????ID????????????"+name+"???,???????????????HTML???????????????????????????");
			}else{
				map.put(name, name);
			}
		}
	}
	

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(AutoFormEntity autoForm, HttpServletRequest req) {
		
		//??????styleForm?????????????????????json??????
//		String hql = "from AutoFormStyleEntity";
//		List<AutoFormStyleEntity> list = this.systemService.findHql(hql);
		JSONArray jsonArray = JSONArray.fromObject(getStyleList(""));
		req.setAttribute("styleSelect", jsonArray.toString());
	
		return new ModelAndView("jeecg/cgform/autoform/autoForm-add");
	}
	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(AutoFormEntity autoForm, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(autoForm.getId())) {
			autoForm = autoFormService.getEntity(AutoFormEntity.class, autoForm.getId());
			req.setAttribute("autoFormPage", autoForm);
		}
		JSONArray jsonArray = JSONArray.fromObject(getStyleList(autoForm.getFormStyleId()));
		req.setAttribute("styleSelect", jsonArray.toString());
		
		JSONArray dbArray = JSONArray.fromObject(getFormDbList(autoForm.getId()));
		req.setAttribute("dbDate", dbArray.toString());
		
		return new ModelAndView("jeecg/cgform/autoform/autoForm-update");
	}
	
	/**
	 * ????????????????????????????????????
	 * @return
	 */
	private List<Map<String,Object>> getStyleList(String styleid){
		
		String hql = "from AutoFormStyleEntity";
		List<AutoFormStyleEntity> list = this.systemService.findHql(hql);
		
		List<Map<String,Object>> dateList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		for(AutoFormStyleEntity style:list){
			map = new HashMap<String,Object>();
			map.put("id", style.getId());
			map.put("name", style.getStyleDesc());
			//map.put("content", style.getStyleContent().replaceAll("\"", "&quot;"));
			if(StringUtils.isNotBlank(styleid) && styleid.equals(style.getId())){
				//????????????
				map.put("checked", true);
			} else {
				map.put("checked", false);
			}
			dateList.add(map);
		}
		return dateList;
	}
	
	@RequestMapping(params = "treeReload")
	@ResponseBody
	public AjaxJson treeReload(AutoFormDbEntity autoFormdb, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		JSONArray jsonArray = JSONArray.fromObject(getFormDbList(autoFormdb.getAutoFormId()));
		j.setMsg(jsonArray.toString());
		return j;
	}
	
	/**
	 * ??????formId???????????????????????????
	 * @param
	 * @return
	 */
	
	private List<Map<String,Object>> getFormDbList(String autoFormId){
			
			List<AutoFormDbEntity> list = this.systemService.findByProperty(AutoFormDbEntity.class, "autoFormId", autoFormId);
			
			List<Map<String,Object>> dateList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			for(AutoFormDbEntity dbForm:list){
				map = new HashMap<String,Object>();
				map.put("id", dbForm.getId());
				map.put("name", dbForm.getDbChName()+"("+dbForm.getDbName()+")");
				map.put("dbCode", dbForm.getDbName());
			    map.put("pid", "0");
				//???????????????
				dateList.add(map);
				//???????????????
				if("table".equals(dbForm.getDbType())){
					List<AutoFormDbFieldEntity> fieldlist = this.systemService.findByProperty(AutoFormDbFieldEntity.class, "autoFormDbId", dbForm.getId());
					for(AutoFormDbFieldEntity field: fieldlist){
						map = new HashMap<String,Object>();
						map.put("id", field.getId());
						map.put("name", (StringUtils.isBlank(field.getFieldText())||"null".equals(field.getFieldText()))?field.getFieldName():field.getFieldText()+"("+field.getFieldName()+")");
						map.put("pId", dbForm.getId());
						map.put("nocheck",true);
						dateList.add(map);
					}
				} else if("sql".equals(dbForm.getDbType())){
					//?????????????????????????????????????????????????????????????????????sql
					List<AutoFormDbFieldEntity> fieldlist = this.systemService.findByProperty(AutoFormDbFieldEntity.class, "autoFormDbId", dbForm.getId());
					if(fieldlist.size() > 0){
						for(AutoFormDbFieldEntity field: fieldlist){
							map = new HashMap<String,Object>();
							map.put("id", field.getId());
							map.put("name", (StringUtils.isBlank(field.getFieldText())||"null".equals(field.getFieldText()))?field.getFieldName():field.getFieldText()+"("+field.getFieldName()+")");
							map.put("pId", dbForm.getId());
							map.put("nocheck",true);
							dateList.add(map);
						}
					} else {
						if(StringUtils.isNotBlank(dbForm.getDbDynSql())){
							List<String> files = autoFormDbService.getSqlFields(dbForm.getDbDynSql());
							for(String filed : files){
								map = new HashMap<String,Object>();
								map.put("id", "");
								map.put("name", filed);
								map.put("pId", dbForm.getId());
								map.put("nocheck",true);
								dateList.add(map);
							}
						}
					}
				} else {
					//clazz
				}
				
			}
			return dateList;
		}
	
	/**
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "docreateCode")
	@ResponseBody
	public AjaxJson docreateCode(String formdbId,String styleId,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		//1.??????formStyle???????????????
		AutoFormStyleEntity styleEntity = this.systemService.get(AutoFormStyleEntity.class, styleId);
		String content = styleEntity.getStyleContent();		
		
		//2??????FormDb?????????????????????
		String[] ids = formdbId.split(",");
		List<Map<String,Object>> dsList = new ArrayList<Map<String,Object>>();
		if(ids.length>0){
			for(String autoFormDbId: ids){
				Map<String,Object> dsData = new HashMap<String, Object>();
				AutoFormDbEntity db = this.systemService.findUniqueByProperty(AutoFormDbEntity.class, "id", autoFormDbId);
				dsData.put("dsName", db.getDbName());
				dsList.add(dsData);
			    if("table".equals(db.getDbType())){
			    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
					List<Map<String,Object>> columns = this.systemService.findHql(hql, autoFormDbId);
					dsData.put("columns", columns);
			    } else if("sql".equals(db.getDbType())){
			    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
					List<Map<String,Object>> columns = this.systemService.findHql(hql, autoFormDbId);
					if(columns.size()>0){
						dsData.put("columns", columns);
					} else {
						List<String> fileds = autoFormDbService.getSqlFields(db.getDbDynSql());
						List<Map<String,Object>> column = new ArrayList<Map<String,Object>>();
						for(String filed:fileds){
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("fieldName", filed.toLowerCase());
							column.add(map);
						}
						dsData.put("columns", column);
					}
			    }
			}
		}
		
		String result = createFtl(content,dsList);
		j.setMsg(result.replaceAll("\"", "&quot;"));
		return j;
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param content
	 * @param
	 * @return
	 */
	private String createFtl(String content,List<Map<String,Object>> dsList){
		 String result = "";
		try { 
		 Configuration cfg = new Configuration();
		 //cfg.setClassForTemplateLoading(FreemarkerHelper.class,"autoForm");
		 StringTemplateLoader stringLoader = new StringTemplateLoader();
		 stringLoader.putTemplate("autoformTemplete",content);
		
		 cfg.setTemplateLoader(stringLoader);
         Template template = cfg.getTemplate("autoformTemplete","utf-8");  
            Map<String,Object> root = new HashMap<String,Object>();    
            root.put("dsList", dsList);  
            StringWriter writer = new StringWriter(); 
            template.process(root, writer);  
            result = writer.toString();
        }catch (TemplateException e) {  
            e.printStackTrace();
        } catch (IOException e) {  
        	 e.printStackTrace();
        } 
		return result;
	}
	
	/*
	 * ??????????????????????????????????????????????????????????????????????????????
	 */
	@RequestMapping(params = "parse")
	public ModelAndView parse(AutoFormEntity autoForm, HttpServletRequest req) {
		
		if(StringUtils.isNotBlank(autoForm.getId())){
			autoForm = this.systemService.findUniqueByProperty(AutoFormEntity.class, "id", autoForm.getId());
			List<String> paramList = getFormParams(autoForm.getId());
			//3.????????????
			req.setAttribute("paramList", paramList);
			req.setAttribute("autoFormPage", autoForm);
		}
		return new ModelAndView("jeecg/cgform/autoform/autoForm-view");
	}
	
	
	private List<String> getFormParams(String fromId){
		List<String> paramList = new ArrayList<String>();
		List<AutoFormDbEntity> formDbList = this.systemService.findByProperty(AutoFormDbEntity.class, "autoFormId", fromId);
		//2.??????dbList,??????AutoFormDbEntity???sql??????????????????sql??????param?????????paramList???
		for(AutoFormDbEntity dbEntity:formDbList){
			if("sql".equals(dbEntity.getDbType())){
				List<AutoFormParamEntity> params = this.systemService.findByProperty(AutoFormParamEntity.class, "autoFormDbId", dbEntity.getId());
				if(params.size()>0){
					//2.1 ????????????????????????????????????????????????????????????
					for(AutoFormParamEntity entity:params){
						if(!paramList.contains(entity.getParamName())){
							paramList.add(entity.getParamName());
						}
					}
				}
			}else if("table".equals(dbEntity.getDbType())){
				paramList.add("id");
			}
		}
		return paramList;
	}
	
//	private String createFtl(String content,Map paras){
//		 String result = "";
//		try { 
//		 Configuration cfg = new Configuration();
//		 //cfg.setClassForTemplateLoading(FreemarkerHelper.class,"autoForm");
//		 StringTemplateLoader stringLoader = new StringTemplateLoader();
//		 stringLoader.putTemplate("autoformTemplete",content);
//		
//		 cfg.setTemplateLoader(stringLoader);
//        Template template = cfg.getTemplate("autoformTemplete","utf-8");  
//           StringWriter writer = new StringWriter(); 
//           template.process(paras, writer);  
//           result = writer.toString();
//       }catch (TemplateException e) {  
//           e.printStackTrace();
//       } catch (IOException e) {  
//       	 e.printStackTrace();
//       } 
//		return result;
//	}
	
	/**
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "viewContent")
	public ModelAndView viewContent(AutoFormEntity autoForm, HttpServletRequest req) {
		String message = "";
		try {
			//????????????
//			Map<String,Object> paramMap = req.getParameterMap();
			
			Map paramMap = req.getParameterMap();
			String op = req.getParameter("op");
			if(StringUtil.isEmpty(op)){
				op = AutoFormTemplateParseUtil.OP_VIEW;
			}
			Map<String, List<Map<String, Object>>> paras = new HashMap<String, List<Map<String, Object>>>();
			if(StringUtils.isNotBlank(autoForm.getFormName())){
				autoForm = this.systemService.findUniqueByProperty(AutoFormEntity.class, "formName", autoForm.getFormName());
				if(autoForm==null){
					return new ModelAndView("jeecg/cgform/autoform/autoForm-error").addObject("message", "???????????????????????????");
				}
			}else{
				return new ModelAndView("jeecg/cgform/autoform/autoForm-error").addObject("message", "formName???????????????");
			}

			//??????formid?????????????????????
			List<String> paramList = getFormParams(autoForm.getId());
			if(paramList!=null&&paramList.size()>0){
				for(String param:paramList){
					String paramValue = req.getParameter(param);
					if(paramValue==null){
						return new ModelAndView("jeecg/cgform/autoform/autoForm-error").addObject("message", "?????????????????????"+param);
					}
				}
			}
			req.setAttribute("formName", autoForm.getFormName());
			req.setAttribute("param", paramMap);
//			paras = getFormData(autoForm,paramMap);
			
			String ftlContent = autoForm.getFormParse();
			// 1.??????????????????
			if(AutoFormTemplateParseUtil.OP_VIEW.equals(op)){
				// 2.??????ueditor html ????????????????????????????????????
				paras = getFormData(autoForm,paramMap);
				ftlContent = AutoFormTemplateParseUtil.parseHtmlForView(ftlContent,paras);
			}else if(AutoFormTemplateParseUtil.OP_ADD.equals(op)){
				// 2.??????ueditor html ????????????????????????????????????
				ftlContent = AutoFormTemplateParseUtil.parseHtmlForAdd(ftlContent,paras);
				req.setAttribute("formContent", ftlContent);
				req.setAttribute("op", op);
				return new ModelAndView("jeecg/cgform/autoform/autoForm-review-add");
			}else if(AutoFormTemplateParseUtil.OP_UPDATE.equals(op)){
				paras = getFormData(autoForm,paramMap);
				String id = req.getParameter("id");
				if(StringUtil.isEmpty(id)){
					 throw new BusinessException("??????id???????????????");
				}
				// 2.??????ueditor html ????????????????????????????????????
				ftlContent = AutoFormTemplateParseUtil.parseHtmlForUpdate(ftlContent,paras);
				req.setAttribute("formContent", ftlContent);
				req.setAttribute("op", op);
				return new ModelAndView("jeecg/cgform/autoform/autoForm-review-update");
			}else if(AutoFormTemplateParseUtil.OP_ADD_OR_UPDATE.equals(op)){
				paras = getFormData(autoForm,paramMap);
				// 2.??????ueditor html ????????????????????????????????????
				ftlContent = AutoFormTemplateParseUtil.parseHtmlForAddOrUpdate(ftlContent,paras);
				req.setAttribute("formContent", ftlContent);
				req.setAttribute("op", op);
				return new ModelAndView("jeecg/cgform/autoform/autoForm-review-addorupdate");
			}
			req.setAttribute("formContent", ftlContent);
			req.setAttribute("op", op);
			return new ModelAndView("jeecg/cgform/autoform/autoForm-review");
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????"+e.getMessage();
		} 
		return new ModelAndView("jeecg/cgform/autoform/autoForm-error").addObject("message", message);
	}
	
	
	/**
	 * ??????????????????
	 * @param formName
	 * @param paramMap
	 * @return
	 */
	private Map<String, List<Map<String, Object>>> getFormData(AutoFormEntity autoForm,Map<String,Object> paramMap){
		String message = "";
		//?????????????????????
		Map<String, List<Map<String, Object>>> paras = new HashMap<String, List<Map<String, Object>>>();
		List<AutoFormDbEntity> formDbList = this.systemService.findByProperty(AutoFormDbEntity.class, "autoFormId", autoForm.getId());
		for(AutoFormDbEntity formDb: formDbList){
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if("table".equals(formDb.getDbType())){
				//????????????????????????????????????????????????????????????????????????SQL
				String hqlField = "from AutoFormDbFieldEntity where 1 = 1 AND aUTO_FORM_DB_ID = ? ";
			    try{
			    	List<AutoFormDbFieldEntity> autoFormDbFieldEntityList = systemService.findHql(hqlField,formDb.getId());
			    	
			    	if(autoFormDbFieldEntityList.size()>0){
			    		StringBuffer hqlTable = new StringBuffer().append("select ");
			    		for(AutoFormDbFieldEntity autoFormDbFieldEntity:autoFormDbFieldEntityList){
			    			hqlTable.append(autoFormDbFieldEntity.getFieldName()+",");
				    	}
			    		hqlTable.deleteCharAt(hqlTable.length()-1).append(" from "+formDb.getDbTableName());
			    		String id = "";
			    		Object value = paramMap.get("id");
						if(value instanceof String[]){
							String[] paramValue=(String[])value;
							id = paramValue[0];
		                }else{
		                	id = value.toString();
		                }
			    		hqlTable.append(" where ID ='").append(id).append("'");
						if("".equals(formDb.getDbKey())){
							//?????????????????????DB?????????????????????????????????????????????
							data = systemService.findForJdbc(hqlTable.toString());
						}
						else{
							DynamicDataSourceEntity dynamicDataSourceEntity = dynamicDataSourceServiceI.getDynamicDataSourceEntityForDbKey(formDb.getDbKey());
							if(dynamicDataSourceEntity!=null){
								data = DynamicDBUtil.findList(formDb.getDbKey(),hqlTable.toString());
							}
						}
						
			    	}else{
			    		message = "????????????????????????";
			    		throw new BusinessException(message);
			    	}
			    	paras.put(formDb.getDbName(), data);
				}catch(Exception e){
					logger.info(e.getMessage());
				}
			}else if("sql".equals(formDb.getDbType())){
				//????????????????????????SQL???????????????????????????SQL??????????????????????????????????????????SQL
				String dbDynSql = formDb.getDbDynSql();
				List<String> params = autoFormDbService.getSqlParams(dbDynSql);
				for(String param:params){
					Object value = paramMap.get(param);
					if(value instanceof String[]){
						String[] paramValue=(String[])value;
						dbDynSql = dbDynSql.replaceAll("\\$\\{"+param+"\\}", paramValue[0]);
	                }else{
	                	String paramValue= value.toString();
	                	dbDynSql = dbDynSql.replaceAll("\\$\\{"+param+"\\}", paramValue);
	                }
				}
				
				//??????sql?????????????????????????????????????????????????????????????????????
				if(dbDynSql.contains("\\$")){
					message = "??????SQL?????????????????????";
					throw new BusinessException(message);
				}else{
					try {
						data = systemService.findForJdbc(dbDynSql);
					} catch (Exception e) {
						logger.info(e.getMessage());
						message = "??????SQL?????????????????????";
						throw new BusinessException(message);
					}
					
				}
				paras.put(formDb.getDbName(), data);
			}else{
				//?????????CLAZZ??????
			}
		}
		return  paras;
	}
	
	
	
	
	/**
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(params = "review")
	public ModelAndView review(AutoFormEntity autoForm, HttpServletRequest req) {
		
		TemplateUtil tool = new TemplateUtil();
		Map<String,Object> map = tool.processor(autoForm.getFormContent());
		req.setAttribute("formContent", map.get("parseHtml"));
		return new ModelAndView("jeecg/cgform/autoform/autoForm-review");
	}	
	/**
	 * 
	 * @param autoForm
	 * @param req
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(params="checkFormNm")
//	public AjaxJson checkFormNm(AutoFormEntity autoForm,HttpServletRequest req){
//		AjaxJson j = new AjaxJson();
//		String hql = "from AutoFormEntity t where t.formName = ?";
//		List<AutoFormStyleEntity> list = this.systemService.findHql(hql, autoForm.getFormName()); 
//    	j.setMsg(String.valueOf(list.size()));
//    	return j;
//	}
	
	/**
	 * ?????????form??????????????????
	 * @param autoForm
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getFormDb")
	public AjaxJson getFormDb(AutoFormDbEntity dbForm,HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		String message = "";
		//1.??????ID??????????????????option?????????
		if(StringUtils.isNotBlank(dbForm.getAutoFormId())){
			List<AutoFormDbEntity> dbFormList = this.systemService.findByProperty(AutoFormDbEntity.class, "autoFormId", dbForm.getAutoFormId());
			if(dbFormList.size()>0){
				message = "<option value='' selected='selected'>??????????????????</option>";
				for(AutoFormDbEntity entity:dbFormList){
					//???????????????????????????????????????
					message += "<option value='"+entity.getDbName()+"'>"+(StringUtils.isBlank(entity.getDbChName())?entity.getDbName():entity.getDbChName())+"</option>";
				}
				j.setSuccess(true);
				j.setMsg(message);
			} else{
				j.setSuccess(false);
				message = "<option value='' selected='selected'>?????????????????????</option>";
				j.setMsg(message);
			}
		}else{
		//2.??????ID????????????option??????????????????
			j.setSuccess(false);
			message = "<option  value='' selected='selected'>?????????????????????</option>";
			j.setMsg(message);
		}
    	return j;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param AutoFormDbEntity
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getFormField")
	public AjaxJson getFormField(AutoFormDbEntity dbForm,HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		String message = "";
		//1.??????ID??????????????????option?????????
		//???????????????????????????????????????
		if(StringUtils.isNotBlank(dbForm.getDbName()) && StringUtils.isNotBlank(dbForm.getAutoFormId())){
			String hqlList = "from AutoFormDbEntity t where t.dbName = ? and autoFormId = ?";
			List<AutoFormDbEntity> list= this.systemService.findHql(hqlList, dbForm.getDbName(),dbForm.getAutoFormId());
			if(list.size() ==1){
				dbForm = list.get(0);
				//dbForm = this.systemService.findUniqueByProperty(AutoFormDbEntity.class, "dbName", dbForm.getDbName());
				List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
				 if("table".equals(dbForm.getDbType())){
				    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
						columns = this.systemService.findHql(hql, dbForm.getId());
				    } else if("sql".equals(dbForm.getDbType())){
				    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
						columns = this.systemService.findHql(hql, dbForm.getId());
						if(columns.size() ==0){
							List<String> fileds = autoFormDbService.getSqlFields(dbForm.getDbDynSql());
							for(String filed:fileds){
								Map<String,Object> map = new HashMap<String,Object>();
								map.put("fieldName", filed);
								columns.add(map);
							}
						}
				   }
				if(columns.size()>0){
					message = "<option value='' selected='selected'>???????????????</option>";
					for(Map<String,Object> map:columns){
						message += "<option value='"+map.get("fieldName")+"'>"+(StringUtils.isBlank((String)map.get("fieldText"))?map.get("fieldName"):map.get("fieldText")) +"</option>";
					}
					j.setSuccess(true);
					j.setMsg(message);
				} else{
					j.setSuccess(false);
					message = "<option value='' selected='selected'>??????????????????</option>";
					j.setMsg(message);
				}
			}else{
			//2.??????ID????????????option??????????????????
				j.setSuccess(false);
				message = "<option value='' selected='selected'>?????????????????????</option>";
				j.setMsg(message);
			}
		} 
    	return j;
	}
	/**
	 * ???????????????????????????????????????,????????????table?????????
	 * @param dbForm
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getTrList")
	public AjaxJson getTrList(AutoFormDbEntity dbForm,HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		String message = "";
		//1.??????ID??????????????????tr?????????
		//???????????????????????????????????????
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dbForm.getDbName()) && StringUtils.isNotBlank(dbForm.getAutoFormId())){
			String hqlList = "from AutoFormDbEntity t where t.dbName = ? and autoFormId = ?";
			List<AutoFormDbEntity> list= this.systemService.findHql(hqlList, dbForm.getDbName(),dbForm.getAutoFormId());
			if(list.size() ==1){
				dbForm = list.get(0);
				
				//???????????????
				if(StringUtils.isNotBlank(dbForm.getDbChName())){
					attributes.put("dbName", dbForm.getDbChName());
				} else {
					attributes.put("dbName", dbForm.getDbName());
				}
				//dbForm = this.systemService.findUniqueByProperty(AutoFormDbEntity.class, "dbName", dbForm.getDbName());
				List<Map<String,Object>> columns = new ArrayList<Map<String,Object>>();
				 if("table".equals(dbForm.getDbType())){
				    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
						columns = this.systemService.findHql(hql, dbForm.getId());
				    } else if("sql".equals(dbForm.getDbType())){
				    	String hql = "select new Map(t.fieldName as fieldName,t.fieldText as fieldText) from AutoFormDbFieldEntity t where t.autoFormDbId=?";
						columns = this.systemService.findHql(hql, dbForm.getId());
						if(columns.size() ==0){
							List<String> fileds = autoFormDbService.getSqlFields(dbForm.getDbDynSql());
							for(String filed:fileds){
								Map<String,Object> map = new HashMap<String,Object>();
								map.put("fieldName", filed);
								columns.add(map);
							}
						}
				   }
				    StringBuilder options = null;
				    StringBuilder trList  = new StringBuilder();
				    String headStr = "";
				    String columnFiledNm = "";
				    String optionFiledNm = "";
				    String columnFiledTx = "";
				    if(columns.size()>0){
				    
			    	StringBuffer datatypeList = new StringBuffer();
			    	datatypeList.append("<option value=\"\" selected>???</option>");
		    		List<TSType> typeList = this.systemService.findByProperty(TSType.class, "TSTypegroup.id", "4028838850c35b6a0150c37251e00002");
		    		for(TSType type:typeList){
		    			datatypeList.append("<option value=\""+type.getTypecode()+"\">"+type.getTypename()+"</option>");
		    		} 	
				    
		    		StringBuffer unitOptions = new StringBuffer();
		    		unitOptions.append("<option value=\"#\" selected>???</option>");
		    		List<TSType> unitList = this.systemService.findByProperty(TSType.class, "TSTypegroup.id", "4028ab8c5134f1ed0151350f08d90003");
		    		for(TSType type:unitList){
		    			unitOptions.append("<option value=\""+type.getTypecode()+"\">"+type.getTypename()+"</option>");
		    		}
		    		
			    	for(int i=0;i<columns.size();i++){
			    		
			    		Map<String,Object> columnMap = columns.get(i);
			    		
			    		columnFiledNm = columnMap.get("fieldName").toString();
			    		columnFiledTx = columnMap.get("fieldText") == null ?"":columnMap.get("fieldText").toString();
			    		
			    		if(StringUtils.isNotBlank(columnFiledTx)){
			    			headStr = columnMap.get("fieldText").toString();
			    		} else {
			    			headStr = columnFiledNm; 
			    		}
			    		
			    		//????????????options
			    		options = new StringBuilder();
				    	for(int m=0;m<columns.size();m++){
							Map<String,Object> map = columns.get(m);
							
							optionFiledNm = map.get("fieldName").toString();
							
							if(optionFiledNm.equals(columnFiledNm)){
								options.append("<option value=\""+map.get("fieldName")+"\" selected>"+map.get("fieldName")+"</option>");
							} else {
								options.append("<option value=\""+map.get("fieldName")+"\">"+map.get("fieldName")+"</option>");
							}
						}
			    		if("ID".equals(columnFiledNm.toUpperCase())){
							trList.append("<tr>");
							trList.append("<td><span class='badge'>"+(i+1)+"</span></td>");
							trList.append("<td title='Tab??????????????????'><input id='item_"+(i+1)+"' type='text' class='input-mini' value='"+headStr+"' style=\"width: 90%\" disabled></td>");
							trList.append("<td title='Tab??????????????????'>");
							trList.append("<select id='coltype_"+(i+1)+"' class='input-medium' style=\"width: 100%\" disabled>");
							trList.append("<option value='text'>???????????????</option>");
							trList.append("<option value='textarea'>???????????????</option>");
							trList.append("<option value='radio'>????????????</option>");
							trList.append("<option value='select'>?????????</option>");
							trList.append("<option value='checkbox'>?????????</option>");
							trList.append("<option value='popup'>popup??????</option>");
							trList.append("</select>");
							trList.append("</td>");
							trList.append("<td title='Tab??????????????????'> <label><select id='field_"+(i+1)+"' class=\"input-medium\"  onchange=\"changeValue(this.id,this.value,'"+dbForm.getId()+"')\" style=\"width: 100%\" disabled>");
							trList.append(options);
							trList.append("</select></label> </td>");
							trList.append("<td title='Tab??????????????????'> <label><input type='text' style=\"width:40px\" id='length_"+(i+1)+"' value='157' disabled><span style='font-size:18px;'>px</span> </label> </td>");
							//trList.append("<td title='Tab??????????????????'> <label><input type='text' class='input-mini' id='unit_"+(i+1)+"' value='' disabled> </label> </td>");
							trList.append("<td title='Tab??????????????????'>");
							trList.append("<select id='unit_"+(i+1)+"' class='input-medium' style=\"width: 100%\" disabled>");
							trList.append(unitOptions);
							trList.append("</select>");
							trList.append("</td>");
							trList.append("<td title='Tab??????????????????'> <label><input type='text' class='input-mini' id='dict_"+(i+1)+"' value='' disabled> </label> </td>");
							trList.append("<td title='Tab??????????????????'> <label> <input type='checkbox' id='sum_"+(i+1)+"' class='csum' value='3' disabled> </label> </td>");
			                trList.append("<td title='Tab??????????????????'><input id='colvalue_"+(i+1)+"'  type='text' class='input-mini' value='' disabled></td>");
			                trList.append("<td title='Tab??????????????????'><select id='ruletype_"+(i+1)+"' class='input-medium' style=\"width: 100%\" disabled>"+datatypeList.toString()+"</select></td>");
			                trList.append("<td title='Tab??????????????????'><label> <input type=\"checkbox\" id='isHide_"+(i+1)+"' value=\"1\"");
			                trList.append("checked=\"checked\"");
			                trList.append(" disabled> </label> </td>");
			                trList.append("<td></td>");
			                trList.append("</tr>");
			    		} else {
			    			trList.append("<tr>");
							trList.append("<td><span class='badge'>"+(i+1)+"</span></td>");
							trList.append("<td title='Tab??????????????????'><input id='item_"+(i+1)+"' type='text' class='input-mini' value='"+headStr+"' style=\"width: 90%\"></td>");
							trList.append("<td title='Tab??????????????????'>");
							trList.append("<select id='coltype_"+(i+1)+"' class='input-medium' style=\"width: 100%\">");
							trList.append("<option value='text'>???????????????</option>");
							trList.append("<option value='textarea'>???????????????</option>");
							trList.append("<option value='radio'>????????????</option>");
							trList.append("<option value='select'>?????????</option>");
							trList.append("<option value='checkbox'>?????????</option>");
							trList.append("<option value='popup'>popup??????</option>");
							trList.append("</select>");
							trList.append("</td>");
							trList.append("<td title='Tab??????????????????'> <label><select id='field_"+(i+1)+"' class=\"input-medium\"  onchange=\"changeValue(this.id,this.value,'"+dbForm.getId()+"')\" style=\"width: 100%\">");
							trList.append(options);
							trList.append("</select></label> </td>");
							trList.append("<td title='Tab??????????????????'> <label><input type='text' style=\"width:40px\" id='length_"+(i+1)+"' value='157'><span style='font-size:18px;'>px</span> </label> </td>");
							//trList.append("<td title='Tab??????????????????'> <label><input type='text' class='input-mini' id='unit_"+(i+1)+"' value=''> </label> </td>");
							trList.append("<td title='Tab??????????????????'>");
							trList.append("<select id='unit_"+(i+1)+"' class='input-medium' style=\"width: 100%\">");
							trList.append(unitOptions);
							trList.append("</select>");
							trList.append("</td>");
							trList.append("<td title='Tab??????????????????'> <label><input type='text' class='input-mini' id='dict_"+(i+1)+"' value=''> </label> </td>");
							trList.append("<td title='Tab??????????????????'> <label> <input type='checkbox' id='sum_"+(i+1)+"' class='csum' value='3'> </label> </td>");
			                trList.append("<td title='Tab??????????????????'><input id='colvalue_"+(i+1)+"'  type='text' class='input-mini' value=''/></td>");
			                trList.append("<td title='Tab??????????????????'><select id='ruletype_"+(i+1)+"' class='input-medium' style=\"width: 100%\">"+datatypeList.toString()+"</select></td>");
			                trList.append("<td title='Tab??????????????????'><label> <input type=\"checkbox\" id='isHide_"+(i+1)+"' value=\"1\"");
			                trList.append("> </label> </td>");
			                trList.append("<td><button class='btn btn-small btn-success delrow' type='button'>??????</button></td>");	
			                trList.append("</tr>");
			    		}
					}
			    	trList.append("<script type=\"text/javascript\">");
			    	trList.append("function changeValue(id,value,dbFormId){");
			    	trList.append(" var index = id.split('_')[1];");
			    	trList.append("$.post(");
			    	trList.append("'autoFormController.do?getHead',");
			    	trList.append("{column:value,autoFormId:dbFormId},");
			    	trList.append("function(data){");
			    	trList.append("var d = $.parseJSON(data);");
			    	trList.append("if(d.success){");
			    	trList.append("if(value == 'ID' || value == 'id'){$('#isHide_'+(index)).attr('checked',true);}");
			    	trList.append("if(d.msg == null || d.msg == ''){");
			    	trList.append("$('#item_'+(index)).val(index);");
			    	trList.append("}else{");
			    	trList.append("$('#item_'+(index)).val(d.msg);");
			    	trList.append("}}");
			    	trList.append("});");
			    	trList.append("}");
			    	trList.append("$(function(){");
			    	trList.append("$(\".delrow\").live(\"click\",function(){$(this).parent().parent().remove();resetTrNum();});");
			    	trList.append("});");
			    	trList.append("</script>");
			    	
			    	message +=trList.toString();
				} else {
					message = "<option value=\"\">?????????</option>";
				}
				    
				j.setSuccess(true);
				//j.setMsg(message);
				attributes.put("tableHtml", message);
				j.setAttributes(attributes);
				} else{
					j.setSuccess(true);
					attributes.put("dbName", "");
					attributes.put("tableHtml", "");
				}
			}else{
			   //2.??????ID????????????option??????????????????
				j.setSuccess(false);
			}
		return j;
	}
	/**
	 * ??????????????????????????????????????????????????????
	 */
	@RequestMapping(params = "getHead")
	@ResponseBody
	public AjaxJson getHead(String column, String autoFormId, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "";
		try{
			String hqlList = "from AutoFormDbFieldEntity t where t.fieldName = ? and t.autoFormDbId = ?";
			List<AutoFormDbFieldEntity> list= this.systemService.findHql(hqlList, column, autoFormId);
			
			if(list.size()>0){
				AutoFormDbFieldEntity entry =  list.get(0);
				if(StringUtils.isNotBlank(entry.getFieldText())){
					message = entry.getFieldText();
				}
			}
			
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "addForm")
	@ResponseBody
	public AjaxJson addForm(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "";
		try{
			Map paramData = request.getParameterMap();
			String formName = request.getParameter("formName");
			if(paramData!=null){
				Map<String,Map<String,Object>> dataMap = AutoFormCommUtil.mapConvert(paramData);
				// ????????????
				Map<String, Object> param = dataMap.get("param");
				AutoFormEntity autoForm = this.systemService.findUniqueByProperty(AutoFormEntity.class, "formName", formName);
				//??????????????????
				Map<String, List<Map<String, Object>>> oldDataMap = new HashMap<String, List<Map<String, Object>>>();
				oldDataMap = getFormData(autoForm,param);
				String id = this.autoFormService.doUpdateTable(formName,dataMap,oldDataMap);
				j.setObj(id);
				message = "??????????????????";
			}
		}catch(Exception e){
			e.printStackTrace();
			j.setSuccess(false);
			message = "?????????????????????"+e.getMessage();
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * ????????????
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "updateForm")
	@ResponseBody
	public AjaxJson updateForm(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "??????????????????";
		try{
			Map paramData = request.getParameterMap();
			String formName = request.getParameter("formName");
			if(paramData!=null){
				Map<String,Map<String,Object>> dataMap = AutoFormCommUtil.mapConvert(paramData);
				// ????????????
				Map<String, Object> param = dataMap.get("param");
				AutoFormEntity autoForm = this.systemService.findUniqueByProperty(AutoFormEntity.class, "formName", formName);
				//??????????????????
				Map<String, List<Map<String, Object>>> oldDataMap = new HashMap<String, List<Map<String, Object>>>();
				oldDataMap = getFormData(autoForm,param);
				this.autoFormService.doUpdateTable(formName,dataMap,oldDataMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			j.setSuccess(false);
			message = "?????????????????????"+e.getMessage();
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????????????????
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "addorupdateForm")
	@ResponseBody
	public AjaxJson addorupdateForm(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "??????????????????";
		try{
			Map paramData = request.getParameterMap();
			String formName = request.getParameter("formName");
			if(paramData!=null){
				Map<String,Map<String,Object>> dataMap = AutoFormCommUtil.mapConvert(paramData);
				// ????????????
				Map<String, Object> param = dataMap.get("param");
				AutoFormEntity autoForm = this.systemService.findUniqueByProperty(AutoFormEntity.class, "formName", formName);
				//??????????????????
				Map<String, List<Map<String, Object>>> oldDataMap = new HashMap<String, List<Map<String, Object>>>();
				oldDataMap = getFormData(autoForm,param);
				this.autoFormService.doUpdateTable(formName,dataMap,oldDataMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			j.setSuccess(false);
			message = "??????????????????";
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "checkTbCode")
	@ResponseBody
	public com.alibaba.fastjson.JSONObject checkTbCode(String param,String cVal){
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		if(org.apache.commons.lang.StringUtils.isNotBlank(cVal)&&cVal.equals(param)){
			jsonObject.put("info", "???????????????");
			jsonObject.put("status", "y");
			return jsonObject;
		}
		List<AutoFormEntity> list = new ArrayList<AutoFormEntity>();
		String hql = "from AutoFormEntity t where t.formName = ?";
		list = this.systemService.findHql(hql, param);

		if(list.size()>0){
			jsonObject.put("status", "n");
			jsonObject.put("info", "?????????????????????????????????");
			return jsonObject;
		}
		jsonObject.put("info", "???????????????");
		jsonObject.put("status", "y");
		return jsonObject;
	}
	private void delFormDb(String autoFormId){
		List<AutoFormDbEntity> list = this.systemService.findByProperty(AutoFormDbEntity.class, "autoFormId", autoFormId);
		if(list!=null&&list.size()>0) {
			for (AutoFormDbEntity dbForm : list) {
				systemService.updateBySqlString("delete from auto_form_db_field where auto_form_db_id='" + dbForm.getId()+"'");
				systemService.delete(dbForm);
			}
		}
	}
}
