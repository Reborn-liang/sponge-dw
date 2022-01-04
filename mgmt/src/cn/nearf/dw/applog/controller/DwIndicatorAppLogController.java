package cn.nearf.dw.applog.controller;
import cn.nearf.dw.applog.entity.DwIndicatorAppLogEntity;
import cn.nearf.dw.applog.service.DwIndicatorAppLogServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
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
 * @Description: app日志记录
 * @author onlineGenerator
 * @date 2018-08-08 17:11:07
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorAppLogController")
public class DwIndicatorAppLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorAppLogController.class);

	@Autowired
	private DwIndicatorAppLogServiceI dwIndicatorAppLogService;
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
	 * 改变日志状态
	 * */
	@RequestMapping(params="changeStatus")
	@ResponseBody
	public AjaxJson changeStatus(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = "状态切换成功";
		if(StringUtil.isNotEmpty(dwIndicatorAppLog.getId())) {
			dwIndicatorAppLog=dwIndicatorAppLogService.getEntity(DwIndicatorAppLogEntity.class, dwIndicatorAppLog.getId());
			Integer status = dwIndicatorAppLog.getStatus();
			if(status!=null) {
				if(status==1) {
					dwIndicatorAppLog.setStatus(0);
				}
				if(status==0) {
					dwIndicatorAppLog.setStatus(1);
				}
			}
			try {
				dwIndicatorAppLogService.updateEntitie(dwIndicatorAppLog);
			}catch (Exception e) {
				message="状态切换失败,原因:"+e.getMessage();
			}
			
		}else {
			message="状态切换失败,未知原因";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * app日志记录列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/applog/dwIndicatorAppLogList");
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
	public void datagrid(DwIndicatorAppLogEntity dwIndicatorAppLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorAppLogEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorAppLog, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorAppLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除app日志记录
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorAppLog = systemService.getEntity(DwIndicatorAppLogEntity.class, dwIndicatorAppLog.getId());
		message = "app日志记录删除成功";
		try{
			dwIndicatorAppLogService.delete(dwIndicatorAppLog);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "app日志记录删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除app日志记录
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "app日志记录删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorAppLogEntity dwIndicatorAppLog = systemService.getEntity(DwIndicatorAppLogEntity.class, 
				Integer.parseInt(id)
				);
				dwIndicatorAppLogService.delete(dwIndicatorAppLog);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "app日志记录删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加app日志记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "app日志记录添加成功";
		try{
			dwIndicatorAppLogService.save(dwIndicatorAppLog);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "app日志记录添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新app日志记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "app日志记录更新成功";
		DwIndicatorAppLogEntity t = dwIndicatorAppLogService.get(DwIndicatorAppLogEntity.class, dwIndicatorAppLog.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorAppLog, t);
			dwIndicatorAppLogService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "app日志记录更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * app日志记录新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorAppLog.getId())) {
			dwIndicatorAppLog = dwIndicatorAppLogService.getEntity(DwIndicatorAppLogEntity.class, dwIndicatorAppLog.getId());
			req.setAttribute("dwIndicatorAppLogPage", dwIndicatorAppLog);
		}
		return new ModelAndView("cn/nearf/dw/applog/dwIndicatorAppLog-add");
	}
	/**
	 * app日志记录编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorAppLogEntity dwIndicatorAppLog, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorAppLog.getId())) {
			dwIndicatorAppLog = dwIndicatorAppLogService.getEntity(DwIndicatorAppLogEntity.class, dwIndicatorAppLog.getId());
			req.setAttribute("dwIndicatorAppLogPage", dwIndicatorAppLog);
		}
		return new ModelAndView("cn/nearf/dw/applog/dwIndicatorAppLog-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorAppLogController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorAppLogEntity dwIndicatorAppLog,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorAppLogEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorAppLog, request.getParameterMap());
		List<DwIndicatorAppLogEntity> dwIndicatorAppLogs = this.dwIndicatorAppLogService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"app日志记录");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorAppLogEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("app日志记录列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorAppLogs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorAppLogEntity dwIndicatorAppLog,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"app日志记录");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorAppLogEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("app日志记录列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorAppLogEntity> listDwIndicatorAppLogEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorAppLogEntity.class,params);
				for (DwIndicatorAppLogEntity dwIndicatorAppLog : listDwIndicatorAppLogEntitys) {
					dwIndicatorAppLogService.save(dwIndicatorAppLog);
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
