package cn.nearf.ggz.config;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.util.PropertiesUtil;

import cn.nearf.ggz.utils.ObjectUtils;

public class ErpDBConfig extends ConfigUtils {
	private static final PropertiesUtil dbPro = new PropertiesUtil("dbconfig.properties");
	
	private static final Map<String, String> params = new HashMap<String, String>();
	
	
	public static boolean getBooleanProperty(String key) {
		return ObjectUtils.getBooleanValue(getProperty(key));
	}
	
	public static int getIntProperty(String key) {
		return ObjectUtils.getIntValue(getProperty(key));
	}
	
	public static String getProperty(String key) {
		String value = params.get(key);
		if (value == null) {
			value = getStringProperty(dbPro, key);
			params.put(key, value);
		}
		return value;
	}
	
	
}
