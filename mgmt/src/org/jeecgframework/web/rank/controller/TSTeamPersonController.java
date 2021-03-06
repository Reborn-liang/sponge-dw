package org.jeecgframework.web.rank.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.engine.FreemarkerHelper;
import org.jeecgframework.web.rank.entity.TSTeamPersonEntity;
import org.jeecgframework.web.rank.service.TSTeamPersonServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Title: Controller
 * @Description: ???????????????
 * @author onlineGenerator
 * @date 2015-07-04 21:29:29
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/tSTeamPersonController")
public class TSTeamPersonController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TSTeamPersonController.class);
	
	private final String FTL_Teachers="clzcontext/template/cms/rank/html/teachers.ftl";
	private final String FTL_Teacher="clzcontext/template/cms/rank/html/teacher.ftl";
	private final String FTL_Introduce="clzcontext/template/cms/rank/html/introduce.ftl";
	@Autowired
	private TSTeamPersonServiceI tSTeamPersonService;
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
	 * ????????????????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "tSTeamPerson")
	public ModelAndView tSTeamPerson(HttpServletRequest request) {
		return new ModelAndView("system/rank/tSTeamPersonList");
	}
	/**
	 * ??????-????????????
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getTeacherList")
	public void getTeacherList(HttpServletRequest request, HttpServletResponse response){
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		Map<String, Object> map = new HashMap<String, Object>();
		List<TSTeamPersonEntity> teamPersonEntities = this.tSTeamPersonService.findByQueryString("from TSTeamPersonEntity order by isJoin desc, jionDate");
		
		String path = request.getContextPath();
		String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		map.put("teachers", teamPersonEntities);
		map.put("url", url);
		String html = viewEngine.parseTemplate(FTL_Teachers, map);
		
		try {
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ??????.????????????
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getTeacher")
	public void getTeacher(@RequestParam String id, HttpServletRequest request, HttpServletResponse response){
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		
		TSTeamPersonEntity teamPersonEntity = this.tSTeamPersonService.get(TSTeamPersonEntity.class, id);
		String path = request.getContextPath();
		String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teacher", teamPersonEntity);
		map.put("url", url);
		String html = viewEngine.parseTemplate(FTL_Teacher, map);
		
		try {
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void datagrid(TSTeamPersonEntity tSTeamPerson,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSTeamPersonEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tSTeamPerson, request.getParameterMap());
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TSTeamPersonEntity tSTeamPerson, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tSTeamPerson = systemService.getEntity(TSTeamPersonEntity.class, tSTeamPerson.getId());
		message = "???????????????????????????";
		try{
			tSTeamPersonService.delete(tSTeamPerson);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		try{
			for(String id:ids.split(",")){
				TSTeamPersonEntity tSTeamPerson = systemService.getEntity(TSTeamPersonEntity.class, 
				id
				);
				tSTeamPersonService.delete(tSTeamPerson);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ?????????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TSTeamPersonEntity tSTeamPerson, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		try{
			tSTeamPersonService.save(tSTeamPerson);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ?????????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TSTeamPersonEntity tSTeamPerson, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		TSTeamPersonEntity t = tSTeamPersonService.get(TSTeamPersonEntity.class, tSTeamPerson.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tSTeamPerson, t);
			tSTeamPersonService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TSTeamPersonEntity tSTeamPerson, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tSTeamPerson.getId())) {
			tSTeamPerson = tSTeamPersonService.getEntity(TSTeamPersonEntity.class, tSTeamPerson.getId());
			req.setAttribute("tSTeamPersonPage", tSTeamPerson);
		}
		return new ModelAndView("system/rank/tSTeamPerson-add");
	}
	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TSTeamPersonEntity tSTeamPerson, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tSTeamPerson.getId())) {
			tSTeamPerson = tSTeamPersonService.getEntity(TSTeamPersonEntity.class, tSTeamPerson.getId());
			req.setAttribute("tSTeamPersonPage", tSTeamPerson);
		}
		return new ModelAndView("system/rank/tSTeamPerson-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tSTeamPersonController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TSTeamPersonEntity tSTeamPerson,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TSTeamPersonEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tSTeamPerson, request.getParameterMap());
		List<TSTeamPersonEntity> tSTeamPersons = this.tSTeamPersonService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"???????????????");
		modelMap.put(NormalExcelConstants.CLASS,TSTeamPersonEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("?????????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST, tSTeamPersons);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TSTeamPersonEntity tSTeamPerson,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		modelMap.put(TemplateExcelConstants.FILE_NAME, "???????????????");
		modelMap.put(TemplateExcelConstants.PARAMS,new TemplateExportParams("Excel????????????"));
		modelMap.put(TemplateExcelConstants.MAP_DATA,null);
		modelMap.put(TemplateExcelConstants.CLASS,TSTeamPersonEntity.class);
		modelMap.put(TemplateExcelConstants.LIST_DATA,null);
		return TemplateExcelConstants.JEECG_TEMPLATE_EXCEL_VIEW;
	}
	
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
				List<TSTeamPersonEntity> listTSTeamPersonEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TSTeamPersonEntity.class,params);
				for (TSTeamPersonEntity tSTeamPerson : listTSTeamPersonEntitys) {
					tSTeamPersonService.save(tSTeamPerson);
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
	/**
	 * ????????????
	 * @param
	 * @return
	 */
	@RequestMapping(params = "introduce")
	public void introduce(HttpServletRequest request,HttpServletResponse response) {
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		String path = request.getContextPath();
		String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", url);
		String html = viewEngine.parseTemplate(FTL_Introduce, map);
		try {
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
