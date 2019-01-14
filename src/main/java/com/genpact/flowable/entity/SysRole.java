package com.genpact.flowable.entity;

import java.io.Serializable;

/**
 * @author sxia
 * @Description: TODO()
 * @date 2019-01-14 14:46:14
 */
public class SysRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//
	private String name;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}

}
