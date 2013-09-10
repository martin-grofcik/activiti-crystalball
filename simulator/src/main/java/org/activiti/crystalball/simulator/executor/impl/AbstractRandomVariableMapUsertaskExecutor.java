package org.activiti.crystalball.simulator.executor.impl;

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


import org.activiti.crystalball.processengine.wrapper.queries.TaskWrapper;
import org.activiti.crystalball.simulator.SimUtils;
import org.activiti.crystalball.simulator.executor.UserTaskExecutor;

import java.util.List;
import java.util.Map;

/**
 * abstract class to randomly choose one variable set 
 *
 */
public abstract class AbstractRandomVariableMapUsertaskExecutor implements UserTaskExecutor {
	
	/**
	 * map to store task ID and variable values to return 
	 */
	protected Map<String, List<Map<String, Object>>> taskVariablesMap;

	public AbstractRandomVariableMapUsertaskExecutor() {
		super();
	}

	/**
	 * get execution time according to task executed
	 * 
	 * @param execTask
	 * @return
	 */
	protected abstract long getExecutionTime(TaskWrapper execTask, Map<String, Object> variables);

	/**
	 * randomly choose one variable map for given task
	 * @param execTask
	 * @return
	 */
	protected Map<String, Object> getVariables(TaskWrapper execTask) {
		if (taskVariablesMap.containsKey( execTask.getTaskDefinitionKey())) {
			List<Map<String, Object>> variablesList = taskVariablesMap.get( execTask.getTaskDefinitionKey());
			if (!variablesList.isEmpty()) {
				return variablesList.get( SimUtils.getRandomInt(variablesList.size()));
			}
		}
		return null;
	}

	/**
	 * return constant time for task execution
	 * choose randomly one possibility for setting variables 
	 */
	public long simulateTaskExecution(TaskWrapper execTask, Map<String, Object> variables) {
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