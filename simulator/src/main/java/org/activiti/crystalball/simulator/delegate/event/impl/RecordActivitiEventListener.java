/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.crystalball.simulator.delegate.event.impl;

import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.delegate.event.ActivitiEventToSimulationEventTransformer;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RecordActivitiEventListener implements ActivitiEventListener {

	private Collection<SimulationEvent> events;
  private List<ActivitiEventToSimulationEventTransformer> transformers;
	private Class<?> entityClass;
	
	public RecordActivitiEventListener(Class<?> entityClass, List<ActivitiEventToSimulationEventTransformer> transformers) {
		this.entityClass = entityClass;
    this.transformers = transformers;
    events = new HashSet<SimulationEvent>();
  }

  public Collection<SimulationEvent> getSimulationEvents() {
    return events;
  }
	

	@Override
	public void onEvent(ActivitiEvent event) {
    Collection<SimulationEvent> simulationEvents = transform(event);
    store(simulationEvents);
	}

  private void store(Collection<SimulationEvent> simulationEvents) {
    events.addAll(simulationEvents);
  }

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
