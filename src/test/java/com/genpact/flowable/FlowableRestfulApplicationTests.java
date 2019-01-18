package com.genpact.flowable;

import org.flowable.engine.RuntimeService;
import org.flowable.form.api.FormInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableRestfulApplicationTests {
	@Autowired
	private RuntimeService runtimeService;
	
	private static String  processDefinitionId= "leavebill:1:8776bf81-1ae5-11e9-8636-64006a45c85b";
	private static String  processInstanceId = "a21c57a2-1ae5-11e9-8636-64006a45c85b";
	
	@Test
	public void formInfo() {
		FormInfo info = runtimeService.getStartFormModel(processDefinitionId, processInstanceId);
		System.out.println(info);
	}

}

