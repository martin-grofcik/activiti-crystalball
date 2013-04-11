package org.activiti.crystalball.simulator.executor.impl;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * generate execution time (unified distribution) in given interval min inclusive max exclusive 
 *
 */
public class RandomUserMapTaskExecutor extends AbstractRandomVariableMapUsertaskExecutor {

	protected int min;
	protected int max; 
	
	public RandomUserMapTaskExecutor(int min, int max) {
		super();
		this.min = min;
		this.max = max;		
	}
	
	@Override
	protected long getExecutionTime(TaskEntity execTask,
			Map<String, Object> variables) {
		return randomGenerator.nextInt(max - min ) + min;
	}

}
