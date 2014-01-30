package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.delegate.event.ActivitiEventToSimulationEventTransformer;
import org.activiti.crystalball.simulator.delegate.event.RecordedActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.ActivitiVariableEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessInstanceCreateTransformer extends AbstractTransformer {

  public static final String PROCESS_INSTANCE_ID = "processInstanceId";
  private final String processDefinitionIdKey;
  private final String businessKey;
  private final String variablesKey;

  public ProcessInstanceCreateTransformer(String simulationEventType, String processDefinitionIdKey, String businessKey, String variablesKey) {
    super(simulationEventType);
    this.processDefinitionIdKey = processDefinitionIdKey;
    this.businessKey = businessKey;
    this.variablesKey = variablesKey;
  }

  @Override
  public SimulationEvent transform(ActivitiEvent event) {
    if (ActivitiEventType.ENTITY_CREATED.equals(event.getType()) &&
      (event instanceof ActivitiEntityEvent) &&
      ((ActivitiEntityEvent) event).getEntity() instanceof ProcessInstance) {

      ProcessInstance processInstance = (ProcessInstance) ((ActivitiEntityEvent) event).getEntity();
      ExecutionEntity executionEntity = (ExecutionEntity) ((ActivitiEntityEvent) event).getEntity();

      Map<String, Object> simEventProperties = new HashMap<String, Object>();
      simEventProperties.put(processDefinitionIdKey, processInstance.getProcessDefinitionId());
      simEventProperties.put(businessKey, processInstance.getBusinessKey());
      simEventProperties.put(variablesKey, executionEntity.getVariables());
      simEventProperties.put(PROCESS_INSTANCE_ID, executionEntity.getProcessInstanceId());

      return new SimulationEvent.Builder(simulationEventType).
                  simulationTime(ClockUtil.getCurrentTime().getTime()).
                  properties(simEventProperties).
                  build();
    }
    return null;
  }
}
