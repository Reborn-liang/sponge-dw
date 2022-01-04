package cn.nearf.dw.mon.service.impl;
import cn.nearf.dw.mon.service.DwIndicatorRerunMonitorCtlServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import cn.nearf.dw.mon.entity.DwIndicatorRerunMonitorCtlEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.io.Serializable;

@Service("dwIndicatorRerunMonitorCtlService")
@Transactional
public class DwIndicatorRerunMonitorCtlServiceImpl extends CommonServiceImpl implements DwIndicatorRerunMonitorCtlServiceI {

	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorRerunMonitorCtlEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((DwIndicatorRerunMonitorCtlEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((DwIndicatorRerunMonitorCtlEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorRerunMonitorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorRerunMonitorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorRerunMonitorCtlEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorRerunMonitorCtlEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{indicator_id}",String.valueOf(t.getIndicatorId()));
 		sql  = sql.replace("#{job_type}",String.valueOf(t.getJobType()));
 		sql  = sql.replace("#{job_inc_flg}",String.valueOf(t.getJobIncFlg()));
 		sql  = sql.replace("#{job_status}",String.valueOf(t.getJobStatus()));
 		sql  = sql.replace("#{refresh_date}",String.valueOf(t.getRefreshDate()));
 		sql  = sql.replace("#{process_data}",String.valueOf(t.getProcessData()));
 		sql  = sql.replace("#{job_start_time}",String.valueOf(t.getJobStartTime()));
 		sql  = sql.replace("#{job_end_time}",String.valueOf(t.getJobEndTime()));
 		sql  = sql.replace("#{job_run_time}",String.valueOf(t.getJobRunTime()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}