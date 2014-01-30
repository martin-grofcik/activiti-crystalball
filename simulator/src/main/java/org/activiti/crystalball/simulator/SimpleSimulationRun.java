package org.activiti.crystalball.simulator;

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


import org.activiti.crystalball.simulator.impl.AcquireJobNotificationEventHandler;
import org.activiti.crystalball.simulator.impl.NoopEventHandler;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.util.ClockUtil;

import java.util.*;

public class SimpleSimulationRun extends AbstractSimulationRun {

  protected FactoryBean<ProcessEngine> processEngineFactory;
	protected FactoryBean<EventCalendar> eventCalendarFactory;

  /** simulation start date*/
  protected Date simulationStartDate = new Date(0);
  protected Date dueDate = null;

  protected SimpleSimulationRun(Builder builder) {
    super(builder.eventHandlers);
    this.eventCalendarFactory = builder.getEventCalendarFactory();
		this.processEngineFactory = builder.getProcessEngineFactory();
    // init internal event handler map.
		eventHandlerMap.put(SimulationEvent.TYPE_END_SIMULATION, new NoopEventHandler());
    if ( builder.getJobExecutor() != null)
      eventHandlerMap.put(SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, new AcquireJobNotificationEventHandler(builder.getJobExecutor()));
  }

  @Override
  public void close() {
    // remove simulation from simulation context
      SimulationRunContext.getEventCalendar().clear();
      SimulationRunContext.removeEventCalendar();
      SimulationRunContext.getProcessEngine().close();
      SimulationRunContext.removeProcessEngine();
  }

  @Override
  protected void initSimulationRunContext() {// init new process engine
    processEngine = processEngineFactory.getObject();

    // add context in which simulation run is executed
    SimulationRunContext.setEventCalendar(eventCalendarFactory.getObject());
    SimulationRunContext.setProcessEngine(processEngine);

    // run simulation
    // init context and task calendar and simulation time is set to current
    ClockUtil.setCurrentTime(simulationStartDate);

    if (dueDate != null)
      SimulationRunContext.getEventCalendar().addEvent(new SimulationEvent.Builder( SimulationEvent.TYPE_END_SIMULATION).
        simulationTime(dueDate.getTime()).
        build());
  }

  @Override
  protected boolean simulationEnd( SimulationEvent event) {
    if ( event != null && event.getType().equals(SimulationEvent.TYPE_BREAK_SIMULATION))
      return true;
		if ( dueDate != null)
			return event == null || ( ClockUtil.getCurrentTime().after( dueDate ));
		return  event == null;
	}

  public static class Builder {
        private Map<String, SimulationEventHandler> eventHandlers;
        private FactoryBean<ProcessEngine> processEngineFactory;
        private FactoryBean<EventCalendar> eventCalendarFactory;
        private JobExecutor jobExecutor;

        public JobExecutor getJobExecutor() {
            return jobExecutor;
        }

        public Builder jobExecutor(JobExecutor jobExecutor) {
            this.jobExecutor = jobExecutor;
            return this;
        }

        public FactoryBean<ProcessEngine> getProcessEngineFactory() {
            return processEngineFactory;
        }

        public Builder processEngineFactory(FactoryBean<ProcessEngine> processEngineFactory) {
            this.processEngineFactory = processEngineFactory;
            return this;
        }

        public FactoryBean<EventCalendar> getEventCalendarFactory() {
            return eventCalendarFactory;
        }

        public Builder eventCalendarFactory(FactoryBean<EventCalendar> eventCalendarFactory) {
            this.eventCalendarFactory = eventCalendarFactory;
            return this;
        }

        public Builder eventHandlers(Map<String, SimulationEventHandler> eventHandlersMap) {
            this.eventHandlers = eventHandlersMap;
            return this;
        }

        public SimpleSimulationRun build() {
            return new SimpleSimulationRun(this);
        }
    }
}
