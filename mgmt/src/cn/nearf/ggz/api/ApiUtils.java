package cn.nearf.ggz.api;

import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.PropertiesUtil;


import cn.nearf.ggz.config.ErpConfig;
import cn.nearf.ggz.config.SysConfig;
import cn.nearf.ggz.utils.DateUtils;
import cn.nearf.ggz.utils.MD5Utils;

public class ApiUtils {
	
	private static final int TokenBegin = 5;
	private static final int TokenEnd = 10;

	private static String privateStr;
	private static final String TEST_AUTH_KEY = "TEST4DEVAUTHKEYSUPERMANVSBATMAN";
	
	public static String getPostStringFromRequest(HttpServletRequest request) throws Exception {
		ApiRequestWrapper requestWrapper = new ApiRequestWrapper(request);
		return requestWrapper.getBody();
	}

	public static void writeResponse(HttpServletResponse response, String content) {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("expires", "0");
		response.setCharacterEncoding("UTF-8");

		//
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(content);
		} catch (Exception e) {
		}
		if (pw != null) {
			pw.flush();
		}
	}


	// 验证
	public static boolean verifyAuthKey(final String publicKey, final String authTokenPart, final String version, final long time, final int tokenBegin, final int tokenEnd) {
		if (ErpConfig.getBooleanProperty("STRICT_API")) {
			if (Math.abs(System.currentTimeMillis() - time) > ErpConfig.getLongProperty("API_Timeout") * 60 * 1000l) {
				return false;
			}
		}
		
		Date date = new Date(time);
		String timestamp = "";
		try {
			timestamp = DateUtils.getDateStringWithFormat(date, "yyyyMMddHHmmssSSS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String v = version;
		if (v == null) {
			v = "";
		}
		
		// 对数据作比较
		String s = publicKey + timestamp + getPrivateStr() + v;
		String md5_s = MD5Utils.MD5(s);
		String v_Key = md5_s.substring(tokenBegin, tokenEnd);

		if (v_Key.equalsIgnoreCase(authTokenPart)) {
			return true;
		}

		return false;
	}

	public static boolean simpleVerifyAuthToken(long time, String authToken, String version) {
		if (authToken == null || authToken.trim().length() == 0) {
			return false;
		}
		
		if (time < 0) {
			return false;
		}

		if (ErpConfig.isDebug() && authToken.equals(TEST_AUTH_KEY)) {
			return true;
		}
		
		String[] auths = authToken.split("\\_");
		if (auths.length != 2) {
			return false;
		}
		
		return verifyAuthKey(auths[0], auths[1], version, time, TokenBegin, TokenEnd);
	}


	private static synchronized String getPrivateStr() {
		if (privateStr == null) {
			privateStr = SysConfig.getStringProperty("AuthToken_Private");
		}
		return privateStr;
	}
	
	
	//generate
	//
	public static String generateAuthToken(final String privateKey, final String publicKey, final String version, final Date date, final int tokenBegin, final int tokenEnd) {
		SimpleDateFormat simpleDateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = simpleDateFormater.format(date);
		
		String v = version;
		if (v == null) {
			v = "";
		}
		String s = publicKey + time + privateKey + v;
		String md5_s = MD5Utils.MD5(s);
		String v_Key = md5_s.substring(tokenBegin, tokenEnd);
		return publicKey + "_" + v_Key;
	}

	public static String generateAuthToken(final String privateKey, final String publicKey, final String version, final long timestamp, final int tokenBegin, final int tokenEnd) {
		return generateAuthToken(privateKey, publicKey, version, new Date(timestamp), tokenBegin, tokenEnd);
	}
	
	
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		String pk = getPrivateStr();
		String pubK = "test123";
		
		String token = generateAuthToken(pk, pubK, "1.0", now, TokenBegin, TokenEnd);
		System.err.println(token);
		System.err.println(simpleVerifyAuthToken(now, token, "1.0"));
	}
}
