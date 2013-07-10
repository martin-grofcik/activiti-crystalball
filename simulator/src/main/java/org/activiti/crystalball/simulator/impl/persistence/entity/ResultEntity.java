package org.activiti.crystalball.simulator.impl.persistence.entity;

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


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.crystalball.simulator.Result;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.engine.impl.db.PersistentObject;

public class ResultEntity extends VariableScopeImpl implements Serializable, Result, PersistentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3210358623900149892L;
	
	protected String id;
	protected String runId;
	protected String type;

	/** loaded sim run according runId */
	protected SimulationRunEntity simulationRun;
	
	public ResultEntity() {
	}

	public ResultEntity(String runId, String type) {
		this.runId = runId;
		this.type = type;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null || !(obj instanceof ResultEntity))
//			return false;
//		ResultEntity ev = (ResultEntity) obj;
//		
//		return  type != null && type.equals(ev.getType())
//				&& processDefinitionKey != null && processDefinitionKey.equals( ev.getProcessDefinitionKey() ) 
//				&& taskDefinitionKey != null && taskDefinitionKey.equals( ev.taskDefinitionKey) 
//				&& description != null && description.equals( ev.description);
//	}
	
	@Override
	public int hashCode() {
		return runId.hashCode() + type.hashCode() + variableInstances.hashCode();
		
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#getDescription()
	 */
	
	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}
	
	public Map<String, VariableInstanceEntity> getVariableInstances() {
		ensureVariableInstancesInitialized();
	    return variableInstances;
    }
	  
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("runId[");
		sb.append(runId).append("] type[").append( type ).append("] variables[").append(variableInstances).append("]");
		return sb.toString();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;		
	}

	@Override
	public Object getPersistentState() {
	    Map<String, Object> persistentState = new  HashMap<String, Object>();
	    persistentState.put("variables", this.variableInstances);
	    persistentState.put("runId", this.runId);
	    if (id != null)
	    	persistentState.put("id", this.getId());
	    return persistentState;
	}

	// variables
	@Override
	protected List<VariableInstanceEntity> loadVariableInstances() {
	    return SimulationContext
	    	      .getCommandContext()
	    	      .getVariableInstanceManager()
	    	      .findVariableInstancesByResultId(id);
	}

	@Override
	protected VariableScopeImpl getParentVariableScope() {
		// parent variable scope is not implemented yet
//		getSimulationRun();
		return null;
	}

	  @Override
	  protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
	    variableInstance.setResultId(this.id);
	    variableInstance.setRunId(runId);
	  }
	
    public SimulationRunEntity getSimulationRun() {
	    if ( (simulationRun ==null) && (runId!=null) ) {
		      this.simulationRun = SimulationContext
		        .getCommandContext()
		        .getSimulationRunManager()
		        .findSimulationRunById(runId);
		}
	    return simulationRun;
	}

}
