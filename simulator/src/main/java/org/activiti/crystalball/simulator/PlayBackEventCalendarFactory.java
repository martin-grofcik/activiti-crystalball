package org.activiti.crystalball.simulator;

import org.activiti.engine.runtime.ClockReader;

import java.util.Collection;
import java.util.Comparator;

public class PlaybackEventCalendarFactory implements FactoryBean<EventCalendar> {

  protected Collection<SimulationEvent> simulationEvents;
  protected Comparator<SimulationEvent> eventComparator;
  protected final ClockReader clockReader;

	public PlaybackEventCalendarFactory(ClockReader clockReader, Comparator<SimulationEvent> eventComparator, Collection<SimulationEvent> simulationEvents) {
    this.clockReader = clockReader;
    this.eventComparator = eventComparator;
    this.simulationEvents = simulationEvents;
	}
	
	@Override
	public SimpleEventCalendar getObject() {
    SimpleEventCalendar simpleEventCalendar = new SimpleEventCalendar(clockReader, eventComparator);
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
