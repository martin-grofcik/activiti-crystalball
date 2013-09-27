package org.activiti.crystalball.simulator.delegate;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.engine.delegate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


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
			RuntimeService runtimeService = SimulationContext.getSimulationEngineConfiguration().getRuntimeService();
			Map<String, Object> resultVariables = new HashMap<String, Object>();
			resultVariables.put("processDefinitionKey", execution.getProcessDefinitionId());
			resultVariables.put("taskDefinitionKey", execution.getCurrentActivityId());
			resultVariables.put( "description", (String) costsValue.getValue(execution));
			runtimeService.saveResult((String) costsId.getValue(execution), resultVariables);
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
