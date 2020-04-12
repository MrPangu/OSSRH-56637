package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.map.MapUtil;
import cn.pigicutils.core.util.ObjectUtil;

import java.util.Map;

/**
 * {@link StackTraceElement} 转换器<br>
 * 只支持Map方式转换
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class StackTraceElementConverter extends AbstractConverter<StackTraceElement> {
	private static final long serialVersionUID = 1L;

	@Override
	protected StackTraceElement convertInternal(Object value) {
		if (value instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>) value;

			final String declaringClass = MapUtil.getStr(map, "className");
			final String methodName = MapUtil.getStr(map, "methodName");
			final String fileName = MapUtil.getStr(map, "fileName");
			final Integer lineNumber = MapUtil.getInt(map, "lineNumber");

			return new StackTraceElement(declaringClass, methodName, fileName, ObjectUtil.defaultIfNull(lineNumber, 0));
		}
		return null;
	}

}
