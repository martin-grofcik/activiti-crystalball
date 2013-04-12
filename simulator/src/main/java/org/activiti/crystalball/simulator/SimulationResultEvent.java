package org.activiti.crystalball.simulator;

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


public class SimulationResultEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3210358623900149892L;
	
	private final String type;
	private final String processDefinitionKey;
	private final String taskDefinitionKey;
	private String description;
	
	public SimulationResultEvent(String type, String processDefinitionKey, String taskDefinitionKey, String description) {
		this.type = type;
		this.processDefinitionKey = processDefinitionKey;
		this.taskDefinitionKey = taskDefinitionKey;
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SimulationResultEvent))
			return false;
		SimulationResultEvent ev = (SimulationResultEvent) obj;
		
		return  type != null && type.equals(ev.getType())
				&& processDefinitionKey != null && processDefinitionKey.equals( ev.getProcessDefinitionKey() ) 
				&& taskDefinitionKey != null && taskDefinitionKey.equals( ev.taskDefinitionKey) 
				&& description != null && description.equals( ev.description);
	}
	
	@Override
	public int hashCode() {
		return type.hashCode() + taskDefinitionKey.hashCode() + processDefinitionKey.hashCode() + description.hashCode();
		
	}

	public String getType() {
		return type;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("type[");
		sb.append( type ).append("] process[").append(processDefinitionKey).append( "] taskDefinitionKey[").append(taskDefinitionKey)
		.append("] description[").append(description).append("]");
		return sb.toString();
	}
}
