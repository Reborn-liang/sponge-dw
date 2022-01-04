package cn.nearf.dw.indicatormodel.service;
import org.jeecgframework.core.common.service.CommonService;

import cn.nearf.dw.indicatormodel.entity.DwIndicatorModelCtlEntity;

import java.io.Serializable;

public interface DwIndicatorModelCtlServiceI extends CommonService{
	
 	public <T> void delete(T entity);
 	
 	public <T> Serializable save(T entity);
 	
 	public <T> void saveOrUpdate(T entity);
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorModelCtlEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorModelCtlEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorModelCtlEntity t);
}
