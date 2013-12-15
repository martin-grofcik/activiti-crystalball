package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class UserTaskCompleteTransformer extends AbstractTransformer {

  public UserTaskCompleteTransformer(String simulationEventType) {
    super(simulationEventType);
  }

  @Override
  public SimulationEvent transform(ActivitiEvent event) {
    if (ActivitiEventType.TASK_COMPLETED.equals(event.getType())) {
      Task task = (Task) ((ActivitiEntityEvent) event).getEntity();

      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put("task", task.getId());
      return new SimulationEvent(ClockUtil.getCurrentTime().getTime(), this.simulationEventType, properties);
    };
    return null;
  }
}
