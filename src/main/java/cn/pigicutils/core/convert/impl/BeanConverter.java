package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.bean.BeanUtil;
import cn.pigicutils.core.bean.copier.BeanCopier;
import cn.pigicutils.core.bean.copier.CopyOptions;
import cn.pigicutils.core.bean.copier.ValueProvider;
import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.map.MapProxy;
import cn.pigicutils.core.util.ReflectUtil;
import cn.pigicutils.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean转换器，支持：
 * <pre>
 * Map =》 Bean
 * Bean =》 Bean
 * ValueProvider =》 Bean
 * </pre>
 * 
 * @param <T> Bean类型
 * @author guchang.pan@hand-china.com
 *
 */
public class BeanConverter<T> extends AbstractConverter<T> {
	private static final long serialVersionUID = 1L;

	private Type beanType;
	private Class<T> beanClass;
	private CopyOptions copyOptions;
	
	/**
	 * 构造，默认转换选项，注入失败的字段忽略
	 * 
	 * @param beanType 转换成的目标Bean类型
	 */
	public BeanConverter(Type beanType) {
		this(beanType, CopyOptions.create().setIgnoreError(true));
	}

	/**
	 * 构造，默认转换选项，注入失败的字段忽略
	 * 
	 * @param beanClass 转换成的目标Bean类
	 */
	public BeanConverter(Class<T> beanClass) {
		this(beanClass, CopyOptions.create().setIgnoreError(true));
	}
	
	/**
	 * 构造
	 * 
	 * @param beanType 转换成的目标Bean类
	 * @param copyOptions Bean转换选项参数
	 */
	@SuppressWarnings("unchecked")
	public BeanConverter(Type beanType, CopyOptions copyOptions) {
		this.beanType = beanType;
		this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
		this.copyOptions = copyOptions;
	}

	@Override
	protected T convertInternal(Object value) {
		if(value instanceof Map || value instanceof ValueProvider || BeanUtil.isBean(value.getClass())) {
			if(value instanceof Map && this.beanClass.isInterface()) {
				// 将Map动态代理为Bean
				return MapProxy.create((Map<?, ?>)value).toProxyBean(this.beanClass);
			}
			
			//限定被转换对象类型
			return BeanCopier.create(value, ReflectUtil.newInstanceIfPossible(this.beanClass), this.beanType, this.copyOptions).copy();
		}
		return null;
	}

	@Override
	public Class<T> getTargetType() {
		return this.beanClass;
	}
}
