package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.FactoryBean;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.Clock;

public class DefaultSimulationProcessEngineFactory implements FactoryBean<ProcessEngineImpl> {
  private ProcessEngineImpl processEngine;

  public DefaultSimulationProcessEngineFactory(Clock clock) {
    this("", clock);
  }

  public DefaultSimulationProcessEngineFactory(String resourceToDeploy, Clock clock) {
    processEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
    if (!resourceToDeploy.isEmpty())
      processEngine.getRepositoryService().
        createDeployment().
        addClasspathResource(resourceToDeploy).
        deploy();

    final ProcessEngineConfigurationImpl processEngineConfiguration = processEngine.getProcessEngineConfiguration();
    processEngineConfiguration.setHistory("full");
    processEngineConfiguration.setClock(clock);
  }

  @Override
  public ProcessEngineImpl getObject() {
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
