package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;

import java.util.UUID;

/**
 * UUID对象转换器转换器
 * 
 * @author guchang.pan@hand-china.com
 *
 *
 */
public class UUIDConverter extends AbstractConverter<UUID> {
	private static final long serialVersionUID = 1L;

	@Override
	protected UUID convertInternal(Object value) {
		return UUID.fromString(convertToStr(value));
	}

}
