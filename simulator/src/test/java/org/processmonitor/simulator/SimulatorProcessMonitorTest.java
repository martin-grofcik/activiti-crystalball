package org.processmonitor.simulator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// uses exactly the same configuration as previous monitor
@ContextConfiguration("classpath*:/org/processmonitor/simulator/SimpleProcessDiagramGeneratorTest-context.xml")
public class SimulatorProcessMonitorTest {

	private static String MONITOR_PROCESS_KEY = "processmonitor";

	@Resource(name="generatorProcessEngine")
	private ProcessEngine processEngine;
		
	@Resource(name="generatorRepositoryService")
	private RepositoryService repositoryService;

	@Resource(name="generatorRuntimeService")
	private RuntimeService runtimeService;

	@Before
	public void before() {
		// deploy processes needed for generating reports
	  repositoryService.createDeployment()
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGenerator.bpmn")
	    .addClasspathResource("org/processmonitor/generator/SimResultProcessDiagramGenerator.bpmn")
	    .addClasspathResource("org/processmonitor/generator/ProcessMonitor.bpmn")
	    .deploy();	  
	}

	@After
  public void after() {
    for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
      repositoryService.deleteDeployment(deployment.getId(), true);
    }
    processEngine.close();
  }

	@Test
	public void testProcessRun() throws Throwable {
		// prepare params
		Map<String, Object> params = new Hashtable<String, Object>();
		
		params.put( "tempDir", System.getProperty("tempDir", "target") );
	    params.put( "runningDatabaseFile", System.getProperty("tempDir", "target") + "/BasicSimulation.h2.db");
	    
	    // start process
		runtimeService.startProcessInstanceByKey(
				MONITOR_PROCESS_KEY, "GENERATOR-KEY-1", params).getId();
	    //check outputs
		File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/SimulatorProcessMonitor-unfinishedTasks-expected.png" );   
		File generated = new File(System.getProperty("tempDir", "target") + "/unfinished_task/threetasksprocess.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	   
	}

}
