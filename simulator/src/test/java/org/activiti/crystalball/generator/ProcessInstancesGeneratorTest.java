package org.activiti.crystalball.generator;

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


import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProcessInstancesGeneratorTest extends PluggableActivitiTestCase {

	private static String PROCESS_KEY = "threetasksprocess";

	private List<String> processInstanceIds;

	protected void setUp() throws Exception {
		super.setUp();
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"org/activiti/crystalball/simulator/ThreeTasksProcess.bpmn")
				.deploy();

		processInstanceIds = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			processInstanceIds.add(runtimeService.startProcessInstanceByKey(
					PROCESS_KEY, "BUSINESS-KEY-" + i).getId());
		}
		
		// init identity service
		identityService.saveGroup( identityService.newGroup("Group1") );
		identityService.saveGroup( identityService.newGroup("Group2") );
		identityService.saveGroup( identityService.newGroup("Group3") );
		identityService.saveUser( identityService.newUser("user1") );
		identityService.saveUser( identityService.newUser("user2") );
		identityService.saveUser( identityService.newUser("user3") );
		
		identityService.createMembership("user1", "Group1");
		identityService.createMembership("user2", "Group2");
		identityService.createMembership("user3", "Group3");

	}

	protected void tearDown() throws Exception {
		for (org.activiti.engine.repository.Deployment deployment : repositoryService
				.createDeploymentQuery().list()) {
			repositoryService.deleteDeployment(deployment.getId(), true);
		}
		
		identityService.deleteMembership("user1", "Group1");
		identityService.deleteMembership("user2", "Group2");
		identityService.deleteMembership("user3", "Group3");

		identityService.deleteUser( "user1");
		identityService.deleteUser( "user2");
		identityService.deleteUser( "user3");
		identityService.deleteGroup( "Group1");
		identityService.deleteGroup( "Group2");
		identityService.deleteGroup( "Group3");
		
		super.tearDown();
	}
	
	/**
	 * Generate ProcessInstances reports for started ThreeTask process. <br/>
	 * The first report should have <a href="https://raw.github.com/gro-mar/activiti/crystalball/master/simulator/src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-1.png">4 tasks on the first node<a>.<br/>
	 * 3 tasks are completed - <a href="https://raw.github.com/gro-mar/activiti/crystalball/master/simulator/src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-2.png">1 task on the first node  3 tasks on the second.<a><br/>
	 * 3 tasks from the 2nd node are completed - <a href="https://raw.github.com/gro-mar/activiti/crystalball/master/simulator/src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-3.png">1 task on the first node  3 tasks on the 3rd<a>.<br/>
	 * 3 tasks from the 3rd node are completed - <a href="https://raw.github.com/gro-mar/activiti/crystalball/master/simulator/src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-4.png">1 task on the first node remains<a>
	 * @throws IOException
	 */
	public void testGenerateProcessDefinition() throws IOException {
	    
		ProcessInstancesGenerator monitor = new ProcessInstancesGenerator();
	    monitor.setRepositoryService(repositoryService);
	    monitor.setRuntimeService(runtimeService);
	    List<ColorInterval> highlightColorIntervalList = new ArrayList<ColorInterval>();
	    highlightColorIntervalList.add( new ColorInterval(1, Color.red));	    
	    monitor.setHighlightColorIntervalList(highlightColorIntervalList);

	    String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey( PROCESS_KEY).singleResult().getId();
	    
	    monitor.generateReport( processDefinitionId, null, null, "target/ProcessInstanceMonitor-1.png");
	    
	    File expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-1.png" );
	    File generatedFile = new File("target/ProcessInstanceMonitor-1.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));
	    
	    // move 3 tasks forward
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("user1").list();
		for (int i = 0; i < 3; i++) {
				Task t = taskList.get(i);
				taskService.claim( t.getId(), "user1" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/ProcessInstanceMonitor-2.png");
	    
	    expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-2.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-2.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user2").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user2" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/ProcessInstanceMonitor-3.png");

		expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-3.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-3.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user3").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user3" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/ProcessInstanceMonitor-4.png");

		expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/ProcessInstanceMonitor-4.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-4.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	}

	public void testGenerateHistoricInstances() throws IOException {
	    
		HistoricInstancesGenerator monitor = new HistoricInstancesGenerator();
	    monitor.setRepositoryService(repositoryService);
	    monitor.setHistoryService(historyService);
	    List<ColorInterval> highlightColorIntervalList = new ArrayList<ColorInterval>();
	    highlightColorIntervalList.add( new ColorInterval(4, Color.green));
	    highlightColorIntervalList.add( new ColorInterval(1,3, Color.yellow));
	    highlightColorIntervalList.add( new ColorInterval(0,0, Color.red));
	    
	    monitor.setHighlightColorIntervalList(highlightColorIntervalList);
	    
	    String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey( PROCESS_KEY).singleResult().getId();
	    
	    monitor.generateReport( processDefinitionId, null, null, "target/HistoricInstanceMonitor-1.png");
	    
	    File expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/HistoricInstanceMonitor-1.png" );
	    File generatedFile = new File("target/HistoricInstanceMonitor-1.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));
	    
	    // move 3 tasks forward
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("user1").list();
		for (int i = 0; i < 3; i++) {
				Task t = taskList.get(i);
				taskService.claim( t.getId(), "user1" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/HistoricInstanceMonitor-2.png");
	    
	    expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/HistoricInstanceMonitor-2.png" );
	    generatedFile = new File("target/HistoricInstanceMonitor-2.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user2").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user2" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/HistoricInstanceMonitor-3.png");

		expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/HistoricInstanceMonitor-3.png" );
	    generatedFile = new File("target/HistoricInstanceMonitor-3.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user3").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user3" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, null, null, "target/HistoricInstanceMonitor-4.png");

		expectedFile = new File("src/test/resources/org/activiti/crystalball/generator/HistoricInstanceMonitor-4.png" );
	    generatedFile = new File("target/HistoricInstanceMonitor-4.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	}

	

}
