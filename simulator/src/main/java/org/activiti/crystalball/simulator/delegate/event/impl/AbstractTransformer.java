package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.delegate.event.ActivitiEventToSimulationEventTransformer;

/**
 * This class...
 */
public abstract class AbstractTransformer implements ActivitiEventToSimulationEventTransformer {
  protected final String simulationEventType;

  public AbstractTransformer(String simulationEventType) {this.simulationEventType = simulationEventType;}
}
