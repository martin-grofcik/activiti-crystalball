package org.processmonitor.simulator;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.processmonitor.generator.ProcessInstancesGenerator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunSimulationSuperClass {

	protected static final String tempDir = System.getProperty("tempDir", "target");
	protected static final String LIVE_DB = tempDir +"/live-SimulateBottleneckTest";
	protected static final String GENERATED_IMAGE = tempDir +"/simulateBottleneckTest-1.png";
	private static final String PROCESS_KEY = "parallelusertaskprocess";

	public RunSimulationSuperClass() {
		super();
	}

	
	@Before
	public void before() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-"+Thread.currentThread().getId()+".h2.db"));
	}

	@After
	public void after() {
		// delete database file
		File f = new File(tempDir+"/simDB.h2.db");
		f.delete();
    }

	/**
	 * run simulation for 30 days and generate report
	 * 
	 * @param appContext
	 * @throws IOException
	 */
	protected void runSimulation(ClassPathXmlApplicationContext appContext) throws IOException {
	
	    SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    
	    Date startDate = new Date();
	    // add 30 days to start day
	    Calendar c = Calendar.getInstance();
	    c.setTime( startDate);
	    c.add( Calendar.DAY_OF_YEAR, 30);
	    Date finishDate = c.getTime();
	    // run simulation for 30 days
	    @SuppressWarnings("unused")
		List<SimulationResultEvent> resultEventList = simRun.execute(startDate, finishDate);
	    
	    ProcessInstancesGenerator generator = (ProcessInstancesGenerator) appContext.getBean( "reportGenerator");
	
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean("simRepositoryService");
	    
	    String processDefinitionId = simRepositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_KEY).singleResult().getId();
	
	    generator.generateReport(processDefinitionId, GENERATED_IMAGE);
	    
	}

}