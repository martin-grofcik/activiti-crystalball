package org.activiti.crystalball.simulator;

import org.activiti.engine.runtime.ClockReader;

import java.util.Comparator;

public class SimpleEventCalendarFactory implements FactoryBean<EventCalendar> {

	protected Comparator<SimulationEvent> eventComparator;
	protected final ClockReader clockReader;

	public SimpleEventCalendarFactory(ClockReader clockReader, Comparator<SimulationEvent> eventComparator) {
		this.eventComparator = eventComparator;
    this.clockReader = clockReader;
  }
	
	@Override
	public SimpleEventCalendar getObject() {
		return new SimpleEventCalendar(clockReader, eventComparator);
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
