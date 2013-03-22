package org.activiti.crystalball.simulator.executor;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

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
	long simulateTaskExecution( TaskEntity execTask, Map<String, Object> variables);
}