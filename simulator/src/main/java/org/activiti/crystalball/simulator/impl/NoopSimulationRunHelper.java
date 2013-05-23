package org.activiti.crystalball.simulator.impl;

import java.io.IOException;

import org.activiti.crystalball.simulator.SimulationRunHelper;

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
