package com.genpact.flowable.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genpact.flowable.entity.Constant;
import com.genpact.flowable.entity.RequestModel;
import com.genpact.flowable.entity.Result;


@Controller
@RequestMapping("/flowable")
public class FlowableController {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

//	@Autowired
//	private LeaveBillService leaveBillService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private ProcessEngine processEngine;




	@PostMapping("/process/deployment")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result deployment() {
		repositoryService.createDeployment().addClasspathResource(Constant.PROCESS_DIAGRAM_FOLDER + Constant.PROCESS_KEY + Constant.PROCESS_EXT_BPMN).addClasspathResource(Constant.PROCESS_DIAGRAM_FOLDER + Constant.PROCESS_KEY + Constant.PROCESS_EXT_PNG).name(Constant.PROCESS_NAME).tenantId(Constant.TENANTID).deploy();
		return Result.ok();
	}

	@RequestMapping(value = "/process/deployment/list", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result listForDeployment() {
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
		return Result.ok(list.stream().collect(Collectors.toMap(Deployment::getId, Deployment::getName)));
	}

	@RequestMapping(value = "/process/definition/list", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result listForDefinition() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(Constant.TENANTID).orderByProcessDefinitionVersion().desc().list();
		// repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().count();
		// List<Deployment> list =
		// repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
		return Result.ok(list.stream().collect(Collectors.toMap(ProcessDefinition::getId, ProcessDefinition::getName)));
	}

	@RequestMapping(value = "/process/delete/{deploymentId}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result del(@PathVariable("deploymentId") String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return Result.ok();
	}

	@PostMapping("/process/start")
	@ResponseBody
	public Result start(@RequestBody RequestModel model,HttpServletRequest request,Principal  principal) {
		Map<String, Object> map = new HashMap<>();
		String userId  = principal.getName();//
//		String userId = request.getHeader(Constant.LOGIN_USER);
		identityService.setAuthenticatedUserId(userId);
		map.put("emp", userId);
		runtimeService.startProcessInstanceById(model.getProcessDefinitionId(), Constant.PROCESS_KEY + Constant.BSINESSKEY_CHAR + model.getBuinessId(), map);
		// runtimeService.startProcessInstanceByKey(Constant.PROCESS_KEY,
		// map);

		return Result.ok();
	}

	@GetMapping("/task/list/{taskType}")
	@ResponseBody
	public Result taskList(HttpServletRequest request,@PathVariable("taskType") String taskType,Principal  principal) {
		String userId = principal.getName().toString();
		List<Task> list = new ArrayList<>();

		if(taskType.equalsIgnoreCase("taskAssignee")) {
			 list = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
		}else if(taskType.equalsIgnoreCase("delegateTask")) {
			list = taskService
            .createTaskQuery()
            .taskDelegationState(DelegationState.PENDING)
//            	任务委派给谁
            .taskAssignee(userId)
//            	任务原有人
//            .taskOwner(arg0)
            .list();
		}

		return Result.ok(list.stream().collect(Collectors.toMap(Task::getId, Task::getName)));
	}



	@PostMapping("/task/claim")
	@ResponseBody
	public Result claimList(@RequestBody RequestModel model,Principal principal) {
		String userId = principal.getName().toString();
		Task task = taskService.createTaskQuery().taskId(model.getTaskId()).singleResult();
		taskService.claim(task.getId(),userId );
		return Result.ok();
	}


	/**
	 *
	 * 方法名:claimList
	 * 描    述: 转办
	 * 返回值:Result
	 * 参    数:@param model
	 * 参    数:@return
	 * 作    者:710009498
	 * 时    间:Jan 10, 2019 9:14:59 AM
	 */
	@PostMapping("/task/transfer")
	@ResponseBody
	public Result claim(@RequestBody RequestModel model) {
		taskService.setAssignee(model.getTaskId(), model.getTransferAssignee());
		return Result.ok();
	}

	/**
	 *
	 * 方法名:delegate
	 * 描    述:任务委托
	 * 返回值:Result
	 * 参    数:@param model
	 * 参    数:@return
	 * 作    者:710009498
	 * 时    间:Jan 10, 2019 3:10:46 PM
	 */
	@PostMapping("/task/delegate")
	@ResponseBody
	public Result delegate(@RequestBody RequestModel model) {
		taskService.delegateTask(model.getTaskId(), model.getDelegateAssignee());
		return Result.ok();
	}

	@RequestMapping(value = "task/view/{taskId}", method = RequestMethod.GET)
	@ResponseBody
	public Result/* ResponseEntity */ view(@PathVariable("taskId") String taskId, Model model) {
		Map<String,String> outSequenceFlowMap = new HashMap<>();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Execution exec = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String currentActivityId = exec.getActivityId();
        System.out.println("currentActivityId: " + currentActivityId);
//		获取当前节点的 flowNode对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        getOutSequenceFlow(currentActivityId,bpmnModel,outSequenceFlowMap);

        return Result.ok(outSequenceFlowMap);

	}


	@RequestMapping(value = "task/withdraw/{hisTaskId}", method = RequestMethod.GET)
	@ResponseBody
	public Result/* ResponseEntity */ withdraw(@PathVariable("hisTaskId") String hisTaskId, Model model) {
		List<String> currentActivityIdList = new ArrayList<>();
		List<FlowElement> preFlowElementList = new ArrayList<>();
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(hisTaskId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(historicTaskInstance.getProcessDefinitionId());
		List<Execution> execList = runtimeService.createExecutionQuery().executionId(historicTaskInstance.getExecutionId()).list();
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
					throw new RuntimeException("Can not withdraw to the start event.");
				}
				runtimeService.createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveActivityIdTo(currentActivityIdList.get(0), preFlowElement.getId())
				.changeState();
			}else {
				if(preFlowElementList.stream().anyMatch(e-> (e instanceof StartEvent))) {
					throw new RuntimeException("Can not withdraw to the start event.");
				}
				runtimeService.createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveSingleActivityIdToActivityIds(currentActivityIdList.get(0), preFlowElementList.stream().map(e->e.getId()).collect(Collectors.toList()))
				.changeState();
			}
		}else {
			if(preFlowElementList.size() == 1) {
				FlowElement preFlowElement = preFlowElementList.get(0);

//				开始节点不能回退
				if(preFlowElement instanceof StartEvent){
					throw new RuntimeException("Can not withdraw to the start event.");
				}
				runtimeService.createChangeActivityStateBuilder()
				.processInstanceId(	historicTaskInstance.getProcessInstanceId())
				.moveActivityIdsToSingleActivityId(currentActivityIdList, preFlowElement.getId())
				.changeState();
			}else {
				if(preFlowElementList.stream().anyMatch(e-> (e instanceof StartEvent))) {
					throw new RuntimeException("Can not withdraw to the start event.");
				}
				//not support
			}
		}

