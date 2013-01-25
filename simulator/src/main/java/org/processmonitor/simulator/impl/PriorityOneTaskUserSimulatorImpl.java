package org.processmonitor.simulator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.Task;
import org.processmonitor.simulator.EventCalendar;
import org.processmonitor.simulator.SimulationEvent;
import org.processmonitor.simulator.Simulator;
import org.processmonitor.simulator.executor.UserTaskExecutor;

/**
 * simulate users' work  
 * assumptions:
 * 		user can work only on one task in parallel - only one task is claimed in time
 * 		user takes task with the highest priority from the task list
 * 		when user does not have any task assigned (claimed), he takes immediately next one
 */
public class PriorityOneTaskUserSimulatorImpl implements Simulator {

	private IdentityService identityService;
	private TaskService taskService;
	private UserTaskExecutor userTaskExecutor;

	/**
	 * for each user in the system schedule his task execution
	 */
	public void init(EventCalendar calendar) {
		List<User> users = identityService.createUserQuery().list();
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		for (User user : users) {
			if ( !isFree( user.getId() )) {
				for ( Task execTask : taskService.createTaskQuery().taskAssignee( user.getId() ).list()) {
					// create complete task event
					Map<String, Object> props = new HashMap<String, Object>();
					props.put( "task", execTask.getId());
					// TODO simulateTaskExecution simulates new task execution - does not take into account work in the progress. It should be changed
					SimulationEvent completeEvent = new SimulationEvent( userTaskExecutor.simulateTaskExecution(execTask, simulationTime), SimulationEvent.TYPE_COMPLETE, props);
					// add complete task event
					calendar.addEvent( completeEvent);
				}
			} else {
				scheduleWork( user.getId(), calendar);
			}
		}
	}

	/**
	 * for each user in the system simulate his work - claim task and schedule it completition 
	 */
	@Override
	public void simulate(EventCalendar calendar) {
		List<User> users = identityService.createUserQuery().list();

		for (User user : users) {
			if ( isFree( user.getId() )) {
				scheduleWork( user.getId(), calendar);
			}
		}		
	}
	
	private void scheduleWork(String userId, EventCalendar calendar) {
		// ClockUtil is set to simulation time in simulation run;
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		List<Task> execTaskList = taskService.createTaskQuery().taskCandidateUser( userId).orderByTaskPriority().desc().listPage(0, 1);
		if ( !execTaskList.isEmpty()) {
			Task execTask = execTaskList.get(0);
			if (execTask != null ) {
				// claim task and update assignee
				taskService.claim( execTask.getId(), userId);
				execTask.setAssignee(userId);
				
				// create complete task event
				Map<String, Object> props = new HashMap<String, Object>();
				props.put( "task", execTask.getId());
				SimulationEvent completeEvent = new SimulationEvent( userTaskExecutor.simulateTaskExecution(execTask, simulationTime), SimulationEvent.TYPE_COMPLETE, props);
				// schedule complete task event
				calendar.addEvent( completeEvent);
			}
		}
	}

	private boolean isFree(String userId) {
		return 0 == taskService.createTaskQuery().taskAssignee( userId).count();
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
