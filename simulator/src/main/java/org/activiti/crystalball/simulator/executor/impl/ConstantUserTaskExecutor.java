package org.activiti.crystalball.simulator.executor.impl;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * execute any user task in constant time,
 * randomly chose one variable set.
 *
 */
public class ConstantUserTaskExecutor extends AbstractRandomVariableMapUsertaskExecutor {
	
	/** constant execution time */ 
	long constantExecutionTime = 1;

	public ConstantUserTaskExecutor() {
	}

	public ConstantUserTaskExecutor( long time ) {
		this.constantExecutionTime = time;
	}
	
	public long getConstantExecutionTime() {
		return constantExecutionTime;
	}

	public void setConstantExecutionTime(long constantExecutionTime) {
		this.constantExecutionTime = constantExecutionTime;
	}

	@Override
	protected long getExecutionTime(TaskEntity execTask, Map<String, Object> variables) {
		return constantExecutionTime;
	}

}
