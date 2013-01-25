package org.processmonitor.simulator.executor.impl;

import java.util.Map;

import org.activiti.engine.task.Task;
import org.processmonitor.simulator.executor.UserTaskExecutor;

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
	public long simulateTaskExecution(Task execTask, long simulationTime) {
		String assigneId = execTask.getAssignee();
		if (userExecutionMap.get( assigneId) != null) {
			return simulationTime + userExecutionMap.get( assigneId);
		}		
		return backUpExecutor.simulateTaskExecution(execTask, simulationTime);
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
