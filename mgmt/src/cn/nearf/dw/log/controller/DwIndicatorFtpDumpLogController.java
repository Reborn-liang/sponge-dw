package cn.nearf.dw.log.controller;
import cn.nearf.dw.log.entity.DwIndicatorFtpDumpLogEntity;
import cn.nearf.dw.log.service.DwIndicatorFtpDumpLogServiceI;
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
 * @Description: 指标导出日志
 * @author onlineGenerator
 * @date 2019-05-21 11:04:21
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorFtpDumpLogController")
public class DwIndicatorFtpDumpLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorFtpDumpLogController.class);

	@Autowired
	private DwIndicatorFtpDumpLogServiceI dwIndicatorFtpDumpLogService;
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
	 * 指标导出日志列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorFtpDumpLogList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorFtpDumpLogEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorFtpDumpLog, request.getParameterMap());
		try{
		//自定义追加查询条件
		String query_createDate_begin = request.getParameter("createDate_begin");
		String query_createDate_end = request.getParameter("createDate_end");
		if(StringUtil.isNotEmpty(query_createDate_begin)){
			cq.ge("createDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_createDate_begin));
		}
		if(StringUtil.isNotEmpty(query_createDate_end)){
			cq.le("createDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_createDate_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorFtpDumpLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除指标导出日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorFtpDumpLog = systemService.getEntity(DwIndicatorFtpDumpLogEntity.class, dwIndicatorFtpDumpLog.getId());
		message = "指标导出日志删除成功";
		try{
			dwIndicatorFtpDumpLogService.delete(dwIndicatorFtpDumpLog);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标导出日志删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除指标导出日志
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "指标导出日志删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog = systemService.getEntity(DwIndicatorFtpDumpLogEntity.class, 
				Integer.parseInt(id)
				);
				dwIndicatorFtpDumpLogService.delete(dwIndicatorFtpDumpLog);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "指标导出日志删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加指标导出日志
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标导出日志添加成功";
		try{
			dwIndicatorFtpDumpLogService.save(dwIndicatorFtpDumpLog);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标导出日志添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新指标导出日志
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标导出日志更新成功";
		DwIndicatorFtpDumpLogEntity t = dwIndicatorFtpDumpLogService.get(DwIndicatorFtpDumpLogEntity.class, dwIndicatorFtpDumpLog.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorFtpDumpLog, t);
			dwIndicatorFtpDumpLogService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "指标导出日志更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 指标导出日志新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorFtpDumpLog.getId())) {
			dwIndicatorFtpDumpLog = dwIndicatorFtpDumpLogService.getEntity(DwIndicatorFtpDumpLogEntity.class, dwIndicatorFtpDumpLog.getId());
			req.setAttribute("dwIndicatorFtpDumpLogPage", dwIndicatorFtpDumpLog);
		}
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorFtpDumpLog-add");
	}
	/**
	 * 指标导出日志编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorFtpDumpLog.getId())) {
			dwIndicatorFtpDumpLog = dwIndicatorFtpDumpLogService.getEntity(DwIndicatorFtpDumpLogEntity.class, dwIndicatorFtpDumpLog.getId());
			req.setAttribute("dwIndicatorFtpDumpLogPage", dwIndicatorFtpDumpLog);
		}
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorFtpDumpLog-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorFtpDumpLogController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorFtpDumpLogEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorFtpDumpLog, request.getParameterMap());
		List<DwIndicatorFtpDumpLogEntity> dwIndicatorFtpDumpLogs = this.dwIndicatorFtpDumpLogService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"指标导出日志");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorFtpDumpLogEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标导出日志列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorFtpDumpLogs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"指标导出日志");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorFtpDumpLogEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标导出日志列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
    	"导出信息"));
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
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<DwIndicatorFtpDumpLogEntity> listDwIndicatorFtpDumpLogEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorFtpDumpLogEntity.class,params);
				for (DwIndicatorFtpDumpLogEntity dwIndicatorFtpDumpLog : listDwIndicatorFtpDumpLogEntitys) {
					dwIndicatorFtpDumpLogService.save(dwIndicatorFtpDumpLog);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
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
