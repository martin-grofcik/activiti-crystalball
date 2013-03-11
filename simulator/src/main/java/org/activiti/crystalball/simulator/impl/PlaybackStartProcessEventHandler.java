package org.activiti.crystalball.simulator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void init(SimulationContext context) {
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
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

		context.getRuntimeService().startProcessInstanceByKey( processToStartKey, variables);
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
