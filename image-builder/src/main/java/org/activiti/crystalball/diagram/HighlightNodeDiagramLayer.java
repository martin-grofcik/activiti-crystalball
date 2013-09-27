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

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * highlight nodes in the process diagram
 *
 */
public class HighlightNodeDiagramLayer extends AbstractProcessDiagramLayerGenerator implements DiagramLayerGenerator {

	public static String PROCESS_DEFINITION_ID = "processDefinitionId";
	public static String HIGHLIGHTED_ACTIVITIES = "highLightedActivities";

	/** color to highlight nodes */
	protected Color color;

	public HighlightNodeDiagramLayer() {
	}

	public HighlightNodeDiagramLayer( RepositoryServiceImpl repositoryService) {
		this(repositoryService, null, null);
	}

	public HighlightNodeDiagramLayer( RepositoryServiceImpl repositoryService, Color color, ProcessDiagramCanvasFactory canvasFactory) {
        super(canvasFactory);
		this.repositoryService = repositoryService;
        if (color != null)
		    this.color = color;
        else {
            this.color = Color.red;
        }
	}

	/**
	 *
	 */
	@Override
	public InputStream generateLayer(String imageType, Map<String, Object> params) {

		// get parameters
		final String processDefinitionKey = (String) params.get( PROCESS_DEFINITION_ID );
		final String processDefinitionId  = repositoryService.createProcessDefinitionQuery().processDefinitionKey( processDefinitionKey ).singleResult().getId();
	    @SuppressWarnings("unchecked")
		List<String> highLightedActivities = (List<String>) params.get( HIGHLIGHTED_ACTIVITIES );

		// get process definition entity
	    ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));

		ProcessDiagramCanvas canvas = initProcessDiagramCanvas( pde );

		for (ActivityImpl activity : pde.getActivities()) {
	      // Draw highlighted activities
	      if (highLightedActivities.contains(activity.getId())) {
	    	  canvas.drawHighLight(activity.getX(), activity.getY(), activity.getWidth(), activity.getHeight(), color);	      }
	    }
	    return canvas.generateImage(imageType);
	}
	public void setColor(Color c) {
		this.color = c;
	}
}
