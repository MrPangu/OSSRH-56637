/**
 * 姓名：陆梦琳
 *下午10:07:02
 */
package com.pigic.hzeropigic;

import com.pigic.hzeropigic.utils.BaseBean;

public class Person extends BaseBean {
	private String name;
	private Integer age;
	
	
	/**
	 * 
	 */
	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param name
	 * @param age
	 */
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	@Pigict(value = 5, name = "张三")
	public String sayHello(){
		return "你好："+name;
	}
}
