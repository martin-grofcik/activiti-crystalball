package org.processmonitor.simulator;

public interface SimulationEventHandler {

	/**
	 * execute event in the context
	 * @param event
	 * @param context
	 */
	void handle(SimulationEvent event, SimulationContext context);

}
