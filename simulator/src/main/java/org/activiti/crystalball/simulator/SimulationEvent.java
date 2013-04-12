package org.activiti.crystalball.simulator;

/*
 * #%L
 * simulator
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
