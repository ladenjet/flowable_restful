package com.genpact.flowable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ProcessEngine;
import org.flowable.task.api.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.genpact.flowable.utils.FlowableModel;
import com.genpact.flowable.utils.FlowableUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
//@FlowableTest
//@ConfigurationResource
public class FlowableRestfulApplicationTests {
//	 @Rule
//	public FlowableRule flowableRule = new FlowableRule();
	@Autowired
	private ProcessEngine processEngine;
	String resourceFile = "flowable/otlprocess.bpmn20.xml";
	String processKey = "otlprocess";
	String buinessId = "1";
	String userId = "2";
	
	@Test
	public void deployment() {
		FlowableModel model = new FlowableModel();
		model.setProcessKey(processKey);
		FlowableUtils.deployment(model, processEngine, resourceFile);
	}
	
	
	@Test
	public void start() {
		FlowableModel model = new FlowableModel();
		model.setProcessKey(processKey);
		model.setUserId(userId);
		model.setBusinessKey(buinessId);
		Map<String, Object> variables = new HashMap<>();
		variables.put("totalTime", 10);
		variables.put("actualTime", 10);
		variables.put("managerId", 3);
		model.setVariables(variables);
		FlowableUtils.start(model, processEngine);
	}
	
	
	
	@Test
	public void taskList() {
		FlowableModel model = new FlowableModel();
		model.setProcessKey(processKey);
		model.setUserId("3");
		List<Task> taskList = FlowableUtils.getTask(model, processEngine);
		taskList.stream().forEach(System.out::println);
	}
	

	@Test
	public void next() {
		FlowableModel model = new FlowableModel();
		model.setTaskId("b01e3322-1edf-11e9-a333-64006a45c85b");
		Map<String, Object> variables = new HashMap<>();
		variables.put("approve", false);
//		variables.put("hrId", 5);
		model.setVariables(variables);
		FlowableUtils.next(model, processEngine);
		
	}
	
	
	@Test
	public void taskWithdraw() {
		FlowableModel model = new FlowableModel();
		model.setTaskId("7b3d5d2e-1edf-11e9-899b-64006a45c85b");
		
//		Map<String, Object> variables = new HashMap<>();
//		variables.put("approve", false);
//		variables.put("hrId", 5);
//		model.setVariables(variables);
		FlowableUtils.taskWithdraw(model, processEngine);
		
	}

}

