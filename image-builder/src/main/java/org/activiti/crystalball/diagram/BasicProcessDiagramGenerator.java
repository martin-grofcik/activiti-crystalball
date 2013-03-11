package org.activiti.crystalball.diagram;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;


import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 * generates basic process diagram without any highlightings
 * just pure image (connection, nodes, names) 
 *
 */
public class BasicProcessDiagramGenerator extends AbstractProcessDiagramLayerGenerator  {

	public BasicProcessDiagramGenerator() {		
	}
	
	public BasicProcessDiagramGenerator(RepositoryServiceImpl repositoryService)
	{
		this.repositoryService = repositoryService ;
	}
	
	/**
	 * @param params
	 * <table>
	 * <tr><td>key</td><td>value</td><td>comment</td></tr>
	 * <tr><td>processDefinitionId</td><td> process definition id (use PROCESS_DEFINITION_ID)</td><td></td></tr> 
	 * </table>
	 * @return null in case of params == null in other cases @see RepositoryServiceImpl ProcessDiagramGenerator 
	 */
	@Override
	public byte[] generateLayer(String imageType, Map<String, Object> params) {
		
		// get process definition entity
		final String processDefinitionKey = (String) params.get( PROCESS_DEFINITION_ID );
		final String processDefinitionId  = repositoryService.createProcessDefinitionQuery().processDefinitionKey( processDefinitionKey ).singleResult().getId();
	    ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));

	    // convert image to byte[]
	    InputStream diagramIs = ProcessDiagramGenerator.generateDiagram(pde, imageType, Collections.<String> emptyList());   
	    
		return convertToByteArray(imageType, diagramIs);
	}

}