        return Result.ok();
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
	public void getOutSequenceFlow(String activityId,BpmnModel bpmnModel,Map<String,String> outSequenceFlowMap){
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
						throw new RuntimeException("Duplicated SequenceFlow name " + outFlow.getName() );
					}
				} else {
					outSequenceFlowMap.put("同意","${approve == 'true'");
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
	public void getInSequenceFlow(String activityId,BpmnModel bpmnModel,List<FlowElement> flowElementList){
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


	@RequestMapping(value = "/task/complete/{taskType}", method = RequestMethod.POST)
	@ResponseBody
	public Result complete(HttpServletRequest request, @RequestBody RequestModel model,@PathVariable("taskType") String taskType,Principal principal) {
		Task task = taskService.createTaskQuery().taskId(model.getTaskId()).singleResult();
//		  	由于流程用户上下文对象是线程独立的，所以要在需要的位置设置，要保证设置和获取操作在同一个线程中

		if(StringUtils.isNotEmpty(model.getCommon())) {
			Authentication.setAuthenticatedUserId(principal.getName());// 批注人的名称
			taskService.addComment(model.getTaskId(), task.getProcessInstanceId(), model.getCommon());
		}
		if(taskType.equalsIgnoreCase("taskAssignee")) {
//			// set choose line
//			Map<String, Object> map = new HashMap<>();
//			if(StringUtils.isNotEmpty(model.getButton())) {
//				map.put("approve", model.getButton());
//			}
			taskService.complete(model.getTaskId(), model.getVariables());
		}else if(taskType.equalsIgnoreCase("delegateTask")) {
			taskService.resolveTask(model.getTaskId(), model.getVariables());
			taskService.complete(model.getTaskId(), model.getVariables());
		}
		return Result.ok();
	}

	@RequestMapping(value = "/common/{buinessId}", method = RequestMethod.GET)
	@ResponseBody
	public Result common(@PathVariable("buinessId") String buinessId) {
		List<Comment> commonList = new ArrayList<>();
		if(StringUtils.isNotEmpty(buinessId)) {
			List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(Constant.PROCESS_KEY + Constant.BSINESSKEY_CHAR + buinessId).list();
			for (HistoricTaskInstance historicTaskInstance : hisTaskList) {
				commonList.addAll(taskService.getTaskComments(historicTaskInstance.getId()));
			}
		}
		return Result.ok(commonList);
	}

	@RequestMapping(value = "/process/diagram/{buinessId}", method = RequestMethod.GET)
	public void findProcessPic(@PathVariable("buinessId") String buinessId, HttpServletResponse response) throws IOException {
//		得到正在执行的Activity的Id
		List<String> activityIds = new ArrayList<>();
//		线
		List<String> flows = new ArrayList<>();

		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(Constant.PROCESS_KEY + Constant.BSINESSKEY_CHAR + buinessId).singleResult();
		String processDefinitionId = null;
		if(processInstance == null) {
			HistoricProcessInstance hisProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(Constant.PROCESS_KEY + Constant.BSINESSKEY_CHAR + buinessId).singleResult();
			if(hisProcessInstance == null) {
				throw new RuntimeException("Process not start.");
			}
			processDefinitionId = hisProcessInstance.getProcessDefinitionId();
		}else {
			processDefinitionId = processInstance.getProcessDefinitionId();
			List<Execution> executionList = runtimeService.createExecutionQuery().processDefinitionId(processDefinitionId).list();
//			得到正在执行的Activity的Id
			for (Execution execution : executionList) {
				List<String> ids = runtimeService.getActiveActivityIds(execution.getId());
				activityIds.addAll(ids);
			}
		}
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
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



}
