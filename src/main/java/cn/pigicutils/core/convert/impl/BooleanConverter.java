package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.util.BooleanUtil;

/**
 * 波尔转换器
 * @author guchang.pan@hand-china.com
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Boolean convertInternal(Object value) {
		if(boolean.class == value.getClass()){
			return Boolean.valueOf((boolean)value);
		}
		String valueStr = convertToStr(value);
		return Boolean.valueOf(BooleanUtil.toBoolean(valueStr));
	}

}
