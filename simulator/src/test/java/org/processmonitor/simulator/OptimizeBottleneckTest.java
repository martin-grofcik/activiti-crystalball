package org.processmonitor.simulator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.processmonitor.generator.ProcessInstancesGenerator;
import org.processmonitor.simulator.impl.StartProcessEventHandler;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OptimizeBottleneckTest {
	protected static final String tempDir = System.getProperty("tempDir", "target");
	protected static final String LIVE_DB = tempDir +"/live-SimulateBottleneckTest";
	protected static final String PROCESS_KEY = "parallelusertaskprocess";

	@Test
	public void testUser2OverLoadedWithHelp3() throws IOException {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload3-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload3-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add User3 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.createMembership("user3", "Group2");
        
        runSimulation(appContext, tempDir + "/OptimizeBottleneckTest3.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/OptimizeBottleneckTest3.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest3.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");		
	}

	
	@Test
	public void testUser2OverLoadedWithHelp4() throws IOException {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload4-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload4-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add user4 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");

        identityService.createMembership("user4", "Group2");
        
        runSimulation(appContext,tempDir + "/OptimizeBottleneckTest4.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/OptimizeBottleneckTest4.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest4.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}

	@Test
	public void testUser2OverLoadedWithHelp5() throws IOException {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload5-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload5-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add new user5 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.saveUser( identityService.newUser("user5") );
        identityService.createMembership("user5", "Group2");
        
        runSimulation(appContext,tempDir + "/OptimizeBottleneckTest5.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/OptimizeBottleneckTest5.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest5.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}
	
	@Test
	public void testUser2OverLoadedWithHelp45() throws IOException {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload45-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload45-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add new user5 to the Group2 to help user2 and to Group4 to help user4
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.saveUser( identityService.newUser("user5"));
        identityService.createMembership("user5", "Group2");
        identityService.createMembership("user5", "Group4");
        
        runSimulation(appContext, tempDir + "/OptimizeBottleneckTest45.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/OptimizeBottleneckTest45.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest45.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          
		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}

	/**
	 * run simulation for 30 days and generate report
	 * 
	 * @param appContext
	 * @throws IOException
	 */
	protected void runSimulation(AbstractApplicationContext appContext, String generatedImage) throws IOException {
	
	    SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    
	    Calendar c = Calendar.getInstance();
	    c.clear();
	    c.set(2013, 1, 1);
	    Date startDate = c.getTime();
	    // add 30 days to start day
	    c.add( Calendar.DAY_OF_YEAR, 30);
	    Date finishDate = c.getTime();
	    // run simulation for 30 days
	    @SuppressWarnings("unused")
		List<SimulationResultEvent> resultEventList = simRun.execute(startDate, finishDate);
	    
	    ProcessInstancesGenerator generator = (ProcessInstancesGenerator) appContext.getBean( "reportGenerator");
	
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean("simRepositoryService");
	    
	    String processDefinitionId = simRepositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_KEY).singleResult().getId();
	
	    generator.generateReport(processDefinitionId, generatedImage);
	    
	}
}
