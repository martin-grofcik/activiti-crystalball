package org.activiti.crystalball.examples.mortages.monitoring;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.crystalball.generator.ProbabilityGraphGenerator;
import org.activiti.crystalball.simulator.SimulationEngine;
import org.activiti.crystalball.simulator.impl.cfg.SimulationEngineConfigurationImpl;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.result.ResultQuery;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.junit.Ignore;
import org.junit.Test;

/**
 * present simulation engine monitoring capabilities 
 *
 */
public class MonitoringTest {
	
	/**
	 * How is the probability that timer will expire? and escalation  will occure?
	 * @throws Exception 
	 */
	@Test
	public void escalationProbabilityTest() throws Exception {
		
		SimulationEngine simulationEngine= SimulationEngineConfigurationImpl.createStandaloneSimulationEngineConfiguration().buildSimulationEngine();
		Calendar c = Calendar.getInstance();
		Date start = c.getTime();
		c.add(Calendar.HOUR, 3);
		Date end = c.getTime();
		
		SimulationInstance simulationInstance = simulationEngine.getRuntimeService()
												.startSimulationInstanceByKey("Monitor escalation", "simple simulation to check whether escalation will ocure", (String) null, start, end, 11, 1L, 
													"/org/activiti/crystalball/examples/monitoring/mortages-monitoring-h2-context.xml");
		// wait to finish simulation asynchronously
		do {
			// wait for simulation end.
			Thread.sleep(500);
		} while( simulationEngine.getRuntimeService().isRunning(simulationInstance.getId()) );
		
		ResultQuery resultQuery = simulationEngine.getRuntimeService().createResultQuery().simulationInstanceId( simulationInstance.getId() );
		List<ResultEntity> resultList = resultQuery.list();
		
		assertEquals(7, resultList.size());

		ProbabilityGraphGenerator generator = new ProbabilityGraphGenerator();
		generator.generateGraph(simulationEngine.getRuntimeService(), simulationInstance, "mortagedemo-0:1:4", "mortagedemo-0", "activity_exists", System.getProperty("tempDir", "target") + "/monitoringTest.jpg");
	}
	
	/**
	 * Simulation experiment is almost the same as previous one. But ....
	 * Escalation is implemented as UserTask. In case when escalation is not terminated before process end state is reached. 
	 * Integration constraint violation exception is thrown.
	 * see http://forums.activiti.org/content/when-opened-timer-simultaneously-process-does-not-know-he-ends-until-time-timer-comes-end
	 * 
	 * One more good point to use simulation for testing.
	 * 
	 * @throws InterruptedException 
	 */
	@Test @Ignore()
	public void escalationFailureTest() throws InterruptedException {
		
		SimulationEngine simulationEngine= SimulationEngineConfigurationImpl.createStandaloneSimulationEngineConfiguration().buildSimulationEngine();
		Calendar c = Calendar.getInstance();
		Date start = c.getTime();
		c.add(Calendar.HOUR, 3);
		Date end = c.getTime();
		
		SimulationInstance simulationInstance = simulationEngine.getRuntimeService()
												.startSimulationInstanceByKey("Monitor escalation", "simple simulation to check whether escalation will ocure", (String) null, start, end, 10, 7L, 
													"/org/activiti/crystalball/examples/monitoring/mortages-monitoring-failure-h2-context.xml");
		// wait to finish simulation asynchronously
		do {
			// wait for simulation end.
			Thread.sleep(500);
		} while( simulationEngine.getRuntimeService().isRunning(simulationInstance.getId()) );
		
		ResultQuery resultQuery = simulationEngine.getRuntimeService().createResultQuery().simulationInstanceId( simulationInstance.getId() );
		List<ResultEntity> resultList = resultQuery.list();
		
		assertEquals(10, resultList.size());	
	}
}
