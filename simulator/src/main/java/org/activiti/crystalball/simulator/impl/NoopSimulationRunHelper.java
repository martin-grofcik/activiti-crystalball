package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.SimulationRunHelper;

import java.io.IOException;

/**
 * No operation after or before simulation run
 * 
 */
public class NoopSimulationRunHelper implements SimulationRunHelper {

	public NoopSimulationRunHelper() {
	}

	@Override
	public void before(String configuration) throws IOException {
	}

	@Override
	public void after(String configuration) {
	}

}
