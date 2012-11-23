package org.processmonitor.simulator;

import java.util.Comparator;

public class SimulationEventComparator implements Comparator<SimulationEvent> {

	@Override
	public int compare(SimulationEvent o1, SimulationEvent o2) {
		if ( o1.getSimulationTime() == null || o2.getSimulationTime() == null)
			return 0;
		if ( o1.getSimulationTime().before( o2.getSimulationTime()) ) 
			return -1;
		if ( o1.getSimulationTime().after( o2.getSimulationTime()) ) 
			return 1;
		return 0;
	}

}
