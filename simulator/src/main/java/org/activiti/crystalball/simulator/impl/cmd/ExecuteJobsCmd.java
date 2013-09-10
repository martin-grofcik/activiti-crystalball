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
package org.activiti.crystalball.simulator.impl.cmd;

import org.activiti.crystalball.simulator.ActivitiException;
import org.activiti.crystalball.simulator.JobNotFoundException;
import org.activiti.crystalball.simulator.impl.cfg.TransactionState;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;
import org.activiti.crystalball.simulator.impl.persistence.entity.JobEntity;
import org.activiti.crystalball.simulator.impl.simulationexecutor.FailedJobListener;
import org.activiti.crystalball.simulator.impl.simulationexecutor.JobExecutorContext;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author Tom Baeyens
 */
public class ExecuteJobsCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = Logger.getLogger(ExecuteJobsCmd.class.getName());
  
  protected String jobId;
 
  public ExecuteJobsCmd(String jobId) {
    this.jobId = jobId;
  }

  public Object execute(CommandContext commandContext) {
    if(jobId == null) {
      throw new ActivitiException("jobId is null");
    }
    
    if (log.isLoggable(Level.FINE)) {
      log.fine("Executing job " + jobId);
    }
    JobEntity job = commandContext
      .getJobManager()
      .findJobById(jobId);
    
    if (job == null) {
      throw new JobNotFoundException(jobId);
    }
    
    JobExecutorContext jobExecutorContext = SimulationContext.getJobExecutorContext();
    if(jobExecutorContext != null) { // if null, then we are not called by the job executor     
      jobExecutorContext.setCurrentJob(job);
    }
    
    try { 
      job.execute(commandContext);
    } catch (RuntimeException exception) {
      // When transaction is rolled back, decrement retries
      CommandExecutor commandExecutor = SimulationContext
        .getSimulationEngineConfiguration()
        .getCommandExecutorTxRequiresNew();
      
      commandContext.getTransactionContext().addTransactionListener(
        TransactionState.ROLLED_BACK, 
        new FailedJobListener(commandExecutor, jobId, exception));
       
      // throw the original exception to indicate the ExecuteJobCmd failed
      throw exception;
    } finally {
      if(jobExecutorContext != null) {
        jobExecutorContext.setCurrentJob(null);
      }
    }
    return null;
  }
  
  public String getJobId() {
		return jobId;
	}

}
