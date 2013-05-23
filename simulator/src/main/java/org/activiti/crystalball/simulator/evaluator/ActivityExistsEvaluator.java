package org.activiti.crystalball.simulator.evaluator;

import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.crystalball.simulator.impl.cmd.StoreResultCmd;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;

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
				org.activiti.crystalball.simulator.impl.context.SimulationContext.
				getSimulationEngineConfiguration().getCommandExecutorTxRequired().execute( new StoreResultCmd( simulationRun, type, processDefinitionId, activityId, Long.toString( count)) );
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