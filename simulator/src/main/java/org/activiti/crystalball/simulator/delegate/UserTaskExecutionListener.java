package org.activiti.crystalball.simulator.delegate;

import org.activiti.crystalball.simulator.EventCalendar;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * in the case of task event create simulation event in the evant calendar
 */
public class UserTaskExecutionListener implements TaskListener {

	private static Logger log = LoggerFactory.getLogger(UserTaskExecutionListener.class);
	
	protected EventCalendar eventCalendar;
	protected String type;
	
	public UserTaskExecutionListener() {
		
	}

	public UserTaskExecutionListener(String type, EventCalendar eventCalendar) {
		this.eventCalendar = eventCalendar;
		this.type = type;
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		SimulationEvent e = new SimulationEvent(ClockUtil.getCurrentTime().getTime(), type, delegateTask);
		log.debug("Sim time [{}] adding sim event [{}] to calendar ", ClockUtil.getCurrentTime(), e);
		eventCalendar.addEvent(e);
	}

	public EventCalendar getEventCalendar() {
		return eventCalendar;
	}

	public void setEventCalendar(EventCalendar eventCalendar) {
		this.eventCalendar = eventCalendar;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
