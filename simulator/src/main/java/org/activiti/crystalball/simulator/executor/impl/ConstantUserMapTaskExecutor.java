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


import java.util.Map;

import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * execute user task in constant time specific for each user  
 * in case of user not found in the map use backup executor
 */
public class ConstantUserMapTaskExecutor extends AbstractRandomVariableMapUsertaskExecutor {
	
	/** maps userIds to constant time in milliseconds in which user will complete task */
	private Map<String, Integer> userExecutionMap;

	/** executor used in the case when there is no user task history which can be used*/
	private UserTaskExecutor backUpExecutor;	
	
	public Map<String, Integer> getUserExecutionMap() {
		return userExecutionMap;
	}

	public void setUserExecutionMap(Map<String, Integer> userExecutionMap) {
		this.userExecutionMap = userExecutionMap;
	}

	public UserTaskExecutor getBackUpExecutor() {
		return backUpExecutor;
	}

	public void setBackUpExecutor(UserTaskExecutor backUpExecutor) {
		this.backUpExecutor = backUpExecutor;
	}

	@Override
	protected long getExecutionTime(TaskEntity execTask, Map<String, Object> variables) {
		String assigneId = execTask.getAssignee();
		if (userExecutionMap.get( assigneId) != null) {
			return userExecutionMap.get( assigneId);
		}		
		return backUpExecutor.simulateTaskExecution(execTask, variables);
	}
}
