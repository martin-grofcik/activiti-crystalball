package org.activiti.crystalball.generator;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.NativeHistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DueDateGraphGenerator extends AbstractGraphGenerator {

	protected static Logger log = LoggerFactory.getLogger(DueDateGraphGenerator.class);
	HistoryService historyService;
	
	/** query for selecting all task instances (completed or not) in given time interval for given ProcessDefinitionId and */
	private String QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL = "select count(*) from ACT_HI_TASKINST RES where " +
														"RES.PROC_DEF_ID_ = #{processDefinitionId} and RES.TASK_DEF_KEY_ = #{activityId}" +
														"and (( RES.END_TIME_ > #{startDate}) or (RES.END_TIME_ = null)) " +
														"and (RES.START_TIME_ < #{endDate}) ";
	private String COUNT_AFTER_DUE_HISTORIC_TASK_INSTANCES_FROM_INTERVAL = QUERY_HISTORIC_TASK_INSTANCES_FROM_INTERVAL +
    		"and (RES.DUE_DATE_ is not null) and ((RES.DUE_DATE_ < #{endDate})) " +
    		"and (RES.END_TIME_ is not null) and (RES.END_TIME_ > RES.DUE_DATE_)";
	
	@Override
	protected ProcessDefinitionEntity getProcessData(String processDefinitionId, Date startDate, Date finishDate,
			 Map<Color,List<String>> highLightedActivitiesMap, Map<String, String> counts) {
		
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    List<ActivityImpl> activities = pde.getActivities();
	    // iterate through all activities
	    for( ActivityImpl activity : activities) {
	    	// get count of  executions for userTask in the given process
	    	if (activity.getProperty("type") == "userTask") {
	    		

	    		NativeHistoricTaskInstanceQuery query = historyService.createNativeHistoricTaskInstanceQuery();
	    		
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

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

}
