package cn.nearf.dw.indicator.service;
import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorSftpCtlEntity;
import cn.nearf.dw.common.TypeVO;
import cn.nearf.dw.indicator.entity.DwIndicatorColumnCtlEntity;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface DwIndicatorCtlServiceI extends CommonService{
	
	public List<TypeVO>getTableOrColumnsFromInformation(String schema, String tableName);
	public TypeVO getColumnInformation(String schema, String tableName,String columnName);
	
	public void effectIndicator(DwIndicatorCtlEntity dwIndicatorCtl);
	public void genStd(DwIndicatorCtlEntity dwIndicatorCtl);
	
	public List<String> genFieldsOfSubSQL(DwIndicatorCtlEntity dwIndicatorCtl);
	public String genSelectSubSQL(DwIndicatorCtlEntity dwIndicatorCtl);
	public List<Map<String, Object>> previewFromSql(String sql, Object... objs);
	
 	public <T> void delete(T entity);
	/**
	 * 添加一对多
	 * 
	 */
	public void addMain(DwIndicatorCtlEntity dwIndicatorCtl,
	        List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList,List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList) ;
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DwIndicatorCtlEntity dwIndicatorCtl,
	        List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList,List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList);
	public void delMain (DwIndicatorCtlEntity dwIndicatorCtl);
	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorCtlEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorCtlEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorCtlEntity t);
}
