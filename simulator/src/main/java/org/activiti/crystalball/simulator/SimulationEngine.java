package org.activiti.crystalball.simulator;


/**
 * Provides access to all the services that expose the Simulator operations.
 * 
 */
public interface SimulationEngine extends EngineServices {

  /** the version of the activiti library */
  public static String VERSION = "5.11";

  /** The name as specified in 'process-engine-name' in 
   * the activiti.cfg.xml configuration file.
   * The default name for a process engine is 'default */
  String getName();

  void close();
}
