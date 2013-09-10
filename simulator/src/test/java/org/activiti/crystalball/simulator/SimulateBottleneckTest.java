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

public class SimulateBottleneckTest {
	protected static final String tempDir = System.getProperty("tempDir", "target");
	protected static final String LIVE_DB = tempDir +"/live-SimulateBottleneckTest";
	protected static final String PROCESS_KEY = "parallelusertaskprocess";

	@Test
	public void testOK() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-OK-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-OK-"+Thread.currentThread().getId()+".h2.db"));

        
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        runSimulation(appContext, tempDir + "/SimulateBottleneckTest-1.png");
        
        processEngine.close();        
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/simulator/simulateBottleneckTest-1.png" );
		File generated = new File(tempDir + "/SimulateBottleneckTest-1.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));	
		// delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");
	}

	/**
	 * results differs - that's why this org.activiti.crystalball.processengine.wrapper.test is ignored - possible bug.
	 * @throws Exception 
	 */
	@Test
	public void testUser2OverLoaded() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulateBottleNeck-Overload-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulateBottleNeck-Overload-"+Thread.currentThread().getId()+".h2.db"));

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-simulateBottleneck-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
        // increase frequency of process starts to 45 mins (default is 1 hour)
        StartProcessEventHandler startProcessEventHandler = appContext.getBean(StartProcessEventHandler.class);
        startProcessEventHandler.setPeriod(2700000);
        
        runSimulation(appContext, tempDir + "/SimulateBottleneckTest-2.png");
        
        processEngine.close();
        appContext.close();        

        File expected = new File(System.getProperty("baseDir", ".") + "/src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/simulator/simulateBottleneckTest-2.png" );
		File generated = new File(tempDir + "/SimulateBottleneckTest-2.png");   
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
	protected void runSimulation(AbstractApplicationContext appContext, String generatedImage) throws Exception {
	
	    SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    
	    Calendar c = Calendar.getInstance();
	    c.clear();
	    c.set(2013, 1, 1);
	    Date startDate = c.getTime();
	    c.add( Calendar.DAY_OF_YEAR, 30);
	    Date finishDate = c.getTime();
	    // run simulation for 30 days
	    @SuppressWarnings("unused")
		List<ResultEntity> resultEventList = simRun.execute(startDate, finishDate);
	    
	    AbstractProcessEngineGraphGenerator generator = (AbstractProcessEngineGraphGenerator) appContext.getBean( "reportGenerator");
	
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean("simRepositoryService");
	    
	    String processDefinitionId = simRepositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_KEY).singleResult().getId();
	
	    generator.generateReport(processDefinitionId, startDate, finishDate, generatedImage);
	    
	}

}
