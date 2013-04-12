package org.activiti.crystalball.simulator.executor;

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