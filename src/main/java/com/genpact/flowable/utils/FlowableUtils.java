package com.genpact.flowable.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.genpact.flowable.entity.Constant;
import com.genpact.flowable.exception.FlowableException;

public class FlowableUtils {
	
	
	public static Deployment deployment(FlowableModel model, ProcessEngine processEngine,MultipartFile file) throws IOException {
		
		String processKey = model.getProcessKey();
		if(StringUtils.isEmpty(processKey)) {
			throw new IOException("processKey is empty.");
		}
		if(file == null) {
			throw new IOException("File is empty.");
		}
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment();
		deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
		deploymentBuilder.name(model.getProcessKey());
//		if(StringUtils.isNotBlank(model.getTenantId())) {
//			deploymentBuilder.tenantId(model.getTenantId());
//		}
		Deployment deployment = deploymentBuilder.deploy();
		model.setDeploymentId(deployment.getId());
		
		
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();
		model.setProcessDefinitionId(processDefinition.getId());
		
		return deployment;
	}
	
	
	public static List<Deployment> getDeployment(ProcessEngine processEngine) {
		return processEngine.getRepositoryService().createDeploymentQuery().orderByDeploymenTime().desc().list();
	}
	
	
	public static void deleteDeployment(FlowableModel model,ProcessEngine processEngine) {
		String deploymentId = model.getDeploymentId();
		if (StringUtils.isEmpty(deploymentId)) {
			throw new FlowableException("deploymentId is empty.");
		}
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
	}
	
	
	
	public static List<ProcessDefinition> getProcessDefinition( FlowableModel model,ProcessEngine processEngine) {
		ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
		if (StringUtils.isNotBlank(model.getDeploymentId())) {
			 processDefinitionQuery.deploymentId(model.getDeploymentId());
		}
		if (StringUtils.isNotBlank(model.getProcessDefinitionId())) {
			processDefinitionQuery.processDefinitionId(model.getProcessDefinitionId());
		}
		
		if (StringUtils.isNotBlank(model.getProcessKey())) {
			processDefinitionQuery.processDefinitionKeyLike(model.getProcessKey());
		}
		
		return processDefinitionQuery.orderByProcessDefinitionVersion().desc().list();
	}
	
	
	
