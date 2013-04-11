package org.activiti.crystalball.simulator.executor.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * abstract class to randomly choose one variable set 
 *
 */
public abstract class AbstractRandomVariableMapUsertaskExecutor implements UserTaskExecutor {
	
	/**
	 * map to store task ID and variable values to return 
	 */
	protected Map<String, List<Map<String, Object>>> taskVariablesMap;

	protected Random randomGenerator = new Random();

	public AbstractRandomVariableMapUsertaskExecutor() {
		super();
	}

	/**
	 * get execution time according to task executed
	 * 
	 * @param execTask
	 * @return
	 */
	protected abstract long getExecutionTime(TaskEntity execTask, Map<String, Object> variables);

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
		return getExecutionTime(execTask, variables);		
	}

	public Map<String, List<Map<String, Object>>> getTaskVariablesMap() {
		return taskVariablesMap;
	}

	public void setTaskVariablesMap(
			Map<String, List<Map<String, Object>>> taskVariablesMap) {
		this.taskVariablesMap = taskVariablesMap;
	}
}