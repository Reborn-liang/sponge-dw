package cn.nearf.ggz.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecgframework.core.util.StringUtil;

public class StringUtils extends StringUtil {

	public static List<Integer> getIdsIntegers(String[] ids) {
		List<Integer> intIds = new ArrayList<Integer>();
		if (ids != null) {
			for (String id : ids) {
				try {
					int intId = ObjectUtils.getIntValue(id.trim());
					if (!intIds.contains(intId)) {
						intIds.add(intId);
					}
				} catch (Exception e) {
				}
			}
		}
		return intIds;
	}
	
	public static String getIdsString(String ids) {
		try {
			if (ids != null) {
				ids = ids.trim();
				if (ids.length() > 0) {
					if (ids.indexOf(",") != 0) {
						ids = "," + ids;
					}
					if (ids.lastIndexOf(",") != ids.length() - 1) {
						ids = ids + ",";
					}
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static String getIdsString(HashSet<Integer> ids){
		if(ids == null || ids.size() == 0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		
		Integer[] result = new Integer[ids.size()];
		ids.toArray(result);
		if (result != null && result.length > 0) {
			sb.append(result[0]);
			for (int i = 1; i < result.length; i++) {
				Integer id = result[i];
				sb.append("," + id );
			}
		}
		return sb.toString();
	}
	
	public static String getIdsString(String[] ids) {
		StringBuilder sb = new StringBuilder();
		try {
			if (ids != null) {
				for (String id : ids) {
					try {
						sb.append("," + id.trim());
					} catch (Exception ex) {
					}
				}
				if (sb.length() > 0) {
					sb.append(",");
				}
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	public static String getIdsString(List<String> ids) {
		StringBuilder sb = new StringBuilder();
		try {
			if (ids != null) {
				for (String id : ids) {
					try {
						sb.append("," + id.trim());
					} catch (Exception ex) {
					}
				}
				if (sb.length() > 0) {
					sb.append(",");
				}
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	public static List<String> getIdsFromIdsString(String idsString) {
		List<String> ids = new ArrayList<String>();
		try {
			String[] idsStrs = idsString.trim().split(",");
			for (String id : idsStrs) {
				if (isNotEmpty(id)) {
					ids.add(id);
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static List<Integer> getIntegerIdsFromIdsString(String idsString) {
		List<Integer> ids = new ArrayList<Integer>();
		try {
			String[] idsStrs = idsString.trim().split(",");
			for (String id : idsStrs) {
				if (isNotEmpty(id)) {
					try {
						ids.add(Integer.valueOf(id));
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static String getPermissionIdsString(List<String> stockroomIds){
		if(stockroomIds == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		
		String[] result = new String[stockroomIds.size()];
		stockroomIds.toArray(result);
		if (result != null && result.length > 0) {
			sb.append("'" + result[0] + "'");
			for (int i = 1; i < result.length; i++) {
				String id = result[i];
				sb.append(", '" + id + "'");
			}
		}
		return sb.toString();
	}
	
	public static boolean isStringInvalid(final String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		}
		String strTrm = str.trim();
		if (strTrm.equalsIgnoreCase("null") || strTrm.equalsIgnoreCase("undefined") || strTrm.equalsIgnoreCase("invalid") ) {
			return true;
		}
		return false;
	}
	
	public static boolean hasEmojiChar(final String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		try {
			int len = str.length();
	        boolean isEmoji = false;
	        for (int i = 0; i < len; i++) {
	            char hs = str.charAt(i);
	            if (0xd800 <= hs && hs <= 0xdbff) {
	                if (str.length() > 1) {
	                    char ls = str.charAt(i + 1);
	                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
	                    if (0x1d000 <= uc && uc <= 0x1f77f) {
	                        return true;
	                    }
	                }
	            } else {
	                // non surrogate
	                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
	                    return true;
	                } else if (0x2B05 <= hs && hs <= 0x2b07) {
	                    return true;
	                } else if (0x2934 <= hs && hs <= 0x2935) {
	                    return true;
	                } else if (0x3297 <= hs && hs <= 0x3299) {
	                    return true;
	                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
	                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
	                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
	                    return true;
	                }
	                if (!isEmoji && str.length() > 1 && i < str.length() - 1) {
	                    char ls = str.charAt(i + 1);
	                    if (ls == 0x20e3) {
	                        return true;
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
		}
		return false;
	}
	
	public static String getNumPart(String s) {
		if(s == null){
			return "";
		}
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(s);
		return matcher.replaceAll("");
	}
	
	public static void main(String[] args) {
		System.out.println(hasEmojiChar("123abc0️⃣〇"));
//		System.out.println(hasEmojiChar("��都嗨��、齐静��给你��"));
	}
}
