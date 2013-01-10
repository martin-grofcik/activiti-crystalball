package org.processmonitor.simulator;

public interface Simulator {

	/**
	 * init tasks
	 * @param calendar
	 */
	void init(EventCalendar calendar);

	/**
	 * simulate users's work
	 * @param calendar
	 */
	void simulate(EventCalendar calendar);

}
