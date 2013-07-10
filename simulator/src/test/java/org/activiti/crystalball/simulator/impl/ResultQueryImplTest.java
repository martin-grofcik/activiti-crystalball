package org.activiti.crystalball.simulator.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.engine.impl.test.PvmTestCase;

public class ResultQueryImplTest extends PvmTestCase {

	public void testQueryVariables() {
		SimulationEngine simulationEngine= SimulationEngineConfigurationImpl.createStandaloneInMemSimulationEngineConfiguration().buildSimulationEngine();
		Map<String, Object> resultVariables = new HashMap<String, Object>();
		resultVariables.put("processDefinitionKey", "processDefinitionId");
		resultVariables.put("taskDefinitionKey", "activityId");
		resultVariables.put( "description", "count");
		simulationEngine.getRuntimeService().saveResult("some-type", resultVariables);		
	}
}
