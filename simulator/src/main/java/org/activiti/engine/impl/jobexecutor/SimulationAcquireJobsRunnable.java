package org.activiti.engine.impl.jobexecutor;

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


import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.crystalball.simulator.EventCalendar;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.jobexecutor.AcquireJobsRunnable;
import org.activiti.engine.impl.jobexecutor.AcquiredJobs;
import org.activiti.engine.impl.jobexecutor.GetUnlockedTimersByDuedateCmd;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.util.ClockUtil;

/**
 * "thread" has to be driven by simulation time.
 * In fact new thread is not started. Simulation is driven by simulation time. 
 * In case of thread for acquiring jobs, synchronization will be over complicated.
 * 
 */
public class SimulationAcquireJobsRunnable extends AcquireJobsRunnable {
	
	private static Logger log = Logger.getLogger(SimulationAcquireJobsRunnable.class.getName());

	protected EventCalendar eventCalendar;
	
	/**
	 * 
	 * @param jobExecutor
	 * @param eventCalendar - calendar into which next AcqureJobs execution is scheduled 
	 */
	public SimulationAcquireJobsRunnable(JobExecutor jobExecutor, EventCalendar eventCalendar) {
	    super(jobExecutor);
	    this.eventCalendar = eventCalendar;
	}
	
	  public synchronized void run() {
//		    if (log.isLoggable(Level.INFO)) {
//		      log.info(jobExecutor.getName() + " starting to acquire jobs");
//		    }

		    final CommandExecutor commandExecutor = jobExecutor.getCommandExecutor();

		    // while is not needed - repetition is done by event scheduling 
//		    while (!isInterrupted) {
              isWaiting.set(false);
		      int maxJobsPerAcquisition = jobExecutor.getMaxJobsPerAcquisition();

		      try {
		        AcquiredJobs acquiredJobs = commandExecutor.execute(jobExecutor.getAcquireJobsCmd());

		        for (List<String> jobIds : acquiredJobs.getJobIdBatches()) {
		          jobExecutor.executeJobs(jobIds);
		        }

		        // if all jobs were executed
		        millisToWait = jobExecutor.getWaitTimeInMillis();
		        int jobsAcquired = acquiredJobs.getJobIdBatches().size();
		        if (jobsAcquired < maxJobsPerAcquisition) {
		          
		          isJobAdded = false;
		          
		          // check if the next timer should fire before the normal sleep time is over
		          Date duedate = new Date(ClockUtil.getCurrentTime().getTime() + millisToWait);
		          List<TimerEntity> nextTimers = commandExecutor.execute(new GetUnlockedTimersByDuedateCmd(duedate, new Page(0, 1)));
		          
		          if (!nextTimers.isEmpty()) {
		          long millisTillNextTimer = nextTimers.get(0).getDuedate().getTime() - ClockUtil.getCurrentTime().getTime();
		            if (millisTillNextTimer < millisToWait && millisTillNextTimer != 0) {
		              millisToWait = millisTillNextTimer;
		            }
		          }
		          
		        } else {
		          millisToWait = 0;
		        }

		      } catch (ActivitiOptimisticLockingException optimisticLockingException) { 
		        // See http://jira.codehaus.org/browse/ACT-1390
		        if (log.isLoggable(Level.FINE)) {
		          log.fine("Optimistic locking exception during job acquisition. If you have multiple job executors running against the same database, " +
		          		"this exception means that this thread tried to acquire a job, which already was acquired by another job executor acquisition thread." +
		          		"This is expected behavior in a clustered environment. " +
		          		"You can ignore this message if you indeed have multiple job executor acquisition threads running against the same database. " +
		          		"Exception message: " + optimisticLockingException.getMessage());
		        }
		      } catch (Exception e) {
		        if (log.isLoggable(Level.SEVERE)) {
		          log.log(Level.SEVERE, "exception during job acquisition: " + e.getMessage(), e);          
		        }
		        millisToWait *= waitIncreaseFactor;
		        if (millisToWait > maxWait) {
		          millisToWait = maxWait;
		        } else if (millisToWait==0) {
		          millisToWait = jobExecutor.getWaitTimeInMillis();
		        }
		      }

		      if ((millisToWait > 0) && (!isJobAdded)) {
		        try {
		          if (log.isLoggable(Level.FINE)) {
		            log.fine("job acquisition thread sleeping for " + millisToWait + " millis");
		          }
		          synchronized (MONITOR) {
		            if(!isInterrupted) {
		              isWaiting.set(true);
		              
		              eventCalendar.addEvent( new SimulationEvent(
		            		  ClockUtil.getCurrentTime().getTime() + millisToWait,  
		            		  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
		            		  this) );
		              // do not need to wait. - event scheduling is enough
		              //MONITOR.wait(millisToWait);
		            }
		          } 
		          		          
		          if (log.isLoggable(Level.FINE)) {
		            log.fine("job acquisition thread woke up");
		          }
		        } finally {
//		          isWaiting.set(false);
		        }
		      } else {
		    	  // schedule run now
	              eventCalendar.addEvent( new SimulationEvent(
	            		  ClockUtil.getCurrentTime().getTime(),  
	            		  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
	            		  this) );
		      }
//		    }
		    
//		    if (log.isLoggable(Level.INFO)) {
//		      log.info(jobExecutor.getName() + " stopped job acquisition");
//		    }
	  }
	  
	  public void stop() {
	    synchronized (MONITOR) {
	      isInterrupted = true; 
	      if(isWaiting.compareAndSet(true, false)) { 
	    	  // Notify is not needed - event is enough
              eventCalendar.addEvent( new SimulationEvent(
            		  ClockUtil.getCurrentTime().getTime(),  
            		  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
            		  this) );
              //MONITOR.notifyAll();
	        }
	      }
	  }

	  public void jobWasAdded() {    
		    isJobAdded = true;
		    if(isWaiting.compareAndSet(true, false)) { 
		      // ensures we only notify once
		      // I am OK with the race condition      
		      synchronized (MONITOR) {
//		        MONITOR.notifyAll();
		    	//Notify is not needed - event is enough
                eventCalendar.addEvent( new SimulationEvent(
	            	  ClockUtil.getCurrentTime().getTime(),  
	            	  SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, 
	            	  this) );
		      }
		    }    
		  }

}
