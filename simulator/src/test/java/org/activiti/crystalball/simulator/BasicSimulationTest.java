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


import org.activiti.crystalball.generator.SimulationResultsGraphGenerator;
import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.activiti.engine.impl.test.PvmTestCase;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BasicSimulationTest extends PvmTestCase{

	@Test
	public void testBasicSimulationRun() throws Exception {
		System.setProperty("liveDB", "target/basicSimulation");
		
		SimulationEngine simulationEngine= SimulationEngineConfigurationImpl.createStandaloneSimulationEngineConfiguration().buildSimulationEngine();
		SimulationInstance simulationInstance = simulationEngine.getRuntimeService().startSimulationInstanceByKey("org.activiti.crystalball.processengine.wrapper.test - simulationRun", (String) null, (String) null, new Date(), (Date) null, 1, 1L, "/org/activiti/crystalball/simulator/SimRun-h2-context.xml");
		
		// wait to finish simulation asynchronously
		do {
			// wait for simulation end.
			Thread.sleep(500);
		} while( simulationEngine.getRuntimeService().isRunning(simulationInstance.getId()) );

		ResultQuery resultQuery = simulationEngine.getRuntimeService().createResultQuery().simulationInstanceId( simulationInstance.getId())
								.resultVariableValueEquals("description", "10");
		List<ResultEntity> resultList = resultQuery.list();
		
		assertEquals(1, resultList.size());
		
		Result r = resultList.get(0);
		Map<String, Object> variables = simulationEngine.getRuntimeService().getResultVariables(r.getId());
		assertEquals("10", variables.get("description"));
		assertEquals("threetasksprocess", variables.get("processDefinitionKey"));
		assertEquals("usertask3", variables.get("taskDefinitionKey"));		
		SimulationResultsGraphGenerator generator = new SimulationResultsGraphGenerator();
		generator.generateGraph(simulationEngine.getRuntimeService(), simulationInstance, "threetasksprocess", "unfinished_task", System.getProperty("tempDir", "target") + "basicTest.jpg");
	}
}
