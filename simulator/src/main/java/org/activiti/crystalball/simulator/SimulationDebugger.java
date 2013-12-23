package org.activiti.crystalball.simulator;

/**
 * Allows to run simulation in debug mode
 */
public interface SimulationDebugger {
  /**
   * initialize simulation run
   */
  void init();

  /**
   * step one simulation event forward
   */
  void step();

  /**
   * run simulation
   */
  void run();

  /**
   * close simulation run
   */
  void close();

}
