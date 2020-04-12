package cn.pigicutils.core.comparator;

import cn.pigicutils.core.exceptions.ExceptionUtil;
import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 比较异常
 * @author guchang.pan@hand-china.com
 */
public class ComparatorException extends CommonException {
	private static final long serialVersionUID = 4475602435485521971L;

	public ComparatorException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}
	
	public ComparatorException(String message) {
		super(message);
	}
	
	public ComparatorException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public ComparatorException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ComparatorException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
