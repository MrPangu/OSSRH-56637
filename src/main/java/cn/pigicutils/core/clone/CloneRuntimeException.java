package cn.pigicutils.core.clone;

import cn.pigicutils.core.exceptions.ExceptionUtil;
import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 克隆异常
 * @author guchang.pan@hand-china.com
 */
public class CloneRuntimeException extends CommonException {
	private static final long serialVersionUID = 6774837422188798989L;

	public CloneRuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}
	
	public CloneRuntimeException(String message) {
		super(message);
	}
	
	public CloneRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public CloneRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public CloneRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
