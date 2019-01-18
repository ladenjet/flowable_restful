package com.genpact.flowable.utils;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

import com.genpact.flowable.entity.Constant;

public class FlowableUtils {
	
	
	public static Deployment deployment(RepositoryService repositoryService,FlowableModel model, MultipartFile file) throws IOException {
		
		String processKey = model.getProcessKey();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		if(file == null) {
			throw new IOException("File is empty.");
		}
		if(StringUtils.isEmpty(processKey)) {
			throw new IOException("processKey is empty.");
		}
		deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
		deploymentBuilder.name(model.getProcessKey());
		if(StringUtils.isNotBlank(model.getTenantId())) {
			deploymentBuilder.tenantId(model.getTenantId());
		}
		Deployment deployment = deploymentBuilder.deploy();
		model.setDeploymentId(deployment.getId());
		
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();
		model.setProcessDefinitionId(processDefinition.getId());
		
		return deployment;
	}
	
	
	public static List<ProcessDefinition> getProcessDefinition(RepositoryService repositoryService, FlowableModel model) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		if (StringUtils.isNotBlank(model.getDeploymentId())) {
			 processDefinitionQuery.deploymentId(model.getDeploymentId());
		}
		if (StringUtils.isNotBlank(model.getProcessDefinitionId())) {
			processDefinitionQuery.processDefinitionId(model.getProcessDefinitionId());
		}
		
		if (StringUtils.isNotBlank(model.getTenantId())) {
			processDefinitionQuery.processDefinitionTenantId(model.getTenantId());
		}
		
		if (StringUtils.isNotBlank(model.getTenantId())) {
			processDefinitionQuery.processDefinitionKeyLike(model.getProcessKey());
		}
		
		return processDefinitionQuery.orderByProcessDefinitionVersion().desc().list();
	}
	
	
	
	public static ProcessInstance start( FlowableModel model,IdentityService identityService,RuntimeService runtimeService) {
		String userId=  model.getUserId();
		String processKey = model.getProcessKey();
		String businessKey = model.getBusinessKey();
		if(StringUtils.isEmpty(userId)) {
			throw new RuntimeException("userId is empty.");
		}
		if(StringUtils.isEmpty(processKey)) {
			throw new RuntimeException("processKey is empty.");
		}
		if(StringUtils.isEmpty(businessKey)) {
			throw new RuntimeException("businessKey is empty.");
		}
		identityService.setAuthenticatedUserId(userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, processKey+ Constant.BSINESSKEY_CHAR + model.getBusinessKey(), model.getVariables());
		return processInstance;
	}
	
	
	
	
}
