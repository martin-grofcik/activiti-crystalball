package org.processmonitor.simulator;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.processmonitor.activiti.diagram.BasicProcessDiagramGenerator;
import org.processmonitor.activiti.diagram.HighlightNodeDiagramLayer;
import org.processmonitor.activiti.diagram.MergeLayersGenerator;
import org.processmonitor.activiti.diagram.WriteNodeDescriptionDiagramLayer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/org/processmonitor/simulator/SimEngine-h2-context.xml");
		SimulationRun simRun = appContext.getBean(SimulationRun.class);
		RepositoryService repositoryService  = (RepositoryService) appContext.getBean("simRepositoryService");
		
		List<SimulationResultEvent> resultEventList = simRun.execute(new Date(), null);

		
		Collection<List<SimulationResultEvent>> unfinishedTaskProcesses = SimulationResultsPostProcessor.groupProcessDefinitionKey( SimulationResultsPostProcessor.getEventType("unfinished_task", resultEventList));
		
      
		for (List<SimulationResultEvent> eventList : unfinishedTaskProcesses ) {
			String processDefinitionId=eventList.get(0).getProcessDefinitionKey();
			List<String> highlightTasks =  SimulationResultsPostProcessor.getTaskDefinitionKeys( eventList);
			Map<String,String> nodeDescription = SimulationResultsPostProcessor.getNodeDescriptions( eventList);

			
			File dir = new File(tempDir + "/" + eventList.get(0).getType());
			dir.mkdir();
			String reportFileName = tempDir + "/" + eventList.get(0).getType() +"/"+ processDefinitionId +".png"; 
			reportGraph(reportFileName, processDefinitionId, highlightTasks, nodeDescription, repositoryService);
			
		}
		 
		appContext.close();
 
		File expected = new File(System.getProperty("baseDir", ".") + "/src/test/resources/org/processmonitor/simulator/SimulatorProcessMonitor-unfinishedTasks-expected.png" );   
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
