package cn.nearf.dw.relation.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.web.system.pojo.base.TSType;
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
import com.alibaba.fastjson.JSONArray;

import cn.nearf.dw.common.TypeVO;
import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.relation.entity.DwIndicatorRelationCtlEntity;
import cn.nearf.dw.relation.service.DwIndicatorRelationCtlServiceI;
import cn.nearf.ggz.utils.ObjectUtils;



/**   
 * @Title: Controller
 * @Description: dw_indicator_relation_ctl
 * @author onlineGenerator
 * @date 2018-07-24 15:15:29
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/dwIndicatorRelationCtlController")
public class DwIndicatorRelationCtlController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DwIndicatorRelationCtlController.class);

	@Autowired
	private DwIndicatorRelationCtlServiceI dwIndicatorRelationCtlService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@RequestMapping(params = "getRelationType")
	@ResponseBody
	public List<TypeVO> getRelationType(HttpServletRequest req) {
		List<TypeVO> type=  new ArrayList<TypeVO>();
		int id = ObjectUtils.getIntValue(req.getParameter("id"));
		DwIndicatorCtlEntity dwIndicatorCtl = systemService.getEntity(DwIndicatorCtlEntity.class, id);
		String hql = "FROM TSType WHERE typecode=?";
		List<TSType> types = systemService.findHql(hql, dwIndicatorCtl.getType());
		for (TSType t : types) {
			TypeVO vo = new TypeVO(t.getTypecode(), t.getTypename());
			type.add(vo);
		}
		return type;
	}
	
	@RequestMapping(params = "relationTypeList")
	@ResponseBody
	public List<TypeVO> relationTypeList(HttpServletRequest req) {
		List<TypeVO> type=  new ArrayList<TypeVO>();
		String hql = "FROM TSType WHERE typegroupid='ff808081648ed65d01648eff7b9c0087'";
		List<TSType> types = systemService.findByQueryString(hql);
		for (TSType t : types) {
			TypeVO vo = new TypeVO(t.getTypecode(), t.getTypename());
			type.add(vo);
		}
		return type;
	}
	
	@RequestMapping(params = "indicatorList")
	@ResponseBody
	public List<DwIndicatorCtlEntity> indicatorList(HttpServletRequest req){
		List<DwIndicatorCtlEntity> relations = null;
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorCtlEntity.class);
		cq.add();
		relations = systemService.getListByCriteriaQuery(cq, false);
		if(relations==null) {
			relations = new ArrayList<DwIndicatorCtlEntity>();
		}
		return relations;
	}
	/**
	 * 指标关系列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/relation/dwIndicatorRelationCtlList");
	}
	/**
	 * 指标关系管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "treeList")
	public ModelAndView treeList(HttpServletRequest request) {
		return new ModelAndView("cn/nearf/dw/relation/dwIndicatorRelationCtlTreeList");
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
	public void datagrid(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorRelationCtlEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorRelationCtl, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.dwIndicatorRelationCtlService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	private boolean checkHasChild(int indicatorId) {
		String sql = "select count(id) from dw_indicator_relation_ctl WHERE parent_indicator_id=? ";
		Long count = systemService.getCountForJdbcParam(sql, new Object[] {indicatorId});
		return ObjectUtils.getLongValue(count) > 0;
	}
	
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridTree")
	@ResponseBody
	public Object datagridTree(HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		// 查出所有的父菜单
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		//获取第一个父列表
		if (treegrid != null && treegrid.getId() != null) {
			System.out.println("parent id: treegrid.getId():" + treegrid.getId());
			String sql = "select r.*, i.name as iname, ip.name as pname from dw_indicator_relation_ctl as r \n" + 
					"left join dw_indicator_ctl as i on r.indicator_id = i.id \n" + 
					"left join dw_indicator_ctl as ip on r.parent_indicator_id = ip.id \n" + 
					"WHERE r.parent_indicator_id = ? ";
			List<Map<String, Object>> list = systemService.findForJdbc(sql, treegrid.getId().split("_")[0]);
		   for (Map<String, Object> map : list) {
			   boolean hasChild = checkHasChild(ObjectUtils.getIntValue(map.get("indicator_id")));
			   	TreeGrid tg= new TreeGrid();
			   	tg.setId(map.get("indicator_id") + "_" + UUID.randomUUID().toString().replace("-", ""));
			   	tg.setCode(map.get("indicator_id")+"");
			   	tg.setText(map.get("indicator_id")+"");
				Map<String, Object> fieldMap = new HashMap<String, Object>();
				fieldMap.put("rid", map.get("id")+"");
				tg.setSrc(map.get("id")+"");
				fieldMap.put("pid", map.get("indicator_id")+"");
				fieldMap.put("allowDel", hasChild?"N":"Y");
				fieldMap.put("allowEdit", "Y");
				fieldMap.put("couplingFlg", "Y".equals(map.get("coupling_flg"))?"是":"否");
				tg.setFieldMap(fieldMap);
				tg.setState(hasChild?"closed":"open");
				treeGrids.add(tg);
		   }
		} else {
			String s = "select distinct(parent_indicator_id) as parent_indicator_id,id from dw_indicator_relation_ctl" + 
					" where parent_indicator_id not in" + 
					" (select indicator_id from dw_indicator_relation_ctl)";
			List<Map<String, Object>> list = systemService.findForJdbc(s);
			HashMap<Integer, Integer> countMap = new HashMap<>();
			
			for ( Map<String, Object> map : list) {
				Integer pid = (Integer) map.get("parent_indicator_id");
				Integer count = countMap.get(pid);
				if(count == null) {
					count = 1;
				}else {
					count++;
				}
				countMap.put(pid, count);
			}
			HashSet<Integer> existSet = new HashSet<>();
			for ( Map<String, Object> map : list) {
				//第一层父节点
				Integer pid = (Integer) map.get("parent_indicator_id");
				if(existSet.contains(pid)) {
					continue;
				}
				existSet.add(pid);
				
				TreeGrid tg= new TreeGrid();
				tg.setId(map.get("parent_indicator_id") + "_" + UUID.randomUUID().toString().replace("-", ""));
				tg.setCode(map.get("parent_indicator_id")+"");
				tg.setText(map.get("parent_indicator_id")+"");
				Map<String, Object> fieldMap = new HashMap<String, Object>();
				fieldMap.put("pid", map.get("parent_indicator_id")+"");
				fieldMap.put("rid", countMap.get(pid) > 1 ? "0" : (map.get("id")+""));
				tg.setSrc(countMap.get(pid) > 1 ? "0" : (map.get("id")+""));
				tg.setFieldMap(fieldMap);
				tg.setState("closed");
				treeGrids.add(tg);
			}
		}
        JSONArray jsonArray = new JSONArray();
        for (TreeGrid treeGrid : treeGrids) {
            jsonArray.add(JSON.parse(treeGrid.toJson()));
        }	
        return jsonArray;
	}
	/**
	 * 删除指标关系
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标关系删除成功";
		try{
			String rid = request.getParameter("id");
			DwIndicatorRelationCtlEntity dwIndicatorRelationCtl = systemService.getEntity(DwIndicatorRelationCtlEntity.class, Integer.parseInt(rid));
			dwIndicatorRelationCtlService.delete(dwIndicatorRelationCtl);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标关系删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除dw_indicator_relation_ctl
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "dw_indicator_relation_ctl删除成功";
		try{
			for(String id:ids.split(",")){
				DwIndicatorRelationCtlEntity dwIndicatorRelationCtl = systemService.getEntity(DwIndicatorRelationCtlEntity.class, 
				Integer.parseInt(id)
				);
				dwIndicatorRelationCtlService.delete(dwIndicatorRelationCtl);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "dw_indicator_relation_ctl删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	 private boolean checkCircle(int checkId, int pid) {
		 List<DwIndicatorRelationCtlEntity> dwRelations = systemService.findByProperty(DwIndicatorRelationCtlEntity.class, "indicatorId", pid);
		if (dwRelations == null || dwRelations.size() == 0) {
			// pass
			return false;
		} else {
			for (DwIndicatorRelationCtlEntity dwIndicatorRelationCtlEntity : dwRelations) {
				if(dwIndicatorRelationCtlEntity.getParentIndicatorId().intValue() == checkId) {
					return true;
				}else {
					return checkCircle(checkId, dwIndicatorRelationCtlEntity.getParentIndicatorId());
				}
			}
		}
		return false;
	 }

	/**
	 * 添加指标关系
	 * 
	 * @param ids
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl, HttpServletRequest request) throws ParseException {
		AjaxJson j = new AjaxJson();
		message = "指标关系添加成功";
		dwIndicatorRelationCtl.setCreateDate(new Date());
		
		if(dwIndicatorRelationCtl.getIndicatorId().intValue() == dwIndicatorRelationCtl.getParentIndicatorId().intValue()) {
			return j.setErrorMsg("指标关系不正确，指标自身不能依赖自身，请检查");
		}
		
		boolean isCircleFlag = false;
		isCircleFlag = checkCircle(dwIndicatorRelationCtl.getIndicatorId(), dwIndicatorRelationCtl.getParentIndicatorId());
		if(isCircleFlag) {
			return j.setErrorMsg("新加入的指标关系形成了环路，请检查");
		}

		try{
			dwIndicatorRelationCtlService.save(dwIndicatorRelationCtl);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "指标关系添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新指标关系
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "指标关系更新成功";
		DwIndicatorRelationCtlEntity t = dwIndicatorRelationCtlService.get(DwIndicatorRelationCtlEntity.class, dwIndicatorRelationCtl.getId());
		
		if(dwIndicatorRelationCtl.getIndicatorId().intValue() == dwIndicatorRelationCtl.getParentIndicatorId().intValue()) {
			return j.setErrorMsg("指标关系不正确，指标自身不能依赖自身，请检查");
		}
		
		boolean isCircleFlag = false;
		isCircleFlag = checkCircle(dwIndicatorRelationCtl.getIndicatorId(), dwIndicatorRelationCtl.getParentIndicatorId());
		if(isCircleFlag) {
			return j.setErrorMsg("编辑的指标关系形成了环路，请检查");
		}
		
		try {
			MyBeanUtils.copyBeanNotNull2Bean(dwIndicatorRelationCtl, t);
			dwIndicatorRelationCtlService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "指标关系更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 指标关系新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl, HttpServletRequest req) {
		return new ModelAndView("cn/nearf/dw/relation/dwIndicatorRelationCtl-add");
	}
	/**
	 * 指标关系编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(HttpServletRequest req) {
		String rid = req.getParameter("rid");
		if (StringUtil.isNotEmpty(rid)) {
			DwIndicatorRelationCtlEntity dwIndicatorRelationCtl = dwIndicatorRelationCtlService.getEntity(DwIndicatorRelationCtlEntity.class, ObjectUtils.getIntValue(rid));
			req.setAttribute("dwIndicatorRelationCtlPage", dwIndicatorRelationCtl);
			if(ObjectUtils.getIntValue(rid) == 0) {
				req.setAttribute("error", "被多个指标依赖，请选择子指标进行编辑");
			}
		}
		return new ModelAndView("cn/nearf/dw/relation/dwIndicatorRelationCtl-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","dwIndicatorRelationCtlController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorRelationCtlEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, dwIndicatorRelationCtl, request.getParameterMap());
		List<DwIndicatorRelationCtlEntity> dwIndicatorRelationCtls = this.dwIndicatorRelationCtlService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_relation_ctl");
		modelMap.put(NormalExcelConstants.CLASS,DwIndicatorRelationCtlEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_relation_ctl列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,dwIndicatorRelationCtls);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DwIndicatorRelationCtlEntity dwIndicatorRelationCtl,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"dw_indicator_relation_ctl");
    	modelMap.put(NormalExcelConstants.CLASS,DwIndicatorRelationCtlEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("dw_indicator_relation_ctl列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<DwIndicatorRelationCtlEntity> listDwIndicatorRelationCtlEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DwIndicatorRelationCtlEntity.class,params);
				for (DwIndicatorRelationCtlEntity dwIndicatorRelationCtl : listDwIndicatorRelationCtlEntitys) {
					dwIndicatorRelationCtlService.save(dwIndicatorRelationCtl);
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
	
	@RequestMapping(params = "combotree")
	@ResponseBody
	public List<ComboTree> combotree(String selfCode, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorRelationCtlEntity.class);
		if (StringUtils.isNotEmpty(comboTree.getId())) {
			cq.createAlias("parent", "parent");
			cq.eq("parent.id", cn.nearf.ggz.utils.ObjectUtils.getIntValue(comboTree.getId()));
		} else if (StringUtils.isNotEmpty(selfCode)) {
			cq.eq("id", selfCode);
		} else {
			cq.isNull("parent");
		}
		cq.add();
		List<DwIndicatorRelationCtlEntity> relationList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "indicatorId", "list");
		comboTrees = systemService.ComboTree(relationList, comboTreeModel, null, false);
		MutiLangUtil.setMutiTree(comboTrees);
		return comboTrees;
	}
}
