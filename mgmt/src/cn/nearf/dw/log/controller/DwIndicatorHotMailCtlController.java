package cn.nearf.dw.log.controller;
import cn.nearf.dw.log.entity.DwIndicatorHotMailCtlEntity;
import cn.nearf.dw.log.service.DwIndicatorHotMailCtlServiceI;
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
 * @Description: ??????????????????
 * @author onlineGenerator
 * @date 2019-05-21 11:03:58
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorHotMailCtlController")
public class DwIndicatorHotMailCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorHotMailCtlController.class);

	@Autowired
	private DwIndicatorHotMailCtlServiceI dwIndicatorHotMailCtlService;
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
	 * ???????????????????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorHotMailCtlList");
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
	public void datagrid(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorHotMailCtlEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorHotMailCtl, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorHotMailCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorHotMailCtl = systemService.getEntity(DwIndicatorHotMailCtlEntity.class, dwIndicatorHotMailCtl.getId());
		message = "??????????????????????????????";
		try{
			dwIndicatorHotMailCtlService.delete(dwIndicatorHotMailCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "??????????????????????????????";
		try{
			for(String id:ids.split(",")){
				DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl = systemService.getEntity(DwIndicatorHotMailCtlEntity.class, 
				Integer.parseInt(id)
				);
				dwIndicatorHotMailCtlService.delete(dwIndicatorHotMailCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ????????????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "??????????????????????????????";
		try{
			dwIndicatorHotMailCtlService.save(dwIndicatorHotMailCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "??????????????????????????????";
		DwIndicatorHotMailCtlEntity t = dwIndicatorHotMailCtlService.get(DwIndicatorHotMailCtlEntity.class, dwIndicatorHotMailCtl.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorHotMailCtl, t);
			dwIndicatorHotMailCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "??????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorHotMailCtl.getId())) {
			dwIndicatorHotMailCtl = dwIndicatorHotMailCtlService.getEntity(DwIndicatorHotMailCtlEntity.class, dwIndicatorHotMailCtl.getId());
			req.setAttribute("dwIndicatorHotMailCtlPage", dwIndicatorHotMailCtl);
		}
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorHotMailCtl-add");
	}
	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorHotMailCtl.getId())) {
			dwIndicatorHotMailCtl = dwIndicatorHotMailCtlService.getEntity(DwIndicatorHotMailCtlEntity.class, dwIndicatorHotMailCtl.getId());
			req.setAttribute("dwIndicatorHotMailCtlPage", dwIndicatorHotMailCtl);
		}
		return new ModelAndView("cn/nearf/dw/log/dwIndicatorHotMailCtl-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorHotMailCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorHotMailCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorHotMailCtl, request.getParameterMap());
		List<DwIndicatorHotMailCtlEntity> dwIndicatorHotMailCtls = this.dwIndicatorHotMailCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"??????????????????");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorHotMailCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("????????????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorHotMailCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"??????????????????");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorHotMailCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("????????????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorHotMailCtlEntity> listDwIndicatorHotMailCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorHotMailCtlEntity.class,params);
				for (DwIndicatorHotMailCtlEntity dwIndicatorHotMailCtl : listDwIndicatorHotMailCtlEntitys) {
					dwIndicatorHotMailCtlService.save(dwIndicatorHotMailCtl);
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
