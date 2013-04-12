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
