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
import org.activiti.engine.impl.jobexecutor.AcquireJobsRunnable;
import org.activiti.engine.impl.jobexecutor.SimulationDefaultJobExecutor;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Notify AcquireJobsRunnable to continue in execution 
 *
 */
public class AcquireJobNotificationEventHandler implements
		SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(AcquireJobNotificationEventHandler.class);

	SimulationDefaultJobExecutor jobExecutor = null;
	
	public AcquireJobNotificationEventHandler(SimulationDefaultJobExecutor jobExecutor) {
		this.jobExecutor = jobExecutor;
	}
	
	@Override
	public void init() {
        log.info(jobExecutor.getName() + " starting to acquire jobs");
        jobExecutor.start();
        
        SimulationRunContext.getEventCalendar().addEvent( new SimulationEvent(
        	  ClockUtil.getCurrentTime().getTime(),  
        	  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
        	  jobExecutor.getAcquireJobsRunnable() ) );
        
	}

	@Override
	public void handle(SimulationEvent event) {
        log.debug(" starting to acquire jobs [" + event + "]");
		AcquireJobsRunnable acquireJobs = (AcquireJobsRunnable) event.getProperty();
		acquireJobs.run();
	}

}
