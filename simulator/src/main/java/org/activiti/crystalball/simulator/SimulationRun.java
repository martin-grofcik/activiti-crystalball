package org.activiti.crystalball.simulator;

import java.util.Date;

/**
 * This class...
 */
public interface SimulationRun {
    void execute(Date simDate, Date dueDate) throws Exception;
}
