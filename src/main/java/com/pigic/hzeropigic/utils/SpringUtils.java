package com.pigic.hzeropigic.utils;/***
 * create by lvyz on 2019/5/30
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringUtils
 * @Desciption 获取SpringBean工具类
 *             --- PS: 仅在非Spring管理的Class中使用-仅key获取本项目中的Bean
 * @author guchang.pan@hand-china.com
 *
 **/
@Component
@SuppressWarnings("unchecked")
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
		}
	}

	/**
	 * 获取applicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	/**
	 * 通过name获取 Bean.
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}


	/**
	 * 通过class获取Bean.（常用）
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}


	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

}
