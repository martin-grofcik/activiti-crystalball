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


import org.activiti.crystalball.diagram.AuditTrailProcessDiagramGenerator;
import org.activiti.engine.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.h2.store.fs.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * generate basic process engine state on which simulation is performed 
 *
 */
public class GenerateProcessEngineStateContinue {

    public static final String H2_DB_SUFFIX = ".h2.db";
    static String TEMP_DIR = "target";
    private static Calendar calendar = null;

    private GenerateProcessEngineStateContinue() {
    throw new IllegalStateException("GenerateProcessEngineStateContinue can not be instantiated");
  }

	public static void main(String[] args) throws InterruptedException, IOException {
    if (args != null && args.length == 1)
		  TEMP_DIR = args[0];

        copyDatabase(TEMP_DIR + "/Playback.initial", TEMP_DIR + "/Playback.processed");
        continueInExecution(TEMP_DIR + "/Playback.processed");
    }

    private static void copyDatabase(String source, String destination) throws IOException {
        FileUtils.copy(source+ H2_DB_SUFFIX, destination + H2_DB_SUFFIX);
    }

    private static void continueInExecution(String liveDB) throws IOException {
        System.setProperty("liveDB", liveDB);
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("org/activiti/crystalball/simulator/LiveEngine-context.xml");
        ProcessEngine processEngine;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 01, 25, 14, 30, 00);
        ClockUtil.setCurrentTime(calendar.getTime());

        processEngine = appContext.getBean(ProcessEngine.class);
        // start processes
        Map<String, Object> variables2 = new HashMap<String, Object>();
        variables2.put("x", 3);
        Map<String, Object> variables1 = new HashMap<String, Object>();
        variables1.put("x", 1);
        Map<String, Object> variables0 = new HashMap<String, Object>();
        variables0.put("x", 0);

        String PROCESS_KEY = "playback";
        ProcessInstance processInstance1 = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY, "BUSINESS-KEY-1", variables0);
        calendar.add(Calendar.SECOND, 1);
        ClockUtil.setCurrentTime(calendar.getTime());
        ProcessInstance processInstance2 = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY, "BUSINESS-KEY-2", variables1);
        calendar.add(Calendar.SECOND, 1);
        ClockUtil.setCurrentTime(calendar.getTime());
        processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY, "BUSINESS-KEY-3", variables0);

        // put first 5 tasks to the next node
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().taskCandidateUser("user1").list();
        for (Task t : taskList) {
            processEngine.getTaskService().claim(t.getId(), "user1");
        }

        // complete these tasks
        for (Task t : taskList) {
            // wait some time  to have some data in history
            calendar.add(Calendar.SECOND, 1);
            ClockUtil.setCurrentTime(calendar.getTime());
            // complete task
            processEngine.getTaskService().complete(t.getId(), variables2);
        }

        generateAuditTrails(processEngine, processInstance1, processInstance2);


        processEngine.close();

        appContext.close();

    }

    private static void generateAuditTrails(ProcessEngine processEngine, ProcessInstance processInstance1, ProcessInstance processInstance2) throws IOException {// audit trail images generator
        AuditTrailProcessDiagramGenerator generator = new AuditTrailProcessDiagramGenerator();
        generator.setHistoryService(processEngine.getHistoryService());
        generator.setRepositoryService((RepositoryServiceImpl) processEngine.getRepositoryService());
        generator.setWriteUpdates(true);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AuditTrailProcessDiagramGenerator.PROCESS_INSTANCE_ID, processInstance1.getId());

        ImageIO.write(ImageIO.read(generator.generateLayer("png", params))
            , "png"
            , new File(TEMP_DIR + "/playback-auditTrail1.png"));

        params.clear();
        params.put(AuditTrailProcessDiagramGenerator.PROCESS_INSTANCE_ID, processInstance2.getId());

        ImageIO.write(ImageIO.read(generator.generateLayer("png", params))
            , "png"
            , new File(TEMP_DIR + "/playback-auditTrail2.png"));
    }
}
