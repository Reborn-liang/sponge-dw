package cn.nearf.dw.log.service.impl;
import cn.nearf.dw.log.service.DwIndicatorHotMailCtlServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import cn.nearf.dw.log.entity.DwIndicatorHotMailCtlEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.io.Serializable;

@Service("dwIndicatorHotMailCtlService")
@Transactional
public class DwIndicatorHotMailCtlServiceImpl extends CommonServiceImpl implements DwIndicatorHotMailCtlServiceI {

	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorHotMailCtlEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((DwIndicatorHotMailCtlEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((DwIndicatorHotMailCtlEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorHotMailCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorHotMailCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorHotMailCtlEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorHotMailCtlEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{group_name}",String.valueOf(t.getGroupName()));
 		sql  = sql.replace("#{user_name}",String.valueOf(t.getUserName()));
 		sql  = sql.replace("#{user_email_addr}",String.valueOf(t.getUserEmailAddr()));
 		sql  = sql.replace("#{urgent_flg}",String.valueOf(t.getUrgentFlg()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}