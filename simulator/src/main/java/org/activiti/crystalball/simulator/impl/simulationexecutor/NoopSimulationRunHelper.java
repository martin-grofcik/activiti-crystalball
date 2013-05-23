package org.activiti.crystalball.simulator.impl.simulationexecutor;

import org.activiti.crystalball.simulator.SimulationRunHelper;

/**
 * No operation run helper 
 *
 */
public class NoopSimulationRunHelper implements SimulationRunHelper {

	@Override
	public void before(String configuration) throws Exception {
	}

	@Override
	public void after(String configuration) {
	}

}
