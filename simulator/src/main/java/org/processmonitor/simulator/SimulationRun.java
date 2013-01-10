package org.processmonitor.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.util.ClockUtil;

public class SimulationRun {
	
	private static Logger log = Logger.getLogger(ProcessEngineImpl.class.getName());
	
	/** Map for eventType -> event handlers to execute events on simulation engine */
	private Map<String, SimulationEventHandler> eventHandlerMap;
	
	/** context in which simulation is run */
	private SimulationContext context;
	
	/** 
	 * TODO: list 
	 * currently only usertask simulator is allowed 
	 */
	private Simulator userTaskSimulator;

	private List<HistoryEvaluator> historyEvaluators;
	
	public SimulationRun() {		
	}

	public SimulationRun(Simulator userTaskSimulator, SimulationContext context, Map<String, SimulationEventHandler> eventHandlerMap, List<HistoryEvaluator> historyEvaluators) {
		this.context = context;
		this.eventHandlerMap = eventHandlerMap;		
		this.userTaskSimulator = userTaskSimulator;
		this.historyEvaluators = historyEvaluators;
	}

	public List<SimulationResultEvent> execute(Date simDate, Date dueDate) {
		
		// run simulation
		// init context and task calendar and simulation time is set to current 
		ClockUtil.setCurrentTime( simDate);
		EventCalendar calendar = context.getEventCalendar();
		userTaskSimulator.init( calendar );

		while( !context.getEventCalendar().isEmpty() || ( dueDate != null && ClockUtil.getCurrentTime().before( dueDate ))) {
			execute(context.getEventCalendar().getFirstEvent());
			userTaskSimulator.simulate( context.getEventCalendar() );
		}
		
		return evaluate();
	}

	/** 
	 * evaluate sim. results
	 * @return
	 */
	private List<SimulationResultEvent> evaluate() {
		List<SimulationResultEvent> resultList = new ArrayList<SimulationResultEvent>();
		for ( HistoryEvaluator evaluator : historyEvaluators ) {
			evaluator.evaluate( context.getHistoryService(), resultList);
		}
		return resultList;
	}

	private void execute(SimulationEvent event) {
		// set simulation time to the next event for process engine too
		ClockUtil.setCurrentTime( new Date( event.getSimulationTime()));
		
		SimulationEventHandler handler = eventHandlerMap.get( event.getType() );
		if ( handler != null) 
			handler.handle( event, context);
		else 
			log.log(Level.WARNING, "Event type[{}] does not have any handler assigned.", event.getType());
	}
	
	public Map<String, SimulationEventHandler> getEventHandlerMap() {
		return eventHandlerMap;
	}

	public void setEventHandlerMap(
			Map<String, SimulationEventHandler> eventHandlerMap) {
		this.eventHandlerMap = eventHandlerMap;
	}

}
