package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.delegate.event.impl.AbstractRecordActivitiEventListener;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

/**
 * This class...
 */
public class RecordableProcessEngineFactory extends DefaultSimulationProcessEngineFactory {

  private AbstractRecordActivitiEventListener listener;

  public RecordableProcessEngineFactory(AbstractRecordActivitiEventListener listener) {
    this("", listener);
  }

  public RecordableProcessEngineFactory(String resourceToDeploy, AbstractRecordActivitiEventListener listener) {
    super(resourceToDeploy);
    this.listener = listener;
  }

  @Override
  public ProcessEngine getObject() {
    ProcessEngine processEngine = super.getObject();

    //add eventListener
    final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
    processEngineConfiguration.getEventDispatcher().addEventListener(listener);

    return processEngine;
  }
}