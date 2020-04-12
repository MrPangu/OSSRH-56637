package com.pigic.hzeropigic.utils;

import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

public class ReflectException extends CommonException {
	private static final long serialVersionUID = 1L;

	public ReflectException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public ReflectException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ReflectException(String detailMessage) {
		super(detailMessage);
	}

	public ReflectException(Throwable throwable) {
		super(throwable);
	}
	
}
