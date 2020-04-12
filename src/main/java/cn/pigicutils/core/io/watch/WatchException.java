package cn.pigicutils.core.io.watch;

import cn.pigicutils.core.exceptions.ExceptionUtil;
import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 监听异常
 * @author guchang.pan@hand-china.com
 *
 */
public class WatchException extends CommonException {
	private static final long serialVersionUID = 8068509879445395353L;

	public WatchException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public WatchException(String message) {
		super(message);
	}

	public WatchException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public WatchException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public WatchException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
