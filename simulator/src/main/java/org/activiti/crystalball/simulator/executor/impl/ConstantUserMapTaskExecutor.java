package org.activiti.crystalball.simulator.executor.impl;

import java.util.Map;

import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * execute user task in constant time specific for each user  
 * in case of user not found in the map use backup executor
 */
public class ConstantUserMapTaskExecutor implements UserTaskExecutor {
	
	/** maps userIds to constant time in milliseconds in which user will complete task */
	private Map<String, Integer> userExecutionMap;

	/** executor used in the case when there is no user task history which can be used*/
	private UserTaskExecutor backUpExecutor;	
	
	@Override
	public long simulateTaskExecution(TaskEntity execTask, Map<String, Object> variables) {
		String assigneId = execTask.getAssignee();
		if (userExecutionMap.get( assigneId) != null) {
			return userExecutionMap.get( assigneId);
		}		
		return backUpExecutor.simulateTaskExecution(execTask, variables);
	}

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
}
