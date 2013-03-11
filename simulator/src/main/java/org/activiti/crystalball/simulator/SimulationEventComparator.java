package org.activiti.crystalball.simulator;

import java.util.Comparator;

public class SimulationEventComparator implements Comparator<SimulationEvent> {

	@Override
	public int compare(SimulationEvent o1, SimulationEvent o2) {
		if ( o1.getSimulationTime() < o2.getSimulationTime())  
			return -1;
		if ( o1.getSimulationTime() > o2.getSimulationTime()) 
			return 1;
		return 0;
	}

}
