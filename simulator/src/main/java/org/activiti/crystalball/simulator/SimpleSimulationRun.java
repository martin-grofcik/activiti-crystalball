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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleSimulationRun implements SimulationRun {
	
	private static Logger log = LoggerFactory.getLogger(SimpleSimulationRun.class.getName());
	
	/** Map for eventType -> event handlers to execute events on simulation engine */
	private Map<String, SimulationEventHandler> customEventHandlerMap;
		
	/** simulation run event handlers - e.g. specific handlers for managing simulation time events*/
	private HashMap<String, SimulationEventHandler> eventHandlerMap;

	protected FactoryBean<ProcessEngine> processEngineFactory;
	protected FactoryBean<EventCalendar> eventCalendarFactory;


    private SimpleSimulationRun(Builder builder) {
		this.eventCalendarFactory = builder.getEventCalendarFactory();
		this.processEngineFactory = builder.getProcessEngineFactory();
		this.eventHandlerMap = new HashMap<String, SimulationEventHandler>();
		// init internal event handler map.
		eventHandlerMap.put( SimulationEvent.TYPE_END_SIMULATION, new NoopEventHandler() );
        if ( builder.getJobExecutor() != null)
            eventHandlerMap.put(SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, new AcquireJobNotificationEventHandler(builder.getJobExecutor()));
        this.customEventHandlerMap = builder.customEventHandlerMap;
	}

	@Override
    public void execute(Date simDate, Date dueDate) throws Exception {
		
		// init new process engine
		ProcessEngine processEngine = processEngineFactory.getObject();
				
		// add context in which simulation run is executed
		SimulationRunContext.setEventCalendar(eventCalendarFactory.getObject());
		SimulationRunContext.setProcessEngine(processEngine);

		// run simulation
		// init context and task calendar and simulation time is set to current 
		ClockUtil.setCurrentTime( simDate);

		if (dueDate != null)
			SimulationRunContext.getEventCalendar().addEvent(new SimulationEvent( dueDate.getTime(), SimulationEvent.TYPE_END_SIMULATION, null));

		initHandlers();
		
		SimulationEvent event = SimulationRunContext.getEventCalendar().removeFirstEvent();
		if (event != null)
			ClockUtil.setCurrentTime( new Date(event.getSimulationTime()));
		
		while( !simulationEnd( dueDate, event) ) {
			
			execute( event);
			
			event = SimulationRunContext.getEventCalendar().removeFirstEvent();
			if (event != null)
				ClockUtil.setCurrentTime( new Date( event.getSimulationTime()));
		}

		// remove simulation from simulation context
		SimulationRunContext.removeEventCalendar();
		SimulationRunContext.removeProcessEngine();
		processEngine.close();
	}

	private void initHandlers() {
		for( SimulationEventHandler handler : eventHandlerMap.values()) {
			handler.init();
		}

		for( SimulationEventHandler handler : customEventHandlerMap.values()) {
			handler.init();
		}
		
	}

	private static boolean simulationEnd(Date dueDate, SimulationEvent event) {
		if ( dueDate != null)
			return event == null || ( ClockUtil.getCurrentTime().after( dueDate ));
		return  event == null;
	}

	private void execute(SimulationEvent event) {
		// set simulation time to the next event for process engine too
		log.info( "Simulation time:" + ClockUtil.getCurrentTime());

		// internal handlers first
		SimulationEventHandler handler = eventHandlerMap.get( event.getType() );
		if ( handler != null) {
			log.debug("Handling event of type[{}] internaly.", event.getType());

			handler.handle( event);
		} else {
			handler = customEventHandlerMap.get( event.getType() );
			if ( handler != null) {
				log.debug("Handling event of type[{}].", event.getType());
	
				handler.handle( event);
			} else 
				log.warn("Event type[{}] does not have any handler assigned.", event.getType());
		}
	}

    public static class Builder {
        private Map<String, SimulationEventHandler> customEventHandlerMap;
        private HashMap<String, SimulationEventHandler> eventHandlerMap;
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

        public HashMap<String, SimulationEventHandler> getEventHandlerMap() {
            return eventHandlerMap;
        }

        public Builder eventHandlerMap(HashMap<String, SimulationEventHandler> eventHandlerMap) {
            this.eventHandlerMap = eventHandlerMap;
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

        public Map<String, SimulationEventHandler> getCustomEventHandlerMap() {
            return customEventHandlerMap;
        }

        public Builder customEventHandlerMap(Map<String, SimulationEventHandler> customEventHandlerMap) {
            this.customEventHandlerMap = customEventHandlerMap;
            return this;
        }

        public SimulationRun build() {
            return new SimpleSimulationRun(this);
        }
    }
}
