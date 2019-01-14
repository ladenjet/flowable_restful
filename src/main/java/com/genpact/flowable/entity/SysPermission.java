package com.genpact.flowable.entity;

import java.io.Serializable;

/**
 * @author sxia
 * @Description: TODO()
 * @date 2019-01-14 14:46:13
 */
public class SysPermission implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//
	private String name;
	//
	private String description;
	//
	private String url;
	//
	private Long pid;

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
	/**
	 * 设置：
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：
	 */
	public Long getPid() {
		return pid;
	}

}
