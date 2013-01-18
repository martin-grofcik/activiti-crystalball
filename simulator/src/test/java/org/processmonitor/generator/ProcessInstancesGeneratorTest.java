package org.processmonitor.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;


public class ProcessInstancesGeneratorTest extends PluggableActivitiTestCase {

	private static String PROCESS_KEY = "threetasksprocess";

	private List<String> processInstanceIds;

	protected void setUp() throws Exception {
		super.setUp();
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"org/processmonitor/simulator/ThreeTasksProcess.bpmn")
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
	 * The first report should have <a href="https://raw.github.com/gro-mar/process-monitor/master/simulator/src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-1.png">4 tasks on the first node<a>.<br/>
	 * 3 tasks are completed - <a href="https://raw.github.com/gro-mar/process-monitor/master/simulator/src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-2.png">1 task on the first node  3 tasks on the second.<a><br/>
	 * 3 tasks from the 2nd node are completed - <a href="https://raw.github.com/gro-mar/process-monitor/master/simulator/src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-3.png">1 task on the first node  3 tasks on the 3rd<a>.<br/>
	 * 3 tasks from the 3rd node are completed - <a href="https://raw.github.com/gro-mar/process-monitor/master/simulator/src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-4.png">1 task on the first node remains<a>
	 * @throws IOException
	 */
	public void testGenerateProcessDefinition() throws IOException {
	    
	    ProcessInstancesGenerator monitor = new ProcessInstancesGenerator();
	    monitor.setRepositoryService(repositoryService);
	    monitor.setRuntimeService(runtimeService);
	    
	    String processDefinitionId = repositoryService.createProcessDefinitionQuery().processDefinitionKey( PROCESS_KEY).singleResult().getId();
	    
	    monitor.generateReport( processDefinitionId, "target/ProcessInstanceMonitor-1.png");
	    
	    File expectedFile = new File("src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-1.png" );
	    File generatedFile = new File("target/ProcessInstanceMonitor-1.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));
	    
	    // move 3 tasks forward
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("user1").list();
		for (int i = 0; i < 3; i++) {
				Task t = taskList.get(i);
				taskService.claim( t.getId(), "user1" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, "target/ProcessInstanceMonitor-2.png");
	    
	    expectedFile = new File("src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-2.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-2.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user2").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user2" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, "target/ProcessInstanceMonitor-3.png");

		expectedFile = new File("src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-3.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-3.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	    // move 3 tasks forward
		taskList = taskService.createTaskQuery().taskCandidateUser("user3").list();
		for (Task t : taskList) {
				taskService.claim( t.getId(), "user3" );
				taskService.complete(t.getId());
		}

	    monitor.generateReport( processDefinitionId, "target/ProcessInstanceMonitor-4.png");

		expectedFile = new File("src/test/resources/org/processmonitor/generator/ProcessInstanceMonitor-4.png" );
	    generatedFile = new File("target/ProcessInstanceMonitor-4.png" );
	    assertTrue(FileUtils.contentEquals(expectedFile, generatedFile));

	}

	

}
