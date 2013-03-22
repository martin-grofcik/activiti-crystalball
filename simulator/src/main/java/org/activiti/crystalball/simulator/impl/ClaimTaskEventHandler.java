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
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimTaskEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(ClaimTaskEventHandler.class);
	
	TaskService taskService;
	IdentityService identityService;
	UserTaskExecutor userTaskExecutor;
	
	/**
	 *  everybody should start to work on something
	 */
	@Override
	public void init(SimulationContext context) {
		List<User> users = identityService.createUserQuery().list();
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		for (User user : users) {
			if ( !isUserFree( user.getId() )) {
				for ( Task execTask : taskService.createTaskQuery().taskAssignee( user.getId() ).list()) {
					// create complete task event
					Map<String, Object> props = new HashMap<String, Object>();
					props.put( "task", execTask.getId());
					Map<String, Object> variables = new HashMap<String, Object>();
					long userTaskDelta = userTaskExecutor.simulateTaskExecution((TaskEntity) execTask, variables);
					props.put( "variables", variables);
					
					// TODO simulateTaskExecution simulates new task execution - does not take into account work in the progress. It should be changed
					SimulationEvent completeEvent = new SimulationEvent( simulationTime + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE, props);
					// add complete task event
					context.getEventCalendar().addEvent( completeEvent);
				}
			} else {
				// schedule complete event
				// ClockUtil is set to simulation time in simulation run;
				List<Task> execTaskList = taskService.createTaskQuery().taskCandidateUser( user.getId()).orderByTaskPriority().desc().listPage(0, 1);
				if ( !execTaskList.isEmpty()) {
					Task execTask = execTaskList.get(0);
					if (execTask != null ) {
						// claim task and update assignee
						log.debug( SimpleDateFormat.getTimeInstance().format( new Date(simulationTime)) + 
								": claiming task  [" + execTask.getName() + "][" +execTask.getId() +"] for user ["+user.getId() +"]");
						taskService.claim( execTask.getId(), user.getId());
						
						// create complete task event
						Map<String, Object> props = new HashMap<String, Object>();
						props.put( "task", execTask.getId());
						Map<String, Object> variables = new HashMap<String, Object>();
						long userTaskDelta = userTaskExecutor.simulateTaskExecution((TaskEntity) execTask, variables);
						props.put( "variables", variables);

						SimulationEvent completeEvent = new SimulationEvent( simulationTime + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE, props);
						// schedule complete task event
						context.getEventCalendar().addEvent( completeEvent);
					}
				}
			}
		}
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		TaskEntity task = (TaskEntity) event.getProperty();
		
		if (claimTask(task)) {
		
			// create complete task event
			Map<String, Object> props = new HashMap<String, Object>();
			props.put( "task", task.getId());
			Map<String, Object> variables = new HashMap<String, Object>();
			long userTaskDelta = userTaskExecutor.simulateTaskExecution(task, variables);
			props.put( "variables", variables);
	
			SimulationEvent completeEvent = new SimulationEvent( ClockUtil.getCurrentTime().getTime() + userTaskDelta, SimulationEvent.TYPE_TASK_COMPLETE, props);
			// schedule complete task event
			context.getEventCalendar().addEvent( completeEvent);
			
			log.debug( SimpleDateFormat.getTimeInstance().format( new Date(event.getSimulationTime())) +": claimed {}, name: {}, assignee: {}", task, task.getName(), task.getAssignee());
		}
	}

	/** 
	 * if is it possible claim task
	 * 
	 * @param task
	 */
	private boolean claimTask(TaskEntity task) {
		if (task.getAssignee() != null && isUserFree( task.getAssignee() )) {
			taskService.claim(task.getId(), task.getAssignee());		
			return true;
		} else if ( !task.getCandidates().isEmpty() ) {
			// check task's candidates whether they are free and claim task
			for (IdentityLink identityLink : task.getCandidates()) {
				if (identityLink.getGroupId() != null) {
					String freeUserId = getFreeUserInGroup( identityLink.getGroupId() );
					if (freeUserId != null) {
						taskService.claim(task.getId(), freeUserId);
						task.setAssignee( freeUserId );
						return true;
					}
				} else if (identityLink.getUserId() != null && isUserFree( identityLink.getUserId() )) {
					taskService.claim(task.getId(), identityLink.getUserId());
					task.setAssignee( identityLink.getUserId() );
					return true;
				}
			}
		}
		return false;
	}

	private String getFreeUserInGroup(String groupId) {
		List<User> users =  identityService.createUserQuery().memberOfGroup(groupId).list();
		for (User user : users) {
			if ( isUserFree( user.getId())) {
				return user.getId();
			}
		}
		return null;
	}

	/**
	 * isUser able to work on the new task?
	 * 
	 * @param userId
	 * @return
	 */
	private boolean isUserFree(String userId) {
		return taskService.createTaskQuery().taskAssignee( userId).count() == 0;
	}
	
	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public UserTaskExecutor getUserTaskExecutor() {
		return userTaskExecutor;
	}

	public void setUserTaskExecutor(UserTaskExecutor userTaskExecutor) {
		this.userTaskExecutor = userTaskExecutor;
	}


}