	public static ProcessInstance start( FlowableModel model,ProcessEngine processEngine) {
		String userId=  model.getUserId();
		String processKey = model.getProcessKey();
		String businessKey = model.getBusinessKey();
		if(StringUtils.isEmpty(userId)) {
			throw new FlowableException("userId is empty.");
		}
		if(StringUtils.isEmpty(processKey)) {
			throw new FlowableException("processKey is empty.");
		}
		if(StringUtils.isEmpty(businessKey)) {
			throw new FlowableException("businessKey is empty.");
		}
		
		
		
		processEngine.getIdentityService().setAuthenticatedUserId(userId);
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processKey,processKey + Constant.BSINESSKEY_CHAR + businessKey, model.getVariables());
		return processInstance;
	}
	
	
	
	public static List<Task> getTask(FlowableModel model, ProcessEngine processEngine) {
		String userId=  model.getUserId();
		if(StringUtils.isEmpty(userId)) {
			throw new FlowableException("userId is empty.");
		}
		
		return  processEngine.getTaskService().createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
//		taskDelegationState(DelegationState.PENDING) 的是委托任务
	}
	
	/**
	 * 任务委托
	 * @param model
	 * @param processEngine
	 */
	public static void taskDelegate(FlowableModel model,ProcessEngine processEngine) {
		String taskId = model.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new FlowableException("taskId is empty.");
		}
		String delegateAssignee = model.getDelegateAssignee();
		if (StringUtils.isEmpty(delegateAssignee)) {
			throw new FlowableException("delegateAssignee is empty.");
		}
		
		processEngine.getTaskService().delegateTask(taskId, delegateAssignee);
	}
	
	public static void taskClaim(FlowableModel model,ProcessEngine processEngine ) {
		String userId=  model.getUserId();
		String taskId = model.getTaskId();
		if(StringUtils.isEmpty(userId)) {
			throw new FlowableException("userId is empty.");
		}
		if (StringUtils.isEmpty(taskId)) {
			throw new FlowableException("taskId is empty.");
		}
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		if(task == null){
			throw new FlowableException("Task with id =" + taskId +"is empty.");
		}
		 processEngine.getTaskService().claim(taskId,userId );
	}

	public static void next(FlowableModel model, ProcessEngine processEngine) {
		//TODO 功能可能不完善

		String taskId = model.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new FlowableException("taskId is empty.");
		}
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();

		if (StringUtils.isNotEmpty(model.getCommon())) {
			String userId = model.getUserId();
			if (StringUtils.isEmpty(userId)) {
				throw new FlowableException("userId is empty.");
			}
//	  	由于流程用户上下文对象是线程独立的，所以要在需要的位置设置，要保证设置和获取操作在同一个线程中
			Authentication.setAuthenticatedUserId(userId);// 批注人的名称
			processEngine.getTaskService().addComment(taskId, task.getProcessInstanceId(), model.getCommon());
		}
		
		//委托任务需要两步
		if(DelegationState.PENDING == task.getDelegationState()  ) {
			processEngine.getTaskService().resolveTask(taskId, model.getVariables());
		}
		processEngine.getTaskService().complete(taskId, model.getVariables());
		
	}

	public static List<Comment> getCommon(FlowableModel model, ProcessEngine processEngine) {
		String businessKey = model.getBusinessKey();
		String processKey = model.getProcessKey();
		if (StringUtils.isEmpty(businessKey)) {
			throw new FlowableException("businessKey is empty.");
		}
		if (StringUtils.isEmpty(processKey)) {
			throw new FlowableException("processKey is empty.");
		}
		List<Comment> commonList = new ArrayList<>();
		
		List<HistoricTaskInstance> hisTaskList = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(model.getProcessKey() + Constant.BSINESSKEY_CHAR + businessKey).list();
		for (HistoricTaskInstance historicTaskInstance : hisTaskList) {
			commonList.addAll(processEngine.getTaskService().getTaskComments(historicTaskInstance.getId()));
		}
		return commonList;
	}
	
	
	public static void getDiagram(FlowableModel model, ProcessEngine processEngine, HttpServletResponse response) throws IOException {
		String businessKey = model.getBusinessKey();
		String processKey = model.getProcessKey();
		if (StringUtils.isEmpty(businessKey)) {
			throw new FlowableException("businessKey is empty.");
		}
		if (StringUtils.isEmpty(processKey)) {
			throw new FlowableException("processKey is empty.");
		}
//		得到正在执行的Activity的Id
		List<String> activityIds = new ArrayList<>();
//		线
		List<String> flows = new ArrayList<>();

		ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey(processKey + Constant.BSINESSKEY_CHAR + businessKey).singleResult();
		String processDefinitionId = null;
		if(processInstance == null) {
			HistoricProcessInstance hisProcessInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceBusinessKey(processKey+ Constant.BSINESSKEY_CHAR + businessKey).singleResult();
			if(hisProcessInstance == null) {
				throw new FlowableException("Process not start.");
			}
			processDefinitionId = hisProcessInstance.getProcessDefinitionId();
		}else {
			processDefinitionId = processInstance.getProcessDefinitionId();
			List<Execution> executionList = processEngine.getRuntimeService().createExecutionQuery().processDefinitionId(processDefinitionId).list();
//			得到正在执行的Activity的Id
			for (Execution execution : executionList) {
				List<String> ids = processEngine.getRuntimeService().getActiveActivityIds(execution.getId());
				activityIds.addAll(ids);
			}
		}
		BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionId);
		ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
		ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
		InputStream inputStream = null;
		try {
			inputStream = diagramGenerator.generateDiagram(bpmnModel, "PNG", activityIds, flows, "宋体","宋体","宋体", engconf.getClassLoader(), 1.0, true);
		//	InputStream input = diagramGenerator.generateDiagram(bpmnModel, "PNG", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, false);
			IOUtils.copy(inputStream, response.getOutputStream());
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	
	
	public  static Map<String,String>  getOutSequenceFlow(FlowableModel model, ProcessEngine processEngine)  {
		String taskId = model.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new FlowableException("taskId is empty.");
		}
		Map<String,String> outSequenceFlowMap = new HashMap<>();
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		Execution exec = processEngine.getRuntimeService().createExecutionQuery().executionId(task.getExecutionId()).singleResult();
	    String currentActivityId = exec.getActivityId();
	//	获取当前节点的 flowNode对象
	    BpmnModel bpmnModel =  processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
	    getOutSequenceFlow(currentActivityId,bpmnModel,outSequenceFlowMap);
	    return outSequenceFlowMap;
	}
	
	public static boolean taskWithdraw(ProcessEngine processEngine,FlowableModel model) {
		String taskId = model.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new FlowableException("taskId is empty.");
		}
		List<String> currentActivityIdList = new ArrayList<>();
		List<FlowElement> preFlowElementList = new ArrayList<>();
		HistoricTaskInstance historicTaskInstance = processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(historicTaskInstance.getProcessDefinitionId());
		List<Execution> execList = processEngine.getRuntimeService().createExecutionQuery().executionId(historicTaskInstance.getExecutionId()).list();
		if(!CollectionUtils.isEmpty(execList)){
			for (Execution execution : execList) {
				String currentActivityId = execution.getActivityId();
				currentActivityIdList.add(currentActivityId);
				getInSequenceFlow(currentActivityId,bpmnModel,preFlowElementList);
			}
		}

