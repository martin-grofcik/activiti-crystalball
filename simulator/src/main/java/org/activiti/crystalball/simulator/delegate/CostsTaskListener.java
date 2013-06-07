package org.activiti.crystalball.simulator.delegate;

import org.activiti.crystalball.simulator.impl.cmd.StoreResultCmd;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CostsTaskListener implements TaskListener, ExecutionListener {

	private static Logger log = LoggerFactory.getLogger(CostsTaskListener.class);

	public static final String COSTS_ID = "costsId";	
	public static final String COSTS_VALUE = "costsValue";
	
	protected Expression costsId;
	protected Expression costsValue;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		try {
			notify(delegateTask.getExecution());
		} catch (Exception e) {
			log.error("costs  listener error", e);
		}
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// log result only in the simulation context
		if (SimulationContext.getSimulationRun() != null) {
			// log costs 		
			SimulationContext.getSimulationEngineConfiguration().getCommandExecutorTxRequired()
			.execute( new StoreResultCmd( SimulationContext.getSimulationRun() , (String) costsId.getValue(execution), execution.getProcessDefinitionId(), execution.getCurrentActivityId(), (String) costsValue.getValue(execution)) );
		}
	}

	public Expression getCostsId() {
		return costsId;
	}

	public void setCostsId(Expression costsId) {
		this.costsId = costsId;
	}

	public Expression getCostsValue() {
		return costsValue;
	}

	public void setCostsValue(Expression costsValue) {
		this.costsValue = costsValue;
	}

}
