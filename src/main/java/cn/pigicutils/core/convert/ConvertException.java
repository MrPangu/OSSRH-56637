package cn.pigicutils.core.convert;

import cn.pigicutils.core.exceptions.ExceptionUtil;
import cn.pigicutils.core.util.StrUtil;
import io.choerodon.core.exception.CommonException;

/**
 * 转换异常
 * @author guchang.pan@hand-china.com
 */
public class ConvertException extends CommonException {
	private static final long serialVersionUID = 4730597402855274362L;

	public ConvertException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}
	
	public ConvertException(String message) {
		super(message);
	}
	
	public ConvertException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public ConvertException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ConvertException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
