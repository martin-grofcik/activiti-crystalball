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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.crystalball.simulator.SimulationRun;
import org.activiti.crystalball.simulator.runtime.SuspensionState;
import org.activiti.engine.impl.db.PersistentObject;


public class SimulationRunEntity extends SimulationRun implements	PersistentObject {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	protected String id;

	/**
	 * simulation entity in which simulation run is performed
	 */
	protected String simulationId;
	protected SimulationInstanceEntity simulation;
	
	/**
	 * which simulation run is it in the simulation experiment
	 */
	protected int replication;

	/** 
	 * simulation time for given run.
	 */
	protected Date simulationTime;

	/** 
	 * simulation run state
	 */
	protected int suspensionState = SuspensionState.ACTIVE.getStateCode();

	public SimulationRunEntity() {
	}
	
	public String toString() {
		return "SimulationRunEntity[" + id + "]";
	}

	// getters and setters
	// //////////////////////////////////////////////////////

	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("suspensionState", this.suspensionState);
		return persistentState;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getSimulationId() {
		return simulationId;
	}

	public void setSimulationEntityId(String simulationId) {
		this.simulationId = simulationId;
	}

	public int getReplication() {
		return replication;
	}

	public void setReplication(int replication) {
		this.replication = replication;
	}

	public int getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}

	public Date getSimulationTime() {
		return simulationTime;
	}

	public void setSimulationTime(Date simulationTime) {
		this.simulationTime = simulationTime;
	}

	public void setSimulationId(String simulationId) {
		this.simulationId = simulationId;
	}

	public SimulationInstanceEntity getSimulation() {
		return simulation;
	}

	public void setSimulation(SimulationInstanceEntity simulation) {
		this.simulation = simulation;
	}

}
