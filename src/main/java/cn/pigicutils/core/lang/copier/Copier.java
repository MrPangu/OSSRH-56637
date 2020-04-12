package cn.pigicutils.core.lang.copier;

/**
 * 拷贝接口
 * @author guchang.pan@hand-china.com
 *
 * @param <T> 拷贝目标类型
 */
public interface Copier<T> {
	/**
	 * 执行拷贝
	 * @return 拷贝的目标
	 */
	T copy();
}
