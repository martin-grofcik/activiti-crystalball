package org.activiti.crystalball.simulator;

/**
 * simulation results interface 
 * - event in the given run with type, processDefinitionKey, and description. 
 * Properties map should be considered. 
 *
 */
public interface Result {

	public abstract String getRunId();

	public abstract void setRunId(String runId);

	public abstract String getType();

	public abstract void setType(String type);

	public abstract void setProcessDefinitionKey(String processDefinitionKey);

	public abstract void setTaskDefinitionKey(String taskDefinitionKey);

	public abstract String getTaskDefinitionKey();

	public abstract String getProcessDefinitionKey();

	public abstract String getDescription();

	public abstract void setDescription(String description);

}