package org.activiti.crystalball.simulator;

import java.util.Comparator;

import org.springframework.beans.factory.FactoryBean;

public class SimpleEventCalendarFactory implements FactoryBean<EventCalendar> {

	protected Comparator<SimulationEvent> eventComparator;
	
	public SimpleEventCalendarFactory( Comparator<SimulationEvent> eventComparator) {
		this.eventComparator = eventComparator;  
	}
	
	@Override
	public EventCalendar getObject() throws Exception {
		return new EventCalendar(eventComparator);
	}

	@Override
	public Class<?> getObjectType() {
		return EventCalendar.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
