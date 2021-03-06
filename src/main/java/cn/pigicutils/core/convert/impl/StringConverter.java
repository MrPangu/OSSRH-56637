package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;

/**
 * 字符串转换器
 * @author guchang.pan@hand-china.com
 *
 */
public class StringConverter extends AbstractConverter<String>{
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
