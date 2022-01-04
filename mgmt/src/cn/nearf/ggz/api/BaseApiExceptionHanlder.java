package cn.nearf.ggz.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.exception.GlobalExceptionResolver;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.JSONHelper;
import org.springframework.web.servlet.ModelAndView;


public class BaseApiExceptionHanlder extends GlobalExceptionResolver {

	private static final Logger logger = Logger.getLogger(BaseApiExceptionHanlder.class);
	
	private MessageJson errorJson;


	public MessageJson getErrorJson() {
		return errorJson;
	}

	public String getErrorMessage() {
		return errorJson.toString();
	}

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String requestPath = request.getRequestURI();
		if (requestPath.indexOf("/api/") < 0 && requestPath.indexOf("/innerApi/") < 0) {
			return super.resolveException(request, response, handler, ex);
		}
		
		String exceptionMessage = ExceptionUtil.getExceptionMessage(ex);
		logger.error(exceptionMessage);
		
		errorJson = new MessageJson();
		errorJson.setCode(MessageJson.RESULT_CODE_SYSTEM_ERROR);
		errorJson.appendMessage(ex.getLocalizedMessage());
		
		//ApiUtils.writeResponse(response, getErrorMessage());
		
		
		ModelAndView empty = new ModelAndView();
		// response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter pw = response.getWriter();
			pw.write(JSONHelper.bean2json(errorJson));
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		empty.clear();
		return empty;
	}
}
