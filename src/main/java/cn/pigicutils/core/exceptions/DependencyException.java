package cn.pigicutils.core.exceptions;

import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 依赖异常
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class DependencyException extends CommonException {
	private static final long serialVersionUID = 8247610319171014183L;

	public DependencyException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public DependencyException(String message) {
		super(message);
	}

	public DependencyException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public DependencyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DependencyException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
