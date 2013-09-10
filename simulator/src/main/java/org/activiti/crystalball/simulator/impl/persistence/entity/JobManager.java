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

package org.activiti.crystalball.simulator.impl.persistence.entity;

import org.activiti.crystalball.simulator.ActivitiException;
import org.activiti.crystalball.simulator.impl.Page;
import org.activiti.crystalball.simulator.impl.cfg.TransactionListener;
import org.activiti.crystalball.simulator.impl.cfg.TransactionState;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.persistence.AbstractManager;
import org.activiti.crystalball.simulator.impl.simulationexecutor.ExclusiveJobAddedNotification;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutor;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutorContext;
import org.activiti.crystalball.simulator.impl.simulationexecutor.MessageAddedNotification;
import org.activiti.engine.impl.util.ClockUtil;

import java.util.Date;
import java.util.List;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class JobManager extends AbstractManager {

//  public void send(MessageEntity message) {
//    message.insert();
//    hintJobExecutor(message);    
//  }
// 
  public void schedule(TimerEntity timer) {
    Date duedate = timer.getDuedate();
    if (duedate==null) {
      throw new ActivitiException("duedate is null");
    }

    timer.insert();

    // Check if this timer fires before the next time the job executor will check for new timers to fire.
    // This is highly unlikely because normally waitTimeInMillis is 5000 (5 seconds)
    // and timers are usually set further in the future

    JobExecutor jobExecutor = SimulationContext.getSimulationEngineConfiguration().getJobExecutor();
    int waitTimeInMillis = jobExecutor.getWaitTimeInMillis();
    if (duedate.getTime() < (ClockUtil.getCurrentTime().getTime()+waitTimeInMillis)) {
      hintJobExecutor(timer);
    }
  }

  protected void hintJobExecutor(JobEntity job) {
    JobExecutor jobExecutor = SimulationContext.getSimulationEngineConfiguration().getJobExecutor();
    JobExecutorContext jobExecutorContext = SimulationContext.getJobExecutorContext();
    TransactionListener transactionListener = null;
    if(job.isExclusive()
            && jobExecutorContext != null
            && jobExecutorContext.isExecutingExclusiveJob()) {
      // lock job & add to the queue of the current processor
      Date currentTime = ClockUtil.getCurrentTime();
      job.setLockExpirationTime(new Date(currentTime.getTime() + jobExecutor.getLockTimeInMillis()));
      job.setLockOwner(jobExecutor.getLockOwner());
      transactionListener = new ExclusiveJobAddedNotification(job.getId());
    } else {
      // notify job executor:
      transactionListener = new MessageAddedNotification(jobExecutor);
    }
    SimulationContext.getCommandContext()
    .getTransactionContext()
    .addTransactionListener(TransactionState.COMMITTED, transactionListener);
  }

//  public void cancelTimers(ExecutionEntity execution) {
//    List<TimerEntity> timers = Context
//      .getCommandContext()
//      .getJobManager()
//      .findTimersByExecutionId(execution.getId());
//    
//    for (TimerEntity timer: timers) {
//      timer.delete();
//    }
//  }

  public JobEntity findJobById(String jobId) {
    return (JobEntity) getDbSqlSession().selectOne("selectJob", jobId);
  }
  
  @SuppressWarnings("unchecked")
  public List<JobEntity> findNextJobsToExecute(Page page) {
    Date now = ClockUtil.getCurrentTime();
    return getDbSqlSession().selectList("selectNextJobsToExecute", now, page);
  }
  
//  @SuppressWarnings("unchecked")
//  public List<Job> findJobsBySimulationInstanceId(String executionId) {
//    return getDbSqlSession().selectList("selectJobsByExecutionId", executionId);
//  }
//  
//  @SuppressWarnings("unchecked")
//  public List<JobEntity> findExclusiveJobsToExecute(String processInstanceId) {
//    Map<String,Object> params = new HashMap<String, Object>();
//    params.put("pid", processInstanceId);
//    params.put("now",ClockUtil.getCurrentTime());
//    return getDbSqlSession().selectList("selectExclusiveJobsToExecute", params);
//  }
//
//
  @SuppressWarnings("unchecked")
  public List<JobEntity> findUnlockedJobsByDuedate(Date duedate, Page page) {
    final String query = "selectUnlockedJobsByDuedate";
    return getDbSqlSession().selectList(query, duedate, page);
  }

    @SuppressWarnings("unchecked")
    public List<TimerEntity> findUnlockedTimersByDuedate(Date duedate, Page page) {
        final String query = "selectUnlockedJobsByDuedate";
        return getDbSqlSession().selectList(query, duedate, page);
    }

//
//  @SuppressWarnings("unchecked")
//  public List<TimerEntity> findTimersByExecutionId(String executionId) {
//    return getDbSqlSession().selectList("selectTimersByExecutionId", executionId);
//  }
//
//  @SuppressWarnings("unchecked")
//  public List<Job> findJobsByQueryCriteria(JobQueryImpl jobQuery, Page page) {
//    final String query = "selectJobByQueryCriteria";
//    return getDbSqlSession().selectList(query, jobQuery, page);
//  }
//
//  @SuppressWarnings("unchecked")
//  public List<Job> findJobsByConfiguration(String jobHandlerType, String jobHandlerConfiguration) {
//    Map<String, String> params = new HashMap<String, String>();
//    params.put("handlerType", jobHandlerType);
//    params.put("handlerConfiguration", jobHandlerConfiguration);
//    return getDbSqlSession().selectList("selectJobsByConfiguration", params);
//  }
//
//  public long findJobCountByQueryCriteria(JobQueryImpl jobQuery) {
//    return (Long) getDbSqlSession().selectOne("selectJobCountByQueryCriteria", jobQuery);
//  }

	public long findJobCountBySimulationInstanceId(String simulationInstanceId) {
	  return (Long) getDbSqlSession().selectOne("selectJobCountBySimulationInstanceId", simulationInstanceId);
	}

}
