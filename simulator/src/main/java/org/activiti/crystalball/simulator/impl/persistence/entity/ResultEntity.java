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
import java.util.Map;

import org.activiti.crystalball.simulator.Result;
import org.activiti.engine.impl.db.PersistentObject;


public class ResultEntity implements Serializable, Result, PersistentObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3210358623900149892L;
	
	protected String id;
	protected String runId;
	protected String type;
	protected String processDefinitionKey;
	protected String taskDefinitionKey;
	protected String description;
	
	public ResultEntity() {
	}

	public ResultEntity(String runId, String type, String processDefinitionKey, String taskDefinitionKey, String description) {
		this.runId = runId;
		this.type = type;
		this.processDefinitionKey = processDefinitionKey;
		this.taskDefinitionKey = taskDefinitionKey;
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ResultEntity))
			return false;
		ResultEntity ev = (ResultEntity) obj;
		
		return  type != null && type.equals(ev.getType())
				&& processDefinitionKey != null && processDefinitionKey.equals( ev.getProcessDefinitionKey() ) 
				&& taskDefinitionKey != null && taskDefinitionKey.equals( ev.taskDefinitionKey) 
				&& description != null && description.equals( ev.description);
	}
	
	@Override
	public int hashCode() {
		return type.hashCode() + taskDefinitionKey.hashCode() + processDefinitionKey.hashCode() + description.hashCode();
		
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
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#setProcessDefinitionKey(java.lang.String)
	 */
	@Override
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#setTaskDefinitionKey(java.lang.String)
	 */
	@Override
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#getTaskDefinitionKey()
	 */
	@Override
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#getProcessDefinitionKey()
	 */
	@Override
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.model.SimulationResult#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("type[");
		sb.append( type ).append("] process[").append(processDefinitionKey).append( "] taskDefinitionKey[").append(taskDefinitionKey)
		.append("] description[").append(description).append("]");
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
	    persistentState.put("processDefinitionKey", this.processDefinitionKey);
	    persistentState.put("taskDefinitionKey", this.taskDefinitionKey);
	    persistentState.put("description", this.description);
	    persistentState.put("runId", this.runId);
	    
	    return persistentState;
	}
}
