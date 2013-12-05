package org.activiti.crystalball.simulator;

import java.util.Collection;
import java.util.Comparator;

public class PlaybackEventCalendarFactory implements FactoryBean<EventCalendar> {

  protected Collection<SimulationEvent> simulationEvents;
  protected Comparator<SimulationEvent> eventComparator;

	public PlaybackEventCalendarFactory(Comparator<SimulationEvent> eventComparator, Collection<SimulationEvent> simulationEvents) {
		this.eventComparator = eventComparator;
    this.simulationEvents = simulationEvents;
	}
	
	@Override
	public SimpleEventCalendar getObject() {
    SimpleEventCalendar simpleEventCalendar = new SimpleEventCalendar(eventComparator);
    simpleEventCalendar.addEvents( simulationEvents);
    return simpleEventCalendar;
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
