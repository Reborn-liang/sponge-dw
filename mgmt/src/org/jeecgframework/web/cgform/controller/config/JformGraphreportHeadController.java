package org.jeecgframework.web.cgform.controller.config;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.config.JformGraphreportHeadEntity;
import org.jeecgframework.web.cgform.entity.config.JformGraphreportHeadPage;
import org.jeecgframework.web.cgform.entity.config.JformGraphreportItemEntity;
import org.jeecgframework.web.cgform.service.config.JformGraphreportHeadServiceI;
import org.jeecgframework.web.demo.entity.test.CourseEntity;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**   
 * @Title: Controller
 * @Description: ????????????
 * @author onlineGenerator
 * @date 2015-06-10 17:19:06
 * @version V1.0   
 *
 */
@Scope("prototype") 
@Controller
@RequestMapping("/jformGraphreportHeadController")
public class JformGraphreportHeadController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JformGraphreportHeadController.class);

	@Autowired
	private JformGraphreportHeadServiceI jformGraphreportHeadService;
	@Autowired
	private SystemService systemService;


	/**
	 * ?????????????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "jformGraphreportHead")
	public ModelAndView jformGraphreportHead(HttpServletRequest request) {
		return new ModelAndView("jeecg/cgform/graphreport/jformGraphreportHeadList");
	}

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JformGraphreportHeadEntity jformGraphreportHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformGraphreportHeadEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformGraphreportHead);
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformGraphreportHeadService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformGraphreportHeadEntity jformGraphreportHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		jformGraphreportHead = systemService.getEntity(JformGraphreportHeadEntity.class, jformGraphreportHead.getId());
		String message = "????????????????????????";
		try{
			jformGraphreportHeadService.delMain(jformGraphreportHead);
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
	 * ????????????????????????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "????????????????????????";
		try{
			for(String id:ids.split(",")){
				JformGraphreportHeadEntity jformGraphreportHead = systemService.getEntity(JformGraphreportHeadEntity.class,
				id
				);
				jformGraphreportHeadService.delMain(jformGraphreportHead);
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
	 * ??????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformGraphreportHeadEntity jformGraphreportHead,JformGraphreportHeadPage jformGraphreportHeadPage, HttpServletRequest request) {
		List<JformGraphreportItemEntity> jformGraphreportItemList =  jformGraphreportHeadPage.getJformGraphreportItemList();
		AjaxJson j = new AjaxJson();
		String message = "????????????";
		try{
			jformGraphreportHeadService.addMain(jformGraphreportHead, jformGraphreportItemList);
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
	 * ??????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformGraphreportHeadEntity jformGraphreportHead,JformGraphreportHeadPage jformGraphreportHeadPage, HttpServletRequest request) {
		List<JformGraphreportItemEntity> jformGraphreportItemList =  jformGraphreportHeadPage.getJformGraphreportItemList();
		AjaxJson j = new AjaxJson();
		String message = "????????????";
		try{
			jformGraphreportHeadService.updateMain(jformGraphreportHead, jformGraphreportItemList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
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
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformGraphreportHeadEntity jformGraphreportHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformGraphreportHead.getId())) {
			jformGraphreportHead = jformGraphreportHeadService.getEntity(JformGraphreportHeadEntity.class, jformGraphreportHead.getId());
			req.setAttribute("jformGraphreportHeadPage", jformGraphreportHead);
		}
		return new ModelAndView("jeecg/cgform/graphreport/jformGraphreportHead-add");
	}
	
	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformGraphreportHeadEntity jformGraphreportHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformGraphreportHead.getId())) {
			jformGraphreportHead = jformGraphreportHeadService.getEntity(JformGraphreportHeadEntity.class, jformGraphreportHead.getId());
			req.setAttribute("jformGraphreportHeadPage", jformGraphreportHead);
		}
		return new ModelAndView("jeecg/cgform/graphreport/jformGraphreportHead-update");
	}
	
	
	/**
	 * ??????????????????[??????]
	 * 
	 * @return
	 */
	@RequestMapping(params = "jformGraphreportItemList")
	public ModelAndView jformGraphreportItemList(JformGraphreportHeadEntity jformGraphreportHead, HttpServletRequest req) {
	
		//===================================================================================
		//????????????
		Object id0 = jformGraphreportHead.getId();
		//===================================================================================
		//??????-??????
	    String hql0 = "from JformGraphreportItemEntity where 1 = 1 AND cGREPORT_HEAD_ID = ? ";
	    try{
	    	List<JformGraphreportItemEntity> jformGraphreportItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("jformGraphreportItemList", jformGraphreportItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("jeecg/cgform/graphreport/jformGraphreportItemList");
	}

	@RequestMapping(params = "exportXls")
	public String  exportXls(JformGraphreportHeadEntity jformGraphreportHead,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap map) {
		CriteriaQuery cq = new CriteriaQuery(JformGraphreportHeadEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformGraphreportHead);

		List<JformGraphreportHeadEntity> dataList= this.jformGraphreportHeadService.getListByCriteriaQuery(cq,false);
		List<JformGraphreportHeadPage> pageList=new ArrayList<JformGraphreportHeadPage>();
		if(dataList!=null&&dataList.size()>0){
			String hql0 = "from JformGraphreportItemEntity where 1 = 1 AND cGREPORT_HEAD_ID = ? ";
			for(JformGraphreportHeadEntity headEntity:dataList){
				List<JformGraphreportItemEntity> itemEntities = systemService.findHql(hql0,headEntity.getId());
				pageList.add(new JformGraphreportHeadPage(itemEntities,headEntity));
			}
		}

		map.put(NormalExcelConstants.FILE_NAME,"????????????");
		map.put(NormalExcelConstants.CLASS,JformGraphreportHeadPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("????????????","????????????"));
		map.put(NormalExcelConstants.DATA_LIST, pageList);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	@RequestMapping(params = "goImportExcel")
	public String goImportExcel(){
		return "jeecg/cgform/graphreport/jformGraphreportUpload";
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
			params.setTitleRows(1);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<JformGraphreportHeadPage> listCourses =  ExcelImportUtil.importExcel(file.getInputStream(), JformGraphreportHeadPage.class, params);
				for(JformGraphreportHeadPage page:listCourses){
					JformGraphreportHeadEntity headEntity=page.getJformGraphreportHeadEntity();
					List<JformGraphreportItemEntity> itemEntities=page.getJformGraphreportItemList();
					if(headEntity!=null&&itemEntities!=null){
						this.jformGraphreportHeadService.addMain(headEntity,itemEntities);
					}
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
