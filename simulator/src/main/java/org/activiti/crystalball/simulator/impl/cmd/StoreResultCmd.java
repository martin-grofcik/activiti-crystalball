package org.activiti.crystalball.simulator.impl.cmd;

import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;

public class StoreResultCmd implements Command<ResultEntity> {
	
	protected String simulationRunId = null;
	protected String type;
	protected String processDefinitionKey;
	protected String taskDefinitionKey;
	protected String description;
	
	
	public StoreResultCmd(SimulationRunEntity simulationRun, String type,
			String processKey, String taskDefinitionKey, String description) {
		if (simulationRun != null) {
			simulationRunId = simulationRun.getId();
		}
		this.type = type;
		this.processDefinitionKey = processKey;
		this.taskDefinitionKey = taskDefinitionKey;
		this.description = description;
	}

	@Override
	public ResultEntity execute(CommandContext commandContext) {
		ResultEntity result = new ResultEntity(simulationRunId, type, processDefinitionKey, taskDefinitionKey, description);
		
		commandContext.getResultEntityManager().insert( result);
		return result;
	}

}
