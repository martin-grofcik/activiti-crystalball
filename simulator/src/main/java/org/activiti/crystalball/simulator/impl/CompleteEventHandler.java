package org.activiti.crystalball.simulator.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleteEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(CompleteEventHandler.class);
		
	UserTaskExecutor userTaskExecutor;
	
	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		TaskService taskService = context.getTaskService();
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		
		String taskId = (String) event.getProperty("task");
		Task task = context.getTaskService().createTaskQuery().taskId( taskId ).singleResult();		
		String assignee = task.getAssignee();
		
		// fulfill variables
		@SuppressWarnings("unchecked")
		Map<String, Object> variables = (Map<String, Object>) event.getProperty("variables");		

		context.getTaskService().complete( taskId, variables );
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

				SimulationEvent completeEvent = new SimulationEvent( simulationTime + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE, props);
				//schedule complete task event
				context.getEventCalendar().addEvent( completeEvent);
			}
		}

	}

	@Override
	public void init(SimulationContext context) {
		
	}
	
	public UserTaskExecutor getUserTaskExecutor() {
		return userTaskExecutor;
	}

	public void setUserTaskExecutor(UserTaskExecutor userTaskExecutor) {
		this.userTaskExecutor = userTaskExecutor;
	}

}
