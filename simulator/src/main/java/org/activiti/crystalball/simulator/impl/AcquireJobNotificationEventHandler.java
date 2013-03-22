package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.activiti.crystalball.simulator.processengine.jobexecutor.SimulationDefaultJobExecutor;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.jobexecutor.SimulationAcquireJobsRunnable;
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

	JobExecutor jobExecutor = null;
	
	public AcquireJobNotificationEventHandler(JobExecutor jobExecutor) {
		this.jobExecutor = jobExecutor;
	}
	
	@Override
	public void init(SimulationContext context) {
        log.info(jobExecutor.getName() + " starting to acquire jobs");
        jobExecutor.start();
        
	    context.getEventCalendar().addEvent( new SimulationEvent(
        	  ClockUtil.getCurrentTime().getTime(),  
        	  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
        	  ((SimulationDefaultJobExecutor) jobExecutor).getAcquireJobsRunnable()) );
        
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
        log.debug(" starting to acquire jobs [" + event + "]");
		SimulationAcquireJobsRunnable acquireJobs = (SimulationAcquireJobsRunnable) event.getProperty();
		acquireJobs.run();
	}

}
