package org.activiti.crystalball.simulator.processengine.jobexecutor;

import java.util.List;

import org.activiti.crystalball.simulator.EventCalendar;
import org.activiti.engine.impl.cmd.AcquireJobsCmd;
import org.activiti.engine.impl.jobexecutor.AcquireJobsRunnable;
import org.activiti.engine.impl.jobexecutor.DefaultJobExecutor;
import org.activiti.engine.impl.jobexecutor.ExecuteJobsRunnable;
import org.activiti.engine.impl.jobexecutor.SimulationAcquireJobsRunnable;

/**
 * simulation is driven by simulation time. That's why JobExecutor 
 * has to be driven by simulation time too.  
 *
 */
public class SimulationDefaultJobExecutor extends DefaultJobExecutor {

	protected EventCalendar eventCalendar;
	
	public SimulationDefaultJobExecutor(EventCalendar eventCalendar) {
		this.eventCalendar = eventCalendar;		
	}
	
	/**
	 * use SimulationAcquireJobsRunnable instead of AcquireJobsRunnable. 
	 * To use simulation time instead of "real" time 
	 */
	@Override
	protected void ensureInitialization() { 
	    acquireJobsCmd = new AcquireJobsCmd(this);
	    acquireJobsRunnable = new SimulationAcquireJobsRunnable(this, eventCalendar);  
	}
	
	/**
	 * do not execute new thread - simulation time can move too forward.
	 * 
	 */
	public void executeJobs(List<String> jobIds) {
			(new ExecuteJobsRunnable(this, jobIds)).run();
	}

	public  AcquireJobsRunnable getAcquireJobsRunnable() {
		return acquireJobsRunnable;
	}
	
	  protected void startJobAcquisitionThread() {
//			if (jobAcquisitionThread == null) {
//				jobAcquisitionThread = new Thread(acquireJobsRunnable);
//				jobAcquisitionThread.start();
//			}
		}
		
		protected void stopJobAcquisitionThread() {
//			try {
//				jobAcquisitionThread.join();
//			} catch (InterruptedException e) {
//				log.log(
//						Level.WARNING,
//						"Interrupted while waiting for the job Acquisition thread to terminate",
//						e);
//			}	
//			jobAcquisitionThread = null;
		}
}
