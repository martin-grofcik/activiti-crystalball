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

/**
 * highlight nodes from the history which count is in the specified interval with defined color than limit 
 *
 */
public class HistoricInstancesGenerator extends AbstractProcessEngineGraphGenerator {

	protected static Logger log = LoggerFactory.getLogger(HistoricInstancesGenerator.class);
	HistoryServiceWrapper historyService;
	
	/** query for selecting all task instances (completed or not) in given time interval for given ProcessDefinitionId and */
	private String QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL = "select count(*) from ACT_HI_ACTINST RES where " +
														"RES.PROC_DEF_ID_ = #{processDefinitionId} and RES.ACT_ID_ = #{activityId}" +
														"and (( RES.END_TIME_ >= #{startDate}) or (RES.END_TIME_ = null)) " +
														"and (RES.START_TIME_ <= #{endDate}) ";
	/** end Date is null */
	private String QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL_END_NULL = "select count(*) from ACT_HI_ACTINST RES where " +
														"RES.PROC_DEF_ID_ = #{processDefinitionId} and RES.ACT_ID_ = #{activityId}" +
														"and (( RES.END_TIME_ >= #{startDate}) or (RES.END_TIME_ = null)) ";
	/** start and end Date are null */
	private String QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL_START_END_NULL = "select count(*) from ACT_HI_ACTINST RES where " +
														"RES.PROC_DEF_ID_ = #{processDefinitionId} and RES.ACT_ID_ = #{activityId}";
	
	@Override
	protected ProcessDefinitionWrapper getProcessData(String processDefinitionId, Date startDate, Date finishDate,
			Map<Color, List<String>> highLightedActivitiesMap, Map<String, String> counts) {
		ProcessDefinitionWrapper pde = repositoryService.getDeployedProcessDefinition( processDefinitionId );
	    List<ActivityWrapper> activities = pde.getActivities();
	    // iterate through all activities
	    for( ActivityWrapper activity : activities) {
	    	// get count of  executions for activities in the given process
    		NativeHistoricTaskInstanceQueryWrapper query = historyService.createNativeHistoricTaskInstanceQuery();
	    	long count; 
	    	
	    	if ( finishDate == null) {
	    		if (startDate == null) {	    			
 	    			count = query.sql(QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL_START_END_NULL)
							.parameter("processDefinitionId", processDefinitionId)
							.parameter("activityId", activity.getId())
							.count();
	    		} else {
	    			count = query.sql(QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL_END_NULL)
							.parameter("processDefinitionId", processDefinitionId)
							.parameter("activityId", activity.getId())
							.parameter("startDate", startDate)
							.count();	    			
	    		} 
	    	} else {
    			count = query.sql(QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL)
						.parameter("processDefinitionId", processDefinitionId)
						.parameter("activityId", activity.getId())
						.parameter("startDate", startDate)
						.parameter("endDate", finishDate)
						.count();
	    	}

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
		return pde;
	}

	public HistoryServiceWrapper getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryServiceWrapper historyService) {
		this.historyService = historyService;
	}

}
