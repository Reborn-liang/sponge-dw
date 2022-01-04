package cn.nearf.dw.log.service.impl;
import cn.nearf.dw.log.service.DwIndicatorFtpDumpLogServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import cn.nearf.dw.log.entity.DwIndicatorFtpDumpLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.io.Serializable;

@Service("dwIndicatorFtpDumpLogService")
@Transactional
public class DwIndicatorFtpDumpLogServiceImpl extends CommonServiceImpl implements DwIndicatorFtpDumpLogServiceI {

	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorFtpDumpLogEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((DwIndicatorFtpDumpLogEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((DwIndicatorFtpDumpLogEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorFtpDumpLogEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorFtpDumpLogEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorFtpDumpLogEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorFtpDumpLogEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{indicator_id}",String.valueOf(t.getIndicatorId()));
 		sql  = sql.replace("#{sftp_status}",String.valueOf(t.getSftpStatus()));
 		sql  = sql.replace("#{error_info}",String.valueOf(t.getErrorInfo()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}