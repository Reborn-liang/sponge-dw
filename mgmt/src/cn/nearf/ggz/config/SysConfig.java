package cn.nearf.ggz.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.PropertiesUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.context.ApplicationContext;

import cn.nearf.ggz.utils.ObjectUtils;
import cn.nearf.ggz.utils.gson.GsonUtils;


public class SysConfig {
	
	private static final Logger logger = Logger.getLogger(SysConfig.class);
	private static final Map<String, Object> paramsCache = new ConcurrentHashMap<String, Object>();
	
	private static final PropertiesUtil sysPro = new PropertiesUtil("sysConfig.properties");
	
	
	public static synchronized void clear() {
		paramsCache.clear();
	}
	
	
	public static boolean getBooleanProperty(String key) {
		boolean value = false;
		try {
			Object obj = paramsCache.get(key);
			if (obj == null) {
				value = ConfigUtils.getBooleanProperty(sysPro, key);
				paramsCache.put(key, value);
			} else {
				value = ObjectUtils.getBooleanValue(obj);
			}
		} catch (Exception e) {
			System.err.println("未能获取配置:" + key);
		}
		return value;
	}
	
	public static int getIntProperty(String key) {
		int value;
		Object obj = paramsCache.get(key);
		if (obj == null) {
			value = ConfigUtils.getIntProperty(sysPro, key);
			paramsCache.put(key, value);
		} else {
			value = ObjectUtils.getIntValue(obj);
		}
		return value;
	}
	
	public static double getDoubleProperty(String key) {
		double value;
		Object obj = paramsCache.get(key);
		if (obj == null) {
			value = ConfigUtils.getDoubleProperty(sysPro, key);
			paramsCache.put(key, value);
		} else {
			value = ObjectUtils.getDoubleValue(obj);
		}
		return value;
	}
	
	public static long getLongProperty(String key) {
		long value;
		Object obj = paramsCache.get(key);
		if (obj == null) {
			value = ConfigUtils.getLongProperty(sysPro, key);
			paramsCache.put(key, value);
		} else {
			value = ObjectUtils.getLongValue(obj);
		}
		return value;
	}
	
	public static String getStringProperty(String key) {
		String value;
		Object obj = paramsCache.get(key);
		if (obj == null) {
			value = ConfigUtils.getStringProperty(sysPro, key);
			if(value!=null) {
				paramsCache.put(key, value);
			}else {
				logger.error("===============FATAL ERROR===============");
				logger.error("Can't get config value from system config, please check it, key=" + key);
				logger.error("===============FATAL ERROR===============");
			}
		} else {
			value = ObjectUtils.getString(obj);
		}
		return value;
	}
	
	
	
	
	public static synchronized String getUploadBaseDir() {
		return getStringProperty("ck.baseDir");
	}
	
	public static synchronized boolean isDebug() {
		return getBooleanProperty("DEBUG");
	}

	public static boolean isSupportMongoMigrationOrder() {
		return getBooleanProperty("Support_Mongo_Migration_Order");
	}
	
	public static boolean supportAlipayISV() {
		return getBooleanProperty("Support_Alipay_ISV");
	}
	
	public static boolean supportWechatISV() {
		return getBooleanProperty("Support_Wechat_ISV");
	}
	
	
	
	
	
	
	
	//
	private static final Map<String, List<Map<String, Object>>> allTypeGroups = new ConcurrentHashMap<String,List<Map<String, Object>>>();
	
	private static String allTypeGroupsJson = null;
	
	public static synchronized String getAllTypeGroupsJson() {
		initDataDict();
		
		if (allTypeGroupsJson == null) {
			allTypeGroupsJson = GsonUtils.toJson(allTypeGroups);
		}
		return allTypeGroupsJson;
	}
	
	
	public static synchronized String getTypeGroupJson(String name) {
		initDataDict();
		
		return GsonUtils.toJson(allTypeGroups.get(name));
	}
	
	private static synchronized void initDataDict() {
		if (allTypeGroups.isEmpty()) {
			ApplicationContext context = ApplicationContextUtil.getContext();
			SystemService systemService = context.getBean(SystemService.class);
			
			List<TSTypegroup> typeGroups = systemService.getList(TSTypegroup.class);
			try {
				for (TSTypegroup typeGroup : typeGroups) {
					List<Map<String, Object>> subTypes = allTypeGroups.get(typeGroup.getTypegroupcode());
					if (subTypes == null) {
						subTypes = new ArrayList<Map<String, Object>>();
					}
					try {
						for (TSType type : typeGroup.getTSTypes()) {
							Map<String, Object> typeObj = new HashMap<String, Object>();
							typeObj.put("id", type.getTypecode());
							typeObj.put("name", type.getTypename());
							
							subTypes.add(typeObj);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					allTypeGroups.put(typeGroup.getTypegroupcode(), subTypes);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static void main(String[] args) {
//		String filePath = getProperty("ck.baseDir");
//		System.err.println(getServerFilePath("http://erpimg.helens.com.cn/menuimg/big_watermelon_juice.jpg"));
//		System.err.println(chcekImage("http://erpimg.helens.com.cn/menuimg/big_watermelon_juice.jpg", "http://erpimg.helens.com.cn/menuimg/watermelon_juice.jpg"));
	}
}
