package cn.nearf.dw.indicatormodel.controller;
import java.io.IOException;
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
import cn.nearf.dw.indicatormodel.entity.DwIndicatorModelCtlEntity;
import cn.nearf.dw.indicatormodel.service.DwIndicatorModelCtlServiceI;



/**   
 * @Title: Controller
 * @Description: dw_indicator_model_ctl
 * @author onlineGenerator
 * @date 2018-07-16 19:57:44
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorModelCtlController")
public class DwIndicatorModelCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorModelCtlController.class);

	@Autowired
	private DwIndicatorModelCtlServiceI dwIndicatorModelCtlService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@RequestMapping(params = "clone")
	@ResponseBody
	public AjaxJson clone(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		try{
			for(String id:ids.split(",")){
				DwIndicatorModelCtlEntity oldModel = systemService.getEntity(DwIndicatorModelCtlEntity.class,  Integer.parseInt(id));
				DwIndicatorModelCtlEntity newModel = new DwIndicatorModelCtlEntity();
				MyBeanUtils.copyBeanNotNull2Bean(oldModel, newModel);
				newModel.setId(null);
				newModel.setDimActiveFlg(0);
				newModel.setCreateDate(new Date());
				newModel.setUpdateDate(null);
				systemService.save(newModel);
				systemService.addLog(message, Globals.Log_Type_UPLOAD, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * dw_indicator_model_ctl?????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/indicatorModel/dwIndicatorModelCtlList");
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
	public void datagrid(DwIndicatorModelCtlEntity dwIndicatorModelCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorModelCtlEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorModelCtl, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorModelCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????dw_indicator_model_ctl
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorModelCtlEntity dwIndicatorModelCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorModelCtl = systemService.getEntity(DwIndicatorModelCtlEntity.class, dwIndicatorModelCtl.getId());
		message = "????????????????????????";
		try{
			CriteriaQuery cq = new CriteriaQuery(DwIndicatorCtlEntity.class);
			cq.eq("modelCode", dwIndicatorModelCtl.getModelCode());
			cq.add();
			List<DwIndicatorCtlEntity> inds = systemService.getListByCriteriaQuery(cq, false);
			if(inds!=null && inds.size()>0) {
				message = "???????????????????????????????????????????????????????????????????????????:"+inds.get(0).getName();
				throw new BusinessException(message);
			}
			
			dwIndicatorModelCtlService.delete(dwIndicatorModelCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????dw_indicator_model_ctl
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			for(String id:ids.split(",")){
				DwIndicatorModelCtlEntity dwIndicatorModelCtl = systemService.getEntity(DwIndicatorModelCtlEntity.class,
						Integer.parseInt(id));
				
				CriteriaQuery cq = new CriteriaQuery(DwIndicatorCtlEntity.class);
				cq.eq("modelCode", dwIndicatorModelCtl.getModelCode());
				cq.add();
				List<DwIndicatorCtlEntity> inds = systemService.getListByCriteriaQuery(cq, false);
				if(inds!=null && inds.size()>0) {
					message = "???????????????????????????????????????????????????????????????????????????:"+inds.get(0).getName();
					throw new BusinessException(message);
				}
				
				dwIndicatorModelCtlService.delete(dwIndicatorModelCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????dw_indicator_model_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorModelCtlEntity dwIndicatorModelCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			dwIndicatorModelCtl.setCreateDate(new Date());
			dwIndicatorModelCtlService.save(dwIndicatorModelCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????dw_indicator_model_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorModelCtlEntity dwIndicatorModelCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		DwIndicatorModelCtlEntity t = dwIndicatorModelCtlService.get(DwIndicatorModelCtlEntity.class, dwIndicatorModelCtl.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorModelCtl, t);
			t.setUpdateDate(new Date());
			dwIndicatorModelCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * dw_indicator_model_ctl??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorModelCtlEntity dwIndicatorModelCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorModelCtl.getId())) {
			dwIndicatorModelCtl = dwIndicatorModelCtlService.getEntity(DwIndicatorModelCtlEntity.class, dwIndicatorModelCtl.getId());
			req.setAttribute("dwIndicatorModelCtlPage", dwIndicatorModelCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicatorModel/dwIndicatorModelCtl-add");
	}
	/**
	 * dw_indicator_model_ctl??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorModelCtlEntity dwIndicatorModelCtl, HttpServletRequest req) {
		System.out.println(req.getParameter("id"));
		if (StringUtil.isNotEmpty(req.getParameter("id"))) {
			dwIndicatorModelCtl = dwIndicatorModelCtlService.getEntity(DwIndicatorModelCtlEntity.class, Integer.parseInt(req.getParameter("id")));
			req.setAttribute("dwIndicatorModelCtlPage", dwIndicatorModelCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicatorModel/dwIndicatorModelCtl-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorModelCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorModelCtlEntity dwIndicatorModelCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorModelCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorModelCtl, request.getParameterMap());
		List<DwIndicatorModelCtlEntity> dwIndicatorModelCtls = this.dwIndicatorModelCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_model_ctl");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorModelCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_model_ctl??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorModelCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorModelCtlEntity dwIndicatorModelCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_model_ctl");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorModelCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_model_ctl??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorModelCtlEntity> listDwIndicatorModelCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorModelCtlEntity.class,params);
				for (DwIndicatorModelCtlEntity dwIndicatorModelCtl : listDwIndicatorModelCtlEntitys) {
					dwIndicatorModelCtlService.save(dwIndicatorModelCtl);
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
