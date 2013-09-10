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
package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.executor.impl.ServiceImpl;
import org.activiti.crystalball.simulator.impl.cmd.GetResultVariablesCmd;
import org.activiti.crystalball.simulator.impl.cmd.IsRunningSimulationInstanceCmd;
import org.activiti.crystalball.simulator.impl.cmd.SaveResultCmd;
import org.activiti.crystalball.simulator.impl.cmd.StartSimulationInstanceCmd;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationInstanceEntity;
import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.activiti.crystalball.simulator.runtime.SimulationInstanceQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class RuntimeServiceImpl extends ServiceImpl implements RuntimeService {

	protected boolean startExecutor = false;
	
  public SimulationInstance startSimulationInstanceByKey(String name, String description, String author, Date start, Date end, int replication, Long seed, String simulationConfigUrl) {
    SimulationInstanceEntity simulationInstance = commandExecutor.execute(new StartSimulationInstanceCmd<SimulationInstanceEntity>(name, description, author, start, end, replication, seed, simulationConfigUrl));
	return simulationInstance;
  }
    
  public void deleteSimulationInstance(String simulationInstanceId, String deleteReason) {
//	    commandExecutor.execute(new DeleteProcessInstanceCmd(simulationInstanceId, deleteReason));
  }

//  public ExecutionQuery createExecutionQuery() {
//    return new ExecutionQueryImpl(commandExecutor);
//    return null;
//  }
  
//  public NativeExecutionQuery createNativeExecutionQuery() {
//    return new NativeExecutionQueryImpl(commandExecutor);
//	  return null;
//  }

//  public NativeProcessInstanceQuery createNativeProcessInstanceQuery() {
//    return new NativeProcessInstanceQueryImpl(commandExecutor);
//	  return null;
//  }
  
  public void suspendSimulationInstanceById(String processInstanceId) {
//    commandExecutor.execute(new SuspendProcessInstanceCmd(processInstanceId));
  }

  public void activateSimulationInstanceById(String processInstanceId) {
//    commandExecutor.execute(new ActivateProcessInstanceCmd(processInstanceId));
  }

	@Override
	public List<String> getActiveSimulationIds(String executionId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SimulationInstanceQuery createSimulationInstanceQuery() {
	    return new SimulationInstanceQueryImpl(commandExecutor);
	}

	@Override
	public ResultQuery createResultQuery() {
	    return new ResultQueryImpl(commandExecutor);
	}

	@Override
	public boolean isRunning(String simulationInstanceId) {
		return commandExecutor.execute(new IsRunningSimulationInstanceCmd<Boolean>(simulationInstanceId));
	}

	@Override
	public void saveResult(String type, Map<String, Object> variables) {
	    commandExecutor.execute(new SaveResultCmd(type, variables));
	}

	@Override
	public Map<String, Object> getResultVariables(String resultId) {
	    return commandExecutor.execute(new GetResultVariablesCmd(resultId, null , false));
	}
		  
}
