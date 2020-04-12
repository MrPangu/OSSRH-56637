package cn.pigicutils.core.bean;

import cn.pigicutils.core.exceptions.ExceptionUtil;
import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * Bean异常
 * @author guchang.pan@hand-china.com
 */
public class BeanException extends CommonException {
	private static final long serialVersionUID = -8096998667745023423L;

	public BeanException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}
	
	public BeanException(String message) {
		super(message);
	}
	
	public BeanException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public BeanException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public BeanException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
