package org.activiti.crystalball.simulator.impl.cmd;

import java.util.Map;

import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;

public class SaveResultCmd implements Command<ResultEntity> {
	
	protected String type = null;
	protected Map<String, Object> variables;
	
	public SaveResultCmd(String type, Map<String, Object> variables) {
		this.type = type;
		this.variables = variables;
	}

	@Override
	public ResultEntity execute(CommandContext commandContext) {
		ResultEntity resultEntity = new ResultEntity();
		resultEntity.setId(null);
		if (SimulationContext.getSimulationRun() != null)
			resultEntity.setRunId(SimulationContext.getSimulationRun().getId());
		resultEntity.setType(type);
		commandContext.getResultEntityManager().insert( resultEntity);
		if (variables != null) {
			resultEntity.setVariables(variables);			
		}
		return resultEntity;
	}

}
