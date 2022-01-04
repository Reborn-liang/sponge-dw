package cn.nearf.dw.indicator.controller;
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
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
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

import cn.nearf.dw.indicator.entity.DwIndicatorStdCtlEntity;
import cn.nearf.dw.indicator.service.DwIndicatorStdCtlServiceI;



/**   
 * @Title: Controller
 * @Description: dw_indicator_std_ctl
 * @author onlineGenerator
 * @date 2018-07-12 19:57:24
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorStdCtlController")
public class DwIndicatorStdCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorStdCtlController.class);

	private UserService userService;
	@Autowired
	private DwIndicatorStdCtlServiceI dwIndicatorStdCtlService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@RequestMapping(params = "genView")
	@ResponseBody
	public AjaxJson genView(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = "生成标准化视图成功";
		String stdId = req.getParameter("id");
		if (StringUtil.isNotEmpty(stdId)) {
			dwIndicatorStdCtl = dwIndicatorStdCtlService.getEntity(DwIndicatorStdCtlEntity.class, Integer.parseInt(stdId));
			try {
				dwIndicatorStdCtlService.genView(dwIndicatorStdCtl);
			} catch (Exception e) {
				message = "生成标准化视图失败, 原因："+e.getMessage();
			}
		}else {
			message = "生成标准化视图失败, 未知原因";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * dw_indicator_std_ctl列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorStdCtlList");
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
	public void datagrid(DwIndicatorStdCtlEntity dwIndicatorStdCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		TSUser sessionUser = ResourceUtil.getSessionUserName();
		if (sessionUser.getUserKey().equals("管理员")) {
			CriteriaQuery cq = new CriteriaQuery(DwIndicatorStdCtlEntity.class, dataGrid);
			//查询条件组装器
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorStdCtl, request.getParameterMap());
			try {
				//自定义追加查询条件
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			cq.add();
			this.dwIndicatorStdCtlService.getDataGridReturn(cq, true);
			TagUtil.datagrid(response, dataGrid);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	/**
	 * 删除dw_indicator_std_ctl
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorStdCtl = systemService.getEntity(DwIndicatorStdCtlEntity.class, dwIndicatorStdCtl.getId());
		message = "dw_indicator_std_ctl删除成功";
		try{
			dwIndicatorStdCtlService.delete(dwIndicatorStdCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_std_ctl删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除dw_indicator_std_ctl
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "dw_indicator_std_ctl删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorStdCtlEntity dwIndicatorStdCtl = systemService.getEntity(DwIndicatorStdCtlEntity.class, 
				Integer.parseInt(id)
				);
				dwIndicatorStdCtlService.delete(dwIndicatorStdCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_std_ctl删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加dw_indicator_std_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "dw_indicator_std_ctl添加成功";
		try{
			dwIndicatorStdCtlService.save(dwIndicatorStdCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_std_ctl添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新dw_indicator_std_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "dw_indicator_std_ctl更新成功";
		DwIndicatorStdCtlEntity t = dwIndicatorStdCtlService.get(DwIndicatorStdCtlEntity.class, dwIndicatorStdCtl.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorStdCtl, t);
			dwIndicatorStdCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "dw_indicator_std_ctl更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * dw_indicator_std_ctl新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorStdCtl.getId())) {
			dwIndicatorStdCtl = dwIndicatorStdCtlService.getEntity(DwIndicatorStdCtlEntity.class, dwIndicatorStdCtl.getId());
			req.setAttribute("dwIndicatorStdCtlPage", dwIndicatorStdCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorStdCtl-add");
	}
	/**
	 * dw_indicator_std_ctl编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorStdCtlEntity dwIndicatorStdCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorStdCtl.getId())) {
			dwIndicatorStdCtl = dwIndicatorStdCtlService.getEntity(DwIndicatorStdCtlEntity.class, dwIndicatorStdCtl.getId());
			req.setAttribute("dwIndicatorStdCtlPage", dwIndicatorStdCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorStdCtl-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorStdCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorStdCtlEntity dwIndicatorStdCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorStdCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorStdCtl, request.getParameterMap());
		List<DwIndicatorStdCtlEntity> dwIndicatorStdCtls = this.dwIndicatorStdCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_std_ctl");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorStdCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_std_ctl列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorStdCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorStdCtlEntity dwIndicatorStdCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_std_ctl");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorStdCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_std_ctl列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorStdCtlEntity> listDwIndicatorStdCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorStdCtlEntity.class,params);
				for (DwIndicatorStdCtlEntity dwIndicatorStdCtl : listDwIndicatorStdCtlEntitys) {
					dwIndicatorStdCtlService.save(dwIndicatorStdCtl);
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
