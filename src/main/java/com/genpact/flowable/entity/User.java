package com.genpact.flowable.entity;

import java.io.Serializable;


public class User implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long id;

	private String name;

	private String password;

	//
	private Long managerId;
	//
	private Integer deleteFlag;
	//
	private Integer hrId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Integer getHrId() {
		return hrId;
	}
	public void setHrId(Integer hrId) {
		this.hrId = hrId;
	}




}
