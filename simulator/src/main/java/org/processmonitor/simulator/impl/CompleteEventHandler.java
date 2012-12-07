package org.processmonitor.simulator.impl;

import org.processmonitor.simulator.SimulationContext;
import org.processmonitor.simulator.SimulationEvent;
import org.processmonitor.simulator.SimulationEventHandler;

public class CompleteEventHandler implements SimulationEventHandler {

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		String taskId = (String) event.getProperty("task");
		context.getTaskService().complete( taskId );
	}

}
