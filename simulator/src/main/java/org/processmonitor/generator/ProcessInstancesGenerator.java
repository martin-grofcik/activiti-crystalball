package org.processmonitor.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.processmonitor.activiti.diagram.BasicProcessDiagramGenerator;
import org.processmonitor.activiti.diagram.HighlightNodeDiagramLayer;
import org.processmonitor.activiti.diagram.MergeLayersGenerator;
import org.processmonitor.activiti.diagram.WriteNodeDescriptionDiagramLayer;

public class ProcessInstancesGenerator {

	protected static Logger log = Logger.getLogger(ProcessInstancesGenerator.class.getName());
	
	RuntimeService runtimeService;
	RepositoryService repositoryService;
	
	/**
	 * Generate report about executions for the process instances driven by processDefinitionId.
	 * Report contains counts of tokens and node highlighting where tokens are located
	 *  
	 * @param processDefinitionId
	 * @param fileName
	 * @throws IOException
	 */
	public void generateReport(String processDefinitionId, String fileName) throws IOException {
		log.entering( getClass().getName(), "generateReport");
	    
	    List<String> highLightedActivities = new ArrayList<String>();
	    Map<String,String> counts = new HashMap<String, String>();
	    
		ProcessDefinitionEntity pde = getProcessData(processDefinitionId,
				highLightedActivities, counts);
	    
	    reportGraph(fileName, pde.getKey(), highLightedActivities, counts);
		log.exiting( getClass().getName(), "generateReport");
	}


	private ProcessDefinitionEntity getProcessData(String processDefinitionId,
			List<String> highLightedActivities, Map<String, String> counts) {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    List<ActivityImpl> activities = pde.getActivities();
	    for( ActivityImpl activity : activities) {
	    	long count = runtimeService.createExecutionQuery().processDefinitionId(processDefinitionId).activityId( activity.getId() ).count();
	    	if ( count > 0 ) {
	    		highLightedActivities.add(activity.getId());
	    		counts.put(activity.getId(), Long.toString(count));
	    	}
	    	log.log( Level.FINER, "selected counts "+processDefinitionId +"-"+activity.getId()+"-"+count);
	    }
		return pde;
	}


	private void reportGraph(String fileName, String processDefinitionKey,
			List<String> highLightedActivities, Map<String, String> counts)
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


	public RuntimeService getRuntimeService() {
		return runtimeService;
	}


	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
