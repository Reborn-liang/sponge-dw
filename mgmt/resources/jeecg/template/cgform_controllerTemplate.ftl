<#if packageStyle == "service">
package ${bussiPackage}.${entityPackage}.controller;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;
import ${bussiPackage}.${entityPackage}.service.${entityName}ServiceI;
<#else>
package ${bussiPackage}.controller.${entityPackage};
import ${bussiPackage}.entity.${entityPackage}.${entityName}Entity;
import ${bussiPackage}.service.${entityPackage}.${entityName}ServiceI;
</#if>
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.context.annotation.Scope;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import org.jeecgframework.core.util.ExceptionUtil;



/**   
 * @Title: Controller
 * @Description: ${ftl_description}
 * @author onlineGenerator
 * @date ${ftl_create_time}
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/${entityName?uncap_first}Controller")
public class ${entityName}Controller extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(${entityName}Controller.class);

	@Autowired
	private ${entityName}ServiceI ${entityName?uncap_first}Service;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * ${ftl_description}?????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("${bussiPackage?replace(".","/")}/${entityPackage}/${entityName?uncap_first}List");
	}

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(${entityName}Entity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, ${entityName?uncap_first}, request.getParameterMap());
		try{
		//???????????????????????????
		<#list columns as po>
		<#if po.isQuery =='Y' && po.queryMode =='group'>
		String query_${po.fieldName}_begin = request.getParameter("${po.fieldName}_begin");
		String query_${po.fieldName}_end = request.getParameter("${po.fieldName}_end");
		if(StringUtil.isNotEmpty(query_${po.fieldName}_begin)){
			<#if po.type == "java.util.Date">
			cq.ge("${po.fieldName}", new SimpleDateFormat("yyyy-MM-dd").parse(query_${po.fieldName}_begin));
			<#else>
			cq.ge("${po.fieldName}", Integer.parseInt(query_${po.fieldName}_begin));
			</#if>
		}
		if(StringUtil.isNotEmpty(query_${po.fieldName}_end)){
			<#if po.type == "java.util.Date">
			cq.le("${po.fieldName}", new SimpleDateFormat("yyyy-MM-dd").parse(query_${po.fieldName}_end));
			<#else>
			cq.le("${po.fieldName}", Integer.parseInt(query_${po.fieldName}_end));
			</#if>
		}
		</#if>
		</#list> 
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.${entityName?uncap_first}Service.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????${ftl_description}
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		${entityName?uncap_first} = systemService.getEntity(${entityName}Entity.class, ${entityName?uncap_first}.getId());
		message = "${ftl_description}????????????";
		try{
			${entityName?uncap_first}Service.delete(${entityName?uncap_first});
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "${ftl_description}????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????${ftl_description}
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "${ftl_description}????????????";
		try{
			for(String id:ids.split(",")){
				${entityName}Entity ${entityName?uncap_first} = systemService.getEntity(${entityName}Entity.class, 
				<#if cgformConfig.cgFormHead.jformPkType?if_exists?html == "UUID">
				id
				<#elseif cgformConfig.cgFormHead.jformPkType?if_exists?html == "NATIVE">
				Integer.parseInt(id)
				<#elseif cgformConfig.cgFormHead.jformPkType?if_exists?html == "SEQUENCE">
				Integer.parseInt(id)
				<#else>
				id
				</#if>
				);
				${entityName?uncap_first}Service.delete(${entityName?uncap_first});
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "${ftl_description}????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????${ftl_description}
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "${ftl_description}????????????";
		try{
			${entityName?uncap_first}Service.save(${entityName?uncap_first});
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "${ftl_description}????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????${ftl_description}
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "${ftl_description}????????????";
		${entityName}Entity t = ${entityName?uncap_first}Service.get(${entityName}Entity.class, ${entityName?uncap_first}.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(${entityName?uncap_first}, t);
			${entityName?uncap_first}Service.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "${ftl_description}????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	<#list buttons as btn>
 	<#if btn.buttonStyle =='button' && btn.optType=='action'>
 	/**
	 * ???????????????-sql??????-${btn.buttonName}
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "do${btn.buttonCode?cap_first}")
	@ResponseBody
	public AjaxJson do${btn.buttonCode?cap_first}(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "${btn.buttonName}??????";
		${entityName}Entity t = ${entityName?uncap_first}Service.get(${entityName}Entity.class, ${entityName?uncap_first}.getId());
		try{
			${entityName?uncap_first}Service.do${btn.buttonCode?cap_first}Sql(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "${btn.buttonName}??????";
		}
		j.setMsg(message);
		return j;
	}
 	</#if>
 	</#list> 

	/**
	 * ${ftl_description}??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(${entityName?uncap_first}.getId())) {
			${entityName?uncap_first} = ${entityName?uncap_first}Service.getEntity(${entityName}Entity.class, ${entityName?uncap_first}.getId());
			req.setAttribute("${entityName?uncap_first}Page", ${entityName?uncap_first});
		}
		return new ModelAndView("${bussiPackage?replace(".","/")}/${entityPackage}/${entityName?uncap_first}-add");
	}
	/**
	 * ${ftl_description}??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(${entityName?uncap_first}.getId())) {
			${entityName?uncap_first} = ${entityName?uncap_first}Service.getEntity(${entityName}Entity.class, ${entityName?uncap_first}.getId());
			req.setAttribute("${entityName?uncap_first}Page", ${entityName?uncap_first});
		}
		return new ModelAndView("${bussiPackage?replace(".","/")}/${entityPackage}/${entityName?uncap_first}-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","${entityName?uncap_first}Controller");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(${entityName}Entity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, ${entityName?uncap_first}, request.getParameterMap());
		List<${entityName}Entity> ${entityName?uncap_first}s = this.${entityName?uncap_first}Service.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"${ftl_description}");
		modelMap.put(NormalExcelConstants.CLASS,${entityName}Entity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("${ftl_description}??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,${entityName?uncap_first}s);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"${ftl_description}");
    	modelMap.put(NormalExcelConstants.CLASS,${entityName}Entity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("${ftl_description}??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
    	"????????????"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// ????????????????????????
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<${entityName}Entity> list${entityName}Entitys = ExcelImportUtil.importExcel(file.getInputStream(),${entityName}Entity.class,params);
				for (${entityName}Entity ${entityName?uncap_first} : list${entityName}Entitys) {
					${entityName?uncap_first}Service.save(${entityName?uncap_first});
				}
				j.setMsg("?????????????????????");
			} catch (Exception e) {
				j.setMsg("?????????????????????");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
}
