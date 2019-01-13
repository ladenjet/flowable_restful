package com.genpact.flowable.entity;

import java.util.Map;

public class RequestModel {
	private String taskId;
	private String userId;
	private String processDefinitionId;
	private String deploymentId;
	private String common;
//	private EnumFlowButton button;
	/**
	 * true,false
	 */
	private String button;

	private Map<String,Object> variables;

	private String buinessId;
	/**
	 *任务转办人
	 */
	private String transferAssignee;

	/**
	 * 任务委托人
	 */
	private String delegateAssignee;

	/**
	 *任务类型 taskAssignee,delegateTask
	 */
	private String taskType;

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
//	public EnumFlowButton getButton() {
//		return button;
//	}
//	public void setButton(EnumFlowButton button) {
//		this.button = button;
//	}


	public String getBuinessId() {
		return buinessId;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public void setBuinessId(String buinessId) {
		this.buinessId = buinessId;
	}
	public String getTransferAssignee() {
		return transferAssignee;
	}
	public void setTransferAssignee(String transferAssignee) {
		this.transferAssignee = transferAssignee;
	}
	public String getDelegateAssignee() {
		return delegateAssignee;
	}
	public void setDelegateAssignee(String delegateAssignee) {
		this.delegateAssignee = delegateAssignee;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}





}
//enum EnumFlowButton{
//	APPROVE(0,"同意"),REJECT(1,"驳回");
//	private int code;
//	private String msg;
//	private EnumFlowButton(int code,String msg) {
//		this.code = code;
//		this.msg = msg;
//	}
//	public int getCode() {
//		return code;
//	}
//	public void setCode(int code) {
//		this.code = code;
//	}
//	public String getMsg() {
//		return msg;
//	}
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//}