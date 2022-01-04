package cn.nearf.dw.indicator.service;
import cn.nearf.dw.indicator.entity.DwIndicatorStdCtlEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface DwIndicatorStdCtlServiceI extends CommonService{
	public void genView(DwIndicatorStdCtlEntity stdEntity);
	
 	public <T> void delete(T entity);
 	
 	public <T> Serializable save(T entity);
 	
 	public <T> void saveOrUpdate(T entity);
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorStdCtlEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorStdCtlEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorStdCtlEntity t);
}
