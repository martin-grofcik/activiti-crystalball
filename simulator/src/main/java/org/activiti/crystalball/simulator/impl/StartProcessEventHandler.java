package org.activiti.crystalball.simulator.impl;

import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start new process event handler 
 *
 */
public class StartProcessEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(StartProcessEventHandler.class);
	
	/** process to start key */
	private String processToStartKey;
	/** period in which new process will start in ms*/
	private long period;
	/** event type on which Handler is listening to start new process */
	private String eventType;
	
	/** how many times start the process - -1 for ever*/
	protected int count = -1;
	
	@Override
	public void init(SimulationContext context) {
		// schedule new process instance start now
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		scheduleNextProcessStart(context, simulationTime);
	}

	private void scheduleNextProcessStart(SimulationContext context,
			long simulationTime) {
		if (count != 0 ) {
			if ( count > 0 ) count--;
			SimulationEvent completeEvent = new SimulationEvent( simulationTime, eventType, null);
			// add start process event
			context.getEventCalendar().addEvent( completeEvent);
		}
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		// start process now
		log.debug("Starting new processKey[{}]", processToStartKey);
		context.getRuntimeService().startProcessInstanceByKey( processToStartKey);
		// schedule next process start in + period time
		scheduleNextProcessStart( context, event.getSimulationTime() + period);
	}

	public String getProcessToStartKey() {
		return processToStartKey;
	}

	public void setProcessToStartKey(String processToStartKey) {
		this.processToStartKey = processToStartKey;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String event_type) {
		this.eventType = event_type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
