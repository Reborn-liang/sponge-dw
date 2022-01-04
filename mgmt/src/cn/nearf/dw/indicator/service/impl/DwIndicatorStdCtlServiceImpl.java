package cn.nearf.dw.indicator.service.impl;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.nearf.dw.indicator.entity.DwIndicatorStdCtlEntity;
import cn.nearf.dw.indicator.service.DwIndicatorStdCtlServiceI;

@Service("dwIndicatorStdCtlService")
@Transactional
public class DwIndicatorStdCtlServiceImpl extends CommonServiceImpl implements DwIndicatorStdCtlServiceI {
	private static final Logger logger = Logger.getLogger(DwIndicatorStdCtlServiceImpl.class);
	
	@Resource(name="gpJdbcDao")
	private JdbcDao gpJdbcDao;
	
	public void genView(DwIndicatorStdCtlEntity stdEntity) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorStdCtlEntity.class);
		cq.eq("originalTable", stdEntity.getOriginalTable());
		cq.eq("stdType", "C");
		cq.add();
		List<DwIndicatorStdCtlEntity> stdList = this.getListByCriteriaQuery(cq, false);
		String selectSubSql = null;
		for (DwIndicatorStdCtlEntity dwIndicatorStdCtlEntity : stdList) {
			if(selectSubSql == null) {
				selectSubSql=("select "+ dwIndicatorStdCtlEntity.getOriginalColumn()+" AS "+dwIndicatorStdCtlEntity.getStdName().toLowerCase());
			}else {
				selectSubSql+=(", "+ dwIndicatorStdCtlEntity.getOriginalColumn()+" AS "+dwIndicatorStdCtlEntity.getStdName().toLowerCase());
			}
		}
		if (selectSubSql != null) {
			String sql = "create or replace view view." + stdEntity.getStdName().toLowerCase() + " as " + selectSubSql + " from stage."+stdEntity.getOriginalTable();
			logger.info("Generate view sql: " + sql);
			Integer t = gpJdbcDao.executeSql(sql);
			logger.info("Generate view result: " + t);
		}
	}
	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorStdCtlEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((DwIndicatorStdCtlEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((DwIndicatorStdCtlEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorStdCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorStdCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorStdCtlEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorStdCtlEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{original_table}",String.valueOf(t.getOriginalTable()));
 		sql  = sql.replace("#{original_column}",String.valueOf(t.getOriginalColumn()));
 		sql  = sql.replace("#{std_type}",String.valueOf(t.getStdType()));
 		sql  = sql.replace("#{std_name}",String.valueOf(t.getStdName()));
 		sql  = sql.replace("#{chinese_name}",String.valueOf(t.getChineseName()));
 		sql  = sql.replace("#{value_range_code}",String.valueOf(t.getValueRangeCode()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}