//				撤回操作
		if(currentActivityIdList.size() == 1) {
			if(preFlowElementList.size() == 1) {
				FlowElement preFlowElement = preFlowElementList.get(0);

//				开始节点不能回退
				if(preFlowElement instanceof StartEvent){
					throw new FlowableException("Can not withdraw to the start event.");
				}
				processEngine.getRuntimeService().createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveActivityIdTo(currentActivityIdList.get(0), preFlowElement.getId())
				.changeState();
			}else {
				if(preFlowElementList.stream().anyMatch(e-> (e instanceof StartEvent))) {
					throw new FlowableException("Can not withdraw to the start event.");
				}
				processEngine.getRuntimeService().createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveSingleActivityIdToActivityIds(currentActivityIdList.get(0), preFlowElementList.stream().map(e->e.getId()).collect(Collectors.toList()))
				.changeState();
			}
		}else {
			if(preFlowElementList.size() == 1) {
				FlowElement preFlowElement = preFlowElementList.get(0);

//				开始节点不能回退
				if(preFlowElement instanceof StartEvent){
					throw new FlowableException("Can not withdraw to the start event.");
				}
				processEngine.getRuntimeService().createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveActivityIdsToSingleActivityId(currentActivityIdList, preFlowElement.getId())
				.changeState();
			}else {
				if(preFlowElementList.stream().anyMatch(e-> (e instanceof StartEvent))) {
					throw new FlowableException("Can not withdraw to the start event.");
				}
				//not support
			}
		}
		return true;
	}
	
	
	/**
	 *
	 * 方法名:getOutSequenceFlow
	 * 描    述:获得当前节点出去的线
	 * 返回值:void
	 * 参    数:@param activityId
	 * 参    数:@param bpmnModel
	 * 参    数:@param outSequenceFlowMap
	 * 作    者:710009498
	 * 时    间:Jan 11, 2019 8:44:21 AM
	 */
	private static void getOutSequenceFlow(String activityId,BpmnModel bpmnModel,Map<String,String> outSequenceFlowMap){
		FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(activityId);
       List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
		for (SequenceFlow outFlow : outFlows) {
			 FlowElement nextFlowElement = outFlow.getTargetFlowElement();
			 if(nextFlowElement instanceof ExclusiveGateway) { // 排他网关处理
				 getOutSequenceFlow(nextFlowElement.getId(),bpmnModel,outSequenceFlowMap);
			} else {
				if (outFlow != null && StringUtils.isNotEmpty(outFlow.getName())) {
					if(!outSequenceFlowMap.containsKey(outFlow.getName())) {
						outSequenceFlowMap.put(outFlow.getName(),outFlow.getConditionExpression());
					}else {
						throw new FlowableException("Duplicated SequenceFlow name " + outFlow.getName() );
					}
				} else {
					outSequenceFlowMap.put("default","default");
				}
			}
		}
	}

	/**
	 *
	 * 方法名:getOutSequenceFlow
	 * 描    述:获得当前活动节点的上一个（多个） 节点
	 * 返回值:void
	 * 参    数:@param activityId
	 * 参    数:@param bpmnModel
	 * 参    数:@param outSequenceFlowMap
	 * 作    者:710009498
	 * 时    间:Jan 11, 2019 8:42:58 AM
	 */
	private static void getInSequenceFlow(String activityId,BpmnModel bpmnModel,List<FlowElement> flowElementList){
		FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(activityId);
       List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
		for (SequenceFlow incomeFlow : incomingFlows) {
			 FlowElement preFlowElement = incomeFlow.getSourceFlowElement();
			 if(preFlowElement instanceof ExclusiveGateway) { // 排他网关处理
				 getInSequenceFlow(preFlowElement.getId(),bpmnModel,flowElementList);
			} /*else if(preFlowElement instanceof StartEvent){

			}*/else{
				if (preFlowElement != null && StringUtils.isNotEmpty(preFlowElement.getId())) {
					flowElementList.add(preFlowElement);
				}
			}
		}
	}
	
	
	
}
