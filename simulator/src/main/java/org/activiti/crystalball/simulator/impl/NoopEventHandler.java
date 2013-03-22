package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;

/**
 * No operation event handler 
 *
 */
public class NoopEventHandler implements SimulationEventHandler {

	@Override
	public void init(SimulationContext context) {

	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {

	}

}
