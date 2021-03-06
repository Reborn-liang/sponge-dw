package org.jeecgframework.web.cgform.controller.template;

import jodd.io.StreamUtil;
import jodd.io.ZipUtil;
import junit.framework.Assert;
import org.apache.commons.io.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.template.CgformTemplateEntity;
import org.jeecgframework.web.cgform.service.template.CgformTemplateServiceI;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


/**
 * @Title: Controller
 * @Description: ???????????????
 * @author onlineGenerator
 * @date 2015-06-15 11:04:12
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/cgformTemplateController")
public class CgformTemplateController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CgformTemplateController.class);

	@Autowired
	private CgformTemplateServiceI cgformTemplateService;
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
	 * ????????????????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "cgformTemplate")
	public ModelAndView cgformTemplate(HttpServletRequest request) {
		return new ModelAndView("jeecg/cgform/template/cgformTemplateList");
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
	public void datagrid(CgformTemplateEntity cgformTemplate,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CgformTemplateEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cgformTemplate, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.cgformTemplateService.getDataGridReturn(cq, true);
		List<CgformTemplateEntity> dataList=dataGrid.getResults();
		if(dataList!=null&&dataList.size()>0){
			for(CgformTemplateEntity entity:dataList){
				entity.setTemplatePic("cgformTemplateController.do?showPic&code="+entity.getTemplateCode()+"&path="+entity.getTemplatePic());
			}
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(CgformTemplateEntity cgformTemplate, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cgformTemplate = systemService.getEntity(CgformTemplateEntity.class, cgformTemplate.getId());
		message = "???????????????????????????";
		try{
			cgformTemplateService.delete(cgformTemplate);
			if(cgformTemplate.getTemplateCode()!=null){
				delTemplate(request,cgformTemplate.getTemplateCode());
			}
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	private void delTemplate(HttpServletRequest request,String code){
		String dirPath=getUploadBasePath(request)+File.separator+code;
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(new File(dirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		try{
			for(String id:ids.split(",")){
				CgformTemplateEntity cgformTemplate = systemService.getEntity(CgformTemplateEntity.class, 
				id
				);
				cgformTemplateService.delete(cgformTemplate);
				if(cgformTemplate.getTemplateCode()!=null){
					delTemplate(request,cgformTemplate.getTemplateCode());
				}
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ?????????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(CgformTemplateEntity cgformTemplate, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		try{
			cgformTemplateService.save(cgformTemplate);
			String basePath=getUploadBasePath(request);
			File templeDir=new File(basePath+File.separator+cgformTemplate.getTemplateCode());
			if(!templeDir.exists())
				templeDir.mkdirs();
			removeZipFile(basePath+File.separator+"temp"+File.separator+cgformTemplate.getTemplateZipName(),templeDir.getAbsolutePath());
			removeIndexFile(basePath + File.separator + "temp" + File.separator + cgformTemplate.getTemplatePic(), templeDir.getAbsolutePath());
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	//?????????????????????????????????????????????????????????
	private void removeIndexFile(String templateIndexPath,String templateDir){
		File indexFile=new File(templateIndexPath);
		if(indexFile.exists()&&!indexFile.isDirectory()){
			File destDir=new File(templateDir+File.separator+"images");
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			File destIndexFile=new File(destDir,indexFile.getName());
			if(destIndexFile.exists()){
				FileUtils.delete(destIndexFile.getAbsolutePath());
			}
			try {
				FileCopyUtils.copy(indexFile, destIndexFile);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				FileUtils.delete(indexFile.getAbsolutePath());
			}

		}
	}
	//?????????????????????????????????????????????????????????
	private void removeZipFile(String zipFilePath,String templateDir){
		File zipFile=new File(zipFilePath);
		if(zipFile.exists()&&!zipFile.isDirectory()){
			try {
				unZipFiles(zipFile, templateDir);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				FileUtils.delete(zipFilePath);
			}
		}
	}
	/**
	 * ?????????????????????
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(CgformTemplateEntity cgformTemplate, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "???????????????????????????";
		CgformTemplateEntity t = cgformTemplateService.get(CgformTemplateEntity.class, cgformTemplate.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(cgformTemplate, t);
			String basePath=getUploadBasePath(request);
			File templeDir=new File(basePath+File.separator+t.getTemplateCode());
			if(!templeDir.exists())
				templeDir.mkdirs();
			removeZipFile(basePath+File.separator+"temp"+File.separator+t.getTemplateZipName(),templeDir.getAbsolutePath());
			removeIndexFile(basePath + File.separator + "temp" + File.separator + t.getTemplatePic(), templeDir.getAbsolutePath());
			cgformTemplateService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "???????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(CgformTemplateEntity cgformTemplate, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cgformTemplate.getId())) {
			cgformTemplate = cgformTemplateService.getEntity(CgformTemplateEntity.class, cgformTemplate.getId());
			req.setAttribute("cgformTemplatePage", cgformTemplate);
		}
		return new ModelAndView("jeecg/cgform/template/cgformTemplate-add");
	}
	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(CgformTemplateEntity cgformTemplate, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cgformTemplate.getId())) {
			cgformTemplate = cgformTemplateService.getEntity(CgformTemplateEntity.class, cgformTemplate.getId());
			req.setAttribute("cgformTemplatePage", cgformTemplate);
		}
		return new ModelAndView("jeecg/cgform/template/cgformTemplate-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "cgformTemplateController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(CgformTemplateEntity cgformTemplate,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(CgformTemplateEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cgformTemplate, request.getParameterMap());
		List<CgformTemplateEntity> cgformTemplates = this.cgformTemplateService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"???????????????");
		modelMap.put(NormalExcelConstants.CLASS,CgformTemplateEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("?????????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST, cgformTemplates);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * ????????????????????????
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "checkTemplate")
	@ResponseBody
	public boolean checkTemplate(String id,HttpServletRequest request){
		boolean flag=false;
		if(StringUtils.isNotBlank(id)) {
			CgformTemplateEntity entity = cgformTemplateService.getEntity(CgformTemplateEntity.class, id);
			if (entity != null && entity.getTemplateCode() != null) {
				File dirFile=new File(getUploadBasePath(request)+"/"+entity.getTemplateCode());
				if(dirFile.exists()&&dirFile.isDirectory()){
					flag=true;
				}
			}
		}
		return flag;

	}
	/**
	 * ????????????
	 * 
	 * @param id
	 */
	@RequestMapping(params = "downloadTemplate")
	public void downloadTemplate(String id,HttpServletRequest request,HttpServletResponse response) {
		if(StringUtils.isNotBlank(id)){
			CgformTemplateEntity entity=cgformTemplateService.getEntity(CgformTemplateEntity.class,id);
			if(entity!=null&&entity.getTemplateCode()!=null){
				File zipFile=zipFile(entity.getTemplateCode(),request);
				if(zipFile!=null&&zipFile.exists()){
					FileInputStream inputStream=null;
					try {
						inputStream=new FileInputStream(zipFile);
						downLoadFile(inputStream,entity.getTemplateName()+".zip",zipFile.length(),response);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}finally {
						if(inputStream!=null){
							try {
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						FileUtils.delete(zipFile.getAbsolutePath());
					}
				}
			}
		}else{
			return ;
		}
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
				List<CgformTemplateEntity> listCgformTemplateEntitys = ExcelImportUtil.importExcel(file.getInputStream(),CgformTemplateEntity.class,params);
				for (CgformTemplateEntity cgformTemplate : listCgformTemplateEntitys) {
					cgformTemplateService.save(cgformTemplate);
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

	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "uploadPic")
	@ResponseBody
	public AjaxJson uploadPic(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		File picTempFile=null;
		File tempDir=new File(getUploadBasePath(request),"temp");
		if(!tempDir.exists())
			tempDir.mkdirs();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			picTempFile=new File(tempDir.getAbsolutePath(),"/index_"+request.getSession().getId()+"."+FileUtils.getExtend(file.getOriginalFilename()));
			try{
				if(picTempFile.exists())
					org.apache.commons.io.FileUtils.forceDelete(picTempFile);
				FileCopyUtils.copy(file.getBytes(),picTempFile);
			}catch (Exception e){
				e.printStackTrace();
				j.setMsg("????????????????????????");
				j.setSuccess(false);
			}
			j.setObj(picTempFile.getName());
		}
		j.setMsg("?????????????????????");
		j.setSuccess(true);
		return j;
	}

	/**
	 * ??????zip??????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "uploadZip")
	@ResponseBody
	public AjaxJson uploadZip(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		File picTempFile=null;
		File tempDir=new File(getUploadBasePath(request),"temp");
		if(!tempDir.exists())
			tempDir.mkdirs();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			picTempFile=new File(tempDir.getAbsolutePath(),"/zip_"+request.getSession().getId()+"."+ FileUtils.getExtend(file.getOriginalFilename()));
			try{
				if(picTempFile.exists())
					org.apache.commons.io.FileUtils.forceDelete(picTempFile);
				FileCopyUtils.copy(file.getBytes(),picTempFile);
			}catch (Exception e){
				e.printStackTrace();
				j.setMsg("???????????????????????????");
				j.setSuccess(false);
			}
			j.setObj(picTempFile.getName());
		}
		j.setMsg("???????????????????????????");
		j.setSuccess(true);
		return j;
	}

	/**
	 * ????????????
	 * @param request
	 * @param code
	 * @param path
	 * @param response
	 */
	@RequestMapping(params = "showPic")
	public void showPic(HttpServletRequest request,String code, String path,HttpServletResponse response){
		String defaultPath="default.jpg";
		String defaultCode="default/images/";
		//???????????????
		if(path==null){
			path=defaultPath;
			code=defaultCode;
		}else{
			//????????????
			if(code==null){
				code="temp/";
			}else{
				code+="/images/";
			}
		}
		FileInputStream fis = null;
		response.setContentType("image/" + FileUtils.getExtend(path));
		try {
			OutputStream out = response.getOutputStream();
			File file = new File(getUploadBasePath(request),code+path);
			if(!file.exists()||file.isDirectory()){
				file=new File(getUploadBasePath(request),defaultCode+defaultPath);
			}
			fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			out.write(b);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//?????????????????????
	private String getUploadBasePath(HttpServletRequest request){
//		String path=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/online/template");
		String path= this.getClass().getResource("/").getPath()+"online/template";
		return path;
	}

	/**
	 * ????????????????????????
	 * @param param
	 * @return
	 */
	@RequestMapping(params = "checkCode")
	@ResponseBody
	public AjaxJson checkCode(String param){
		org.springframework.util.Assert.notNull(param);
		AjaxJson j=new AjaxJson();
	 	Long count=cgformTemplateService.getCountForJdbcParam("select count(id) from cgform_template where template_code=?  ", new Object[]{param});
	 	if(count==null||count<=0){
		 	j.setSuccess(true);
	 	}else {
		 	j.setSuccess(false);
	 	}
		return j;
	}

	/**
	 * ???????????????????????????
	 * @param type
	 * @return
	 */
	@RequestMapping(params = "getTemplate")
	@ResponseBody
	public AjaxJson getTemplate(String type){
		org.springframework.util.Assert.notNull(type);
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(cgformTemplateService.getTemplateListByType(type));
		return j;
	}
	//??????zip??????
	private  void unZipFiles(File zipFile,String descDir)throws IOException{
		ZipUtil.unzip(zipFile, new File(descDir));
	}
	//?????????????????????????????????
	private File zipFile(String templateCode,HttpServletRequest request){
		String dirPath=getUploadBasePath(request)+"/"+templateCode;
		ZipOutputStream zos = null;
		File zipFile=null;
		try {
			File dir=new File(dirPath);
			if(dir.exists()&&dir.isDirectory()){
				zipFile=new File(dir+"_"+request.getSession().getId()+".zip");
				File[] files=dir.listFiles();
				if(files!=null){
					zos=ZipUtil.createZip(zipFile);
					for(File file: files){
						ZipUtil.addToZip(zos,file);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(zos!=null){
				StreamUtil.close(zos);
			}
		}
		return zipFile;
	}
	//????????????
	private  void downLoadFile(InputStream inputStream,String fileName,long size,HttpServletResponse response){
		try{
			fileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");
		}catch (Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		if(size>0)
			response.addHeader("Content-Length",size+"");
		OutputStream stream= null;
		try {
			stream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, stream);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
