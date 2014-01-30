package org.activiti.crystalball.simulator;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.util.ClockUtil;

import java.util.Map;

/**
 * This class provides simulation run for replay purposes
 * replay uses real time and running engine to execute simulation events.
 */
public class ReplaySimulationRun extends AbstractSimulationRun {

  private final EventCalendar eventCalendar;

  public ReplaySimulationRun(ProcessEngine processEngine, Map<String, SimulationEventHandler> customEventHandlerMap) {
    this(processEngine, new SimpleEventCalendar(new SimulationEventComparator()), customEventHandlerMap);
  }

  public ReplaySimulationRun(ProcessEngine processEngine, EventCalendar eventCalendar, Map<String, SimulationEventHandler> customEventHandlerMap) {
    super(customEventHandlerMap);
    this.processEngine = processEngine;
    this.eventCalendar = eventCalendar;
  }

  @Override
  protected void initSimulationRunContext() {
    SimulationRunContext.setEventCalendar(eventCalendar);
    SimulationRunContext.setProcessEngine(processEngine);
  }

  /**
   * simulation does not end - it can live forever.
   * @param event - is it end of the simulation run?
   * @return false
   */
  @Override
  protected boolean simulationEnd(SimulationEvent event) {
    return false;
  }

  /**
   * do not affect existing engine
   */
  @Override
  public void close() {
    SimulationRunContext.getEventCalendar().clear();
    SimulationRunContext.removeEventCalendar();
    SimulationRunContext.removeProcessEngine();
  }
}
