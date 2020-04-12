package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.util.BooleanUtil;
import cn.pigicutils.core.util.StrUtil;

/**
 * 字符转换器
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class CharacterConverter extends AbstractConverter<Character> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Character convertInternal(Object value) {
		if (char.class == value.getClass()) {
			return Character.valueOf((char) value);
		} else if (value instanceof Boolean) {
			return BooleanUtil.toCharacter((Boolean) value);
		} else if (boolean.class == value.getClass()) {
			return BooleanUtil.toCharacter((boolean) value);
		} else {
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				return Character.valueOf(valueStr.charAt(0));
			}
		}
		return null;
	}

}
