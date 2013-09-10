package org.activiti.crystalball.simulator.evaluator;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Did process instance reach given activity in the process execution or not?  
 *
 */
public class ActivityExistsEvaluator implements HistoryEvaluator {

	protected String type = "activity_exists";

	protected String processDefinitionId;

	protected String activityId;
	
	
	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void evaluate(SimulationRunEntity simulationRun) {
		if (simulationRun != null) {
			long count = SimulationRunContext.getHistoryService().createHistoricActivityInstanceQuery()
										.processDefinitionId( processDefinitionId)
										.activityId(activityId)
										.count();
			if ( count> 0) {
				RuntimeService runtimeService = SimulationContext.getSimulationEngineConfiguration().getRuntimeService();
				Map<String, Object> resultVariables = new HashMap<String, Object>();
				resultVariables.put("processDefinitionKey", processDefinitionId);
				resultVariables.put("taskDefinitionKey", activityId);
				resultVariables.put( "description", Long.toString( count));
				runtimeService.saveResult(type, resultVariables);
			}
		}
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
}