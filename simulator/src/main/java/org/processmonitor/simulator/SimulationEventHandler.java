package org.processmonitor.simulator;

public interface SimulationEventHandler {

	/**
	 * execute event in the handler
	 * @param event
	 * @param eventCalendar
	 */
	void handle(SimulationEvent event, EventCalendar eventCalendar);

}
