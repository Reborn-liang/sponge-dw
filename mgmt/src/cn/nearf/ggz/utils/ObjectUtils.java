package cn.nearf.ggz.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectUtils {

	public static String getString(Object obj) {
		String str = null;
		if (obj != null) {
			str = obj.toString();
		}
		return str;
	}
	
	public static String getString(Object obj, String defaultVal) {
		String str = defaultVal;
		if (obj != null) {
			str = obj.toString();
		}
		return str;
	}
	
	public static Integer getInteger(Object obj) {
		Integer integer = null;
		try {
			integer = Integer.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return integer;
	}
	
	public static int getIntValue(Object obj) {
		return getIntValue(obj, 0);
	}
	
	public static int getIntValue(Object obj, int defaultValue) {
		int integer = defaultValue;
		try {
			integer = Integer.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return integer;
	}
	
	public static Double getDouble(Object obj) {
		Double db = null;
		try {
			db = Double.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return db;
	}
	
	public static double getDoubleValue(Object obj) {
		return getDoubleValue(obj, 0.0000d);
	}
	
	
	
	public static float getFloatValue(Object obj, float defaultValue) {
		float db = defaultValue;
		try {
			db = Float.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return db;
	}
	
	public static Float getFloat(Object obj) {
		Float db = null;
		try {
			db = Float.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return db;
	}
	
	public static float getFloatValue(Object obj) {
		return getFloatValue(obj, 0.0f);
	}
	
	public static double format(Double d){
		if(d == null){
			return 0.00d;
		}
		DecimalFormat df = new DecimalFormat("#######.##");  
		return getDoubleValue(df.format(d.doubleValue()));
	}
	
	public static double add(double v1, double v2) {  
		   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		   return format(b1.add(b2).doubleValue());  
	}
	
	public static double add(Double v1, Double v2) {  
		   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		   return format(b1.add(b2).doubleValue());  
	}
	
	public static double add(Double v1, double v2) {  
		   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		   BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		   return format(b1.add(b2).doubleValue());  
	}
	
	public static double decimal5(Double v1) {  
		if(v1 == null){
			return 0.00d;
		}
		DecimalFormat df = new DecimalFormat("#######.#####");  
		return getDoubleValue(df.format(v1.doubleValue()));
	}
	
	public static double getDoubleValue(Object obj, double defaultValue) {
		double db = defaultValue;
		try {
			db = Double.valueOf(obj.toString());
		} catch (Exception e) {
		}
		DecimalFormat df = new DecimalFormat("#######.####");  
		try {
			db = Double.valueOf(df.format(db));
		} catch (Exception e) {
		}
		return db;
	}
	
	
	
	public static double decimal(Double v1) {  
		   BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		   return format(b1.doubleValue());  
	}
	
	public static Long getLong(Object obj) {
		Long lg = null;
		try {
			lg = Long.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return lg;
	}
	
	public static long getLongValue(Object obj) {
		return getLongValue(obj, 0l);
	}
	
	public static long getLongValue(Object obj, long defaultValue) {
		long lg = defaultValue;
		try {
			lg = Long.valueOf(obj.toString());
		} catch (Exception e) {
		}
		return lg;
	}
	
	
	public static double addDoubles(Object... doubles) {
		double value = 0.0;
		if (doubles != null) {
			for (Object dou : doubles) {
				value += getDoubleValue(dou);
			}
		}
		return value;
	}
	
	
	public static double getPriceValue(final Object price) {
		BigDecimal b = new BigDecimal(getDoubleValue(price));  
		return b.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue(); 
		//return Math.round(price * 100) / 100.0;
	}
	
	public static double getMaxPriceValue(final Object price) {
		BigDecimal b = new BigDecimal(getDoubleValue(price));  
		return b.setScale(2, BigDecimal.ROUND_CEILING).doubleValue(); 
	}
	
	public static double getPriceYuanValue(final Object price) {
		return Math.round(getDoubleValue(price));
	}
	
	public static boolean isStringTrue(String str) {
		try {
			if (str != null) {
				str = str.trim();
				if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("y")) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public static boolean getBooleanValue(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		if (obj.getClass().equals(boolean.class)) {
			return (boolean) obj;
		}
		return isStringTrue(obj.toString());
	}
	
	public static Map objectToMap(Object object) {
		if (object instanceof Map) {
			return (Map) object;
		}
		return new ObjectMapper().convertValue(object, Map.class);
	}
	
	public static boolean isEqual(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		
		if (obj1 == null && obj2 != null) {
			return false;
		}
		
		if (obj1 != null && obj2 == null) {
			return false;
		}
		
		if (obj1 == obj2) {
			return true;
		}
		
		if (obj1.equals(obj2)) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		//System.out.println(getPriceYuanValue(3.54));
	}

	
}