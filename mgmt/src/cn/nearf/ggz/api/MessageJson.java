package cn.nearf.ggz.api;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

import cn.nearf.ggz.utils.StringUtils;

public class MessageJson implements Serializable {

	private static final long serialVersionUID = -5755057103197751458L;
	
	public static final int RESULT_CODE_OK = 1;
	public static final int RESULT_CODE_SYSTEM_ERROR = 0;
	public static final int RESULT_CODE_UNSUPPORTED_METHOD = -1;
	public static final int RESULT_CODE_REQUESTED_TIME_IS_EMPTY = -2;
	public static final int RESULT_CODE_INVALID_TIME = -3;
	public static final int RESULT_CODE_REQUESTED_AUTUTOKEN_IS_EMPTY = -4;
	public static final int RESULT_CODE_INVALID_AUTHTOKEN = -5;
	public static final int RESULT_CODE_INVALID_PARAMETERS = -6;
	public static final int RESULT_CODE_INVALID_VERSION = -8;

	public static final Map<Integer, String> RESULT_MAP = new LinkedHashMap<Integer, String>() {
		private static final long serialVersionUID = -6825577953107746289L;
		{
			put(RESULT_CODE_OK, "request successed.");
			put(RESULT_CODE_SYSTEM_ERROR, "System error.");
			put(RESULT_CODE_UNSUPPORTED_METHOD, "Unsupported method.");
			put(RESULT_CODE_REQUESTED_TIME_IS_EMPTY, "Requested time is empty.");
			put(RESULT_CODE_INVALID_TIME, "invalid time.");
			put(RESULT_CODE_REQUESTED_AUTUTOKEN_IS_EMPTY, "Requested data is Illegal.");
			put(RESULT_CODE_INVALID_AUTHTOKEN, "Invalid auth token.");
			put(RESULT_CODE_INVALID_PARAMETERS, "Invalid parameters.");
			put(RESULT_CODE_INVALID_VERSION, "Invalid version.");
		}
	};
	
	public static MessageJson newInstanceWithError(Throwable error) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		if (StringUtils.isNotEmpty(error.getLocalizedMessage())) {
			msgJson.message = error.getLocalizedMessage();
		} else  {
			msgJson.message = error.toString();
		}
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(String errMsg) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		msgJson.message = errMsg;
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(int code, String errMsg, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = - Math.abs(code);
		msgJson.message = errMsg;
		msgJson.results = result;
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(String errMsg, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		msgJson.message = errMsg;
		msgJson.results = result;
		return msgJson;
	}
	
	public static MessageJson newInstanceWithCode(int code) {
		MessageJson msgJson = new MessageJson();
		msgJson.setCode(code);
		return msgJson;
	}
	
	public static MessageJson newInstance(Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.results = result;
		return msgJson;
	}
	
	public static MessageJson newInstance(int code, String message, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = code;
		msgJson.message = message;
		msgJson.results = result;
		return msgJson;
	}
	
	public static MessageJson newInstance(String message, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.message = message;
		msgJson.results = result;
		return msgJson;
	}
	
	public static MessageJson newInstanceWithMessage(String message) {
		MessageJson msgJson = new MessageJson();
		msgJson.message = message;
		return msgJson;
	}

	private int code;

	private String message;

	private Object results;

	public MessageJson() {
		setCode(RESULT_CODE_OK);
	}

	public int getCode() {
		return code;
	}

	public boolean isSuccess() {
		return code > 0;
	}
	
	public void setIsSuccess(boolean isSuccess) {
		if (isSuccess) {
			code = 1;
		} else {
			code = -1;
		}
	}
	
	public void appendMessage(String appendMessage) {
		this.message += ": " + appendMessage;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
		if (RESULT_MAP.containsKey(code)) {
			message = RESULT_MAP.get(code);
		}
	}

	public <T extends Object> T getResults() {
		return (T) results;
	}

	public void setResults(Object results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}