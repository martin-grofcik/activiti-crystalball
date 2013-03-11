package org.activiti.crystalball.simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventCalendar {
	
	List<SimulationEvent> eventList = new ArrayList<SimulationEvent>();
	Integer minIndex = null;
	Comparator<SimulationEvent> eventComparator;
	
	public EventCalendar( Comparator<SimulationEvent> eventComparator) {
		this.eventComparator = eventComparator;		
	}
	
	public boolean isEmpty() {
		return minIndex == null;
	}
	
	public SimulationEvent getFirstEvent() {
		if (minIndex == null)
			return null;
		
		SimulationEvent minEvent = eventList.remove( (int) minIndex );
		if (eventList.isEmpty()) { 
			minIndex = null;
		} else {
			minIndex = 0;
			SimulationEvent event = eventList.get(0);
			for ( int i = 1; i < eventList.size(); i++ ) {
				if (eventComparator.compare( eventList.get( i ), event ) < 0) {
					minIndex = i;
					event = eventList.get( i );
				}
			}
		}
		return minEvent;
	}
	
	public void addEvent(SimulationEvent event) {
		if (event != null && isMinimal(event))
			minIndex = eventList.size();
		eventList.add(event);			
	}

	/**
	 * is event the first event in the calendar?
	 * @param event
	 * @return
	 */
	private boolean isMinimal(SimulationEvent event) {
		if (minIndex == null)
			return true;
		if ( eventComparator.compare( event, eventList.get( minIndex )) < 0)
			return true;
		return false;
	}

}
