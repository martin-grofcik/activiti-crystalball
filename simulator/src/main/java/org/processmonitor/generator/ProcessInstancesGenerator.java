package org.processmonitor.generator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstancesGenerator extends AbstractGraphGenerator {

	protected static Logger log = LoggerFactory.getLogger(ProcessInstancesGenerator.class);
	RuntimeService runtimeService;
	
	@Override
	protected ProcessDefinitionEntity getProcessData(String processDefinitionId, Date startDate, Date finishDate,
			List<String> highLightedActivities, Map<String, String> counts) {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ( ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition( processDefinitionId ));
	    List<ActivityImpl> activities = pde.getActivities();
	    // iterate through all activities
	    for( ActivityImpl activity : activities) {
	    	// get count of  executions for userTask in the given process
	    	if (activity.getProperty("type") == "userTask") {
		    	long count = runtimeService.createExecutionQuery().processDefinitionId(processDefinitionId).activityId( activity.getId() ).count();
		    	if ( count > 0 ) {
		    		// store count in the diagram generator data structures
		    		if ( count > limit )
		    			highLightedActivities.add(activity.getId());
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
