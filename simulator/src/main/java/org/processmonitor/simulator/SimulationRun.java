package org.processmonitor.simulator;

import java.util.ArrayList;
import java.util.List;

public class SimulationRun {
	
	private EventCalendar eventCalendar = new EventCalendar( new SimulationEventComparator());
	private List<SimulationResultEvent> results = new ArrayList<SimulationResultEvent>();
	
	public List<SimulationResultEvent> execute() {
		
		initEventCalendar();
		while( !eventCalendar.isEmpty() ) {
			execute(eventCalendar.getFirstEvent());
		}
		return results;
	}

	private void execute(SimulationEvent event) {
		for ( SimulationEventHandler handler : getEventHandler( event.getType() )) {
			handler.handle( event, eventCalendar);
		}
	}

	private List<SimulationEventHandler> getEventHandler(Object type) {
		// TODO Auto-generated method stub
		return null;
	}

	private void initEventCalendar() {
		
		
	}
}
