package org.activiti.crystalball.diagram.generator;

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


import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * highlight user tasks with running process instances higher than limit 
 *
 */
public class ProcessInstancesGenerator extends AbstractProcessEngineGraphGenerator {

	protected static Logger log = LoggerFactory.getLogger(ProcessInstancesGenerator.class);
	RuntimeService runtimeService;
	
	@Override
	protected ProcessDefinitionEntity getProcessData(String processDefinitionId, Date startDate, Date finishDate,
			Map<Color,List<String>> highLightedActivitiesMap, Map<String, String> counts) {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    List<ActivityImpl> activities = pde.getActivities();
	    // iterate through all activities
	    for( ActivityImpl activity : activities) {
	    	// get count of  executions for userTask in the given process
	    	if (activity.getProperty("type") == "userTask") {
		    	long count = runtimeService.createExecutionQuery().processDefinitionId(processDefinitionId).activityId( activity.getId() ).count();

		    	// store count in the diagram generator data structures
		    	for ( ColorInterval colorInterval : highlightColorIntervalList )
		    	{
			    	if ( colorInterval.isInside( count ) ) {
			    			addToHighlighted( highLightedActivitiesMap, colorInterval.color, activity.getId());
			    	}
		    	}

		    	if ( count > 0 ) {
		    		counts.put(activity.getId(), Long.toString(count));
		    	}
		    	log.debug("selected counts "+processDefinitionId +"-"+activity.getId()+"-"+count);
	    	}
	    }
		return pde;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

}
