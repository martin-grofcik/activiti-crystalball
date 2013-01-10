package org.processmonitor.simulator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * generate basic process engine state on which simulation is performed 
 *
 */
public class GenerateProcessEngineState {
	
	
	
	public static void main(String[] args) throws InterruptedException {

		String PROCESS_KEY = "threetasksprocess";
		RepositoryService repositoryService;
		RuntimeService runtimeService;
		TaskService taskService;
		IdentityService identityService;
		ProcessEngine processEngine;

		// delete previous DB instalation and set property to point to the current file
		File prevDB = new File(args[0]+".h2.db");
		System.setProperty("liveDB", args[0]);
		if ( prevDB.exists() )
			prevDB.delete();
		prevDB = null;

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("org/processmonitor/simulator/LiveEngine-context.xml");
		
		repositoryService = appContext.getBean( RepositoryService.class );
		runtimeService = appContext.getBean( RuntimeService.class );
		taskService = appContext.getBean( TaskService.class );
		identityService = appContext.getBean( IdentityService.class );
		processEngine = appContext.getBean( ProcessEngine.class);
		
		// deploy processes
		repositoryService.createDeployment()
	       .addClasspathResource("org/processmonitor/simulator/ThreeTasksProcess.bpmn")
	       .deploy();
		
		// init identity service
		identityService.saveGroup( identityService.newGroup("Group1") );
		identityService.saveGroup( identityService.newGroup("Group2") );
		identityService.saveUser( identityService.newUser("user1") );
		identityService.saveUser( identityService.newUser("user2") );
		
		identityService.createMembership("user1", "Group1");
		identityService.createMembership("user2", "Group2");
		
		// start processes
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, 11, 7, 18, 1, 00);
		Date dueDateFormal =   calendar.getTime();
		calendar.set(2012, 11, 7, 18, 1, 30);
		Date dueDateValue =   calendar.getTime();
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("dueDateFormal", dueDateFormal);
		variables.put("dueDateValue", dueDateValue);
		
		for (int i = 0; i < 10; i++) {			
			runtimeService.startProcessInstanceByKey( PROCESS_KEY, "BUSINESS-KEY-" + i, variables);
		}

		
		// put first 5 tasks to the next node
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("user1").list();
		for (int i = 0; i < 5; i++) {
				Task t = taskList.get(i);
				taskService.claim( t.getId(), "user1" );
		}
		
		// wait some time  to have some data in history 
		Thread.sleep(500);
		
		// complete these tasks
		for (int i = 0; i < 5; i++) {
			Task t = taskList.get(i);
			taskService.complete( t.getId() );
		}
		
		processEngine.close();
		
		appContext.close();
	}

}
