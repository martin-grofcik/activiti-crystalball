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


import org.activiti.crystalball.simulator.executor.UserTaskExecutor;
import org.activiti.crystalball.simulator.impl.PlaybackStartProcessEventHandler;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * Playback user task execution. Task execution takes exactly the same time 
 * as task execution from played back process
 *
 */
public class PlaybackUserTaskExecutor implements UserTaskExecutor {

	/** History service from which user task behavior is played back (copied) */ 
	private HistoryService playbackHistoryService;
	
	/** task service into which variables updates are copied */ 
	private TaskService taskService;

	/**
	 *  runtime service from which playBack process instance ID is received
	 * @TODO: consider obtaining process instance ID from task variables 
	 */ 
	private RuntimeService runtimeService;
	
	/** executor used in the case when there is no user task history which can be used*/
	private UserTaskExecutor backUpExecutor = null;
	
	/** default constructor */
	public PlaybackUserTaskExecutor() {		
	}

	public PlaybackUserTaskExecutor(HistoryService historyService, TaskService taskService, RuntimeService runtimeService) {
		this.setPlaybackHistoryService(historyService);
		this.setTaskService(taskService);	
		this.setRuntimeService(runtimeService);
	}
	
	public PlaybackUserTaskExecutor(HistoryService historyService, TaskService taskService, RuntimeService runtimeService, UserTaskExecutor userTaskExecutor) {
		this.setPlaybackHistoryService(historyService);
		this.setTaskService(taskService);	
		this.setRuntimeService(runtimeService);
		
		this.setBackUpExecutor(userTaskExecutor);
	}

	@Override
	public long simulateTaskExecution(TaskEntity execTask, Map<String, Object> variables) {
		
		String playbackProcessInstanceId = (String) runtimeService.getVariable(execTask.getExecutionId(), PlaybackStartProcessEventHandler.PROCESS_INSTANCE_ID);
		
		// activity instance has to by only one
		HistoricActivityInstance activityInstance = playbackHistoryService.createHistoricActivityInstanceQuery()
				.processInstanceId(playbackProcessInstanceId)
				.activityId(execTask.getTaskDefinitionKey())
				.singleResult();
		if ( activityInstance == null )
			//use backupExecutor
			return backUpExecutor.simulateTaskExecution(execTask, variables);

		List<HistoricDetail> playbackDetails = playbackHistoryService.createHistoricDetailQuery()
				.activityInstanceId(activityInstance.getId())
				.processInstanceId(playbackProcessInstanceId)
				.variableUpdates()
				.list();
		
		// fulfill variables
		for ( HistoricDetail detail : playbackDetails) {
			variables.put( ((HistoricVariableUpdate) detail).getVariableName(), ((HistoricVariableUpdate) detail).getValue());			
		}
		
		//@TODO change in version 5.12
		return activityInstance.getDurationInMillis();
	}

	public HistoryService getPlaybackHistoryService() {
		return playbackHistoryService;
	}

	public void setPlaybackHistoryService(HistoryService historyService) {
		this.playbackHistoryService = historyService;
	}

	public UserTaskExecutor getBackUpExecutor() {
		return backUpExecutor;
	}

	public void setBackUpExecutor(UserTaskExecutor backUpExecutor) {
		this.backUpExecutor = backUpExecutor;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

}
