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
package org.activiti.crystalball.simulator;

import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.activiti.crystalball.simulator.runtime.SimulationInstanceQuery;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/** Service which provides access to {@link Deployment}s,
 * {@link SimulationDefinition}s and {@link SimulationInstance}s.
 * 
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Daniel Meyer
 */
public interface RuntimeService {
  
	/**
	 * Starts a new simulation instance 
	 * 
	 * @param name
	 * @param description
	 * @param author
	 * @param start
	 * @param end
	 * @param replication
	 * @param seed - pseudo random number generator seed.
	 * @param simulationConfigUrl - repository service is not copied form activiti engine - claspath resources are used.
	 * @return
	 */
  SimulationInstance startSimulationInstanceByKey(String name, String description, String author, Date start, Date end, int replication, Long seed, String simulationConfigUrl);
  

  /** Delete an existing runtime process instance.
   * @param processInstanceId id of process instance to delete, cannot be null.
   * @param deleteReason reason for deleting, can be null.
   * @throws ActivitiException when no process instance is found with the given id.
   */
  void deleteSimulationInstance(String processInstanceId, String deleteReason);
    
  /** Finds the activity ids for all executions that are waiting in activities. 
   * This is a list because a single activity can be active multiple times.
   * @param executionId id of the execution, cannot be null.
   * @throws ActivitiException when no execution exists with the given executionId. 
   */
  List<String> getActiveSimulationIds(String executionId);

  // Queries ////////////////////////////////////////////////////////
  
  /** Creates a new {@link ExecutionQuery} instance, 
   * that can be used to query the executions and process instances. */
  ExecutionQuery createExecutionQuery();
  
  /**
   * creates a new {@link NativeExecutionQuery} to query {@link Execution}s
   * by SQL directly
   */
  NativeExecutionQuery createNativeExecutionQuery();
  
  /**
   * Creates a new {@link SimulationInstanceQuery} instance, that can be used
   * to query simulation instances.
   */
  SimulationInstanceQuery createSimulationInstanceQuery();

  /**
   * creates a new {@link NativeProcessInstanceQuery} to query {@link ProcessInstance}s 
   * by SQL directly
   */
  NativeProcessInstanceQuery createNativeProcessInstanceQuery();
  
  // Process instance state //////////////////////////////////////////
    
  /**
   * Suspends the process instance with the given id. 
   * 
   * If a process instance is in state suspended, activiti will not 
   * execute jobs (timers, messages) associated with this instance.
   * 
   * If you have a process instance hierarchy, suspending
   * one process instance form the hierarchy will not suspend other 
   * process instances form that hierarchy.
   * 
   *  @throws ActivitiException if no such processInstance can be found or if the process instance is already in state suspended.
   */
  void suspendSimulationInstanceById(String processInstanceId);
  
  /**
   * Activates the process instance with the given id. 
   * 
   * If you have a process instance hierarchy, suspending
   * one process instance form the hierarchy will not suspend other 
   * process instances form that hierarchy.
   * 
   * @throws ActivitiException if no such processInstance can be found or if the process instance is already in state active.
   */
  void activateSimulationInstanceById(String processInstanceId);

  /**
   * is simulationInstance still running
   * @param SimulationInstanceId
   * @return
   */
  boolean isRunning(String SimulationInstanceId);


  ResultQuery createResultQuery();
  
	/**
	 * Saves the new result to the persistent data store.
	 *  
	 * @param type
	 * @param resultVariables
	 */
	void saveResult(String type, Map<String, Object> resultVariables);

	/**
	 * get variables for given resultId
	 * @param resultId
	 * @return
	 */
	Map<String, Object> getResultVariables(String resultId);

}