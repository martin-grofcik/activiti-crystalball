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


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TwoEnginesWithoutProcessTest
{

    private static String LIVE_DB = "target/simulatorTest-LiveDB";

    @Before
    public void setUp()
        throws Exception
    {
        String PROCESS_KEY = "threetasksprocess";
        File prevDB = new File((new StringBuilder()).append(LIVE_DB).append(".h2.db").toString());
        System.setProperty("liveDB", LIVE_DB);
        if(prevDB.exists())
            prevDB.delete();
        prevDB = null;
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("org/activiti/crystalball/simulator/LiveEngine-context.xml");
        RepositoryService repositoryService = (RepositoryService)appContext.getBean("repositoryService");
        RuntimeService runtimeService = (RuntimeService)appContext.getBean("runtimeService");
        TaskService taskService = (TaskService)appContext.getBean("taskService");
        IdentityService identityService = (IdentityService)appContext.getBean("identityService");
        ProcessEngine processEngine = (ProcessEngine)appContext.getBean("processEngine");
        repositoryService.createDeployment().addClasspathResource("org/activiti/crystalball/simulator/ThreeTasksProcess.bpmn").deploy();
        identityService.saveGroup(identityService.newGroup("Group1"));
        identityService.saveGroup(identityService.newGroup("Group2"));
        identityService.saveUser(identityService.newUser("user1"));
        identityService.saveUser(identityService.newUser("user2"));
        identityService.createMembership("user1", "Group1");
        identityService.createMembership("user2", "Group2");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2012, 11, 7, 18, 1, 0);
        Date dueDateFormal = calendar.getTime();
        calendar.set(2012, 11, 7, 18, 1, 30);
        Date dueDateValue = calendar.getTime();
        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("dueDateFormal", dueDateFormal);
        variables.put("dueDateValue", dueDateValue);
        for(int i = 0; i < 10; i++)
            runtimeService.startProcessInstanceByKey(PROCESS_KEY, (new StringBuilder()).append("BUSINESS-KEY-").append(i).toString(), variables);

        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("user1").list();
        for(int i = 0; i < 5; i++)
        {
            Task t = (Task)taskList.get(i);
            taskService.claim(t.getId(), "user1");
        }

        Thread.sleep(500L);
        for(int i = 0; i < 5; i++)
        {
            Task t = (Task)taskList.get(i);
            taskService.complete(t.getId());
        }

        processEngine.close();
        appContext.close();
    }

    @After
    public void tearDown()
        throws Exception
    {
        File prevDB = new File((new StringBuilder()).append(LIVE_DB).append(".h2.db").toString());
        if(prevDB.exists())
            prevDB.delete();
        prevDB = new File(System.getProperty("_SIM_DB_PATH"));
        if(prevDB.exists())
            prevDB.delete();
    }

    @Test
    public void test()
        throws Exception
    {
        String tempDir = "target";
        FileUtils.copyFile(FileUtils.getFile(new String[] {
            (new StringBuilder()).append(LIVE_DB).append(".h2.db").toString()
        }), FileUtils.getFile(new String[] {
            (new StringBuilder()).append(tempDir).append("/simulationRunDB-aaa-").append(Thread.currentThread().getId()).append(".h2.db").toString()
        }));
        System.setProperty("_SIM_DB_PATH", (new StringBuilder()).append(tempDir).append("/simulationRunDB-aaa-").append(Thread.currentThread().getId()).toString());
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimRun-h2-context.xml");
		PropertyPlaceholderConfigurer propConfig = new PropertyPlaceholderConfigurer();
		Properties properties = new Properties();
		properties.put("simulationRunId", "simulationRunDB-aaa-"+Thread.currentThread().getId());
		propConfig.setProperties(properties);
		appContext.addBeanFactoryPostProcessor(propConfig);
		appContext.refresh();
        
        SimulationRun simRun = (SimulationRun)appContext.getBean(SimulationRun.class);
        String userId = "user1";
        TaskService taskService = (TaskService)appContext.getBean("taskService");
        TaskService simTaskService = (TaskService)appContext.getBean("simTaskService");
        List<Task> liveTaskList = ((TaskQuery)taskService.createTaskQuery().taskCandidateUser(userId).orderByTaskPriority().desc()).listPage(0, 1);
        List<Task> execTaskList = ((TaskQuery)simTaskService.createTaskQuery().taskCandidateUser(userId).orderByTaskPriority().desc()).listPage(0, 1);
        Assert.assertTrue(liveTaskList.size() == execTaskList.size());
        IdentityService identityService = (IdentityService)appContext.getBean("identityService");
        IdentityService simIdentityService = (IdentityService)appContext.getBean("simIdentityService");
        List<User> users = identityService.createUserQuery().list();
        List<User> simUsers = simIdentityService.createUserQuery().list();
        Assert.assertTrue(users.size() == simUsers.size());
        simRun.execute(new Date(), null);
        appContext.close();
    }

}
