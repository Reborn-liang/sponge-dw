package cn.nearf.dw.indicator.controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSON;

import cn.nearf.dw.common.TypeVO;
import cn.nearf.dw.indicator.entity.DwIndicatorColumnCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorSftpCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorStdCtlEntity;
import cn.nearf.dw.indicator.page.DwIndicatorCtlPage;
import cn.nearf.dw.indicator.service.DwIndicatorCtlServiceI;
import cn.nearf.dw.indicatormodel.entity.DwIndicatorModelCtlEntity;
import cn.nearf.dw.mon.entity.DwIndicatorJobMonitorCtlEntity;
import cn.nearf.dw.mon.entity.DwIndicatorRerunMonitorCtlEntity;
import cn.nearf.ggz.config.ErpConfig;
import cn.nearf.ggz.utils.HttpUtils;
import cn.nearf.ggz.utils.ObjectUtils;
/**   
 * @Title: Controller
 * @Description: dw_indicator_ctl
 * @author onlineGenerator
 * @date 2018-07-16 18:55:11
 * @version V1.0   
 *
 */
@Scope("prototype") 
@Controller
@RequestMapping("/dwIndicatorCtlController")
public class DwIndicatorCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorCtlController.class);

	@Autowired
	private DwIndicatorCtlServiceI dwIndicatorCtlService;
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(params = "effectIndicator")
	@ResponseBody
	public AjaxJson effectIndicator(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = "indicator生效成功";
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
			dwIndicatorCtl = dwIndicatorCtlService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorCtl.getId());
			try {
				dwIndicatorCtlService.effectIndicator(dwIndicatorCtl);
				dwIndicatorCtl.setStatus("A");
				systemService.saveOrUpdate(dwIndicatorCtl);
			} catch (Exception e) {
				message = "indicator生效失败, 原因："+e.getMessage();
			}
		}else {
			message = "indicator生效失败, 未知原因";
		}
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "genStd")
	@ResponseBody
	public AjaxJson genStd(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = "生成标准化配置成功";
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
			dwIndicatorCtl = dwIndicatorCtlService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorCtl.getId());
			
			CriteriaQuery cq = new CriteriaQuery(DwIndicatorStdCtlEntity.class);
			cq.eq("originalTable", dwIndicatorCtl.getTargetTable());
			cq.add();
			List<DwIndicatorStdCtlEntity> stdList = systemService.getListByCriteriaQuery(cq, false);
			if(stdList!=null && stdList.size()>0) {
				j.setMsg("标准化配置已存在");
				return j;
			}
			
			try {
				dwIndicatorCtlService.genStd(dwIndicatorCtl);
			} catch (Exception e) {
				message = "生成标准化配置失败, 原因："+e.getMessage();
			}
		}else {
			message = "生成标准化配置失败, 未知原因";
		}
		j.setMsg(message);
		return j;
	}
	
//	schema
//	tableName
//	modelCode
	@RequestMapping(params = "getTableNameOrColumnFromInfomation")
	@ResponseBody
	public AjaxJson getTableNameOrColumnFromInfomation(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String iiiii = req.getParameter("i");
		String schema = req.getParameter("schema");
		String tableName = req.getParameter("tableName");
		String modelCode = req.getParameter("modelCode");
		logger.info("i:"+iiiii+", schema:"+schema+", tableName:"+tableName+", modelCode:"+modelCode);
		List<TypeVO> voList = null;
		if(StringUtils.isEmpty(tableName)) {
			if(StringUtils.isEmpty(modelCode)) {
				voList = dwIndicatorCtlService.getTableOrColumnsFromInformation(schema, tableName);
			}else {
				Set<String>tables = new HashSet<String>();
				//只取model中有的表
				CriteriaQuery factCq = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
				factCq.eq("modelCode", modelCode);
				factCq.eq("factSchema", schema);
				factCq.add();
				List<DwIndicatorModelCtlEntity> modelsForFact = systemService.getListByCriteriaQuery(factCq, false);
				for (DwIndicatorModelCtlEntity dwIndicatorModelCtlEntity : modelsForFact) {
					String table = dwIndicatorModelCtlEntity.getFactTable();
					tables.add(table);
				}
				
				CriteriaQuery dimCQ = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
				dimCQ.eq("modelCode", modelCode);
				dimCQ.eq("dimSchema", schema);
				dimCQ.add();
				List<DwIndicatorModelCtlEntity> modelsForDim = systemService.getListByCriteriaQuery(dimCQ, false);
				for (DwIndicatorModelCtlEntity dwIndicatorModelCtlEntity : modelsForDim) {
					String table = dwIndicatorModelCtlEntity.getDimTable();
					tables.add(table);
				}
				
				List<TypeVO> returnResult = new ArrayList<TypeVO>();
				for (String table : tables) {
					TypeVO vo = new TypeVO(table, table);
					returnResult.add(vo);
				}
				voList = returnResult;
			}
		}else {
			voList = dwIndicatorCtlService.getTableOrColumnsFromInformation(schema, tableName);
		}
		logger.info("i:"+iiiii+", volist:"+voList);
		j.setObj(voList);
		return j;
	}
	
	
