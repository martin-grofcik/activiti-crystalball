package org.activiti.crystalball.simulator.impl.cmd;

import org.activiti.crystalball.simulator.impl.interceptor.Command;
import org.activiti.crystalball.simulator.impl.interceptor.CommandContext;

public class IsRunningSimulationInstanceCmd<T> implements Command<Boolean> {

	protected String simulationInstanceId;
	
	public IsRunningSimulationInstanceCmd(String simulationInstanceId) {
		this.simulationInstanceId = simulationInstanceId;
	}
	
	@Override
	public Boolean execute(CommandContext commandContext) {
		long count = commandContext.getJobManager().findJobCountBySimulationInstanceId(simulationInstanceId);
		return count > 0;
	}

}
