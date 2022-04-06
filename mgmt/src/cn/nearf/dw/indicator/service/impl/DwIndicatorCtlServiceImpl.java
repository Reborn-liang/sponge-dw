package cn.nearf.dw.indicator.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.nearf.dw.common.TypeVO;
import cn.nearf.dw.indicator.entity.DwIndicatorColumnCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorSftpCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorStdCtlEntity;
import cn.nearf.dw.indicator.service.DwIndicatorCtlServiceI;
import cn.nearf.dw.indicatormodel.entity.DwIndicatorModelCtlEntity;
import cn.nearf.ggz.utils.StringUtils;


@Service("dwIndicatorCtlService")
@Transactional
public class DwIndicatorCtlServiceImpl extends CommonServiceImpl implements DwIndicatorCtlServiceI {
	public static String IND_COL_MEARMENT = "M";
	public static String IND_COL_ATTRIBUTE = "A";
	
	public static String INDICATOR_MAIN_DATA_TYPE = "MDM";
	
	@Resource(name="gpJdbcDao")
	private JdbcDao gpJdbcDao;
	
	public List<TypeVO>getTableOrColumnsFromInformation(String schema, String tableName){
		String sql = "";
		List<Map<String, Object>> result= null;
		List<TypeVO> returnResult = new ArrayList<TypeVO>();
		if(StringUtils.isNotEmpty(schema) && StringUtils.isEmpty(tableName)) {
			sql = "SELECT table_name as name from information_schema.tables where table_schema = ? order by table_name asc";
			result= gpJdbcDao.findForJdbc(sql, schema.toLowerCase());
			if(result!=null) {
				for (Map<String, Object> map : result) {
					String name = (String)map.get("name");
					TypeVO vo = new TypeVO(name, name);
					returnResult.add(vo);
				}
			}
			return returnResult;
		}else if(StringUtils.isNotEmpty(schema) && StringUtils.isNotEmpty(tableName)) {
			sql = "SELECT column_name as name,data_type as t,character_maximum_length as l from information_schema.columns where table_schema =? and table_name = ? order by ordinal_position asc";
			result= gpJdbcDao.findForJdbc(sql, schema.toLowerCase(), tableName.toLowerCase());
			if(result!=null) {
				for (Map<String, Object> map : result) {
					String name = (String)map.get("name");
					String t = (String) map.get("t");
					Integer l = (Integer) map.get("l");
					TypeVO vo = new TypeVO(name, name, t, l);
					returnResult.add(vo);
				}
			}
			return returnResult;
		}
		return returnResult;
	}
	
	@Override
	public TypeVO getColumnInformation(String schema, String tableName, String columnName) {
		String sql = "SELECT column_name as name,data_type as t,character_maximum_length as l from information_schema.columns where table_schema =? and table_name = ? and column_name = ? order by ordinal_position asc";
		List<Map<String, Object>> result = gpJdbcDao.findForJdbc(sql, schema.toLowerCase(), tableName.toLowerCase(),columnName.toLowerCase());
		if(result!=null) {
			for (Map<String, Object> map : result) {
				String name = (String)map.get("name");
				String t = (String) map.get("t");
				Integer l = (Integer) map.get("l");
				TypeVO vo = new TypeVO(name, name, t, l);
				return vo;
			}
		}
		return null;
	}
	
	public void effectIndicator(DwIndicatorCtlEntity dwIndicatorCtl) {
		String sql = "create table " //if not exists "
				+ "data."+dwIndicatorCtl.getBizGroup()+"_"+dwIndicatorCtl.getTargetTable()+" as";
//				+ "data." + dwIndicatorCtl.getTargetTable() + " as";
		
		String selectSubSql = genSelectSubSQL(dwIndicatorCtl);
		sql += " ( "+ selectSubSql +" ) with no data ";
		sql += "DISTRIBUTED BY("+dwIndicatorCtl.getBizKey()+")";
		System.out.println("Full SQL: " + sql);
		
		//执行操作
		gpJdbcDao.executeSql(sql);
		
		String addPKSql = "ALTER TABLE "
				+ "data." + dwIndicatorCtl.getBizGroup() + "_" + dwIndicatorCtl.getTargetTable() 
				+ " ADD CONSTRAINT " + dwIndicatorCtl.getBizGroup() + "_" + dwIndicatorCtl.getTargetTable() + "_pkey " 
				+ " PRIMARY KEY (" + dwIndicatorCtl.getBizKey() + ")";
		gpJdbcDao.executeSql(addPKSql);
	}
	
