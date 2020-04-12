package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.util.ClassUtil;

/**
 * 类转换器<br>
 * 将类名转换为类
 * @author guchang.pan@hand-china.com
 *
 */
public class ClassConverter extends AbstractConverter<Class<?>> {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Class<?> convertInternal(Object value) {
		String valueStr = convertToStr(value);
		try {
			return ClassUtil.getClassLoader().loadClass(valueStr);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
