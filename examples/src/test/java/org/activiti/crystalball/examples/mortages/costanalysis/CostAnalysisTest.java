package org.activiti.crystalball.examples.mortages.costanalysis;

import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * present simulation engine monitoring capabilities 
 *
 */
public class CostAnalysisTest {
	
	/**
	 * How is the probability that timer will expire? and escalation  will occure?
	 * @throws Exception 
	 */
	@Test
	public void costAnalysisTest() throws Exception {
		
		SimulationEngine simulationEngine= SimulationEngineConfigurationImpl.createSimulationEngineConfigurationFromResource("/org/activiti/crystalball/examples/costsanalysis/crystalball.cfg.xml").buildSimulationEngine();
		Calendar c = Calendar.getInstance();
		Date start = c.getTime();
		c.add(Calendar.HOUR, 3);
		Date end = c.getTime();
		
		SimulationInstance simulationInstance = simulationEngine.getRuntimeService()
												.startSimulationInstanceByKey("CostAnalysis", "simple simulation to analyse costs", (String) null, start, end, 1, 1L, 
													"/org/activiti/crystalball/examples/costsanalysis/mortages-costsanalysis-h2-context.xml");
		// wait to finish simulation asynchronously
		do {
			// wait for simulation end.
			Thread.sleep(500);
		} while( simulationEngine.getRuntimeService().isRunning(simulationInstance.getId()) );
		
		ResultQuery resultQuery = simulationEngine.getRuntimeService().createResultQuery().simulationInstanceId( simulationInstance.getId() );
		List<ResultEntity> resultList = resultQuery.list();
		
		assertEquals(30, resultList.size());		
	}
	
}
