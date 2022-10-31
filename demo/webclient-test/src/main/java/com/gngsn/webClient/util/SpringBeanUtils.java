package com.gngsn.webClient.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * SpringBeanUtils.
 */
public final class SpringBeanUtils {

	private static final SpringBeanUtils INSTANCE = new SpringBeanUtils();

	private ApplicationContext applicationContext;

	private SpringBeanUtils() {
	}

	/**
	 * get SpringBeanUtils.
	 *
	 * @return SpringBeanUtils instance
	 */
	public static SpringBeanUtils getInstance() {
		return INSTANCE;
	}

	/**
	 * acquire spring bean.
	 *
	 * @param <T>  class
	 * @param type type
	 * @return bean bean
	 */
	public <T> T getBean(final Class<T> type) {
		return applicationContext.getBean(type);
	}

	/**
	 * Gets bean.
	 *
	 * @param <T>      the type parameter
	 * @param beanName the bean name
	 * @return the bean
	 */
	@SuppressWarnings("all")
	public <T> T getBean(final String beanName) {
		return (T) applicationContext.getBean(beanName);
	}

	/**
	 * Register bean.
	 *
	 * @param beanDefinition the bean definition
	 * @param classLoader    the class loader
	 * @return the string
	 */
	public String registerBean(final BeanDefinition beanDefinition, final ClassLoader classLoader) {
		String beanClassName = beanDefinition.getBeanClassName();
		if (StringUtils.isBlank(beanClassName)) {
			throw new NullPointerException("beanDefinition.beanClassName is null");
		}
		String beanName = getBeanName(beanClassName);
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);
		beanFactory.registerBeanDefinition(beanName, beanDefinition);
		return beanName;
	}

	/**
	 * Exist spring bean boolean.
	 * Only applies to successful beans registered by the {@linkplain #registerBean(BeanDefinition, ClassLoader)} method.
	 *
	 * @param className the class name
	 * @return the boolean
	 */
	public boolean existBean(final String className) {
		String beanName = this.getBeanName(className);
		return this.applicationContext.containsBean(beanName);
	}

	/**
	 * Get bean boolean.
	 * Only applies to successful beans registered by the {@linkplain #registerBean(BeanDefinition, ClassLoader)} method.
	 *
	 * @param <T>       the type parameter
	 * @param className the class name
	 * @return boolean bean by class name
	 */
	@SuppressWarnings("all")
	public <T> T getBeanByClassName(final String className) {
		String beanName = this.getBeanName(className);
		try {
			return this.getBean(beanName);
		} catch (BeansException e) {
			return null;
		}
	}

	private String getBeanName(final String className) {
		String name = className.substring(className.lastIndexOf(".") + 1);
		String start = name.substring(0, 1);
		String end = name.substring(1);
		return start.toLowerCase() + end;
	}

	/**
	 * set application context.
	 *
	 * @param applicationContext application context
	 */
	public void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
