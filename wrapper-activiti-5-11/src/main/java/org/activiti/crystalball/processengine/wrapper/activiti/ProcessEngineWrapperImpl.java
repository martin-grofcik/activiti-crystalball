package org.activiti.crystalball.processengine.wrapper.activiti;

import org.activiti.crystalball.processengine.wrapper.*;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessInstanceWrapper;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.IdentityServiceImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;

import java.util.Map;

public class ProcessEngineWrapperImpl implements ProcessEngineWrapper {
    protected ProcessEngine processEngine;
    protected ProcessDiagramService processDiagramService;
    protected RepositoryServiceWrapper repositoryService;
    protected RuntimeServiceWrapper runtimeService;
    protected HistoryServiceWrapper historicDataService;
    protected TaskServiceWrapper taskService;
    protected IdentityServiceWrapper identityService;

    public ProcessEngineWrapperImpl( ProcessEngine processEngine) {
        this.processEngine = processEngine;
        processDiagramService = new ProcessDiagramServiceImpl();
        historicDataService = new HistoryServiceWrapperImpl(processEngine.getHistoryService());
        repositoryService = new RepositoryServiceWrapperImpl( (RepositoryServiceImpl) processEngine.getRepositoryService());
        taskService = new TaskServiceWrapperImpl(processEngine.getTaskService());
        runtimeService = new RuntimeServiceWrapperImpl(processEngine.getRuntimeService());
        identityService = new IdentityServiceWrapperImpl(processEngine.getIdentityService());
    }

    @Override
    public HistoryServiceWrapper getHistoryService() {
        return historicDataService;
    }

    @Override
    public void close() {
        processEngine.close();
    }

    @Override
    public RepositoryServiceWrapper getRepositoryService() {
        return repositoryService;
    }

    @Override
    public TaskServiceWrapper getTaskService() {
        return taskService;
    }

    @Override
    public RuntimeServiceWrapper getRuntimeService() {
        return runtimeService;
    }

    @Override
    public IdentityServiceWrapper getIdentityService() {
        return identityService;
    }

    @Override
    public ProcessDiagramService getProcessDiagramService() {
        return processDiagramService;
    }
}
