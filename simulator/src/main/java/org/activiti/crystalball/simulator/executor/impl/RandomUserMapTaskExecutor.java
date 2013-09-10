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

import java.util.Map;

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
	protected long getExecutionTime(TaskWrapper execTask,
			Map<String, Object> variables) {
		return SimUtils.getRandomInt(max - min ) + min;
	}

}
