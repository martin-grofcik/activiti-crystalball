package org.activiti.crystalball.simulator;

import java.util.Date;
import java.util.Map;

public class SimulationEvent {
	
	public static final String TYPE_TASK_COMPLETE = "COMPLETE";
	public static final String TYPE_TASK_CREATE = "TASK_CREATE";

	/** event type used to notify  AcquireJobsRunnable monitor to continue in execution */
	public static final String TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT = "ACQUIRE_JOB_NOTIFICATION_EVENT";
	
	public static final String TYPE_END_SIMULATION = "END_SIMULATION";
	
	private final long simulationTime;
	private final String type;
	private final Map<String, Object> properties;
	private final Object property;

	public SimulationEvent( long simulationTime, String type, Object property) {
		this.simulationTime = simulationTime;
		this.type = type;
		this.properties = null;
		this.property = property;
	}

	public SimulationEvent( long simulationTime, String type, Map<String, Object> properties) {
		this.simulationTime = simulationTime;
		this.type = type;
		this.properties = properties;
		property = null;
	}
	
	public Object getProperty() {
		return property;
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
	
	@Override
	public String toString() {
		return (new Date(simulationTime)).toString() +", "+ type +", " +property + ", " + properties; 
	}
}
