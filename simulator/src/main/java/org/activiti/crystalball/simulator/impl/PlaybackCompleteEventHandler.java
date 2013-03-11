package org.activiti.crystalball.simulator.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * complete user task event handler copies result variables from played back process
 * into process instance which simulates process instance run 
 *
 */
public class PlaybackCompleteEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(PlaybackCompleteEventHandler.class);
	
	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		String taskId = (String) event.getProperty("task");
		
		// fulfill variables
		@SuppressWarnings("unchecked")
		Map<String, Object> variables = (Map<String, Object>) event.getProperty("variables");		
		
		context.getTaskService().complete( taskId, variables );
		log.debug( SimpleDateFormat.getTimeInstance().format( new Date(event.getSimulationTime())) +": completed taskId "+ taskId + " variable update [" + variables +"]");
	}

	@Override
	public void init(SimulationContext context) {
		
	}
}
