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
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.crystalball.simulator.SimulationResultEvent;
import org.activiti.crystalball.simulator.SimulationRun;
import org.activiti.crystalball.simulator.impl.PlaybackScheduleStartProcessEventHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PlaybackTest {

	@After @Before
	public void after() {
		File db = new File(System.getProperty("tempDir", "target") + "/Playback-test.h2.db");
		db.delete();
		db = new File(System.getProperty("tempDir", "target") + "/Playback-test2.h2.db");
		db.delete();
	}

	@Test
	public void testPlaybackRun() throws IOException {
		System.setProperty("_SIM_DB_PATH", System.getProperty("tempDir", "target") + "/Playback-test");
		System.setProperty("liveDB", "target/Playback");

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/PlaybackSimEngine-h2-context.xml");
		
		HistoryService simHistoryService = (HistoryService)appContext.getBean("simHistoryService");
		
		// init identity service
		IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
		identityService.saveGroup( identityService.newGroup("Group1") );
		identityService.saveUser( identityService.newUser("user1") );
		identityService.createMembership("user1", "Group1");
		
	    SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean( "simRepositoryService" );
		
		// deploy processes
		simRepositoryService.createDeployment()
	       .addClasspathResource("org/activiti/crystalball/simulator/Playback.bpmn")
	       .deploy();
		
	    Calendar c = Calendar.getInstance();
	    c.clear();
	    c.set(2013, 3, 3);
	    Date startDate = c.getTime();
	    c.add( Calendar.SECOND, 10);
	    Date finishDate = c.getTime();
	    // run simulation for 10 seconds
	    @SuppressWarnings("unused")
		List<SimulationResultEvent> resultEventList = simRun.execute(startDate, finishDate);
	
	    assertEquals( 3, simHistoryService.createHistoricProcessInstanceQuery().count());
	    List<HistoricProcessInstance> processInstances = simHistoryService.createHistoricProcessInstanceQuery()
	    		.orderByProcessInstanceStartTime().asc()
	    		.list();
	    HistoricProcessInstance processInstance = processInstances.get(0);
	    HistoricVariableInstance variableInstance = simHistoryService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());
		
	    processInstance = processInstances.get(1);
	    variableInstance = simHistoryService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(2, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(2);
	    variableInstance = simHistoryService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());

		appContext.close();
	}

	/**
	 * repeat playback 2 times + 2 seconds (2 new processes are started0
	 * @throws IOException
	 */
	@Test
	public void testPlayback2Run() throws IOException {
		System.setProperty("_SIM_DB_PATH", System.getProperty("tempDir", "target") + "/Playback-test2");
		System.setProperty("liveDB", "target/Playback");

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/PlaybackSimEngine-h2-context.xml");
		
		// deploy processes
	    RepositoryService simRepositoryService = (RepositoryService) appContext.getBean( "simRepositoryService" );		
		simRepositoryService.createDeployment()
	       .addClasspathResource("org/activiti/crystalball/simulator/Playback.bpmn")
	       .deploy();
		
		HistoryService historyService = (HistoryService)appContext.getBean("simHistoryService");
		// init identity service
		IdentityService identityService = (IdentityService) appContext.getBean("simIdentityService");
		identityService.saveGroup( identityService.newGroup("Group1") );
		identityService.saveUser( identityService.newUser("user1") );
		identityService.createMembership("user1", "Group1");
		
		SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
	    PlaybackScheduleStartProcessEventHandler schedule = (PlaybackScheduleStartProcessEventHandler)appContext.getBean("scheduleProcessEventHandler");
	    schedule.setRepeatPlayback( true );
	    
	    Calendar c = Calendar.getInstance();
	    c.clear();
	    c.set(2013, 3, 3);
	    Date startDate = c.getTime();
	    c.add( Calendar.SECOND, 12);
	    Date finishDate = c.getTime();
	    // run simulation for 12 seconds
	    @SuppressWarnings("unused")
		List<SimulationResultEvent> resultEventList = simRun.execute(startDate, finishDate);

	    assertEquals( 8, historyService.createHistoricProcessInstanceQuery().count());
	    List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
	    		.orderByProcessInstanceStartTime().asc()
	    		.list();
	    HistoricProcessInstance processInstance = processInstances.get(0);
	    HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(1);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(2, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(2);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(3);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(4);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(2, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(5);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(3, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(6);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    //user task was not finished yet
	    assertEquals(1, ((Integer) variableInstance.getValue()).intValue());

	    processInstance = processInstances.get(7);
	    variableInstance = historyService.createHistoricVariableInstanceQuery()
	    		.processInstanceId( processInstance.getId())
	    		.variableName( "x")
	    		.singleResult();
	    assertEquals(2, ((Integer) variableInstance.getValue()).intValue());

		appContext.close();
	}

}
