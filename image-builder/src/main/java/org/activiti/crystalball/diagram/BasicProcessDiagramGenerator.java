package org.activiti.crystalball.diagram;

/*
 * #%L
 * image-builder
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
