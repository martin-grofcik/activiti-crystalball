package org.processmonitor.simulator.executor.impl;

import java.util.Map;

import org.activiti.engine.task.Task;
import org.processmonitor.simulator.executor.UserTaskExecutor;

/**
 * execute any user task in constant time 
 *
 */
public class ConstantUserTaskExecutor implements UserTaskExecutor {

	/** constant execution time */ 
	private long constantExecutionTime = 1;

	public ConstantUserTaskExecutor() {
	}

	public ConstantUserTaskExecutor( long time ) {
		this.constantExecutionTime = time;
	}
	
	public long simulateTaskExecution(Task execTask, Map<String, Object> variables) {
		return constantExecutionTime;
	}

	public long getConstantExecutionTime() {
		return constantExecutionTime;
	}

	public void setConstantExecutionTime(long constantExecutionTime) {
		this.constantExecutionTime = constantExecutionTime;
	}

}
