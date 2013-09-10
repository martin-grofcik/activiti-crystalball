/* Licensed under the Apache License, Version 2.0 (the "License");
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

import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.JobEntity;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntity;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;
import org.activiti.crystalball.simulator.impl.simulationexecutor.SimulationRunExecuteJobHandler;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class StartSimulationInstanceCmd<T> implements Command<SimulationInstanceEntity>, Serializable {

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String description;
	protected String author;
	
	/* scenario parameters */
	/** Start time of the scenario */
	protected Date start;
	/** End time of the scenario */ 
	protected Date end;
	/** Number of replication of that scenario that needs to be executed. Defaults to 1. */
	protected int replication = 1;
	/**
	 * A random seed to be used to initialize a pseudo random number generator.
	 * Given the exact same simulation experiment and a given seed, the  results should 
	 * be the same across executions.
	 */
	protected Long seed;

	protected String simulationConfigUrl;
  
  public StartSimulationInstanceCmd(String name, String description, String author, Date start, Date end, int replication, Long seed, String simulationConfigUrl) {
	  this.name = name;
	  this.description = description;
	  this.author = author;
	  this.start = start;
	  this.end = end;
	  this.replication = replication;
	  this.seed = seed;
	  this.simulationConfigUrl = simulationConfigUrl;
  }
  
  public SimulationInstanceEntity execute(CommandContext commandContext) {

    SimulationInstanceEntity simulationInstance = commandContext.getSimulationInstanceManager()
    	    .createSimulationInstance(name, description, author, start, end, replication, seed, simulationConfigUrl);

    for (int counter = 0; counter < replication; counter++) {
    	SimulationRunEntity simulationRunEntity = commandContext.getSimulationRunManager()
        	    .createSimulationRun(simulationInstance, counter);
	    JobEntity job = new JobEntity();
	    job.setSimulationInstanceId(simulationInstance.getId());
	    job.setJobHandlerType(SimulationRunExecuteJobHandler.TYPE);
	    job.setJobHandlerConfiguration(simulationRunEntity.getId());
	    commandContext.getJobManager().insert(job);
    }
    
    return simulationInstance;
  }
}
