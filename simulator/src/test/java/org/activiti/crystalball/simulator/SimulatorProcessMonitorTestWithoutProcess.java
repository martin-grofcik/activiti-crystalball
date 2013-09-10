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


import org.activiti.crystalball.diagram.BasicProcessDiagramGenerator;
import org.activiti.crystalball.diagram.HighlightNodeDiagramLayer;
import org.activiti.crystalball.diagram.MergeLayersGenerator;
import org.activiti.crystalball.diagram.WriteNodeDescriptionDiagramLayer;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class SimulatorProcessMonitorTestWithoutProcess {

	private static final String tempDir = System.getProperty("tempDir", "target");
	private static final String LIVE_DB = tempDir + "/BasicSimulation";
	
	@Before
	public void before() throws IOException {
        System.setProperty("liveDB", LIVE_DB);
        System.setProperty("_SIM_DB_PATH", tempDir+"/simulationRunDB-SimulatorProcessMonitorTest-"+Thread.currentThread().getId());
        
        FileUtils.copyFile( new File(LIVE_DB+".h2.db"), new File(tempDir+"/simulationRunDB-SimulatorProcessMonitorTest-"+Thread.currentThread().getId()+".h2.db"));

	}

	@After
	public void after() {
		// delete database file
		File f = new File(System.getProperty("_SIM_DB_PATH")+".h2.db");
		f.delete();
	}

	@Test
	public void testProcessRun() throws Throwable {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/activiti/crystalball/simulator/SimRun-h2-context.xml");
		SimulationRun simRun = appContext.getBean(SimulationRun.class);
		RepositoryService repositoryService  = (RepositoryService) appContext.getBean("simRepositoryService");
		
		List<ResultEntity> resultEventList = simRun.execute(new Date(), null);

		
		Collection<List<ResultEntity>> unfinishedTaskProcesses = SimulationResultsPostProcessor.groupProcessDefinitionKey( SimulationResultsPostProcessor.getEventType("unfinished_task", resultEventList));
		
      
		for (List<ResultEntity> eventList : unfinishedTaskProcesses ) {
			String processDefinitionId=(String) eventList.get(0).getVariable("processDefinitionKey");
			List<String> highlightTasks =  SimulationResultsPostProcessor.getTaskDefinitionKeys( eventList);
			Map<String,String> nodeDescription = SimulationResultsPostProcessor.getNodeDescriptions( eventList);

			
			File dir = new File(tempDir + "/" + eventList.get(0).getType());
			dir.mkdir();
			String reportFileName = tempDir + "/" + eventList.get(0).getType() +"/"+ processDefinitionId +".png"; 
			reportGraph(reportFileName, processDefinitionId, highlightTasks, nodeDescription, repositoryService);
			
		}
		 
		appContext.close();
 
		File expected = new File(System.getProperty("baseDir", ".") + "/src/org.activiti.crystalball.processengine.wrapper.test/resources/org/activiti/crystalball/simulator/SimulatorProcessMonitor-unfinishedTasks-expected.png" );
		File generated = new File(System.getProperty("tempDir", "target") + "/unfinished_task/threetasksprocess.png");   
	    assertTrue( FileUtils.contentEquals(expected, generated));
	    
	}

	private void reportGraph(String fileName, String processDefinitionKey,
			List<String> highLightedActivities, Map<String, String> counts, RepositoryService repositoryService)
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "processDefinitionId", processDefinitionKey);

		Map<String, Object> highlightParams = new HashMap<String, Object>();
		highlightParams.put( "processDefinitionId", processDefinitionKey);
		highlightParams.put( "highLightedActivities", highLightedActivities);

		Map<String, Object> writeCountParams = new HashMap<String, Object>();
		writeCountParams.put( "processDefinitionId", processDefinitionKey);
		
		writeCountParams.putAll( counts );
	    BasicProcessDiagramGenerator basicGenerator = new BasicProcessDiagramGenerator((RepositoryServiceImpl) repositoryService);
	    HighlightNodeDiagramLayer highlightGenerator = new HighlightNodeDiagramLayer((RepositoryServiceImpl) repositoryService);
	    WriteNodeDescriptionDiagramLayer countGenerator = new WriteNodeDescriptionDiagramLayer((RepositoryServiceImpl) repositoryService);
	    MergeLayersGenerator mergeGenerator = new MergeLayersGenerator();
	    
	    Map<String, Object> mergeL = new HashMap<String, Object>();
		mergeL.put( "1", basicGenerator.generateLayer("png", params));
		mergeL.put( "2", highlightGenerator.generateLayer("png", highlightParams));
		mergeL.put( "3", countGenerator.generateLayer("png", writeCountParams));
	    
		mergeGenerator.generateLayer("png", mergeL);
		File generatedFile = new File(fileName );
	    ImageIO.write( ImageIO.read(new ByteArrayInputStream( mergeGenerator.generateLayer("png", mergeL)))
				, "png"
				, generatedFile);
	}
}
