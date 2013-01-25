package org.processmonitor.simulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.activiti.engine.TaskService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicSimulationTest {

	/**
	 * initialize process engine on which simulation will run
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	@Before
	public void before() throws InterruptedException, IOException {
		// copy process engine database to the 2 new files - one for backup and one for simulation run.
		File runningDB = new File(System.getProperty("tempDir", "target") + "/basicSimulation.h2.db");
		File simDB = new File(System.getProperty("tempDir", "target") + "/simDB.h2.db");
		File backupDB = new File(System.getProperty("tempDir", "target") + "/backupDB.h2.db");

		FileUtils.copyFile( runningDB , simDB);
		FileUtils.copyFile( runningDB , backupDB);
	}

	@After
	public void after() {
		File db = new File(System.getProperty("tempDir", "target") + "/simDB.h2.db");
		File backupDB = new File(System.getProperty("tempDir", "target") + "/backupDB.h2.db");
		db.delete();
		backupDB.delete();
	}

	@Test
	public void testBasicSimulationRun() throws IOException {
		System.setProperty("_SIM_DB_PATH", System.getProperty("tempDir", "target") + "/simDB");
		System.setProperty("liveDB", "target/basicSimulation");

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-h2-context.xml");
		SimulationRun simRun = (SimulationRun) appContext.getBean("simulationRun");

		//
		// execute simulation run
		//
		List<SimulationResultEvent> resultEventList = simRun.execute(new Date(), null);
		TaskService simTaskService = (TaskService) appContext.getBean("simTaskService");
		
		TaskService runTaskService = (TaskService) appContext.getBean("taskService");
		// all tasks should be in on the third step in sim engine
		assertEquals( 0, simTaskService.createTaskQuery().taskDefinitionKey("usertask1").count());
		assertEquals( 0, simTaskService.createTaskQuery().taskDefinitionKey("usertask2").count());
		assertEquals( 10, simTaskService.createTaskQuery().taskDefinitionKey("usertask3").count());

		// all tasks should remain on the same positions in the runn engine
		assertEquals( 5, runTaskService.createTaskQuery().taskDefinitionKey("usertask1").count());
		assertEquals( 5, runTaskService.createTaskQuery().taskDefinitionKey("usertask2").count());
		assertEquals( 0, runTaskService.createTaskQuery().taskDefinitionKey("usertask3").count());
		
		assertTrue( resultEventList.contains( new SimulationResultEvent("unfinished_task", "threetasksprocess", "usertask3", "10")) );
		appContext.close();
	}
}
