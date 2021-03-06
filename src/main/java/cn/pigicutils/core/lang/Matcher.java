package cn.pigicutils.core.lang;

/**
 * 匹配接口
 * @author guchang.pan@hand-china.com
 *
 * @param <T> 匹配的对象类型
 */
public interface Matcher<T>{
	/**
	 * 给定对象是否匹配
	 * @param t 对象
	 * @return 是否匹配
	 */
	public boolean match(T t);
}
