package org.activiti.crystalball.simulator.executor.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * execute any user task in constant time,
 * randomly chose one variable set.
 *
 */
public class ConstantUserTaskExecutor implements UserTaskExecutor {

	/**
	 * map to store task ID and variable values to return 
	 */
	protected Map<String, List<Map<String, Object>>> taskVariablesMap;

	Random randomGenerator = new Random();
	
	/** constant execution time */ 
	private long constantExecutionTime = 1;

	public ConstantUserTaskExecutor() {
	}

	public ConstantUserTaskExecutor( long time ) {
		this.constantExecutionTime = time;
	}
	
	/**
	 * return constant time for task execution
	 * choose randomly one possibility for setting variables 
	 */
	public long simulateTaskExecution(TaskEntity execTask, Map<String, Object> variables) {
		if (taskVariablesMap != null) {
			Map<String, Object> var = getVariables(execTask);
			if ( var != null)
				variables.putAll( var );
		}
		return constantExecutionTime;		
	}

	/**
	 * randomly choose one variable map for given task
	 * @param execTask
	 * @return
	 */
	protected Map<String, Object> getVariables(TaskEntity execTask) {
		if (taskVariablesMap.containsKey( execTask.getTaskDefinitionKey())) {
			List<Map<String, Object>> variablesList = taskVariablesMap.get( execTask.getTaskDefinitionKey());
			if (!variablesList.isEmpty()) {
				return variablesList.get( randomGenerator.nextInt(variablesList.size()));
			}
		}
		return null;
	}

	public long getConstantExecutionTime() {
		return constantExecutionTime;
	}

	public void setConstantExecutionTime(long constantExecutionTime) {
		this.constantExecutionTime = constantExecutionTime;
	}

	public Map<String, List<Map<String, Object>>> getTaskVariablesMap() {
		return taskVariablesMap;
	}

	public void setTaskVariablesMap(
			Map<String, List<Map<String, Object>>> taskVariablesMap) {
		this.taskVariablesMap = taskVariablesMap;
	}

}
