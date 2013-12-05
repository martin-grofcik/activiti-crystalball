package org.activiti.crystalball.simulator.impl;

/*
 * #%L
 * simulator
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * #L%
 */


import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start new process event handler 
 *
 */
public class PeriodicStartProcessEventHandler extends StartProcessEventHandler {

	private static Logger log = LoggerFactory.getLogger(PeriodicStartProcessEventHandler.class);

  /**
   * event type on which Handler is listening to start new process
   */
  protected String eventType;
	/** period in which new process will start in ms*/
	protected long period;

	/** how many times start the process - -1 for ever*/
	protected int count = -1;

  public PeriodicStartProcessEventHandler(String processDefinitionId, String businessKey, String variablesKey) {
    super(processDefinitionId, businessKey, variablesKey);
  }

  @Override
	public void init() {
    super.init();
		// schedule new process instance start now
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		scheduleNextProcessStart(simulationTime);
	}

	private void scheduleNextProcessStart(long simulationTime) {
		if (count != 0 ) {
			if ( count > 0 ) count--;
			SimulationEvent completeEvent = new SimulationEvent( simulationTime, eventType, null);
			// add start process event
			SimulationRunContext.getEventCalendar().addEvent( completeEvent);
		}
	}

	@Override
	public void handle(SimulationEvent event) {
    super.handle(event);
		// schedule next process start in + period time
		scheduleNextProcessStart(event.getSimulationTime() + period);
	}


	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String event_type) {
    this.eventType = event_type;
  }

}
