package org.activiti.crystalball.generator;

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


import org.activiti.crystalball.processengine.wrapper.HistoryServiceWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ActivityWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.NativeHistoricTaskInstanceQueryWrapper;
import org.activiti.crystalball.processengine.wrapper.queries.ProcessDefinitionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DueDateGraphGenerator extends AbstractProcessEngineGraphGenerator {

	protected static Logger log = LoggerFactory.getLogger(DueDateGraphGenerator.class);
	HistoryServiceWrapper historyService;
	
	/** query for selecting all task instances (completed or not) in given time interval for given ProcessDefinitionId and */
	private String QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL = "select count(*) from ACT_HI_TASKINST RES where " +
														"RES.PROC_DEF_ID_ = #{processDefinitionId} and RES.TASK_DEF_KEY_ = #{activityId}" +
														"and (( RES.END_TIME_ > #{startDate}) or (RES.END_TIME_ = null)) " +
														"and (RES.START_TIME_ < #{endDate}) ";
	private String COUNT_AFTER_DUE_HISTORIC_TASK_INSTANCES_FROM_INTERVAL = QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL +
    		"and (RES.DUE_DATE_ is not null) and ((RES.DUE_DATE_ < #{endDate})) " +
    		"and (RES.END_TIME_ is not null) and (RES.END_TIME_ > RES.DUE_DATE_)";
	
	@Override
	protected ProcessDefinitionWrapper getProcessData(String processDefinitionId, Date startDate, Date finishDate,
			 Map<Color,List<String>> highLightedActivitiesMap, Map<String, String> counts) {
		
		ProcessDefinitionWrapper pde = repositoryService.getDeployedProcessDefinition( processDefinitionId );
	    List<ActivityWrapper> activities = pde.getActivities();
	    // iterate through all activities
	    for( ActivityWrapper activity : activities) {
	    	// get count of  executions for userTask in the given process
	    	if (activity.getProperty("type") == "userTask") {
	    		

	    		NativeHistoricTaskInstanceQueryWrapper query = historyService.createNativeHistoricTaskInstanceQuery();
	    		
		    	long count = query.sql(QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL)
		    						.parameter("processDefinitionId", processDefinitionId)
		    						.parameter("activityId", activity.getId())
		    						.parameter("startDate", startDate)
		    						.parameter("endDate", finishDate)
		    						.count();
		    	
		    	long countAfterDue = query.sql(COUNT_AFTER_DUE_HISTORIC_TASK_INSTANCES_FROM_INTERVAL)
						.parameter("processDefinitionId", processDefinitionId)
						.parameter("activityId", activity.getId())
						.parameter("startDate", startDate)
						.parameter("endDate", finishDate)
						.count();
		    	long percentage = (countAfterDue * 100) / count;
		    	for ( ColorInterval colorInterval : highlightColorIntervalList )
		    	{
			    	if ( colorInterval.isInside(percentage) ) {
			    			addToHighlighted( highLightedActivitiesMap, colorInterval.color, activity.getId());
			    	}
		    	}
		    	if (percentage > 0)
		    		counts.put(activity.getId(), Long.toString(percentage) +"%");

		    	log.debug("selected counts "+processDefinitionId +"-"+activity.getId()+"-"+count);
	    	}
	    }
		return pde;
	}

	public HistoryServiceWrapper getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryServiceWrapper historyService) {
		this.historyService = historyService;
	}

}
