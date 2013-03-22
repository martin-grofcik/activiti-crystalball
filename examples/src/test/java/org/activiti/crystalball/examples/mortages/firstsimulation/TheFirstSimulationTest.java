package org.activiti.crystalball.examples.mortages.firstsimulation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Date;

import org.activiti.crystalball.simulator.SimulationRun;
import org.activiti.engine.ProcessEngine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * the first simulation 
 *
 */
public class TheFirstSimulationTest {
	
	private final String SIM_DB = this.getClass().getName();

	@Before
	public void before() {
		System.setProperty("_SIM_DB_PATH", System.getProperty("tempDir", "target") + "/" + SIM_DB);

		File db = new File(System.getProperty("tempDir", "target") + "/"+ SIM_DB +".h2.db");
		db.delete();
	}
	
	@Test
	public void testFirstRun() {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("org/activiti/crystalball/examples/mortages/firstsimulation/mortages-h2-context.xml");
		SimulationRun simRun = (SimulationRun) appContext.getBean("simulationRun");
		ProcessEngine simProcessEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
		
		simProcessEngine.getRepositoryService().createDeployment()
	       .addClasspathResource("org/activiti/crystalball/examples/mortages/MortageDemo-0.bpmn")
	       .deploy();
		//
		// execute simulation run, without end date
		//
		simRun.execute(new Date(), null);
		
		assertEquals(1, simProcessEngine.getHistoryService().createHistoricProcessInstanceQuery().count());
		
		simProcessEngine.close();
		appContext.close();

	}
}
