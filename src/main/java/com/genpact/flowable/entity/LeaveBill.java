package com.genpact.flowable.entity;

import java.io.Serializable;
public class LeaveBill implements Serializable{

	private static final long serialVersionUID = 1L;


	private Long id;

	private Long userId;

	/**
	 * 请假人信息
	 */
	private User user;

	/**
	 * 请假原因
	 */
	private String reason;

	/**
	 * 备注信息
	 */
	private String remark;


	/**
	 * 请假天数
	 */
	private int leaveDays;


	/**
	 * 流程状态
	 * 0 草稿
	 * 1 审批中
	 * 2 审批完成
	 */
	private int state = 0;

	/**
	 * delete flag  default = 0;
	 */
	private int deleteFlag = 0;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public int getLeaveDays() {
		return leaveDays;
	}


	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public int getDeleteFlag() {
		return deleteFlag;
	}


	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}


	@Override
	public String toString() {
		return "LeaveBill [id=" + id + ", user=" + user + ", reason=" + reason + ", remark=" + remark + ", leaveDays=" + leaveDays + ", state=" + state + "]";
	}


	public LeaveBill( User user, String reason, String remark, int leaveDays) {
		this.user = user;
		this.reason = reason;
		this.remark = remark;
		this.leaveDays = leaveDays;
	}

	public LeaveBill() {
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}



}
