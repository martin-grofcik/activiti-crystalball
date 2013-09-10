package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.RepositoryServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.ProcessDefinitionQueryWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.activiti.queries.ProcessDefinitionWrapperImpl;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.*;
import org.activiti.engine.task.IdentityLink;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class RepositoryServiceWrapperImpl implements RepositoryServiceWrapper {
    protected final RepositoryServiceImpl repostioryService;

    public DeploymentBuilder createDeployment() {
        return repostioryService.createDeployment();
    }

    public void deleteDeployment(String deploymentId) {
        repostioryService.deleteDeployment(deploymentId);
    }

    public void deleteDeploymentCascade(String deploymentId) {
        repostioryService.deleteDeploymentCascade(deploymentId);
    }

    public void deleteDeployment(String deploymentId, boolean cascade) {
        repostioryService.deleteDeployment(deploymentId, cascade);
    }

    public List<String> getDeploymentResourceNames(String deploymentId) {
        return repostioryService.getDeploymentResourceNames(deploymentId);
    }

    public InputStream getResourceAsStream(String deploymentId, String resourceName) {
        return repostioryService.getResourceAsStream(deploymentId, resourceName);
    }

    @Override
    public ProcessDefinitionQueryWrapper createProcessDefinitionQuery() {
        return new ProcessDefinitionQueryWrapperImpl( repostioryService.createProcessDefinitionQuery());
    }

    @Override
    public ProcessDefinitionWrapper getDeployedProcessDefinition(String id) {
        return new ProcessDefinitionWrapperImpl( (ProcessDefinitionEntity) repostioryService.getDeployedProcessDefinition(id));
    }

    public DeploymentQuery createDeploymentQuery() {
        return repostioryService.createDeploymentQuery();
    }

    public void suspendProcessDefinitionById(String processDefinitionId) {
        repostioryService.suspendProcessDefinitionById(processDefinitionId);
    }

    public void suspendProcessDefinitionById(String processDefinitionId, boolean suspendProcessInstances, Date suspensionDate) {
        repostioryService.suspendProcessDefinitionById(processDefinitionId, suspendProcessInstances, suspensionDate);
    }

    public void suspendProcessDefinitionByKey(String processDefinitionKey) {
        repostioryService.suspendProcessDefinitionByKey(processDefinitionKey);
    }

    public void suspendProcessDefinitionByKey(String processDefinitionKey, boolean suspendProcessInstances, Date suspensionDate) {
        repostioryService.suspendProcessDefinitionByKey(processDefinitionKey, suspendProcessInstances, suspensionDate);
    }

    public void activateProcessDefinitionById(String processDefinitionId) {
        repostioryService.activateProcessDefinitionById(processDefinitionId);
    }

    public void activateProcessDefinitionById(String processDefinitionId, boolean activateProcessInstances, Date activationDate) {
        repostioryService.activateProcessDefinitionById(processDefinitionId, activateProcessInstances, activationDate);
    }

    public void activateProcessDefinitionByKey(String processDefinitionKey) {
        repostioryService.activateProcessDefinitionByKey(processDefinitionKey);
    }

    public void activateProcessDefinitionByKey(String processDefinitionKey, boolean activateProcessInstances, Date activationDate) {
        repostioryService.activateProcessDefinitionByKey(processDefinitionKey, activateProcessInstances, activationDate);
    }

    public InputStream getProcessModel(String processDefinitionId) {
        return repostioryService.getProcessModel(processDefinitionId);
    }

    public InputStream getProcessDiagram(String processDefinitionId) {
        return repostioryService.getProcessDiagram(processDefinitionId);
    }

    public ProcessDefinition getProcessDefinition(String processDefinitionId) {
        return repostioryService.getProcessDefinition(processDefinitionId);
    }

    public DiagramLayout getProcessDiagramLayout(String processDefinitionId) {
        return repostioryService.getProcessDiagramLayout(processDefinitionId);
    }

    public Model newModel() {
        return repostioryService.newModel();
    }

    public void saveModel(Model model) {
        repostioryService.saveModel(model);
    }

    public void deleteModel(String modelId) {
        repostioryService.deleteModel(modelId);
    }

    public void addModelEditorSource(String modelId, byte[] bytes) {
        repostioryService.addModelEditorSource(modelId, bytes);
    }

    public void addModelEditorSourceExtra(String modelId, byte[] bytes) {
        repostioryService.addModelEditorSourceExtra(modelId, bytes);
    }

    public ModelQuery createModelQuery() {
        return repostioryService.createModelQuery();
    }

    public Model getModel(String modelId) {
        return repostioryService.getModel(modelId);
    }

    public byte[] getModelEditorSource(String modelId) {
        return repostioryService.getModelEditorSource(modelId);
    }

    public byte[] getModelEditorSourceExtra(String modelId) {
        return repostioryService.getModelEditorSourceExtra(modelId);
    }

    public void addCandidateStarterUser(String processDefinitionId, String userId) {
        repostioryService.addCandidateStarterUser(processDefinitionId, userId);
    }

    public void addCandidateStarterGroup(String processDefinitionId, String groupId) {
        repostioryService.addCandidateStarterGroup(processDefinitionId, groupId);
    }

    public void deleteCandidateStarterUser(String processDefinitionId, String userId) {
        repostioryService.deleteCandidateStarterUser(processDefinitionId, userId);
    }

    public void deleteCandidateStarterGroup(String processDefinitionId, String groupId) {
        repostioryService.deleteCandidateStarterGroup(processDefinitionId, groupId);
    }

    public List<IdentityLink> getIdentityLinksForProcessDefinition(String processDefinitionId) {
        return repostioryService.getIdentityLinksForProcessDefinition(processDefinitionId);
    }

    public RepositoryServiceWrapperImpl(RepositoryServiceImpl repositoryService) {
        this.repostioryService = repositoryService;
    }
}
