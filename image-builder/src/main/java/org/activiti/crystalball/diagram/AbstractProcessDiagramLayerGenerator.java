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
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractProcessDiagramLayerGenerator implements DiagramLayerGenerator{

	public static String PROCESS_DEFINITION_ID = "processDefinitionId";
	protected RepositoryServiceImpl repositoryService;
	protected static Logger log = Logger.getLogger(AbstractProcessDiagramLayerGenerator.class.getName());

    protected ProcessDiagramCanvasFactory canvasFactory;

	public AbstractProcessDiagramLayerGenerator() {
		this( null);
	}

    public AbstractProcessDiagramLayerGenerator(ProcessDiagramCanvasFactory canvasFactory) {
        super();
        if ( canvasFactory != null)
            this.canvasFactory = canvasFactory;
        else
            this.canvasFactory = new DefaultCanvasFactoryImpl();
    }

	public RepositoryServiceImpl getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryServiceImpl repositoryService) {
		this.repositoryService = repositoryService;
	}

	protected ProcessDiagramCanvas initProcessDiagramCanvas(
			ProcessDefinitionEntity processDefinition) {
	    int minX = Integer.MAX_VALUE;
	    int maxX = 0;
	    int minY = Integer.MAX_VALUE;
	    int maxY = 0;

	    if(processDefinition.getParticipantProcess() != null) {
	      ParticipantProcess pProc = processDefinition.getParticipantProcess();

	      minX = pProc.getX();
	      maxX = pProc.getX() + pProc.getWidth();
	      minY = pProc.getY();
	      maxY = pProc.getY() + pProc.getHeight();
	    }

	    for (ActivityImpl activity : processDefinition.getActivities()) {

	      // width
	      if (activity.getX() + activity.getWidth() > maxX) {
	        maxX = activity.getX() + activity.getWidth();
	      }
	      if (activity.getX() < minX) {
	        minX = activity.getX();
	      }
	      // height
	      if (activity.getY() + activity.getHeight() > maxY) {
	        maxY = activity.getY() + activity.getHeight();
	      }
	      if (activity.getY() < minY) {
	        minY = activity.getY();
	      }

	      for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
	        List<Integer> waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
	        for (int i = 0; i < waypoints.size(); i += 2) {
	          // width
	          if (waypoints.get(i) > maxX) {
	            maxX = waypoints.get(i);
	          }
	          if (waypoints.get(i) < minX) {
	            minX = waypoints.get(i);
	          }
	          // height
	          if (waypoints.get(i + 1) > maxY) {
	            maxY = waypoints.get(i + 1);
	          }
	          if (waypoints.get(i + 1) < minY) {
	            minY = waypoints.get(i + 1);
	          }
	        }
	      }
	    }

	    if(processDefinition.getLaneSets() != null && processDefinition.getLaneSets().size() > 0) {
	      for(LaneSet laneSet : processDefinition.getLaneSets()) {
	        if(laneSet.getLanes() != null && laneSet.getLanes().size() > 0) {
	          for(Lane lane : laneSet.getLanes()) {
	            // width
	            if (lane.getX() + lane.getWidth() > maxX) {
	              maxX = lane.getX() + lane.getWidth();
	            }
	            if (lane.getX() < minX) {
	              minX = lane.getX();
	            }
	            // height
	            if (lane.getY() + lane.getHeight() > maxY) {
	              maxY = lane.getY() + lane.getHeight();
	            }
	            if (lane.getY() < minY) {
	              minY = lane.getY();
	            }
	          }
	        }
	      }
	    }

	    return canvasFactory.createCanvas(maxX + 10, maxY + 10, minX, minY);
	}

    abstract public InputStream generateLayer(String imageType, Map<String, Object> params);
}
