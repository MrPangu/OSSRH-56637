package cn.pigicutils.core.exceptions;

import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 工具类异常
 * @author guchang.pan@hand-china.com
 */
public class UtilException extends CommonException {
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UtilException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
