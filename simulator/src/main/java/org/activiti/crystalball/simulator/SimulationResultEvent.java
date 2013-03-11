package org.activiti.crystalball.simulator;

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
