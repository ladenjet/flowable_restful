package com.genpact.flowable.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.genpact.flowable.entity.Result;
import com.genpact.flowable.utils.FlowableModel;
import com.genpact.flowable.utils.FlowableUtils;


@Controller
@RequestMapping("/flowable")
public class FlowableController {
	private static Logger LOGGER = LoggerFactory.getLogger(FlowableController.class);
	@Autowired
	private ProcessEngine processEngine;

	@PostMapping("/process/deployment")
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result deployment(@RequestParam("file") MultipartFile file,FlowableModel model) throws IOException {
		FlowableUtils.deployment(model, processEngine,file);
		return Result.ok(model);
	}

	@RequestMapping(value = "/process/deployment/list", method = RequestMethod.GET)
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result listForDeployment() {
		List<Deployment> list = FlowableUtils.getDeployment(processEngine);
		return Result.ok(Result.ok(list.stream().collect(Collectors.toMap(Deployment::getId, Deployment::getName))));
	}

	

	@RequestMapping(value = "/process/definition/list", method = RequestMethod.GET)
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result listForDefinition(@RequestBody FlowableModel model) {
		List<ProcessDefinition> list = FlowableUtils.getProcessDefinition(model,processEngine);
		return Result.ok(list.stream().collect(Collectors.toMap(ProcessDefinition::getId, ProcessDefinition::getName)));
	}

	@RequestMapping(value = "/process/delete/{deploymentId}", method = RequestMethod.GET)
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result del(@PathVariable("deploymentId") String deploymentId,FlowableModel model) {
		model.setDeploymentId(deploymentId);
		FlowableUtils.deleteDeployment(model,processEngine);
		return Result.ok();
	}



	@PostMapping("/process/start")
	@ResponseBody
	public Result start(@RequestBody FlowableModel model,Principal  principal) {
		model.setUserId(principal.getName());
		FlowableUtils.start(model, processEngine);
		return Result.ok();
	}

	@GetMapping("/task/list")
	@ResponseBody
	public Result taskList(FlowableModel model,Principal  principal) {
		model.setUserId(principal.getName());
		List<Task> list = FlowableUtils.getTask(model,processEngine);
		return Result.ok(list.stream().collect(Collectors.toMap(Task::getId, Task::getName)));
	}

	



	@PostMapping("/task/claim")
	@ResponseBody
	public Result claimList(@RequestBody FlowableModel model,Principal principal) {
		model.setUserId(principal.getName());
		FlowableUtils.taskClaim(model, processEngine);
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
	public Result transfer(@RequestBody FlowableModel model) {
		processEngine.getTaskService().setAssignee(model.getTaskId(), model.getTransferAssignee());
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
	public Result delegate(@RequestBody FlowableModel model) {
		FlowableUtils.taskDelegate(model, processEngine);
		return Result.ok();
	}



	@RequestMapping(value = "task/view/{taskId}", method = RequestMethod.GET)
	@ResponseBody
	public Result/* ResponseEntity */ view(@PathVariable("taskId") String taskId,FlowableModel model) {
		model.setTaskId(taskId);
		Map<String, String> outSequenceFlowMap = FlowableUtils.getOutSequenceFlow(model,processEngine);
        return Result.ok(outSequenceFlowMap);

	}


	@RequestMapping(value = "task/withdraw/{hisTaskId}", method = RequestMethod.GET)
	@ResponseBody
	public Result withdraw(@PathVariable("hisTaskId") String hisTaskId, FlowableModel model) {
		model.setTaskId(hisTaskId);
		FlowableUtils.taskWithdraw(processEngine, model);
        return Result.ok();
	}

	


	@RequestMapping(value = "/task/complete", method = RequestMethod.POST)
	@ResponseBody
	public Result complete(HttpServletRequest request, @RequestBody FlowableModel model,Principal principal) {
		model.setUserId(principal.getName());
		FlowableUtils.next(model, processEngine);
		return Result.ok();
		
	}

	@RequestMapping(value = "/common", method = RequestMethod.GET)
	@ResponseBody
	public Result common( @RequestBody FlowableModel model) {
		List<Comment> commonList = FlowableUtils.getCommon(model, processEngine);
		return Result.ok(commonList);
	}

	@RequestMapping(value = "/process/diagram", method = RequestMethod.POST)
	public void findProcessPic(@RequestBody FlowableModel model ,HttpServletResponse response) throws IOException {
		FlowableUtils.getDiagram(model,processEngine, response);

	}

}
