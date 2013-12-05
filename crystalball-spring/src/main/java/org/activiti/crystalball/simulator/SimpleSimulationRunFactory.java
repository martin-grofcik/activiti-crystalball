package org.activiti.crystalball.simulator;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.springframework.beans.factory.FactoryBean;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SimpleSimulationRunFactory implements FactoryBean<SimulationRun> {

    protected Map<String, SimulationEventHandler> customEventHandlerMap;
    protected HashMap<String, SimulationEventHandler> eventHandlerMap;
    protected FactoryBean<ProcessEngine> processEngineFactory;
    protected FactoryBean<EventCalendar> eventCalendarFactory;
    private JobExecutor jobExecutor;

    public SimpleSimulationRunFactory() {
	}
	
	@Override
	public SimulationRun getObject() throws Exception {
		return new SimpleSimulationRun.Builder().
            customEventHandlerMap(customEventHandlerMap).
//            eventCalendarFactory(eventCalendarFactory).
//            processEngineFactory(processEngineFactory).
//            eventCalendarFactory(eventCalendarFactory).
            jobExecutor(jobExecutor).
            build();
	}

	@Override
	public Class<? extends SimulationRun> getObjectType() {
		return SimpleSimulationRun.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

    public void setCustomEventHandlerMap(Map<String, SimulationEventHandler> customEventHandlerMap) {
        this.customEventHandlerMap = customEventHandlerMap;
    }

    public void setEventHandlerMap(HashMap<String, SimulationEventHandler> eventHandlerMap) {
        this.eventHandlerMap = eventHandlerMap;
    }

    public void setProcessEngineFactory(FactoryBean<ProcessEngine> processEngineFactory) {
        this.processEngineFactory = processEngineFactory;
    }

    public void setEventCalendarFactory(FactoryBean<EventCalendar> eventCalendarFactory) {
        this.eventCalendarFactory = eventCalendarFactory;
    }

    public void setJobExecutor(JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
}
