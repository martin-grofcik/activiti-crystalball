package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.FactoryBean;
import org.activiti.crystalball.simulator.delegate.UserTaskExecutionListener;
import org.activiti.crystalball.simulator.delegate.event.impl.AbstractRecordActivitiEventListener;
import org.activiti.crystalball.simulator.impl.bpmn.parser.handler.AddListenerUserTaskParseHandler;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.parse.BpmnParseHandler;

import java.util.Arrays;

/**
 * This class...
 */
public class ReplayProcessEngineFactory implements FactoryBean<ProcessEngine> {

  private ProcessEngine processEngine;

  public ReplayProcessEngineFactory(String resourceToDeploy, String userTaskCompletedEventType, AbstractRecordActivitiEventListener listener) {
  }

  @Override
  public ProcessEngine getObject() throws RuntimeException {
    return processEngine;
  }

  @Override
  public Class<?> getObjectType() {
    return ProcessEngine.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