	public void genStd(DwIndicatorCtlEntity dwIndicatorCtl) {
		String sql = "select column_name from information_schema.columns as col where table_schema='stage' and table_name='"+dwIndicatorCtl.getTargetTable()+"' order by ordinal_position";
		List<Map<String, Object>> srcTableInfoList = gpJdbcDao.findForJdbc(sql);
		
		if (srcTableInfoList.isEmpty()) {
			throw new BusinessException("需要生成标准化配置的表在stage schema中不存在");
		}
		
		DwIndicatorStdCtlEntity tableStd = new DwIndicatorStdCtlEntity();
		tableStd.setOriginalTable(dwIndicatorCtl.getTargetTable());
		tableStd.setStdType("T");
		tableStd.setOriginalColumn(dwIndicatorCtl.getTargetTable());
		tableStd.setStdName(dwIndicatorCtl.getTargetTable());
		tableStd.setCreateDate(new Date());
		this.save(tableStd);
		
		for (Map<String, Object> map : srcTableInfoList) {
			String colName = (String)map.get("column_name");
			DwIndicatorStdCtlEntity colStd = new DwIndicatorStdCtlEntity();
			colStd.setOriginalTable(dwIndicatorCtl.getTargetTable());
			colStd.setStdType("C");
			colStd.setOriginalColumn(colName);
			colStd.setStdName(colName);
			colStd.setCreateDate(new Date());
			this.save(colStd);
		}
	}
	
	public List<Map<String, Object>> previewFromSql(String sql, Object... objs) {
		return gpJdbcDao.findForJdbc(sql, objs);
	}

	private List<Map<String, Object>> genFieldsOfPreview(DwIndicatorCtlEntity dwIndicatorCtl) {
		String sql = "select column_name,table_schema from information_schema.columns as col where table_name='" + dwIndicatorCtl.getTargetTable() + "' order by ordinal_position";
		List<Map<String, Object>> srcTableInfoList = gpJdbcDao.findForJdbc(sql);
		if (srcTableInfoList.isEmpty()) {
			throw new BusinessException("需要预览的表在schema中不存在");
		}
		return srcTableInfoList;
	}

	public List<String> genFieldsOfSubSQL(DwIndicatorCtlEntity dwIndicatorCtl){
		List<String> fields = new ArrayList<>();
		List<Map<String, Object>> srcTableInfoList = genFieldsOfPreview(dwIndicatorCtl);
		for (Map<String, Object> map : srcTableInfoList) {
			fields.add((String) map.get("column_name"));
		}
		return fields;
	}
	/*public List<String> genFieldsOfSubSQL(DwIndicatorCtlEntity dwIndicatorCtl) {
		List<String> fields = new ArrayList<String>();
		if (!INDICATOR_MAIN_DATA_TYPE.equals(dwIndicatorCtl.getType())) {
			fields.add("type");
		}

		CriteriaQuery cq = new CriteriaQuery(DwIndicatorColumnCtlEntity.class);
		cq.eq("indicatorId", dwIndicatorCtl.getId());
		cq.add();
		List<DwIndicatorColumnCtlEntity> columns = this.getListByCriteriaQuery(cq, false);
		
		Set<String> colSet = new HashSet<String>();
		for (DwIndicatorColumnCtlEntity col : columns) {
			if(!IND_COL_MEARMENT.equals(col.getType())) {
				if (!StringUtils.isEmpty(col.getFromSchema()) && !StringUtils.isEmpty(col.getFromTable()) && !StringUtils.isEmpty(col.getFromColumn())) {
					colSet.add(col.getFromColumn().toLowerCase());
				}
			}
		}
		if (!INDICATOR_MAIN_DATA_TYPE.equals(dwIndicatorCtl.getType())) {
			if(!colSet.contains("fiscal_period")) {
				fields.add("fiscal_period");
			}
			if(!colSet.contains("fiscal_year")) {
				fields.add("fiscal_year");
			}
			if(!colSet.contains("posting_period")) {
				fields.add("posting_period");
			}
		}
		fields.add("biz_date");

		for (DwIndicatorColumnCtlEntity col : columns) {
			if(IND_COL_MEARMENT.equals(col.getType())) {
				fields.add(col.getIndicatorColumn());
			}else {
				if (!StringUtils.isEmpty(col.getFromSchema()) && !StringUtils.isEmpty(col.getFromTable()) ) {
					fields.add(col.getIndicatorColumn());
				}
			}
		}
		fields.add("create_date");
		fields.add("update_date");
		return fields;
	}*/

