package org.activiti.crystalball.simulator;

import java.util.Map;

public class SimulationEvent {
	
	public static final String TYPE_COMPLETE = "COMPLETE";
	
	private final long simulationTime;
	private final String type;
	private final Map<String, Object> properties;
	
	public SimulationEvent( long simulationTime, String type, Map<String, Object> properties) {
		this.simulationTime = simulationTime;
		this.type = type;
		this.properties = properties;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public long getSimulationTime() {
		return simulationTime;
	}

	public String getType() {
		return type;
	}
	
}
