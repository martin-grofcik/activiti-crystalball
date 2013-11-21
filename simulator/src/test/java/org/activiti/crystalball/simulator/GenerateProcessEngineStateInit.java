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
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.h2.store.fs.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * generate basic process engine state on which simulation is performed 
 *
 */
public class GenerateProcessEngineStateInit {

    public static final String H2_DB_SUFFIX = ".h2.db";
    static String TEMP_DIR = "target";

    private GenerateProcessEngineStateInit() {
    throw new IllegalStateException("GenerateProcessEngineStateContinue can not be instantiated");
  }

	public static void main(String[] args) throws InterruptedException, IOException {
    if (args != null && args.length == 1)
		  TEMP_DIR = args[0];

    initEnvironment(TEMP_DIR + "/Playback.initial");

    }
    private static void initEnvironment(String liveDB) {
        initDB(liveDB);

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("org/activiti/crystalball/simulator/LiveEngine-context.xml");
        ProcessEngine processEngine;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 01, 25, 14, 30, 00);
        ClockUtil.setCurrentTime(calendar.getTime());

        processEngine = appContext.getBean(ProcessEngine.class);

        // init identity service
        processEngine.getIdentityService().saveGroup(processEngine.getIdentityService().newGroup("Group1"));
        processEngine.getIdentityService().saveUser(processEngine.getIdentityService().newUser("user1"));
        processEngine.getIdentityService().createMembership("user1", "Group1");

        processEngine.close();

        appContext.close();
    }

    private static void initDB(String liveDB) {
        File prevDB = new File(liveDB + H2_DB_SUFFIX);
        if (prevDB.exists())
            prevDB.delete();
        System.setProperty("liveDB", liveDB);
    }

}
