package org.activiti.crystalball.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationRun {
	
	private static Logger log = LoggerFactory.getLogger(SimulationRun.class.getName());
	
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
		
		initHandlers();
		
		userTaskSimulator.init( calendar );

		while( !simulationEnd( dueDate)) {
			log.debug( "Simulation time:" + ClockUtil.getCurrentTime());
			
			execute(context.getEventCalendar().getFirstEvent());
			// after each event execution, users have to simulate their work.
			userTaskSimulator.simulate( context.getEventCalendar() );

			log.debug( "Status:" +getStatus());
		}
		
		return evaluate();
	}

	private String getStatus() {
		return "Active process instances :" +context.getRuntimeService().createProcessInstanceQuery().active().count() +
		" \n Unassigned tasks :" + context.getTaskService().createTaskQuery().taskUnassigned().count();
	}

	private void initHandlers() {
		for( SimulationEventHandler handler : eventHandlerMap.values()) {
			handler.init(context);
		}
		
	}

	private boolean simulationEnd(Date dueDate) {
		if ( dueDate != null)
			return context.getEventCalendar().isEmpty() || ( ClockUtil.getCurrentTime().after( dueDate ));
		return  context.getEventCalendar().isEmpty();
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
		if ( handler != null) {
			log.debug("Handling event of type[{}].", event.getType());

			handler.handle( event, context);
		} else 
			log.warn("Event type[{}] does not have any handler assigned.", event.getType());
	}
	
	public Map<String, SimulationEventHandler> getEventHandlerMap() {
		return eventHandlerMap;
	}

	public void setEventHandlerMap(
			Map<String, SimulationEventHandler> eventHandlerMap) {
		this.eventHandlerMap = eventHandlerMap;
	}

}
