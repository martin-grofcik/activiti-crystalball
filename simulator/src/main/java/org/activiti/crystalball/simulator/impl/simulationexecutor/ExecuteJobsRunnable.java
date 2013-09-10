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
package org.activiti.crystalball.simulator.impl.simulationexecutor;

import org.activiti.crystalball.simulator.impl.cmd.ExecuteJobsCmd;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.interceptor.CommandExecutor;

import java.util.List;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class ExecuteJobsRunnable implements Runnable {

  private final List<String> jobIds;
  private final JobExecutor jobExecutor;
  
  public ExecuteJobsRunnable(JobExecutor jobExecutor, List<String> jobIds) {
    this.jobExecutor = jobExecutor;
    this.jobIds = jobIds;
  }

  public void run() {
    final JobExecutorContext jobExecutorContext = new JobExecutorContext();
    final List<String> currentProcessorJobQueue = jobExecutorContext.getCurrentProcessorJobQueue();
    final CommandExecutor commandExecutor = jobExecutor.getCommandExecutor();

    currentProcessorJobQueue.addAll(jobIds);
    
    SimulationContext.setJobExecutorContext(jobExecutorContext);
    try {
      while (!currentProcessorJobQueue.isEmpty()) {
        commandExecutor.execute(new ExecuteJobsCmd(currentProcessorJobQueue.remove(0)));
      }      
    }finally {
      SimulationContext.removeJobExecutorContext();
    }
  }
}
