package cn.nearf.dw.mon.controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.mon.entity.DwIndicatorJobMonitorCtlEntity;
import cn.nearf.dw.mon.entity.DwIndicatorRerunMonitorCtlEntity;
import cn.nearf.dw.mon.service.DwIndicatorJobMonitorCtlServiceI;
import cn.nearf.ggz.utils.ObjectUtils;



/**   
 * @Title: Controller
 * @Description: 指标执行状态管理
 * @author onlineGenerator
 * @date 2018-07-12 23:06:46
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorJobMonitorCtlController")
public class DwIndicatorJobMonitorCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorJobMonitorCtlController.class);
	@Autowired
	private DwIndicatorJobMonitorCtlServiceI dwIndicatorJobMonitorCtlService;
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
	 * 重做
	 * */
	@RequestMapping(params = "rerun")
	public ModelAndView rerun(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorJobMonitorCtl.getId())) {
			dwIndicatorJobMonitorCtl = dwIndicatorJobMonitorCtlService.getEntity(DwIndicatorJobMonitorCtlEntity.class, dwIndicatorJobMonitorCtl.getId());
			
			DwIndicatorCtlEntity dwIndicatorCtl = systemService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorJobMonitorCtl.getIndicatorId());
			if("SRC".equals(dwIndicatorCtl.getType())){
				req.setAttribute("rerunError", 1);
			}else {
				req.setAttribute("rerunError", 0);
				req.setAttribute("dwIndicatorCtlPage", dwIndicatorCtl);
			}
		}
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorJobMonitorCtl-rerun");
		
	}
	/**
	 * doRerun
	 * */
	@RequestMapping(params = "doRerun")
	@ResponseBody
	public AjaxJson doRerun(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		j.setMsg("重做任务添加成功，请在重做管理中激活此任务.");
		int id = ObjectUtils.getIntValue(req.getParameter("id"));
		Date refreshDate = dwIndicatorJobMonitorCtl.getJobStartTime();
		DwIndicatorCtlEntity ind = systemService.getEntity(DwIndicatorCtlEntity.class, id);
		DwIndicatorRerunMonitorCtlEntity rerun = new DwIndicatorRerunMonitorCtlEntity();
		// 加载类型没找到
		rerun.setJobStatus("O");
		rerun.setJobIncFlg(ind.getJobIncFlg());
		rerun.setJobType(ind.getJobType());
		rerun.setRefreshDate(refreshDate);
		rerun.setIndicatorId(ind.getId());
		rerun.setCreateDate(new Date());
		try {
			systemService.save(rerun);
		} catch (Exception e) {
			e.printStackTrace();
			j.setErrorMsg("重做任务添加失败 :" + e.getMessage());
		}
		return j;
	}
	/**
	 * 指标执行状态管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorJobMonitorCtlList");
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
	public void datagrid(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorJobMonitorCtlEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorJobMonitorCtl, request.getParameterMap());
		try{
		//自定义追加查询条件
		String query_indicatorId_begin = request.getParameter("indicatorId_begin");
		String query_indicatorId_end = request.getParameter("indicatorId_end");
		if(StringUtil.isNotEmpty(query_indicatorId_begin)){
			cq.ge("indicatorId", Integer.parseInt(query_indicatorId_begin));
		}
		if(StringUtil.isNotEmpty(query_indicatorId_end)){
			cq.le("indicatorId", Integer.parseInt(query_indicatorId_end));
		}
		String query_jobStatus_begin = request.getParameter("jobStatus_begin");
		String query_jobStatus_end = request.getParameter("jobStatus_end");
		if(StringUtil.isNotEmpty(query_jobStatus_begin)){
			cq.ge("jobStatus", Integer.parseInt(query_jobStatus_begin));
		}
		if(StringUtil.isNotEmpty(query_jobStatus_end)){
			cq.le("jobStatus", Integer.parseInt(query_jobStatus_end));
		}
		String query_jobStartTime_begin = request.getParameter("jobStartTime_begin");
		String query_jobStartTime_end = request.getParameter("jobStartTime_end");
		if(StringUtil.isNotEmpty(query_jobStartTime_begin)){
			cq.ge("jobStartTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_jobStartTime_begin));
		}
		if(StringUtil.isNotEmpty(query_jobStartTime_end)){
			cq.le("jobStartTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_jobStartTime_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorJobMonitorCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除指标执行状态管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorJobMonitorCtl = systemService.getEntity(DwIndicatorJobMonitorCtlEntity.class, dwIndicatorJobMonitorCtl.getIndicatorId());
		message = "指标执行状态管理删除成功";
		try{
			dwIndicatorJobMonitorCtlService.delete(dwIndicatorJobMonitorCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标执行状态管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除指标执行状态管理
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "指标执行状态管理删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl = systemService.getEntity(DwIndicatorJobMonitorCtlEntity.class, 
						Integer.parseInt(id));
				dwIndicatorJobMonitorCtlService.delete(dwIndicatorJobMonitorCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "指标执行状态管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加指标执行状态管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标执行状态管理添加成功";
		try{
			dwIndicatorJobMonitorCtlService.save(dwIndicatorJobMonitorCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标执行状态管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新指标执行状态管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标执行状态管理更新成功";
		DwIndicatorJobMonitorCtlEntity t = dwIndicatorJobMonitorCtlService.get(DwIndicatorJobMonitorCtlEntity.class, dwIndicatorJobMonitorCtl.getIndicatorId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorJobMonitorCtl, t);
			dwIndicatorJobMonitorCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "指标执行状态管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 指标执行状态管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorJobMonitorCtl.getIndicatorId())) {
			dwIndicatorJobMonitorCtl = dwIndicatorJobMonitorCtlService.getEntity(DwIndicatorJobMonitorCtlEntity.class, dwIndicatorJobMonitorCtl.getIndicatorId());
			req.setAttribute("dwIndicatorJobMonitorCtlPage", dwIndicatorJobMonitorCtl);
		}
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorJobMonitorCtl-add");
	}
	/**
	 * 指标执行状态管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorJobMonitorCtl.getIndicatorId())) {
			dwIndicatorJobMonitorCtl = dwIndicatorJobMonitorCtlService.getEntity(DwIndicatorJobMonitorCtlEntity.class, dwIndicatorJobMonitorCtl.getIndicatorId());
			req.setAttribute("dwIndicatorJobMonitorCtlPage", dwIndicatorJobMonitorCtl);
		}
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorJobMonitorCtl-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorJobMonitorCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorJobMonitorCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorJobMonitorCtl, request.getParameterMap());
		List<DwIndicatorJobMonitorCtlEntity> dwIndicatorJobMonitorCtls = this.dwIndicatorJobMonitorCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"指标执行状态管理");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorJobMonitorCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标执行状态管理列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorJobMonitorCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"指标执行状态管理");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorJobMonitorCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标执行状态管理列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorJobMonitorCtlEntity> listDwIndicatorJobMonitorCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorJobMonitorCtlEntity.class,params);
				for (DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl : listDwIndicatorJobMonitorCtlEntitys) {
					dwIndicatorJobMonitorCtlService.save(dwIndicatorJobMonitorCtl);
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
