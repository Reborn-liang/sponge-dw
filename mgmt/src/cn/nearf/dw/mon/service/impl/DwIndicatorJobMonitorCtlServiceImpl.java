package cn.nearf.dw.mon.service.impl;
import cn.nearf.dw.mon.service.DwIndicatorJobMonitorCtlServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import cn.nearf.dw.mon.entity.DwIndicatorJobMonitorCtlEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.io.Serializable;

@Service("dwIndicatorJobMonitorCtlService")
@Transactional
public class DwIndicatorJobMonitorCtlServiceImpl extends CommonServiceImpl implements DwIndicatorJobMonitorCtlServiceI {

	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorJobMonitorCtlEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((DwIndicatorJobMonitorCtlEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((DwIndicatorJobMonitorCtlEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorJobMonitorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorJobMonitorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorJobMonitorCtlEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorJobMonitorCtlEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{indicator_id}",String.valueOf(t.getIndicatorId()));
 		sql  = sql.replace("#{job_status}",String.valueOf(t.getJobStatus()));
 		sql  = sql.replace("#{process_data}",String.valueOf(t.getProcessData()));
 		sql  = sql.replace("#{job_start_time}",String.valueOf(t.getJobStartTime()));
 		sql  = sql.replace("#{job_end_time}",String.valueOf(t.getJobEndTime()));
 		sql  = sql.replace("#{job_run_time}",String.valueOf(t.getJobRunTime()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}