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


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TimerBoundaryEventTest {
	protected static final String tempDir = System.getProperty("tempDir", "target");
	// live db is not needed
	protected static final String LIVE_DB = tempDir +"/" + TimerBoundaryEventTest.class.getName() + "-live";
	protected static final String PROCESS_KEY = "boundarytimereventtest";

	@Test
	public void testTimerBoundaryEvent() throws Exception {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/"+TimerBoundaryEventTest.class.getName() + "-sim-"+Thread.currentThread().getId());
	    // delete database file
		File f = new File( System.getProperty("_SIM_DB_PATH") +".h2.db");
		if ( !f.delete() )
			System.err.println("unable to delete file");		

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimEngine-BoundaryTimer-h2-context.xml");
        ProcessEngine processEngine = (ProcessEngine) appContext.getBean("simProcessEngine");
        
		// deploy processes
		processEngine.getRepositoryService().createDeployment()
	       .addClasspathResource("org/activiti/crystalball/simulator/TimerBoundaryEventTest.bpmn")
	       .deploy();
		
	    SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    
	    Calendar c = Calendar.getInstance();
	    Date startDate = c.getTime();

	    c.add( Calendar.MINUTE, 5);
	    Date finishDate = c.getTime();
	    
	    // run simulation for 5 minutes
	    @SuppressWarnings("unused")
		List<ResultEntity> resultEventList = simRun.execute(startDate, finishDate);
	    
	    TaskService simTaskService = processEngine.getTaskService();
	    // in 5 minutes 11 processes will be started (0 is included too)
	    assertEquals( 11, simTaskService.createTaskQuery().taskDefinitionKey("firstLine").count());
	    // two tasks were not escalated yet escalation timer is 35sec
	    assertEquals( 9, simTaskService.createTaskQuery().taskDefinitionKey("escalation").count());
	    
        processEngine.close();
        appContext.close();        

	}
}
