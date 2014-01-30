package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.delegate.event.ActivitiEventToSimulationEventTransformer;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class...
 */
public abstract class AbstractRecordActivitiEventListener implements ActivitiEventListener {
  protected List<ActivitiEventToSimulationEventTransformer> transformers;

  public AbstractRecordActivitiEventListener(List<ActivitiEventToSimulationEventTransformer> transformers) {this.transformers = transformers;}

  public abstract Collection<SimulationEvent> getSimulationEvents();

  @Override
  public void onEvent(ActivitiEvent event) {
    Collection<SimulationEvent> simulationEvents = transform(event);
    store(simulationEvents);
  }

  protected abstract void store(Collection<SimulationEvent> simulationEvents);

  protected Collection<SimulationEvent> transform(ActivitiEvent event) {
    List<SimulationEvent> simEvents = new ArrayList<SimulationEvent>();
    for (ActivitiEventToSimulationEventTransformer t : transformers) {
      SimulationEvent simEvent = t.transform(event);
      if (simEvent != null)
        simEvents.add(simEvent);
    }
    return simEvents;
  }

  @Override
	public boolean isFailOnException() {
		return true;
	}
}