//	schema
//	tableName
//	modelCode
	@RequestMapping(params = "getColumnInfomation")
	@ResponseBody
	public AjaxJson getColumnInfomation(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String schema = req.getParameter("schema");
		String tableName = req.getParameter("tableName");
		String columnName = req.getParameter("columnName");
		TypeVO vo = dwIndicatorCtlService.getColumnInformation(schema, tableName, columnName);
		j.setObj(vo);
		return j;
	}
	
	@RequestMapping(params = "goPreview")
	public ModelAndView goPreview(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
			request.setAttribute("indId", dwIndicatorCtl.getId());
		}
		String id = request.getParameter("id");
		DwIndicatorCtlEntity ind = systemService.getEntity(DwIndicatorCtlEntity.class, Integer.parseInt(id));
		request.setAttribute("fieldNames",  dwIndicatorCtlService.genFieldsOfSubSQL(ind));
		return new ModelAndView("cn/nearf/dw/query/customQueryResult");
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "doPreview")
	public void doPreview(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String id = request.getParameter("id");
		DwIndicatorCtlEntity ind = systemService.getEntity(DwIndicatorCtlEntity.class, Integer.parseInt(id));
		// 查询条件组装器
		try {
//			Long countRes = 10L;
			String selectSubSql = dwIndicatorCtlService.genSelectSubSQL(ind);
			String sql = selectSubSql + " LIMIT 10";
			List<Map<String, Object>> queryRes = dwIndicatorCtlService.previewFromSql(sql);
			dataGrid.setTotal(queryRes.size());
			dataGrid.setResults(queryRes);
		} catch (Exception e) {
			e.printStackTrace();
			dataGrid.setTotal(0);
			dataGrid.setResults(new ArrayList());
		}

		TagUtil.datagrid(response, dataGrid);
	}
	

	/**
	 * dw_indicator_ctl列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorCtlList");
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
	public void datagrid(DwIndicatorCtlEntity dwIndicatorCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorCtlEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorCtl);
		try{
		//自定义追加查询条件
		String query_jobType_begin = request.getParameter("jobType_begin");
		String query_jobType_end = request.getParameter("jobType_end");
		if(StringUtil.isNotEmpty(query_jobType_begin)){
			cq.ge("jobType", Integer.parseInt(query_jobType_begin));
		}
		if(StringUtil.isNotEmpty(query_jobType_end)){
			cq.le("jobType", Integer.parseInt(query_jobType_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 立即执行
	 * @return
	 */
	@RequestMapping(params = "doRerun")
	@ResponseBody
	public AjaxJson doRerun(DwIndicatorJobMonitorCtlEntity dwIndicatorJobMonitorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
//		String domain = ErpConfig.getProperty("BIZ_PROVIDER_DOMAIN");
//		String time = request.getParameter("jobStartTime");
//		String url = String.format("http://%s/view/indicator/rerun?id=%s&start_time=%s", domain, id, time);
//		logger.info("URL:"+url);
//		String jsonResultStr = HttpUtils.fetchHttpUrlResponse(url, null, null);
//		logger.info("RESULT:"+jsonResultStr);
//		try {
//			com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject) JSON.parseObject(jsonResultStr);
//			String msg = result.getString("message");
//			j.setMsg(msg);
//		} catch (Exception e) {
//			e.printStackTrace();
//			j.setMsg("业务服务异常，请联系系统管理员");
//		}
		
		int id = ObjectUtils.getIntValue(request.getParameter("id"));
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
			j.setErrorMsg("立即执行任务添加失败 :" + e.getMessage());
		}
		j.setMsg("立即执行任务已添加到队列，请在重做管理功能中激活此任务");
		
		
		return j;
	}
	
	
	/**
	 * 立即执行
	 * */
	@RequestMapping(params = "goRerun")
	public ModelAndView goRerun(HttpServletRequest req) {
		String id = req.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			DwIndicatorCtlEntity dwIndicatorCtl = dwIndicatorCtlService.getEntity(DwIndicatorCtlEntity.class, Integer.parseInt(id));
			if("SRC".equals(dwIndicatorCtl.getType())){
				req.setAttribute("rerunError", 1);
			}else {
				req.setAttribute("rerunError", 0);
				req.setAttribute("dwIndicatorCtlPage", dwIndicatorCtl);
			}
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorCtl-rerun");
	}

	/**
	 * 删除dw_indicator_ctl
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dwIndicatorCtl = systemService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorCtl.getId());
		String message = "dw_indicator_ctl删除成功";
		try{
			String modelCode = dwIndicatorCtl.getModelCode();
			if (!StringUtils.isEmpty(modelCode)) {
				CriteriaQuery factCq = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
				factCq.eq("modelCode", modelCode);
				factCq.add();
				List<DwIndicatorModelCtlEntity> modelsForFact = systemService.getListByCriteriaQuery(factCq, false);
				if(!modelsForFact.isEmpty()) {
					return j.setErrorMsg("该指标在模型中被依赖，无法删除。");
				}
			}
			
			dwIndicatorCtlService.delMain(dwIndicatorCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_ctl删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除dw_indicator_ctl
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "dw_indicator_ctl删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorCtlEntity dwIndicatorCtl = systemService.getEntity(DwIndicatorCtlEntity.class,
				Integer.parseInt(id)
				);
				dwIndicatorCtlService.delMain(dwIndicatorCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_ctl删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加dw_indicator_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorCtlEntity dwIndicatorCtl,DwIndicatorCtlPage dwIndicatorCtlPage, HttpServletRequest request) {
		List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList =  dwIndicatorCtlPage.getDwIndicatorSftpCtlList();
		List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList =  dwIndicatorCtlPage.getDwIndicatorColumnCtlList();
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try{
			//模型为空
			if(StringUtil.isEmpty(dwIndicatorCtlPage.getModelCode()) && dwIndicatorColumnCtlList != null && !dwIndicatorColumnCtlList.isEmpty()) {
				HashSet<String> tableSet = new HashSet<>();
				for (DwIndicatorColumnCtlEntity dwIndicatorColumnCtlEntity : dwIndicatorColumnCtlList) {
					if (!StringUtils.isEmpty(dwIndicatorColumnCtlEntity.getFromTable())) {
						tableSet.add(dwIndicatorColumnCtlEntity.getFromTable());
					}
				}
				if(tableSet.size() > 1) {
					return j.setErrorMsg("未选取模型时，只能选某一个表。");
				}
			}
			
			if ("SRC".equals(dwIndicatorCtl.getType())) {
				//目录必填，
				if(StringUtil.isEmpty(dwIndicatorCtl.getSourcePath())) {
					return j.setErrorMsg("源表必须指定数据源目录，请检查。");
				}
				if ("delimiter".equals(dwIndicatorCtl.getSourceType())) {
					//如果是delimiter类型的，分隔符必填
					if(StringUtil.isEmpty(dwIndicatorCtl.getDelimiter())) {
						return j.setErrorMsg("分隔符类型的源表，必须指定分隔符，请检查。");
					}
				}
				
			}
			
			if (!"SRC".equals(dwIndicatorCtl.getType()) && !"CALC".equals(dwIndicatorCtl.getType())) {
				String bizKey = dwIndicatorCtl.getBizKey();
				String keys[] = bizKey.split(",");
				for (String key : keys) {
					if("biz_date".equalsIgnoreCase(key)) {
						//biz_date 作为主键时，不用检查下面字段的配置中是否包含biz_date，因为创建时自动会长
						continue;
					}
					boolean found = false;
					for (DwIndicatorColumnCtlEntity col : dwIndicatorColumnCtlList) {
						String colField = col.getIndicatorColumn().trim();
						String checkedField = colField;
						if (colField.toLowerCase().indexOf(" as ") > 0) {
							String []t = colField.toLowerCase().split(" as ");
							if (t != null && t.length > 1) {
								checkedField = t[1];
							}else {
								message = "指标中的字段名含有as关键字，但未指定别名，请检查, 出错的字段为：" + colField;
								return j.setErrorMsg(message);
							}
						}
						if (key.trim().equals(checkedField)) {
							found = true;
							break;
						}
					}
					if(found == false) {
						message = "指标中的业务主键未存在于指标字段中，请检查, 出错的主键为：" + key;
						return j.setErrorMsg(message);
					}
				}
				
				String modelCode = dwIndicatorCtl.getModelCode();
				if (!StringUtils.isEmpty(modelCode)) {
					//检查是否在模型中
					Set<String>tables = new HashSet<String>();
					//只取model中有的表
					CriteriaQuery factCq = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
					factCq.eq("modelCode", modelCode);
					factCq.add();
					List<DwIndicatorModelCtlEntity> modelsForFact = systemService.getListByCriteriaQuery(factCq, false);
					for (DwIndicatorModelCtlEntity dwIndicatorModelCtlEntity : modelsForFact) {
						String table = dwIndicatorModelCtlEntity.getFactTable();
						String dimTable = dwIndicatorModelCtlEntity.getDimTable();
						tables.add(table);
						tables.add(dimTable);
					}
					for (DwIndicatorColumnCtlEntity col : dwIndicatorColumnCtlList) {
						String fromTable = col.getFromTable();
						if(!StringUtils.isEmpty(fromTable)) {
							if(!tables.contains(fromTable)) {
								message = "指标字段的来源表必须是模型中定义的表, 出错的源表名为：" + fromTable;
								return j.setErrorMsg(message);
							}
						}
					}
				}
				
			}
			
			if ("SRC".equals(dwIndicatorCtl.getType()) || "CALC".equals(dwIndicatorCtl.getType())) {
				dwIndicatorCtl.setStatus("A");
			}else {
				if(StringUtils.isEmpty(dwIndicatorCtl.getBizKey())) {
					return j.setErrorMsg("不是源表类型的指标，业务主键不能为空");
				}
				dwIndicatorCtl.setStatus("I");
			}
			dwIndicatorCtlService.addMain(dwIndicatorCtl, dwIndicatorColumnCtlList,dwIndicatorSftpCtlList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_ctl添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 更新dw_indicator_ctl
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorCtlEntity dwIndicatorCtl,DwIndicatorCtlPage dwIndicatorCtlPage, HttpServletRequest request) {
		List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList =  dwIndicatorCtlPage.getDwIndicatorSftpCtlList();
		List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList =  dwIndicatorCtlPage.getDwIndicatorColumnCtlList();
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try{
			
			if("D".equalsIgnoreCase(dwIndicatorCtl.getStatus())) {
				DwIndicatorCtlEntity oldDwIndicatorCtl = systemService.getEntity(DwIndicatorCtlEntity.class,dwIndicatorCtl.getId());
				systemService.evict(oldDwIndicatorCtl);
				if(!"D".equalsIgnoreCase(oldDwIndicatorCtl.getStatus())) {
					//check reference in model
					
					//check reference in relateion
				}
			}
			
			if ("SRC".equals(dwIndicatorCtl.getType())) {
				//目录必填，
				if(StringUtil.isEmpty(dwIndicatorCtl.getSourcePath())) {
					return j.setErrorMsg("源表必须指定数据源目录，请检查。");
				}
				if ("delimiter".equals(dwIndicatorCtl.getSourceType())) {
					//如果是delimiter类型的，分隔符必填
					if(StringUtil.isEmpty(dwIndicatorCtl.getDelimiter())) {
						return j.setErrorMsg("分隔符类型的源表，必须指定分隔符，请检查。");
					}
				}
				
			}
			
			//模型为空
			if(StringUtil.isEmpty(dwIndicatorCtlPage.getModelCode()) && dwIndicatorColumnCtlList != null && !dwIndicatorColumnCtlList.isEmpty()) {
				HashSet<String> tableSet = new HashSet<>();
				for (DwIndicatorColumnCtlEntity dwIndicatorColumnCtlEntity : dwIndicatorColumnCtlList) {
					if (!StringUtils.isEmpty(dwIndicatorColumnCtlEntity.getFromTable())) {
						tableSet.add(dwIndicatorColumnCtlEntity.getFromTable());
					}
				}
				if(tableSet.size() > 1) {
					return j.setErrorMsg("未选取模型时，只能选某一个表。");
				}
			}
			
			if (!"SRC".equals(dwIndicatorCtl.getType()) && !"CALC".equals(dwIndicatorCtl.getType())) {
				String bizKey = dwIndicatorCtl.getBizKey();
				String keys[] = bizKey.split(",");
				for (String key : keys) {
					if("biz_date".equalsIgnoreCase(key)) {
						//biz_date 作为主键时，不用检查下面字段的配置中是否包含biz_date，因为创建时自动会长
						continue;
					}
					boolean found = false;
					for (DwIndicatorColumnCtlEntity col : dwIndicatorColumnCtlList) {
						String colField = col.getIndicatorColumn().trim();
						String checkedField = colField;
						if (colField.toLowerCase().indexOf(" as ") > 0) {
							String []t = colField.toLowerCase().split(" as ");
							if (t != null && t.length > 1) {
								checkedField = t[1];
							}else {
								message = "指标中的字段名含有as关键字，但未指定别名，请检查, 出错的字段为：" + colField;
								return j.setErrorMsg(message);
							}
						}
						if (key.trim().equals(checkedField)) {
							found = true;
							break;
						}
					}
					if(found == false) {
						message = "指标中的业务主键未存在于指标字段中，请检查, 出错的主键为：" + key;
						return j.setErrorMsg(message);
					}
				}
				
				String modelCode = dwIndicatorCtl.getModelCode();
				if (!StringUtils.isEmpty(modelCode)) {
					//检查是否在模型中
					Set<String>tables = new HashSet<String>();
					//只取model中有的表
					CriteriaQuery factCq = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
					factCq.eq("modelCode", modelCode);
					factCq.add();
					List<DwIndicatorModelCtlEntity> modelsForFact = systemService.getListByCriteriaQuery(factCq, false);
					for (DwIndicatorModelCtlEntity dwIndicatorModelCtlEntity : modelsForFact) {
						String table = dwIndicatorModelCtlEntity.getFactTable();
						String dimTable = dwIndicatorModelCtlEntity.getDimTable();
						tables.add(table);
						tables.add(dimTable);
					}
					for (DwIndicatorColumnCtlEntity col : dwIndicatorColumnCtlList) {
						String fromTable = col.getFromTable();
						if(!StringUtils.isEmpty(fromTable)) {
							if(!tables.contains(fromTable)) {
								message = "指标字段的来源表必须是模型中定义的表, 出错的源表名为：" + fromTable;
								return j.setErrorMsg(message);
							}
						}
					}
				}
			}
			dwIndicatorCtlService.updateMain(dwIndicatorCtl, dwIndicatorColumnCtlList, dwIndicatorSftpCtlList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新指标定义失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * dw_indicator_ctl新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
			dwIndicatorCtl = dwIndicatorCtlService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorCtl.getId());
			req.setAttribute("dwIndicatorCtlPage", dwIndicatorCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorCtl-add");
	}
	/**
	 * 过滤条件窗口跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goFilter")
	public ModelAndView goFilter(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		String filter = req.getParameter("filter");
		req.setAttribute("filter", filter);
		String row = req.getParameter("row");
		req.setAttribute("row", row);
		String column = req.getParameter("column");
		req.setAttribute("column", column);
		String columnName = req.getParameter("columnName");
		req.setAttribute("columnName", columnName);
		return new ModelAndView("cn/nearf/dw/indicator/myform");
	}
	
	/**
	 * dw_indicator_ctl编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
			dwIndicatorCtl = dwIndicatorCtlService.getEntity(DwIndicatorCtlEntity.class, dwIndicatorCtl.getId());
			req.setAttribute("dwIndicatorCtlPage", dwIndicatorCtl);
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorCtl-update");
	}
	
	
	/**
	 * 加载明细列表[dw_indicator_ctl]
	 * 
	 * @return
	 */
	@RequestMapping(params = "dwIndicatorColumnCtlList")
	public ModelAndView dwIndicatorColumnCtlList(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
	
		//===================================================================================
		//获取参数
		Object id0 = dwIndicatorCtl.getId();
		//===================================================================================
		//查询-dw_indicator_ctl
	    String hql0 = "from DwIndicatorColumnCtlEntity where 1 = 1 AND indicatorId = ? ";
	    try{
	    	List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("dwIndicatorColumnCtlList", dwIndicatorColumnCtlEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorColumnCtlList");
	}
	/**
	 * 加载明细列表[dw_indicator_ctl]
	 * 
	 * @return
	 */
	@RequestMapping(params = "dwIndicatorSftpCtlList")
	public ModelAndView dwIndicatorSftpCtlList(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
	
		//===================================================================================
		//获取参数
		Object id1 = dwIndicatorCtl.getId();
		String sfyn = req.getParameter("sftpFlg");
		//===================================================================================
		//查询-dw_indicator_ctl
	    String hql1 = "from DwIndicatorSftpCtlEntity where 1 = 1 AND indicatorId = ? ";
	    try{
	    	List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlEntityList = systemService.findHql(hql1,id1);
			req.setAttribute("dwIndicatorSftpCtlList", dwIndicatorSftpCtlEntityList);
			req.setAttribute("sfyn", sfyn);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorSftpCtlList");
	}
	
	@RequestMapping(params = "parseSQL")
	public AjaxJson parseSQL(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
//			解析SQL：/view/indicator/parse?id=<indicator_id>
			System.out.println("dwIndicatorCtl.getId():"+dwIndicatorCtl.getId());
			
			String domain = ErpConfig.getProperty("BIZ_PROVIDER_DOMAIN");
			String url = String.format("http://%s/view/indicator/parse?id=%s", domain, dwIndicatorCtl.getId());
			logger.info("URL:"+url);
			try {
				String jsonResultStr = HttpUtils.fetchHttpUrlResponse(url, null, null);
				logger.info("RESULT:"+jsonResultStr);
				com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject) JSON.parseObject(jsonResultStr);
				int code = result.getInteger("status");
				if (code < 0) {
					String msg = result.getString("message");
					req.setAttribute("errorMsg", msg);
				} else {
					req.setAttribute("uri", "/export_" + dwIndicatorCtl.getId() + ".zip");
				}
				j.setMsg("解析成功");
			} catch (Exception e) {
				e.printStackTrace();
				j.setMsg("业务服务异常，请联系系统管理员");
			}
		}else {
			j.setMsg("解析失败，ID为空，请检查");
		}
		
		return j;
	}
	
	
	@RequestMapping(params = "goExport")
	public ModelAndView goExport(DwIndicatorCtlEntity dwIndicatorCtl, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(dwIndicatorCtl.getId())) {
//			导入指标：/view/indicator/import?path=<file_path>
//			导出指标：/view/indicator/export?id=<indicator_id>
			
			System.out.println("dwIndicatorCtl.getId():"+dwIndicatorCtl.getId());
			
			String domain = ErpConfig.getProperty("BIZ_PROVIDER_DOMAIN");
			String url = String.format("http://%s/view/indicator/export?id=%s", domain, dwIndicatorCtl.getId());
			logger.info("URL:"+url);
			try {
				String jsonResultStr = HttpUtils.fetchHttpUrlResponse(url, null, null);
				logger.info("RESULT:"+jsonResultStr);
				com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject) JSON.parseObject(jsonResultStr);
				int code = result.getInteger("status");
				if (code < 0) {
					String msg = result.getString("message");
					req.setAttribute("errorMsg", msg);
				} else {
					req.setAttribute("uri", "/export_" + dwIndicatorCtl.getId() + ".zip");
				}
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("errorMsg", "业务服务异常，请联系系统管理员");
			}
		}
		
		return new ModelAndView("cn/nearf/dw/indicator/dwIndicatorCtl-export");
	}
	
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			String path = this.saveFile(file);

			String domain = ErpConfig.getProperty("BIZ_PROVIDER_DOMAIN");
			String url = String.format("http://%s/view/indicator/import?path=%s", domain, path);
			logger.info("URL:" + url);
			try {
				String jsonResultStr = HttpUtils.fetchHttpUrlResponse(url, null, null);
				logger.info("RESULT:" + jsonResultStr);
				com.alibaba.fastjson.JSONObject result = (com.alibaba.fastjson.JSONObject) JSON.parseObject(jsonResultStr);
				int code = result.getInteger("status");
				if (code < 0) {
					String msg = result.getString("message");
					request.setAttribute("errorMsg", msg);
					j.setMsg("导入失败，原因：" + msg);
				} else {
					j.setMsg("导入成功！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				j.setMsg("导入失败，原因：" + e.getMessage());
			}
		}
		return j;
	}
	
	private String saveFile(MultipartFile file) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 转存文件
				String uploadBasePath = ErpConfig.getProperty("BIZ_UPLOAD_PATH");
				String filePath=uploadBasePath + file.getName();
				File newFile = new File(filePath);
				if(newFile.exists()) {
					newFile.delete();
				}
				file.transferTo(newFile);
				return filePath;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

    /**
    * 导出excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(DwIndicatorCtlEntity dwIndicatorCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(DwIndicatorCtlEntity.class, dataGrid);
    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorCtl);
    	try{
    	//自定义追加查询条件
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<DwIndicatorCtlEntity> list=this.dwIndicatorCtlService.getListByCriteriaQuery(cq, false);
    	List<DwIndicatorCtlPage> pageList=new ArrayList<DwIndicatorCtlPage>();
        if(list!=null&&list.size()>0){
        	for(DwIndicatorCtlEntity entity:list){
        		try{
        		DwIndicatorCtlPage page=new DwIndicatorCtlPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
            	    Object id0 = entity.getId();
				    String hql0 = "from DwIndicatorColumnCtlEntity where 1 = 1 AND indicatorId = ? ";
        	        List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlEntityList = systemService.findHql(hql0,id0);
            		page.setDwIndicatorColumnCtlList(dwIndicatorColumnCtlEntityList);
            	    Object id1 = entity.getId();
				    String hql1 = "from DwIndicatorSftpCtlEntity where 1 = 1 AND indicatorId = ? ";
        	        List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlEntityList = systemService.findHql(hql1,id1);
            		page.setDwIndicatorSftpCtlList(dwIndicatorSftpCtlEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"dw_indicator_ctl");
        map.put(NormalExcelConstants.CLASS,DwIndicatorCtlPage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_ctl列表", "导出人:Jeecg",
            "导出信息"));
        map.put(NormalExcelConstants.DATA_LIST,pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	* 导出excel 使模板
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"dw_indicator_ctl");
		map.put(NormalExcelConstants.CLASS,DwIndicatorCtlPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_ctl列表", "导出人:"+ ResourceUtil.getSessionUserName().getRealName(),
		"导出信息"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	* 导入功能跳转
	*
	* @return
	*/
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "dwIndicatorCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

}
