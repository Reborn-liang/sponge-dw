package cn.nearf.ggz.api;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.nearf.ggz.config.SysConfig;
import cn.nearf.ggz.utils.gson.GsonUtils;
import net.sf.json.JSONObject;

public class BaseApiFilter implements Filter {

	private static final String[] NoNeedAuthApi = {
		"/api/wechat/",
		"/api/alipay/",
		"/api/eleme/",
		"/api/meituanwm/",
		"/api/pos/checkVersion",
		"/api/pos/getServerTime",
		"/api/member/memberSecret",
	};

	public MessageJson check(String jsonString, HttpServletRequest req) {
		if (req.getRequestURI().toLowerCase().indexOf(".do") > 0) {
			return MessageJson.newInstanceWithError("");
		}
		
		if (!req.getMethod().equalsIgnoreCase("post") && !req.getMethod().equalsIgnoreCase("get")) {
			return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_UNSUPPORTED_METHOD);
		}
		
		for (String url : NoNeedAuthApi) {
			if (req.getRequestURI().indexOf(url) >= 0) {
				return MessageJson.newInstance("OK");
			}
		}
		
		String timestamp = req.getParameter("time");
		String authToken = req.getParameter("authtoken");
		String version = req.getParameter("ver");
		String source = req.getParameter("source");
		
		JSONObject jsonObject = null;
		try {
			if (timestamp == null) {
				if (jsonObject == null) {
					jsonObject = JSONObject.fromObject(jsonString);
				}
				timestamp = jsonObject.get("time").toString();
			}
		} catch (Exception e) {
		}
		
		try {
			if (authToken == null) {
				if (jsonObject == null) {
					jsonObject = JSONObject.fromObject(jsonString);
				}
				authToken = jsonObject.get("authtoken").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if (timestamp == null || timestamp.trim().length() == 0) {
			return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_REQUESTED_TIME_IS_EMPTY);
		}
		
		long time = 0;
		try {
			time = Long.valueOf(timestamp);
		} catch (Exception e) {
			return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_REQUESTED_TIME_IS_EMPTY);
		}
		
		if (authToken == null || authToken.trim().length() == 0) {
			return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_REQUESTED_AUTUTOKEN_IS_EMPTY);
		}
		
		//目前只能对html进行限制
		if (source != null && source.toLowerCase().equals("html")) {
			if (version == null || version.length() == 0 || !version.equals(SysConfig.getStringProperty("API_Version"))) {
				return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_INVALID_VERSION);
			}
		}
		
		if (!ApiUtils.simpleVerifyAuthToken(time, authToken, version)) {
			return MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_INVALID_AUTHTOKEN);
		}

		return MessageJson.newInstance("OK");
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		ApiRequestWrapper request = new ApiRequestWrapper((HttpServletRequest) req);
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String contentType = request.getContentType();
		if (contentType == null || contentType.trim().length() <= 0) {
			request.setAttribute("Content-Type", "application/json");
		}
		
		MessageJson msg;
		try {
			String postString = request.getBody();
			msg = check(postString, request);
		} catch (Exception e) {
			e.printStackTrace();
			msg = MessageJson.newInstanceWithCode(MessageJson.RESULT_CODE_SYSTEM_ERROR);
			msg.appendMessage(e.toString());
		}
		
		if (!msg.isSuccess()) {
			ApiUtils.writeResponse(response, GsonUtils.toJson(msg));
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
