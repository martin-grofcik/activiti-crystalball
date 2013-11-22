package org.activiti.crystalball.simulator;

/**
 * This class...
 */
public interface EventCalendar {
    boolean isEmpty();

    SimulationEvent removeFirstEvent();

    void addEvent(SimulationEvent event);
}
