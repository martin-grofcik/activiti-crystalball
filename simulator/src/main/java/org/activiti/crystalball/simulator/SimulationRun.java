package org.activiti.crystalball.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.crystalball.simulator.impl.AcquireJobNotificationEventHandler;
import org.activiti.crystalball.simulator.impl.NoopEventHandler;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationRun {
	
	private static Logger log = LoggerFactory.getLogger(SimulationRun.class.getName());
	
	/** Map for eventType -> event handlers to execute events on simulation engine */
	private Map<String, SimulationEventHandler> customEventHandlerMap;
	
	/** context in which simulation is run */
	private SimulationContext context;
	
	private List<HistoryEvaluator> historyEvaluators;

	/** simulation run event handlers - e.g. specific handlers for managing simulation time events*/
	private HashMap<String, SimulationEventHandler> eventHandlerMap;

	public SimulationRun() {		
	}

	public SimulationRun(SimulationContext context, Map<String, SimulationEventHandler> eventHandlerMap, List<HistoryEvaluator> historyEvaluators) {
		this.context = context;
		this.eventHandlerMap = new HashMap<String, SimulationEventHandler>();
		// init internal event handler map.
		eventHandlerMap.put( SimulationEvent.TYPE_END_SIMULATION, new NoopEventHandler() );
		this.customEventHandlerMap = eventHandlerMap;		
		this.historyEvaluators = historyEvaluators;
	}
	
	public SimulationRun( SimulationContext context, Map<String, SimulationEventHandler> customEventHandlerMap, List<HistoryEvaluator> historyEvaluators, JobExecutor jobExecutor) {
		this(context, customEventHandlerMap, historyEvaluators);
		// init internal event handler map.
		eventHandlerMap.put( SimulationEvent.TYPE_ACQUIRE_JOB_NOTIFICATION_EVENT, new AcquireJobNotificationEventHandler(jobExecutor) );
	}

	public List<SimulationResultEvent> execute(Date simDate, Date dueDate) {
		
		// run simulation
		// init context and task calendar and simulation time is set to current 
		ClockUtil.setCurrentTime( simDate);

		if (dueDate != null)
			context.getEventCalendar().addEvent(new SimulationEvent( dueDate.getTime(), SimulationEvent.TYPE_END_SIMULATION, null));

		initHandlers();
		
		SimulationEvent event = context.getEventCalendar().removeFirstEvent();
		if (event != null)
			ClockUtil.setCurrentTime( new Date(event.getSimulationTime()));
		
		while( !simulationEnd( dueDate, event) ) {
			
			execute( event );
			
			event = context.getEventCalendar().removeFirstEvent();
			if (event != null)
				ClockUtil.setCurrentTime( new Date( event.getSimulationTime()));
		}
		
		return evaluate();
	}

	private void initHandlers() {
		for( SimulationEventHandler handler : eventHandlerMap.values()) {
			handler.init(context);
		}

		for( SimulationEventHandler handler : customEventHandlerMap.values()) {
			handler.init(context);
		}
		
	}

	private static boolean simulationEnd(Date dueDate, SimulationEvent event) {
		if ( dueDate != null)
			return event == null || ( ClockUtil.getCurrentTime().after( dueDate ));
		return  event == null;
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
		log.info( "Simulation time:" + ClockUtil.getCurrentTime());

		// internal handlers first
		SimulationEventHandler handler = eventHandlerMap.get( event.getType() );
		if ( handler != null) {
			log.debug("Handling event of type[{}] internaly.", event.getType());

			handler.handle( event, context);
		} else {
			handler = customEventHandlerMap.get( event.getType() );
			if ( handler != null) {
				log.debug("Handling event of type[{}].", event.getType());
	
				handler.handle( event, context);
			} else 
				log.warn("Event type[{}] does not have any handler assigned.", event.getType());
		}
	}
	
	public Map<String, SimulationEventHandler> getEventHandlerMap() {
		return customEventHandlerMap;
	}

	public void setEventHandlerMap(
			Map<String, SimulationEventHandler> eventHandlerMap) {
		this.customEventHandlerMap = eventHandlerMap;
	}

}
