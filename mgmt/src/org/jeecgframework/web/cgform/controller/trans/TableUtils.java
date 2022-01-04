package org.jeecgframework.web.cgform.controller.trans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecgframework.codegenerate.pojo.Columnt;
import org.jeecgframework.core.common.model.common.DBTable;
import org.jeecgframework.web.system.service.SystemService;

import cn.nearf.ggz.utils.ObjectUtils;
import cn.nearf.ggz.utils.StringUtils;

public class TableUtils {

	private static volatile String DatabaseName;
	public static synchronized String getDatabaseName(SystemService systemService) {
		if (DatabaseName == null) {
			String sql = "select database() as DatabaseName";
			Map<String, Object> res = systemService.findOneForJdbc(sql);
			DatabaseName = ObjectUtils.getString(res.get("DatabaseName"));
		}
		return DatabaseName;
	}
	

	public static List<String> readAllTableNames(SystemService systemService) {
		try {
			return readAllTableNames2(systemService);
		} catch (Exception e) {
			try {
				return readAllTableNames3(systemService);
			} catch (Exception e2) {
				try {
					return readAllTableNames1(systemService);
				} catch (Exception e3) {
					return null;
				}
			}
		}
	}

	private static List<String> readAllTableNames1(SystemService systemService) throws Exception {
		List<String> names = new ArrayList<>();
		List<DBTable> tables = systemService.getAllDbTableName();
		for (DBTable table : tables) {
			names.add(table.getTableName());
		}
		return names;
	}

	private static List<String> readAllTableNames2(SystemService systemService) throws Exception {
		List<String> names = new ArrayList<>();
		String sql = "show table status from " + getDatabaseName(systemService);

		List<Map<String, Object>> res = systemService.findForJdbc(sql);

		for (Map<String, Object> map : res) {
			names.add(map.get("Name").toString());
		}
		return names;
	}

	private static List<String> readAllTableNames3(SystemService systemService) throws Exception {
		List<String> names = new ArrayList<>();
		String sql = "select table_name from information_schema.tables where table_schema='" + getDatabaseName(systemService) + "' and table_type='base table'";

		List<Map<String, Object>> res = systemService.findForJdbc(sql);

		for (Map<String, Object> map : res) {
			names.add(map.get("table_name").toString());
		}
		return names;
	}

	
	
	////////////////////
	////////////////////
	////////////////////
	
	public static final List<String> IntType = new ArrayList<String>() {
		private static final long serialVersionUID = 3497270625541188286L; {
			add("int");
			add("bool");
		}
	};
	
	public static final List<String> DoubleType = new ArrayList<String>() {
		private static final long serialVersionUID = 5599913463129035112L; {
			add("float");
			add("double");
			add("real");
			add("decimal");
		}
	};
	
	public static final List<String> StringType = new ArrayList<String>() {
		private static final long serialVersionUID = -2600044774090555443L; {
			add("char");
			add("text");
			add("text");
		}
	};
	public static final List<String> DateType = new ArrayList<String>() {
		private static final long serialVersionUID = -2600044774090555444L; {
			add("date");
			add("time");
			add("year");
		}
	};
	
	public static List<Columnt> readOriginalTableColumn(SystemService systemService, String tableName) {
		List<Columnt> columnts = new ArrayList<>();
		
		String sql = "select * from information_schema.columns where table_schema='" + getDatabaseName(systemService) + "' and table_name='" + tableName + "'";
		
		List<Map<String, Object>> res = systemService.findForJdbc(sql);
		for (Map<String, Object> map : res) {
			Columnt col = new Columnt();
			col.setFieldDbName(ObjectUtils.getString(map.get("COLUMN_NAME")));
			col.setFieldName(ObjectUtils.getString(map.get("COLUMN_COMMENT")));
			col.setFiledComment(ObjectUtils.getString(map.get("COLUMN_COMMENT")));
			
			if (col.getFieldDbName().equalsIgnoreCase("id")) {
				if (StringUtils.isEmpty(col.getFieldName())) {
					col.setFieldName("主键");
				}
				if (StringUtils.isEmpty(col.getFiledComment())) {
					col.setFiledComment("主键");
				}
			}
			
			boolean isNullable = ObjectUtils.getBooleanValue(map.get("IS_NULLABLE"));
			col.setNullable(isNullable ? "Y" : "N");
			
			
			col.setCharmaxLength(ObjectUtils.getString(map.get("CHARACTER_MAXIMUM_LENGTH")));
			
			col.setPrecision(ObjectUtils.getString(map.get("NUMERIC_PERCISION")));
//			String dataPercision = ObjectUtils.getString(map.get("DATETIME_PERCISION"));
//			if (StringUtils.isNotEmpty(dataPercision)) {
//				col.setPrecision(dataPercision);
//			}
			
			col.setScale(ObjectUtils.getString(map.get("NUMERIC_SCALE")));
			
//			col.setClassType_row(classType_row);
//			col.setOptionType(optionType);
			
			String fieldType;
			String type = ObjectUtils.getString(map.get("DATA_TYPE"));
			if (type == null || type.length() == 0) {
				fieldType = "java.lang.String";
			} else if (type.toLowerCase().equals("bigint")) {
				fieldType = "java.lang.Long";
			} else if (checkIn(IntType, type)) {
				fieldType = "java.lang.Integer";
			} else if (checkIn(DoubleType, type)) {
				fieldType = "java.lang.Double";
			} else if (checkIn(StringType, type)) {
				fieldType = "java.lang.String";
			} else if (checkIn(DateType, type)) {
				fieldType = "java.util.Date";
			} else {
				fieldType = type.toLowerCase();
			}
			
			col.setFieldType(fieldType);
			
			columnts.add(col);
		}
		
		
		
		
		return columnts;
	}
	
	private static boolean checkIn(List<String> types, String type) {
		for (String string : types) {
			string = string.toLowerCase();
			type = type.toLowerCase();
			if (string.indexOf(type) >= 0 || type.indexOf(string) >= 0) {
				return true;
			}
		}
		return false;
	}
}
