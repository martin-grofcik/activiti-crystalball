package org.processmonitor.generator.queue;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.activiti.engine.ProcessEngine;
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

	private Logger log = Logger.getLogger(getClass().getName());

	private static String ONETASK_PROCESS_KEY = "onetaskprocess";


	@Autowired 
	private ProcessEngine processEngine;
		
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	private List<String> processInstanceIds;
	
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
