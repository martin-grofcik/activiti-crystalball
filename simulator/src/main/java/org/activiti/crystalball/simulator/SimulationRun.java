package org.activiti.crystalball.simulator;

import java.util.Date;

/**
 * This is basic interface for SimRun implementation
 * it allows to execute simulation without break
 */
public interface SimulationRun {

  /**
   * executes simulation run according to configuration already set
   * @throws Exception
   */
  void execute() throws Exception;

}