	public String genSelectSubSQL(DwIndicatorCtlEntity dwIndicatorCtl) {
		List<Map<String, Object>> srcTableInfoList = genFieldsOfPreview(dwIndicatorCtl);
		StringBuilder colName= new StringBuilder();
		String tableSchema = (String) srcTableInfoList.get(0).get("table_schema");
		for (Map<String, Object> map : srcTableInfoList) {
			colName.append(map.get("column_name")).append(",");
		}
		return "select "+colName.substring(0,colName.length()-1)+" from "+tableSchema+"."+dwIndicatorCtl.getTargetTable();
	}

	/*public String genSelectSubSQL(DwIndicatorCtlEntity dwIndicatorCtl) {
		CriteriaQuery cq = new CriteriaQuery(DwIndicatorColumnCtlEntity.class);
		System.out.println( " dwIndicatorCtl.getId(): "+dwIndicatorCtl.getId());
		cq.eq("indicatorId", dwIndicatorCtl.getId());
		cq.add();
		List<DwIndicatorColumnCtlEntity> columns = this.getListByCriteriaQuery(cq, false);
		
		Set<String> colSet = new HashSet<String>();
		for (DwIndicatorColumnCtlEntity col : columns) {
			if(!IND_COL_MEARMENT.equals(col.getType())) {
				if (!StringUtils.isEmpty(col.getFromSchema()) && !StringUtils.isEmpty(col.getFromTable()) && !StringUtils.isEmpty(col.getFromColumn())) {
					colSet.add(col.getFromColumn().toLowerCase());
				}
			}
		}
		
		String selectSubSql = "select ";
		if (!INDICATOR_MAIN_DATA_TYPE.equals(dwIndicatorCtl.getType())) {
			selectSubSql += "CAST ('"+dwIndicatorCtl.getJobType()+"' as char) as type";			//日，周，月，
			if(!colSet.contains("fiscal_period")) {
				selectSubSql += ", CAST ('201801' as char(6)) as fiscal_period";
			}
			if(!colSet.contains("fiscal_year")) {
				selectSubSql += ", CAST ('2018' as char(4)) as fiscal_year";
			}
			if(!colSet.contains("posting_period")) {
				selectSubSql += ", CAST ('01' as char(2)) as posting_period";
			}
			if(!colSet.contains("biz_date")) {
				selectSubSql += ", CURRENT_DATE as biz_date";
			}
		}else {
			selectSubSql += "CURRENT_DATE as biz_date";
		}

		Set<String> shcemaTableSet = new HashSet<String>();
		for (DwIndicatorColumnCtlEntity col : columns) {
			if(IND_COL_MEARMENT.equals(col.getType())) {
				selectSubSql += ", " + col.getFormula() +" as " + col.getIndicatorColumn();
			}else {
				if (!StringUtils.isEmpty(col.getFromSchema()) && !StringUtils.isEmpty(col.getFromTable()) ) {
					shcemaTableSet.add(col.getFromSchema()+"."+col.getFromTable());
					selectSubSql += ", " + col.getFormula() +" as " + col.getIndicatorColumn();
				}
			}
		}
		selectSubSql += ", CURRENT_TIMESTAMP as create_date";
		selectSubSql += ", CURRENT_TIMESTAMP as update_date";
		selectSubSql += " from ";
//		Iterator<String>shcemaTableIte =  shcemaTableSet.iterator();
//		String fromTableString = "";
//		while(shcemaTableIte.hasNext()) {
//			if (StringUtils.isEmpty(fromTableString)) {
//				fromTableString += shcemaTableIte.next();
//			} else {
//				fromTableString += "," + shcemaTableIte.next();
//			}
//		}
//		selectSubSql += fromTableString;
		
		//拼FROM部分
		if(StringUtils.isNotEmpty(dwIndicatorCtl.getModelCode())) {
			//例子
			//erp.erp_order LEFT JOIN erp.erp_store_city_province_area ON erp_order.belong_store_id = erp_store_city_province_area.store_id 
			//	    LEFT JOIN erp.erp_member ON erp_order.member_id = erp_member.member_id
			//是有模型的指标
			CriteriaQuery modelCq = new CriteriaQuery(DwIndicatorModelCtlEntity.class);
			modelCq.eq("modelCode", dwIndicatorCtl.getModelCode());
			modelCq.add();
			List<DwIndicatorModelCtlEntity> models = this.getListByCriteriaQuery(modelCq, false);
			boolean hasLeftofLeftJoin = false; //是否有LEFT JOIN左边的值了，有了就再不能拼SQL了，否则会出错
			for (DwIndicatorModelCtlEntity m : models) {
				if(!hasLeftofLeftJoin) {
					selectSubSql += (" " + m.getFactSchema() + "." + m.getFactTable() + " " + m.getJoinType() + " " + m.getDimSchema() + "." + m.getDimTable() + " on " + m.getJoinCondition());
					hasLeftofLeftJoin = true;
				}else {
					selectSubSql += (" " + m.getJoinType() + " " + m.getDimSchema() + "." + m.getDimTable() + " on " + m.getJoinCondition());
				}
			}
		}else {
			Iterator<String>shcemaTableIte =  shcemaTableSet.iterator();
			String fromTableString = "";
			while(shcemaTableIte.hasNext()) {
				if (StringUtils.isEmpty(fromTableString)) {
					fromTableString += shcemaTableIte.next();
				} else {
					fromTableString += "," + shcemaTableIte.next();
				}
			}
			selectSubSql += fromTableString;
		}
		
		String whereClause = null;
		for (DwIndicatorColumnCtlEntity col : columns) {
			if(StringUtils.isNotEmpty(col.getFilters())) {
				if(StringUtils.isNotEmpty(whereClause)) {
					whereClause += " and " + col.getFilters();
				}else {
					whereClause = col.getFilters();
				}
			}
		}
		if(StringUtils.isNotEmpty(whereClause)) {
			selectSubSql += (" WHERE " + whereClause);
		}
		
		String groupByCondition = null;
		for (DwIndicatorColumnCtlEntity col : columns) {
			if("M".equals(col.getType())) {		//度量不用group by
				
			}else {
				//属性字段，才group by
				if(StringUtils.isNotEmpty(groupByCondition)) {
//							groupByCondition += "," + col.getFromTable()+"."+col.getFromColumn();
					groupByCondition += ", " + col.getIndicatorColumn();
				}else {
//							groupByCondition = col.getFromTable()+"."+col.getFromColumn();
					groupByCondition = col.getIndicatorColumn();
				}
			}
		}
		if(StringUtils.isNotEmpty(groupByCondition)) {
			selectSubSql +=" group by " + groupByCondition;
		}
				
		System.out.println("Select sub SQL: " + selectSubSql);
		return selectSubSql;
	}*/
	
	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((DwIndicatorCtlEntity)entity);
 	}
	
	public void addMain(DwIndicatorCtlEntity dwIndicatorCtl,
	        List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList,List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList){
			//保存主信息
			dwIndicatorCtl.setCreateDate(new Date());
			this.save(dwIndicatorCtl);
		
			/**保存-dw_indicator_ctl*/
			for(DwIndicatorColumnCtlEntity dwIndicatorColumnCtl:dwIndicatorColumnCtlList){
				//外键设置
				dwIndicatorColumnCtl.setIndicatorId(dwIndicatorCtl.getId());
				this.save(dwIndicatorColumnCtl);
			}
			/**保存-dw_indicator_ctl*/
			for(DwIndicatorSftpCtlEntity dwIndicatorSftpCtl:dwIndicatorSftpCtlList){
				//外键设置
				dwIndicatorSftpCtl.setIndicatorId(dwIndicatorCtl.getId());
				this.save(dwIndicatorSftpCtl);
			}
			//执行新增操作配置的sql增强
 			this.doAddSql(dwIndicatorCtl);
	}

	
	public void updateMain(DwIndicatorCtlEntity dwIndicatorCtl,
	        List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList,List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList) {
		//保存主表信息
		this.saveOrUpdate(dwIndicatorCtl);
		//===================================================================================
		//获取参数
		Object id0 = dwIndicatorCtl.getId();
		Object id1 = dwIndicatorCtl.getId();
		//===================================================================================
		//1.查询出数据库的明细数据-dw_indicator_ctl
	    String hql0 = "from DwIndicatorSftpCtlEntity where 1 = 1 AND INDICATOR_ID = ? ";
	    List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlOldList = this.findHql(hql0,id0);
		//2.筛选更新明细数据-dw_indicator_ctl
		for(DwIndicatorSftpCtlEntity oldE:dwIndicatorSftpCtlOldList){
			boolean isUpdate = false;
				for(DwIndicatorSftpCtlEntity sendE:dwIndicatorSftpCtlList){
					//需要更新的明细数据-dw_indicator_ctl
					if(oldE.getId().equals(sendE.getId())){
		    			try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate= true;
		    			break;
		    		}
		    	}
	    		if(!isUpdate){
		    		//如果数据库存在的明细，前台没有传递过来则是删除-dw_indicator_ctl
		    		super.delete(oldE);
	    		}
	    		
			}
			//3.持久化新增的数据-dw_indicator_ctl
			for(DwIndicatorSftpCtlEntity dwIndicatorSftpCtl:dwIndicatorSftpCtlList){
				if(oConvertUtils.isEmpty(dwIndicatorSftpCtl.getId())){
					//外键设置
					dwIndicatorSftpCtl.setIndicatorId(dwIndicatorCtl.getId());
					dwIndicatorSftpCtl.setCreateDate(new Date());
					this.save(dwIndicatorSftpCtl);
				}
			}
		//===================================================================================
		//1.查询出数据库的明细数据-dw_indicator_ctl
	    String hql1 = "from DwIndicatorColumnCtlEntity where 1 = 1 AND iNDICATOR_ID = ? ";
	    List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlOldList = this.findHql(hql1,id1);
		//2.筛选更新明细数据-dw_indicator_ctl
		for(DwIndicatorColumnCtlEntity oldE:dwIndicatorColumnCtlOldList){
			boolean isUpdate = false;
				for(DwIndicatorColumnCtlEntity sendE:dwIndicatorColumnCtlList){
					//需要更新的明细数据-dw_indicator_ctl
					if(oldE.getId().equals(sendE.getId())){
		    			try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate= true;
		    			break;
		    		}
		    	}
	    		if(!isUpdate){
		    		//如果数据库存在的明细，前台没有传递过来则是删除-dw_indicator_ctl
		    		super.delete(oldE);
	    		}
	    		
			}
			//3.持久化新增的数据-dw_indicator_ctl
			for(DwIndicatorColumnCtlEntity dwIndicatorColumnCtl:dwIndicatorColumnCtlList){
				if(oConvertUtils.isEmpty(dwIndicatorColumnCtl.getId())){
					//外键设置
					dwIndicatorColumnCtl.setIndicatorId(dwIndicatorCtl.getId());
					this.save(dwIndicatorColumnCtl);
				}
			}
		//执行更新操作配置的sql增强
 		this.doUpdateSql(dwIndicatorCtl);
	}

	
	public void delMain(DwIndicatorCtlEntity dwIndicatorCtl) {
		//删除主表信息
		this.delete(dwIndicatorCtl);
		//===================================================================================
		//获取参数
		Object id0 = dwIndicatorCtl.getId();
		Object id1 = dwIndicatorCtl.getId();
		//===================================================================================
		//删除-dw_indicator_ctl
	    String hql0 = "from DwIndicatorColumnCtlEntity where 1 = 1 AND iNDICATOR_ID = ? ";
	    List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlOldList = this.findHql(hql0,id0);
		this.deleteAllEntitie(dwIndicatorColumnCtlOldList);
		//===================================================================================
		//删除-dw_indicator_ctl
	    String hql1 = "from DwIndicatorSftpCtlEntity where 1 = 1 AND iNDICATOR_ID = ? ";
	    List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlOldList = this.findHql(hql1,id1);
		this.deleteAllEntitie(dwIndicatorSftpCtlOldList);
	}
	
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(DwIndicatorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(DwIndicatorCtlEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(DwIndicatorCtlEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,DwIndicatorCtlEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{biz_group}",String.valueOf(t.getBizGroup()));
 		sql  = sql.replace("#{code}",String.valueOf(t.getCode()));
 		sql  = sql.replace("#{target_table}",String.valueOf(t.getTargetTable()));
 		sql  = sql.replace("#{type}",String.valueOf(t.getType()));
 		sql  = sql.replace("#{status}",String.valueOf(t.getStatus()));
 		sql  = sql.replace("#{biz_key}",String.valueOf(t.getBizKey()));
 		sql  = sql.replace("#{model_code}",String.valueOf(t.getModelCode()));
 		sql  = sql.replace("#{history_keep_days}",String.valueOf(t.getHistoryKeepDays()));
 		sql  = sql.replace("#{job_type}",String.valueOf(t.getJobType()));
 		sql  = sql.replace("#{job_inc_flg}",String.valueOf(t.getJobIncFlg()));
 		sql  = sql.replace("#{job_start_time}",String.valueOf(t.getJobStartTime()));
 		sql  = sql.replace("#{sftp_flg}",String.valueOf(t.getSftpFlg()));
 		sql  = sql.replace("#{duplication_check_flg}",String.valueOf(t.getDuplicationCheckFlg()));
 		sql  = sql.replace("#{name}",String.valueOf(t.getName()));
 		sql  = sql.replace("#{memo}",String.valueOf(t.getMemo()));
 		sql  = sql.replace("#{sqls}",String.valueOf(t.getSqls()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}

}