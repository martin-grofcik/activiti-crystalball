package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.FactoryBean;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class DefaultSimulationProcessEngineFactory implements FactoryBean<ProcessEngine> {
  private ProcessEngine processEngine;

  public DefaultSimulationProcessEngineFactory() {
    this("");
  }

  public DefaultSimulationProcessEngineFactory(String resourceToDeploy) {
    processEngine = ProcessEngines.getDefaultProcessEngine();
    if (!resourceToDeploy.isEmpty())
      processEngine.getRepositoryService().
        createDeployment().
        addClasspathResource(resourceToDeploy).
        deploy();

    final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
    processEngineConfiguration.setHistory("full");
  }

  @Override
  public ProcessEngine getObject() {
    return processEngine;
  }

  @Override
  public Class<?> getObjectType() {
    return ProcessEngineImpl.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
