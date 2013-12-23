package org.activiti.crystalball.simulator.impl;

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


import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * claim task - we have unlimited resources to process a task. 
 *
 */
public class SimpleClaimTaskEventHandler implements SimulationEventHandler {
	private static final String DEFAULT_USER = "default_user";

	private static Logger log = LoggerFactory.getLogger(SimpleClaimTaskEventHandler.class);
	
	protected UserTaskExecutor userTaskExecutor;
	
	/**
	 * everybody should start to work on something - we have unlimited resources 
	 * :-)
	 * 
	 */
	@Override
	public void init() {
		TaskService taskService = SimulationRunContext.getTaskService();
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		
		for ( Task execTask : taskService.createTaskQuery().list()) {
			if (execTask != null ) {
				// claim task and update assignee
				log.debug( SimpleDateFormat.getTimeInstance().format( new Date(simulationTime)) + 
						": claiming task  [" + execTask.getName() + "][" +execTask.getId() +"] for user ["+DEFAULT_USER+"]");
				taskService.claim( execTask.getId(), DEFAULT_USER);
				
				// create complete task event
				Map<String, Object> props = new HashMap<String, Object>();
				props.put( "task", execTask.getId());
				Map<String, Object> variables = new HashMap<String, Object>();
				long userTaskDelta = userTaskExecutor.simulateTaskExecution((TaskEntity) execTask, variables);
				props.put( "variables", variables);

				SimulationEvent completeEvent = new SimulationEvent.Builder(simulationTime + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE).
          properties(props).
          build();
				// schedule complete task event
				SimulationRunContext.getEventCalendar().addEvent( completeEvent);
			}
		}
	}

	@Override
	public void handle(SimulationEvent event) {
		TaskEntity task = (TaskEntity) event.getProperty();
		
		SimulationRunContext.getTaskService().claim(task.getId(), DEFAULT_USER);		
		task.setAssignee( DEFAULT_USER );
		
		// create complete task event
		Map<String, Object> props = new HashMap<String, Object>();
		props.put( "task", task.getId());
		Map<String, Object> variables = new HashMap<String, Object>();
		long userTaskDelta = userTaskExecutor.simulateTaskExecution(task, variables);
		props.put( "variables", variables);
	
		SimulationEvent completeEvent = new SimulationEvent.Builder(ClockUtil.getCurrentTime().getTime() + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE).
      properties(props).
      build();
		// schedule complete task event
		SimulationRunContext.getEventCalendar().addEvent( completeEvent);
			
		log.debug( SimpleDateFormat.getTimeInstance().format( new Date(event.getSimulationTime())) +": claimed {}, name: {}, assignee: {}", task, task.getName(), task.getAssignee());

	}

	public UserTaskExecutor getUserTaskExecutor() {
		return userTaskExecutor;
	}

	public void setUserTaskExecutor(UserTaskExecutor userTaskExecutor) {
		this.userTaskExecutor = userTaskExecutor;
	}


}
