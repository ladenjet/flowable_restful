package com.genpact.flowable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.genpact.flowable.generator.service.SysGeneratorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableRestfulApplicationTests {

	@Autowired
	private SysGeneratorService sysGeneratorService;

	@Test
	public void generatorCode() {
		String[] tableNames = { "SYS_PERMISSION"};
		sysGeneratorService.generatorCode("d:/generatorCode.zip",tableNames);

	}
}

