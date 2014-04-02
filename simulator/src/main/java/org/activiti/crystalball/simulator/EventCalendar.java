package org.activiti.crystalball.simulator;

/**
 * This class provides access to simulation events
 */
public interface EventCalendar {
  boolean isEmpty();

  SimulationEvent peekFirstEvent();

  SimulationEvent removeFirstEvent();

  void addEvent(SimulationEvent event);

  void clear();
}
