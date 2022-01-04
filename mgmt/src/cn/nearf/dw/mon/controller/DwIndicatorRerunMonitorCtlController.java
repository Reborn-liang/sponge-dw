package cn.nearf.dw.mon.controller;
import java.io.IOException;
import java.util.ArrayList;
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

import cn.nearf.dw.mon.entity.DwIndicatorRerunMonitorCtlEntity;
import cn.nearf.dw.mon.service.DwIndicatorRerunMonitorCtlServiceI;



/**   
 * @Title: Controller
 * @Description: 指标重做管理
 * @author onlineGenerator
 * @date 2018-08-16 23:37:13
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorRerunMonitorCtlController")
public class DwIndicatorRerunMonitorCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorRerunMonitorCtlController.class);

	@Autowired
	private DwIndicatorRerunMonitorCtlServiceI dwIndicatorRerunMonitorCtlService;
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
	 * 指标重做管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorRerunMonitorCtlList");
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
	public void datagrid(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorRerunMonitorCtlEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorRerunMonitorCtl, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorRerunMonitorCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "active")
	@ResponseBody
	public AjaxJson clone(String ids, HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "激活重做任务成功";
		try{
			String []idArray = ids.split(",");
			for (String id : idArray) {
				DwIndicatorRerunMonitorCtlEntity rerunEntity = systemService.getEntity(DwIndicatorRerunMonitorCtlEntity.class,  Integer.parseInt(id));
				rerunEntity.setJobStatus("A");
				systemService.saveOrUpdate(rerunEntity);
			}
			systemService.addLog(message, Globals.Log_Type_UPLOAD, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "激活重做任务失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除指标重做管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorRerunMonitorCtl = systemService.getEntity(DwIndicatorRerunMonitorCtlEntity.class, dwIndicatorRerunMonitorCtl.getId());
		message = "指标重做管理删除成功";
		try{
			if(!"O".equals(dwIndicatorRerunMonitorCtl.getJobStatus())){
				j.setMsg("此任务非暂停状态，不能删除，请检查");
				return j;
			}
			dwIndicatorRerunMonitorCtlService.delete(dwIndicatorRerunMonitorCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标重做管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除指标重做管理
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "指标重做管理删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl = systemService.getEntity(DwIndicatorRerunMonitorCtlEntity.class, 
				Integer.parseInt(id));
				if(!"O".equals(dwIndicatorRerunMonitorCtl.getJobStatus())){
					j.setMsg("批量删除的任务中包含有非暂停状态的任务，不能删除，请检查");
					return j;
				}
				dwIndicatorRerunMonitorCtlService.delete(dwIndicatorRerunMonitorCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "指标重做管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加指标重做管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标重做管理添加成功";
		try{
			dwIndicatorRerunMonitorCtlService.save(dwIndicatorRerunMonitorCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标重做管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新指标重做管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标重做管理更新成功";
		DwIndicatorRerunMonitorCtlEntity t = dwIndicatorRerunMonitorCtlService.get(DwIndicatorRerunMonitorCtlEntity.class, dwIndicatorRerunMonitorCtl.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorRerunMonitorCtl, t);
			dwIndicatorRerunMonitorCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "指标重做管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 指标重做管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorRerunMonitorCtl.getId())) {
			dwIndicatorRerunMonitorCtl = dwIndicatorRerunMonitorCtlService.getEntity(DwIndicatorRerunMonitorCtlEntity.class, dwIndicatorRerunMonitorCtl.getId());
			req.setAttribute("dwIndicatorRerunMonitorCtlPage", dwIndicatorRerunMonitorCtl);
		}
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorRerunMonitorCtl-add");
	}
	/**
	 * 指标重做管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorRerunMonitorCtl.getId())) {
			dwIndicatorRerunMonitorCtl = dwIndicatorRerunMonitorCtlService.getEntity(DwIndicatorRerunMonitorCtlEntity.class, dwIndicatorRerunMonitorCtl.getId());
			req.setAttribute("dwIndicatorRerunMonitorCtlPage", dwIndicatorRerunMonitorCtl);
		}
		return new ModelAndView("cn/nearf/dw/mon/dwIndicatorRerunMonitorCtl-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorRerunMonitorCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorRerunMonitorCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorRerunMonitorCtl, request.getParameterMap());
		List<DwIndicatorRerunMonitorCtlEntity> dwIndicatorRerunMonitorCtls = this.dwIndicatorRerunMonitorCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"指标重做管理");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorRerunMonitorCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标重做管理列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorRerunMonitorCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"指标重做管理");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorRerunMonitorCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("指标重做管理列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorRerunMonitorCtlEntity> listDwIndicatorRerunMonitorCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorRerunMonitorCtlEntity.class,params);
				for (DwIndicatorRerunMonitorCtlEntity dwIndicatorRerunMonitorCtl : listDwIndicatorRerunMonitorCtlEntitys) {
					dwIndicatorRerunMonitorCtlService.save(dwIndicatorRerunMonitorCtl);
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
