package org.processmonitor.simulator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/org/processmonitor/generator/usertasksimulator/TaskInstanceHistoryDueDateMonitorTest-context.xml")
public class BasicSimulationTest {

	
	@Before
	public void before() throws InterruptedException {
	}

	@After
	  public void after() {
	  }

	@Test
	public void testDueTimeNotReached() throws IOException {
		fail();
	}
}
