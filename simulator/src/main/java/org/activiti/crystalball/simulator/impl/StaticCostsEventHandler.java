package org.activiti.crystalball.simulator.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.engine.impl.util.ClockUtil;

public class StaticCostsEventHandler implements SimulationEventHandler {

	protected String costsId;
	/** value for costs */
	protected String costsValue;
	/** period in milisec */
	protected int period;	
	/** event type on which Handler is listening to log new costs*/
	private String eventType;
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public void init() {
		scheduleNextCosts();			
	}
	
	@Override
	public void handle(SimulationEvent event) {
		// schedule new process instance start now
		scheduleNextCosts();
		// log costs result
		
		RuntimeService runtimeService = SimulationContext.getSimulationEngineConfiguration().getRuntimeService();
		
		Map<String, Object> resultVariables = new HashMap<String, Object>();
		resultVariables.put( "description", costsValue);
		runtimeService.saveResult("costId", resultVariables);	
				
	}	
	
	protected void scheduleNextCosts() {
		long simulationTime = ClockUtil.getCurrentTime().getTime();
		
		SimulationEvent costEvent = new SimulationEvent( simulationTime + period, eventType, null);
		// add cost event
		SimulationRunContext.getEventCalendar().addEvent( costEvent);
	}
	
	public String getCostsId() {
		return costsId;
	}
	public void setCostsId(String costsId) {
		this.costsId = costsId;
	}
	public String getCostsValue() {
		return costsValue;
	}
	public void setCostsValue(String costsValue) {
		this.costsValue = costsValue;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}	
}
