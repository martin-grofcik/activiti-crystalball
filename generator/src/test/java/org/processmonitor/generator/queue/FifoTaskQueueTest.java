package org.processmonitor.generator.queue;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/org/processmonitor/generator/queue/OneTaskProcessTest-context.xml")
public class FifoTaskQueueTest {

	private static String ONETASK_PROCESS_KEY = "onetaskprocess";

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;
	@Before
	public void before() {
	  repositoryService.createDeployment()
	    .addClasspathResource("org/processmonitor/generator/queue/OneTaskProcess.bpmn")
	    .addClasspathResource("org/processmonitor/generator/queue/OneTaskProcess.png")
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGenerator.bpmn")
	    .addClasspathResource("org/processmonitor/generator/SimpleProcessDiagramGenerator.png")
	    .addClasspathResource("org/processmonitor/generator/queue/QueueOverFlown.bpmn")
	    .addClasspathResource("org/processmonitor/generator/queue/QueueOverFlown.png")
	    .deploy();
	}

	@Test
	public void test() {
		for (int i = 0; i < 5; i++) {
			runtimeService.startProcessInstanceByKey( ONETASK_PROCESS_KEY, "BUSINESS-KEY-" + i);
		}
		
	}

}
