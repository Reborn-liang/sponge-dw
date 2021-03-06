package org.jeecgframework.web.cgform.controller.autolist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jeecgframework.web.cgform.common.CgAutoListConstant;
import org.jeecgframework.web.cgform.engine.FreemarkerHelper;
import org.jeecgframework.web.cgform.entity.config.CgFormFieldEntity;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.entity.template.CgformTemplateEntity;
import org.jeecgframework.web.cgform.service.autolist.CgTableServiceI;
import org.jeecgframework.web.cgform.service.autolist.ConfigServiceI;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.service.template.CgformTemplateServiceI;
import org.jeecgframework.web.cgform.util.QueryParamUtil;
import org.jeecgframework.web.cgform.util.TemplateUtil;
import org.jeecgframework.web.system.pojo.base.DictEntity;
import org.jeecgframework.web.system.pojo.base.TSOperation;
import org.jeecgframework.web.system.service.SystemService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.enums.SysThemesEnum;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.JeecgDataAutorUtils;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.SysThemesUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @Title:CgAutoListController
 * @description:?????????????????????[?????????????????????????????????????????????????????????]
 * @author ?????????
 * @date Jul 5, 2013 2:55:36 PM
 * @version V1.0
 */
@Controller
@RequestMapping("/cgAutoListController")
public class CgAutoListController extends BaseController{
	@Autowired
	private ConfigServiceI configService;
	@Autowired
	private CgTableServiceI cgTableService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	@Autowired
	private CgformTemplateServiceI cgformTemplateService;
	private static Logger log = Logger.getLogger(CgAutoListController.class);
	/**
	 * ????????????????????????
	 * @param id ????????????ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "list")
	public void list(String id, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		//step.1 ??????????????????????????????????????????
		String jversion = cgFormFieldService.getCgFormVersionByTableName(id);
		Map<String, Object> configs = configService.queryConfigs(id,jversion);
		//step.2 ????????????ftl????????????
		FreemarkerHelper viewEngine = new FreemarkerHelper();
		Map<String, Object> paras = new HashMap<String, Object>();
		//step.3 ??????????????????
		loadVars(configs,paras,request);
		//step.4 ????????????+?????????????????????????????????
		String template=request.getParameter("olstylecode");
		if(StringUtils.isBlank(template)){
				CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(id);
				template=head.getFormTemplate();
			paras.put("_olstylecode","");
		}else{
			paras.put("_olstylecode",template);
		}
        paras.put("this_olstylecode",template);
		CgformTemplateEntity entity=cgformTemplateService.findByCode(template);
		String html = viewEngine.parseTemplate(TemplateUtil.getTempletPath(entity,0, TemplateUtil.TemplateType.LIST), paras);
		try {
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter writer = response.getWriter();
			writer.println(html);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.debug("???????????????????????????"+(end-start)+" ms");
	}

	/**
	 * ????????????????????????
	 * @param configId ??????id ????????????id??????????????????????????????
	 * @param page ????????????
	 * @param rows ????????????
	 * @param request 
	 * @param response
	 * @param dataGrid
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(String configId,String page,String field,String rows,String sort,String order, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		Object dataRuleSql =JeecgDataAutorUtils.loadDataSearchConditonSQLString(); //request.getAttribute(Globals.MENU_DATA_AUTHOR_RULE_SQL);
		long start = System.currentTimeMillis();
		//step.1 ??????????????????
		String jversion = cgFormFieldService.getCgFormVersionByTableName(configId);
		Map<String, Object>  configs = configService.queryConfigs(configId,jversion);
		String table = (String) configs.get(CgAutoListConstant.TABLENAME);
		Map params =  new HashMap<String,Object>();
		//step.2 ???????????????????????????
		List<CgFormFieldEntity> beans = (List<CgFormFieldEntity>) configs.get(CgAutoListConstant.FILEDS);
		Map<String, String[]> fieldMap = new HashMap<String, String[]>();
		for(CgFormFieldEntity b:beans){
			QueryParamUtil.loadQueryParams(request,b,params);
			fieldMap.put(b.getFieldName(), new String[]{b.getType(), b.getFieldDefault()});
		}
		//????????????
		boolean isTree = configs.get(CgAutoListConstant.CONFIG_ISTREE) == null ? false
				: CgAutoListConstant.BOOL_TRUE.equalsIgnoreCase(configs.get(CgAutoListConstant.CONFIG_ISTREE).toString());
		String treeId = request.getParameter("id");
		String parentIdFieldName = null;
		String parentIdDefault = null;
		String parentIdFieldType = null;
		if(isTree) {
			parentIdFieldName = configs.get(CgAutoListConstant.TREE_PARENTID_FIELDNAME).toString();
		    parentIdFieldType = fieldMap.get(parentIdFieldName)[0];
			parentIdDefault = fieldMap.get(parentIdFieldName)[1];
			if("null".equalsIgnoreCase(parentIdDefault)) {
				parentIdDefault = null;
			}
			if(treeId == null) {
				treeId = parentIdDefault;
			}else {
				if(parentIdFieldType.equalsIgnoreCase(CgAutoListConstant.TYPE_STRING)) {
					treeId = "'" + treeId + "'";
				}
			}
			if(treeId == null) {
				params.put(parentIdFieldName, " is null");
			}else {
				params.put(parentIdFieldName, "=" + treeId);
			}
		}
		
		int p = page==null?1:Integer.parseInt(page);
		int r = rows==null?99999:Integer.parseInt(rows);
		//step.3 ????????????????????????????????????tree????????????????????????????????????
		List<Map<String, Object>> result = null;
		if(isTree && treeId !=null) {
			//???????????????????????????????????????500???
			result=cgTableService.querySingle(table, field.toString(), params,sort,order, 1, 500);
		}else {
			result=cgTableService.querySingle(table, field.toString(), params,sort,order, p,r );
		}
		
		//treeform ???????????????????????????
		if(isTree) {
			cgTableService.treeFromResultHandle(table, parentIdFieldName, parentIdFieldType,
					result);
		}
		
		//????????????????????????checkbox????????????code??????????????????text?????????
		Map<String, Object> dicMap = new HashMap<String, Object>();
		for(CgFormFieldEntity b:beans){
			loadDic(dicMap, b);
			List<DictEntity> dicList = (List<DictEntity>)dicMap.get(CgAutoListConstant.FIELD_DICTLIST);
			if(dicList.size() > 0){
				for(Map<String, Object> resultMap:result){
					StringBuffer sb = new StringBuffer();
					String value = (String)resultMap.get(b.getFieldName());
					if(oConvertUtils.isEmpty(value)){continue;}
					String[] arrayVal = value.split(",");
					if(arrayVal.length > 1){
						for(String val:arrayVal){
							for(DictEntity dictEntity:dicList){
								if(val.equals(dictEntity.getTypecode())){
									sb.append(dictEntity.getTypename());
									sb.append(",");
								}
								
							}
						}
						resultMap.put(b.getFieldName(), sb.toString().substring(0, sb.toString().length()-1));
					}
					
				}
			}
		}
		Long size = cgTableService.getQuerySingleSize(table, field, params);
		dealDic(result,beans);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			if(isTree && treeId !=null) {
				//????????????
				writer.println(QueryParamUtil.getJson(result));
			}else {
				writer.println(QueryParamUtil.getJson(result,size));
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.debug("???????????????????????????"+(end-start)+" ms");
	}
	
	/**
	 * ??????????????????
	 * @param result ??????????????????
	 * @param beans ????????????
	 */
	@SuppressWarnings("unchecked")
	private void dealDic(List<Map<String, Object>> result,
			List<CgFormFieldEntity> beans) {
		for(CgFormFieldEntity bean:beans){
			String dicTable = bean.getDictTable();//??????Table
			String dicCode = bean.getDictField();//??????Code
			String dicText= bean.getDictText();//??????text
			if(StringUtil.isEmpty(dicTable)&& StringUtil.isEmpty(dicCode)){
				//?????????????????????
				continue;
			}else{
				if(!bean.getShowType().equals("popup")){
					List<DictEntity> dicDataList = queryDic(dicTable, dicCode,dicText);
					for(Map r:result){
						String value = String.valueOf(r.get(bean.getFieldName()));
//						for(Map m:dicDatas){
//							String typecode = String.valueOf(m.get("typecode"));
//							String typename = String.valueOf(m.get("typename"));
//							if(value.equalsIgnoreCase(typecode)){
//								r.put(bean.getFieldName(),typename);
//							}
//						}
						for(DictEntity dictEntity:dicDataList){
							if(value.equalsIgnoreCase(dictEntity.getTypecode())){
								r.put(bean.getFieldName(),MutiLangUtil.getMutiLangInstance().getLang(dictEntity.getTypename()));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * ???????????????
	 * @param configId ??????id
	 * @param id ??????
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(String configId,String id,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String jversion = cgFormFieldService.getCgFormVersionByTableName(configId);
		String table = (String) configService.queryConfigs(configId,jversion).get(CgAutoListConstant.TABLENAME);
		cgTableService.delete(table, id);
		String message = "????????????";
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}
	/**
	 * ???????????????-??????
	 * @param configId ??????id
	 * @param id ??????
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delBatch")
	@ResponseBody
	public AjaxJson delBatch(String configId,String ids,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String jversion = cgFormFieldService.getCgFormVersionByTableName(configId);
		String table = (String) configService.queryConfigs(configId,jversion).get(CgAutoListConstant.TABLENAME);
		String message = "????????????";
		try {
			String[] id = ids.split(",");
			cgTableService.deleteBatch(table, id);
		} catch (Exception e) {
			message = e.getMessage();
		}
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????????????????ftl????????????
	 * @param configs ?????????????????????????????????
	 * @param paras ?????????ftl?????????????????????configs?????????????????????
	 * @param request 
	 * @return ?????????ftl?????????????????????????????????paras??????????????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	private Map loadVars(Map<String, Object> configs,Map<String, Object> paras, HttpServletRequest request) {
		paras.putAll(configs);
		List<Map> fieldList = new ArrayList<Map>();
		List<Map> queryList = new ArrayList<Map>();
		StringBuilder fileds = new StringBuilder();
		StringBuilder initQuery = new StringBuilder();
		Set<String> operationCodes = (Set<String>) request.getAttribute(Globals.OPERATIONCODES);
		Map<String,TSOperation> operationCodesMap = new HashMap<String, TSOperation>();
		if(operationCodes != null){
			TSOperation tsOperation;
			for (String id : operationCodes) {
				tsOperation = systemService.getEntity(TSOperation.class, id);
				if(tsOperation != null && tsOperation.getOperationType() == 0 && tsOperation.getStatus() == 0){
					operationCodesMap.put(tsOperation.getOperationcode(), tsOperation);
				}
			}
		}
		for (CgFormFieldEntity bean : (List<CgFormFieldEntity>) configs.get(CgAutoListConstant.FILEDS)) {
			if(operationCodesMap.containsKey(bean.getFieldName())) {
				continue;
			}
			Map fm = new HashMap<String, Object>();
			fm.put(CgAutoListConstant.FILED_ID, bean.getFieldName());
			fm.put(CgAutoListConstant.FIELD_TITLE, bean.getContent());
			String isShowList = bean.getIsShowList();
			if(StringUtil.isEmpty(isShowList)){
				if("id".equalsIgnoreCase(bean.getFieldName())){
					isShowList = CgAutoListConstant.BOOL_FALSE;
				}else{
					isShowList = CgAutoListConstant.BOOL_TRUE;
				}
			}
			fm.put(CgAutoListConstant.FIELD_ISSHOW, isShowList);
			fm.put(CgAutoListConstant.FIELD_QUERYMODE, bean.getQueryMode());
			fm.put(CgAutoListConstant.FIELD_SHOWTYPE, bean.getShowType());
			fm.put(CgAutoListConstant.FIELD_TYPE, bean.getType());
			fm.put(CgAutoListConstant.FIELD_LENGTH, bean.getFieldLength()==null?"120":bean.getFieldLength());
			fm.put(CgAutoListConstant.FIELD_HREF, bean.getFieldHref()==null?"":bean.getFieldHref());
			loadDic(fm,bean);
			fieldList.add(fm);
			if (CgAutoListConstant.BOOL_TRUE.equals(bean.getIsQuery())) {
				Map fmq = new HashMap<String, Object>();
				fmq.put(CgAutoListConstant.FILED_ID, bean.getFieldName());
				fmq.put(CgAutoListConstant.FIELD_TITLE, bean.getContent());
				fmq.put(CgAutoListConstant.FIELD_QUERYMODE, bean.getQueryMode());
				fmq.put(CgAutoListConstant.FIELD_TYPE, bean.getType());
				fmq.put(CgAutoListConstant.FIELD_SHOWTYPE, bean.getShowType());
				fmq.put(CgAutoListConstant.FIELD_DICTFIELD, bean.getDictField());
				fmq.put(CgAutoListConstant.FIELD_DICTTABLE, bean.getDictTable());
				fmq.put(CgAutoListConstant.FIELD_ISQUERY,"Y");
				loadDefaultValue(fmq,bean,request);
				loadDic(fmq,bean);
				queryList.add(fmq);
			}
			loadUrlDataFilter(queryList,bean,request);
			loadInitQuery(initQuery,bean,request);
			fileds.append(bean.getFieldName()).append(",");
		}
		loadAuth(paras, request);
		loadIframeConfig(paras, request);
		paras.put(CgAutoListConstant.CONFIG_FIELDLIST, fieldList);
		paras.put(CgAutoListConstant.CONFIG_QUERYLIST, queryList);
		paras.put(CgAutoListConstant.FILEDS, fileds);
		paras.put(CgAutoListConstant.INITQUERY, initQuery);
		return paras;
	}
	/**
	 * ??????iframe??????
	 * @param paras
	 * @param request
	 */
	private void loadIframeConfig(Map<String, Object> paras,
			HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		String lang = (String)session.getAttribute("lang");
		
		//???????????????iframe???????????????????????????????????????
		StringBuilder sb= new StringBuilder("");
		if(!request.getQueryString().contains("isHref")){
//			String cssTheme ="default";
//			Cookie[] cookies = request.getCookies();
//			for (Cookie cookie : cookies) {
//				if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
//					continue;
//				}
//				if (cookie.getName().equalsIgnoreCase("JEECGCSSTHEME")) {
//					cssTheme = cookie.getValue();
//				}
//			}
//			if(StringUtil.isEmpty(cssTheme)){
//				cssTheme ="default";
//			}
			SysThemesEnum sysThemesEnum = SysThemesUtil.getSysTheme(request);
			sb.append("<script type=\"text/javascript\" src=\"plug-in/jquery/jquery-1.8.3.js\"></script>");
			sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/dataformat.js\"></script>");
			sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"plug-in/accordion/css/accordion.css\">");
//			sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/easyui/themes/"+cssTheme+"/easyui.css\" type=\"text/css\"></link>");
			sb.append(SysThemesUtil.getEasyUiTheme(sysThemesEnum));
			sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui/themes/icon.css\" type=\"text/css\"></link>");
			sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"plug-in/accordion/css/icons.css\">");
			sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/jquery.easyui.min.1.3.2.js\"></script>");
			sb.append("<script type=\"text/javascript\" src=\"plug-in/easyui/locale/zh-cn.js\"></script>");
			sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/syUtil.js\"></script>");
			sb.append("<script type=\"text/javascript\" src=\"plug-in/My97DatePicker/WdatePicker.js\"></script>");
//			sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/common.css\" type=\"text/css\"></link>");
			sb.append(SysThemesUtil.getCommonTheme(sysThemesEnum));
//			sb.append("<script type=\"text/javascript\" src=\"plug-in/lhgDialog/lhgdialog.min.js\"></script>");
			sb.append(SysThemesUtil.getLhgdialogTheme(sysThemesEnum));
			sb.append(StringUtil.replace("<script type=\"text/javascript\" src=\"plug-in/tools/curdtools_{0}.js\"></script>", "{0}", lang));
			
			sb.append("<script type=\"text/javascript\" src=\"plug-in/tools/easyuiextend.js\"></script>");
//			if("metro".equals(cssTheme)){
//				sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"plug-in/easyui/themes/"+cssTheme+"/main.css\" type=\"text/css\"></link>");
//			}
			sb.append(SysThemesUtil.getEasyUiMainTheme(sysThemesEnum));
		}else{
		}
		paras.put(CgAutoListConstant.CONFIG_IFRAME, sb.toString());
	}
	/**
	 * ??????????????????
	 * @param paras
	 * @param request
	 */
	private void loadAuth(Map<String, Object> paras, HttpServletRequest request) {
		List<TSOperation>  nolist = (List<TSOperation>) request.getAttribute(Globals.NOAUTO_OPERATIONCODES);
		if(ResourceUtil.getSessionUserName().getUserName().equals("admin")|| !Globals.BUTTON_AUTHORITY_CHECK){
			nolist = null;
		}
		List<String> list = new ArrayList<String>();
		String nolistStr = "";
		if(nolist!=null){
			for(TSOperation operation:nolist){
				nolistStr+=operation.getOperationcode();
				nolistStr+=",";
				list.add(operation.getOperationcode());
			}
		}
		paras.put(CgAutoListConstant.CONFIG_NOLIST, list);
		paras.put(CgAutoListConstant.CONFIG_NOLISTSTR, nolistStr==null?"":nolistStr);
	}
	/**
	 * ??????????????????????????????-
	 * @param initQuery
	 * @param bean
	 * @param request
	 */
	private void loadInitQuery(StringBuilder initQuery, CgFormFieldEntity bean,
			HttpServletRequest request) {
		if(bean.getFieldName().equalsIgnoreCase("id")){
			return;
		}
		String paramV = request.getParameter(bean.getFieldName());
		String paramVbegin = request.getParameter(bean.getFieldName()+"_begin");
		String paramVend = request.getParameter(bean.getFieldName()+"_end");
		paramV = getSystemValue(paramV);
		if(!StringUtil.isEmpty(paramV)){
			initQuery.append("&"+bean.getFieldName()+"="+paramV);
		}
		if(!StringUtil.isEmpty(paramVbegin)){
			initQuery.append("&"+bean.getFieldName()+"_begin="+paramVbegin);
		}
		if(!StringUtil.isEmpty(paramVend)){
			initQuery.append("&"+bean.getFieldName()+"_end="+paramVend);
		}
	}

	/**
	 * ??????URL??????????????????[?????????????????????????????????????????????hidden????????????]
	 * @param queryList
	 * @param bean
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void loadUrlDataFilter(List<Map> queryList, CgFormFieldEntity bean,
			HttpServletRequest request) {
		String paramV = request.getParameter(bean.getFieldName());
		String paramVbegin = request.getParameter(bean.getFieldName()+"_begin");
		String paramVend = request.getParameter(bean.getFieldName()+"_end");
		if(bean.getFieldName().equalsIgnoreCase("id")){
			return;
		}
		for(Map mq:queryList){
			if(mq.containsValue(bean.getFieldName())){
				return;
			}
		}
		if(!StringUtil.isEmpty(paramV) || !StringUtil.isEmpty(paramVbegin) ||!StringUtil.isEmpty(paramVend)){
			Map fmq = new HashMap<String, Object>();
			fmq.put(CgAutoListConstant.FILED_ID, bean.getFieldName());
			fmq.put(CgAutoListConstant.FIELD_TITLE, bean.getContent());
			fmq.put(CgAutoListConstant.FIELD_QUERYMODE, bean.getQueryMode());
			fmq.put(CgAutoListConstant.FIELD_TYPE, bean.getType());
			fmq.put(CgAutoListConstant.FIELD_ISQUERY,"N");
			paramV = getSystemValue(paramV);
			fmq.put(CgAutoListConstant.FIELD_VALUE, paramV);
			paramVend = getSystemValue(paramVend);
			fmq.put(CgAutoListConstant.FIELD_VALUE_BEGIN, StringUtil.isEmpty(paramVbegin)?"":paramVbegin);
			fmq.put(CgAutoListConstant.FIELD_VALUE_END, StringUtil.isEmpty(paramVend)?"":paramVend);
			queryList.add(fmq);
		}
	}

	/**
	 * ??????URL??????????????????[????????????????????????????????????????????????????????????????????????]
	 * @param fmq
	 * @param bean
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	private void loadDefaultValue(Map fmq, CgFormFieldEntity bean,
			HttpServletRequest request) {
		if(bean.getQueryMode().equalsIgnoreCase("single")){
			String paramV = request.getParameter(bean.getFieldName());
			if(!StringUtil.isEmpty(paramV)){
				paramV = getSystemValue(paramV);
				fmq.put(CgAutoListConstant.FIELD_VALUE, paramV);
			}
		}else if(bean.getQueryMode().equalsIgnoreCase("group")){
			String paramVbegin = request.getParameter(bean.getFieldName()+"_begin");
			String paramVend = request.getParameter(bean.getFieldName()+"_end");
			fmq.put(CgAutoListConstant.FIELD_VALUE_BEGIN, StringUtil.isEmpty(paramVbegin)?"":paramVbegin);
			fmq.put(CgAutoListConstant.FIELD_VALUE_END, StringUtil.isEmpty(paramVend)?"":paramVend);
		}
	}

	/**
	 * ??????????????????
	 * @param m	?????????freemarker?????????
	 * @param bean ?????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	private void loadDic(Map m, CgFormFieldEntity bean) {
		String dicT = bean.getDictTable();//??????Table
		String dicF = bean.getDictField();//??????Code
		String dicText = bean.getDictText();//??????Text
		if(StringUtil.isEmpty(dicT)&& StringUtil.isEmpty(dicF)){
			//??????????????????????????????????????????????????????
			m.put(CgAutoListConstant.FIELD_DICTLIST, new ArrayList(0));
			return;
		}
		if(!bean.getShowType().equals("popup")){
			List<DictEntity> dicDatas = queryDic(dicT, dicF,dicText);
			m.put(CgAutoListConstant.FIELD_DICTLIST, dicDatas);
		}else{
			m.put(CgAutoListConstant.FIELD_DICTLIST, new ArrayList(0));
		}
	}
	/**
	 * ??????????????????
	 * @param dicTable ???????????????
	 * @param dicCode	??????????????????
	 * @param dicText ????????????????????????
	 * @return
	 */
	private List<DictEntity> queryDic(String dicTable, String dicCode,String dicText) {
//		StringBuilder dicSql = new StringBuilder();
//		if(StringUtil.isEmpty(dicTable)){//step.1 ?????????????????????????????????????????????
//			dicTable = CgAutoListConstant.SYS_DIC;
//			dicSql.append(" SELECT TYPECODE,TYPENAME FROM");
//			dicSql.append(" "+dicTable);
//			dicSql.append(" "+"WHERE TYPEGROUPID = ");
//			dicSql.append(" "+"(SELECT ID FROM "+CgAutoListConstant.SYS_DICGROUP+" WHERE TYPEGROUPCODE = '"+dicCode+"' )");
//		}else{//step.2 ?????????????????????????????????????????????????????????????????????????????????????????????
//			//table?????????
//			dicSql.append("SELECT DISTINCT ").append(dicCode).append(" as typecode, ");
//			if(dicText!=null&&dicText.length()>0){
//				dicSql.append(dicText).append(" as typename ");
//			}else{
//				dicSql.append(dicCode).append(" as typename ");
//			}
//			dicSql.append(" FROM ").append(dicTable);
//			dicSql.append(" ORDER BY ").append(dicCode);
//		}
//		//step.3 ????????????
//		List<Map<String, Object>> dicDatas = systemService.findForJdbc(dicSql.toString());
		return systemService.queryDict(dicTable, dicCode, dicText);
	}
	
	private String getSystemValue(String sysVarName) {
		if(StringUtil.isEmpty(sysVarName)){
			return sysVarName;
		}
		if(sysVarName.contains("{") && sysVarName.contains("}")){
			sysVarName = sysVarName.replaceAll("\\{", "");
			sysVarName = sysVarName.replaceAll("\\}", "");
			sysVarName =sysVarName.replace("sys.", "");
			return ResourceUtil.getUserSystemData(sysVarName);
		}else{
			return sysVarName;
		}
	}
}
