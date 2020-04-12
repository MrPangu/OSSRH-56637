package cn.pigicutils.core.exceptions;

import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 未初始化异常
 * 
 * @author guchang.pan@hand-china.com
 */
public class NotInitedException extends CommonException {
	private static final long serialVersionUID = 8247610319171014183L;

	public NotInitedException(Throwable e) {
		super(e);
	}

	public NotInitedException(String message) {
		super(message);
	}

	public NotInitedException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public NotInitedException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public NotInitedException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
