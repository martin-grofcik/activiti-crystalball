package org.activiti.crystalball.simulator;

import java.util.List;

import org.activiti.engine.HistoryService;

public interface HistoryEvaluator {

	/**
	 * set evaluator type (each event will have this type)
	 * @param type
	 */
	public void setType( String type);
	
	/**
	 * evaluate simulation results from history service
	 * 
	 * @param historyService
	 * @param resultList
	 */
	public void evaluate(HistoryService historyService,
			List<SimulationResultEvent> resultList);

}