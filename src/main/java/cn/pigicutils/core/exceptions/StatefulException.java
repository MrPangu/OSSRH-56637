package cn.pigicutils.core.exceptions;

import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 带有状态码的异常
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class StatefulException extends CommonException {
	private static final long serialVersionUID = 6057602589533840889L;

	// 异常状态码
	private int status;

	public StatefulException(String msg) {
		super(msg);
	}

	public StatefulException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public StatefulException(Throwable throwable) {
		super(throwable);
	}

	public StatefulException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public StatefulException(int status, String msg) {
		super(msg);
		this.status = status;
	}

	public StatefulException(int status, Throwable throwable) {
		super(throwable);
		this.status = status;
	}

	public StatefulException(int status, String msg, Throwable throwable) {
		super(msg, throwable);
		this.status = status;
	}

	/**
	 * @return 获得异常状态码
	 */
	public int getStatus() {
		return status;
	}
}
