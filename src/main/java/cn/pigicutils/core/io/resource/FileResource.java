package cn.pigicutils.core.io.resource;

import cn.pigicutils.core.io.FileUtil;
import cn.pigicutils.core.util.StrUtil;
import cn.pigicutils.core.util.URLUtil;

import java.io.File;
import java.nio.file.Path;

/**
 * 文件资源访问对象
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class FileResource extends UrlResource {
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param path 文件
	 *
	 */
	public FileResource(Path path) {
		this(path.toFile());
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 */
	public FileResource(File file) {
		this(file, file.getName());
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param fileName 文件名，如果为null获取文件本身的文件名
	 */
	public FileResource(File file, String fileName) {
		super(URLUtil.getURL(file), StrUtil.isBlank(fileName) ? file.getName() : fileName);
	}

	/**
	 * 构造
	 * 
	 * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
	 */
	public FileResource(String path) {
		this(FileUtil.file(path));
	}
	// ----------------------------------------------------------------------- Constructor end

}
