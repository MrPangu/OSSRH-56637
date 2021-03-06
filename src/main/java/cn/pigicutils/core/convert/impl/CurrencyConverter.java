package cn.pigicutils.core.convert.impl;

import cn.pigicutils.core.convert.AbstractConverter;

import java.util.Currency;

/**
 * 货币{@link Currency} 转换器
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class CurrencyConverter extends AbstractConverter<Currency> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Currency convertInternal(Object value) {
		return Currency.getInstance(convertToStr(value));
	}

}
