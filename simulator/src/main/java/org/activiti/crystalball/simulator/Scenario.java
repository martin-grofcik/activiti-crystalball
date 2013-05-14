package org.activiti.crystalball.simulator;

import java.util.Date;


public class Scenario {
	protected String id;
	protected String name;
	protected String description;
	protected String author;
	
	/* scenario parameters */
	/** Start time of the scenario */
	protected Date start;
	/** End time of the scenario */ 
	protected Date end;
	/** Number of replication of that scenario that needs to be executed. Defaults to 1. */
	protected int replication = 1;
	/**
	 * A random seed to be used to initialize a pseudo random number generator.
	 * Given the exact same simulation experiment and a given seed, the  results should 
	 * be the same across executions.
	 */
	protected Long seed;

	protected SimulationRun simulationRun;
	
	/**
	 * utility to replicate process engine DB
	 */
	protected SimulationRunHelper dbReplicator;
	
	public void execute() throws Exception {
		
		// executes n-times simulation run
		for (int replicationCounter = 0; replicationCounter < replication; replicationCounter++) {
			dbReplicator.before(Integer.toString(replicationCounter));
			simulationRun.execute(start, end);
			dbReplicator.after(Integer.toString(replicationCounter));
		}		
	}
}
