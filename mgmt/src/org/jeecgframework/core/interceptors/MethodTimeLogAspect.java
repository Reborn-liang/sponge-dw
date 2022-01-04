package org.jeecgframework.core.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Aspect
public class MethodTimeLogAspect implements HandlerInterceptor {

	private Logger logger = Logger.getLogger(MethodTimeLogAspect.class);

	private long startTs = 0l;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
		long ts = System.currentTimeMillis() - startTs;
		String url = request.getRequestURI() + "?";
		try {
			url += request.getQueryString().split("&")[0];
		} catch (Exception e) {
			url +=  request.getQueryString();
		}
		logger.info(url + " time=" + ts + "ms");
//		System.out.println("输出:" + url + "; 执行时间：" + ts + "ms.");
//		System.out.println("===========执行后置通知============");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
//		System.out.println("=========执行前置通知==========");
		startTs =  System.currentTimeMillis();
		return true;
	}
	
	
}