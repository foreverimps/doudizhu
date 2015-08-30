package com.zj.platform.gamecenter.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring ApplicationContext工具类. <br>
 * 获取web应用的applicationContext及其管理的bean.
 * <p>
 * Copyright: Copyright (c) 2014年7月23日 上午11:01:19
 * <p>
 * Company: 苏州米谷网络科技有限公司
 * <p>
 * 
 * @author loujinhe@migu99.com
 * @version 1.0.0
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ApplicationContextUtil.applicationContext == null) {
			ApplicationContextUtil.applicationContext = applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取applicationContext中的bean
	 * 
	 * @param name
	 *            beanId
	 * @param requiredType
	 *            bean类型
	 * @return bean
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}

	/**
	 * 获取applicationContext中的bean
	 * 
	 * @param name
	 *            beanId
	 * @param requiredType
	 *            bean类型
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}

	/**
	 * 获取applicationContext中的bean
	 * 
	 * @param name
	 *            beanId
	 * @param requiredType
	 *            bean类型
	 * @return bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}
}
