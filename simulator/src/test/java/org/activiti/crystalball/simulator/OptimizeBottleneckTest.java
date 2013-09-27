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


import org.activiti.crystalball.generator.AbstractProcessEngineGraphGenerator;
import org.activiti.crystalball.simulator.impl.StartProcessEventHandler;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class OptimizeBottleneckTest {
	protected static final String tempDir = System.getProperty("tempDir", "target");
	protected static final String LIVE_DB = tempDir +"/live-SimulateBottleneckTest";
	protected static final String PROCESS_KEY = "parallelusertaskprocess";

	@Test
	public void testUser2OverLoadedWithHelp3() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload3-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload3-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add User3 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.createMembership("user3", "Group2");
        
        runSimulation(appContext, tempDir + "/OptimizeBottleneckTest3.png", tempDir + "/OptimizeBottleneckTest3-dueDate.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest3.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest3.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          

        expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest3-dueDate.png" );   
		generated = new File(tempDir + "/OptimizeBottleneckTest3-dueDate.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          

	    // delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");		
	}

	
	@Test
	public void testUser2OverLoadedWithHelp4() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload4-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload4-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add user4 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");

        identityService.createMembership("user4", "Group2");
        
        runSimulation(appContext,tempDir + "/OptimizeBottleneckTest4.png", tempDir + "/OptimizeBottleneckTest4-dueDate.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest4.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest4.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	
	    
        expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest4-dueDate.png" );   
		generated = new File(tempDir + "/OptimizeBottleneckTest4-dueDate.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          

		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}

	@Test
	public void testUser2OverLoadedWithHelp5() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload5-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload5-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add new user5 to the Group2 to help user2
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.saveUser( identityService.newUser("user5") );
        identityService.createMembership("user5", "Group2");
        
        runSimulation(appContext,tempDir + "/OptimizeBottleneckTest5.png", tempDir + "/OptimizeBottleneckTest5-dueDate.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest5.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest5.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));
	    
        expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest5-dueDate.png" );   
		generated = new File(tempDir + "/OptimizeBottleneckTest5-dueDate.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	          

		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}
	
	@Test
	public void testUser2OverLoadedWithHelp45() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload45-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload45-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        // add new user5 to the Group2 to help user2 and to Group4 to help user4
        IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
        identityService.saveUser( identityService.newUser("user5"));
        identityService.createMembership("user5", "Group2");
        identityService.createMembership("user5", "Group4");
        
        runSimulation(appContext, tempDir + "/OptimizeBottleneckTest45.png", tempDir + "/OptimizeBottleneckTest45-dueDate.png");

        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest45.png" );   
		File generated = new File(tempDir + "/OptimizeBottleneckTest45.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));
	    
        expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/activiti/crystalball/simulator/OptimizeBottleneckTest45-dueDate.png" );   
		generated = new File(tempDir + "/OptimizeBottleneckTest45-dueDate.png");   
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
	 * @throws Exception 
	 */
	protected void runSimulation(AbstractApplicationContext appContext, String instancesGeneratedImage, String dueDateGeneratedImage) throws Exception {
	
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
		List<ResultEntity> resultEventList = simRun.execute(startDate, finishDate);
	    	
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean("simRepositoryService");	    
	    String processDefinitionId = simRepositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_KEY).singleResult().getId();
	
	    // GENERATE REPORTS
	    
	    // instances report 
	    AbstractProcessEngineGraphGenerator generator = (AbstractProcessEngineGraphGenerator) appContext.getBean( "reportGenerator");
	    generator.generateReport(processDefinitionId, startDate, finishDate, instancesGeneratedImage);
	    
	    // after due date report
	    generator = (AbstractProcessEngineGraphGenerator) appContext.getBean( "dueDateReportGenerator");
	    generator.generateReport(processDefinitionId, startDate, finishDate, dueDateGeneratedImage);
	}
}
