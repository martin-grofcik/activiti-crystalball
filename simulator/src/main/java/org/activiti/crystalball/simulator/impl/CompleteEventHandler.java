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
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(CompleteEventHandler.class);
		
	UserTaskExecutor userTaskExecutor;
	
	@Override
	public void handle(SimulationEvent event) {
		TaskService taskService = SimulationRunContext.getTaskService();
		long simulationTime = SimulationRunContext.getClock().getCurrentTime().getTime();
		
		String taskId = (String) event.getProperty("task");
		Task task = SimulationRunContext.getTaskService().createTaskQuery().taskId( taskId ).singleResult();		
		String assignee = task.getAssignee();
		
		// fulfill variables
		@SuppressWarnings("unchecked")
		Map<String, Object> variables = (Map<String, Object>) event.getProperty("variables");		

		SimulationRunContext.getTaskService().complete( taskId, variables );
		log.debug( SimpleDateFormat.getTimeInstance().format( new Date(event.getSimulationTime())) +": completed {}, {}, {}, {}", task, task.getName(), assignee, variables);
		
		//claim new task to assignee
		List<Task> execTaskList = taskService.createTaskQuery().taskCandidateUser( assignee).orderByTaskPriority().desc().listPage(0, 1);
		if ( !execTaskList.isEmpty()) {
			TaskEntity execTask = (TaskEntity) execTaskList.get(0);
			if (execTask != null ) {
				// claim task and update assignee
				log.debug( SimpleDateFormat.getTimeInstance().format( new Date(simulationTime)) + 
						": claiming task  [" + execTask.getName() + "][" +execTask.getId() +"] for user ["+assignee +"]");
				taskService.claim( execTask.getId(), assignee);
				execTask.setAssignee(assignee);
				
				// create complete task event
				Map<String, Object> props = new HashMap<String, Object>();
				props.put( "task", execTask.getId());
				variables = new HashMap<String, Object>();
				long userTaskDelta = userTaskExecutor.simulateTaskExecution(execTask, variables);
				props.put( "variables", variables);

				SimulationEvent completeEvent = new SimulationEvent.Builder(SimulationEvent.TYPE_TASK_COMPLETE).
                      simulationTime(simulationTime + userTaskDelta).
                      properties(props).
                      build();
				//schedule complete task event
				SimulationRunContext.getEventCalendar().addEvent( completeEvent);
			}
		}

	}

	@Override
	public void init() {
		
	}
	
	public void setUserTaskExecutor(UserTaskExecutor userTaskExecutor) {
		this.userTaskExecutor = userTaskExecutor;
	}

}
