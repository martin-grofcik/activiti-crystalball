package org.activiti.crystalball.simulator.delegate;

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
