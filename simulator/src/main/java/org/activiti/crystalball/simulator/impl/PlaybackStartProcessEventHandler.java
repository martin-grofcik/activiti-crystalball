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
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * event handler acts as playback - starts defined processes in the given simulation time. 
 *
 * currently supports only  processes which starts with "startEvent" activity type  
 */
public class PlaybackStartProcessEventHandler implements SimulationEventHandler {

	public static final String PROCESS_INSTANCE_ID = "_playback.processInstanceId";
	public static final String HISTORY_SERVICE = "_playback.historyService";

	private static Logger log = LoggerFactory.getLogger(PlaybackStartProcessEventHandler.class);
	
	/** process to start key - this process will be play backed.*/
	private String processToStartKey;

	/** event type on which Handler is listening to start new process */
	private String eventType;
	
	/** history from which process starts will be played */
	private HistoryService historyService;

	@Override
	public void init() {
	}

	@Override
	public void handle(SimulationEvent event) {
		// start process now
		String processInstanceId = (String) event.getProperty(PROCESS_INSTANCE_ID);
		// get process variables for startEvent
		HistoricActivityInstance activityInstance = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.activityType("startEvent")
				.singleResult();
		List<HistoricDetail> details = historyService.createHistoricDetailQuery()
				.processInstanceId( processInstanceId )
				.activityInstanceId( activityInstance.getId())
				.variableUpdates()
				.list();
		
		// fulfill variables
		Map<String, Object> variables = new HashMap<String,Object>();		
		for ( HistoricDetail detail : details) {
			variables.put( ((HistoricVariableUpdate) detail).getVariableName(), ((HistoricVariableUpdate) detail).getValue());			
		}
		
		variables.put( PROCESS_INSTANCE_ID, processInstanceId);
		log.debug("[{}] Starting new processKey[{}] properties[{}]", ClockUtil.getCurrentTime(), processToStartKey, variables);

		SimulationRunContext.getRuntimeService().startProcessInstanceByKey( processToStartKey, variables);
	}

	public String getProcessToStartKey() {
		return processToStartKey;
	}

	public void setProcessToStartKey(String processToStartKey) {
		this.processToStartKey = processToStartKey;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String event_type) {
		this.eventType = event_type;
	}


	public HistoryService getHistoryService() {
		return historyService;
	}


	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

}
