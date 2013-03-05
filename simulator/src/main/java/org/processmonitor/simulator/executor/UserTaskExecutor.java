package org.processmonitor.simulator.executor;

import java.util.Map;

import org.activiti.engine.task.Task;

/**
 * Simulate user task execution 
 *
 */
public interface UserTaskExecutor {
	
	/**
	 * 
	 * 
	 * @param execTask task to execute 
	 * @param variables variables to set on task complete event
	 * @return simulation time increased by UserTask execution
	 */
	long simulateTaskExecution( Task execTask, Map<String, Object> variables);
}