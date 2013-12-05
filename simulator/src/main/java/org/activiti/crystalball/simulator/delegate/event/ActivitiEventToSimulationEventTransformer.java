package org.activiti.crystalball.simulator.delegate.event;

import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;

import java.util.List;

public interface ActivitiEventToSimulationEventTransformer {
  SimulationEvent transform(ActivitiEvent event);
}
