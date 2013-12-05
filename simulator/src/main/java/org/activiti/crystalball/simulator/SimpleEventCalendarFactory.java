package org.activiti.crystalball.simulator;

import java.util.Comparator;

public class SimpleEventCalendarFactory implements FactoryBean<SimpleEventCalendar> {

	protected Comparator<SimulationEvent> eventComparator;
	
	public SimpleEventCalendarFactory( Comparator<SimulationEvent> eventComparator) {
		this.eventComparator = eventComparator;  
	}
	
	@Override
	public SimpleEventCalendar getObject() {
		return new SimpleEventCalendar(eventComparator);
	}

	@Override
	public Class<?> getObjectType() {
		return SimpleEventCalendar.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
