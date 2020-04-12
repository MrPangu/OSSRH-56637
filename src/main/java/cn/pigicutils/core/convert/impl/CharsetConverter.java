package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;
import cn.pigicutils.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 * @author guchang.pan@hand-china.com
 *
 */
public class CharsetConverter extends AbstractConverter<Charset>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
