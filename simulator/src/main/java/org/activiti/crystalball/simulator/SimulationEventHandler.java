package org.activiti.crystalball.simulator;

public interface SimulationEventHandler {

	/**
	 * initialize event handler
	 * @param context
	 */
	void init(SimulationContext context);
	
	/**
	 * execute event in the context
	 * @param event
	 * @param context
	 */
	void handle(SimulationEvent event, SimulationContext context);

}
