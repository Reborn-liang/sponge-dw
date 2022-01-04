package org.jeecgframework.core.util;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 
 * @author  张代浩
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
	
	public static String getMessage(String key) {
		return ApplicationContextUtil.getContext().getMessage(key, null, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String key, Object[] params) {
		return ApplicationContextUtil.getContext().getMessage(key, params, LocaleContextHolder.getLocale());
	}
	
	public static String getMessage(String key, Object[] params, Locale locale) {
		return ApplicationContextUtil.getContext().getMessage(key, params, locale);
	}
}
