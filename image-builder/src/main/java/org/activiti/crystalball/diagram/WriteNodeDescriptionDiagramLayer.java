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


import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * write counts into nodes  
 *
 */
public class WriteNodeDescriptionDiagramLayer extends AbstractProcessDiagramLayerGenerator {

	public static String PROCESS_DEFINITION_ID = "processDefinitionId";
	
	public WriteNodeDescriptionDiagramLayer() {
	}

    public WriteNodeDescriptionDiagramLayer( RepositoryServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
    }

	public WriteNodeDescriptionDiagramLayer( RepositoryServiceImpl repositoryService, ProcessDiagramCanvasFactory canvasFactory) {
        super( canvasFactory);
		this.repositoryService = repositoryService;
	}

	@Override
	public InputStream generateLayer(String imageType,
			Map<String, Object> params) {
		// get parameters
		final String processDefinitionKey = (String) params.get( PROCESS_DEFINITION_ID );
		final String processDefinitionId  = repositoryService.createProcessDefinitionQuery().processDefinitionKey( processDefinitionKey ).singleResult().getId();
	    
	    // get process activities to write count
		Set<String> writeableActivities = (Set<String>) params.keySet();
	    writeableActivities.remove( PROCESS_DEFINITION_ID);
	    
		// get process definition entity
	    ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    
		ProcessDiagramCanvas canvas = initProcessDiagramCanvas( pde );

		for (ActivityImpl activity : pde.getActivities()) {
	      // Draw count into activities
	      if (writeableActivities.contains(activity.getId())) {
	    	  canvas.drawStringToNode( params.get( activity.getId() ).toString(), activity.getX(), activity.getY(), activity.getWidth(), activity.getHeight());	      
	      }
	    }
	    return canvas.generateImage(imageType);
	}
	}
