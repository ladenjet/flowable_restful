package com.genpact.flowable.utils;

import java.util.Map;
/**
 * @author 710009498
 *
 */
public class FlowableModel {
	private String deploymentId;
	private String processInstanceId;
	private String processDefinitionId;
	private String processKey;
	private Map<String,Object> variables;
	private String taskId;
	private String common;
	private String businessKey;
//	private String tenantId;
	private String userId;
	/**
	 *任务转办人
	 */
	private String transferAssignee;

	/**
	 * 任务委托人
	 */
	private String delegateAssignee;


	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String buinessId) {
		this.businessKey = buinessId;
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
	

//	public String getTenantId() {
//		return tenantId;
//	}
//
//	public void setTenantId(String tenantId) {
//		this.tenantId = tenantId;
//	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	


	


}
