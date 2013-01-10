package org.processmonitor.simulator;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

/**
 * Context in which simulation is run.
 * It contains references to process engine, user simulator.
 *
 */
public class SimulationContext {
    
	//
	//  Process engine on which simulation will be executed
	//
    private RuntimeService runtimeService;
    private TaskService taskService;
    private HistoryService historyService;

    //
    // Simulation objects
    //
    private EventCalendar eventCalendar;
    
    public SimulationContext() {
    	eventCalendar = new EventCalendar( new SimulationEventComparator());
    }
    
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public EventCalendar getEventCalendar() {
		return eventCalendar;
	}

	public void setEventCalendar(EventCalendar eventCalendar) {
		this.eventCalendar = eventCalendar;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
}
