package org.activiti.crystalball.simulator.runtime;

import org.activiti.crystalball.simulator.SimulationRun;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;

public abstract class SimulationInstanceImpl implements SimulationInstance {
	protected String name;
	protected String description;
	protected String author;
	
	protected int suspensionState = SuspensionState.ACTIVE.getStateCode();

	/* scenario parameters */
	/** Start time of the scenario */
	protected Date start;
	/** End time of the scenario */ 
	protected Date end;
	/** Number of replication of that scenario that needs to be executed. Defaults to 1. */
	protected int replication = 1;

	/**
	 * replication counter
	 */
	protected int replicationCounter = 0;

	protected Job job;
	
	/**
	 * A random seed to be used to initialize a pseudo random number generator.
	 * Given the exact same simulation experiment and a given seed, the  results should 
	 * be the same across executions.
	 */
	protected Long seed;

	protected String simulationConfigUrl;

	protected FactoryBean<SimulationRun> simulationRunFactory;

	@Override
	public boolean isEnded() {		
		return suspensionState == SuspensionState.FINISHED.getStateCode();
	}

	@Override
	public boolean isSuspended() {
		return suspensionState == SuspensionState.SUSPENDED.getStateCode();
	}

	@Override
	public String getSimulationConfigurationId() {
		return simulationConfigUrl;
	}
	
	public void start() {
		suspensionState = SuspensionState.ACTIVE.getStateCode();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getReplication() {
		return replication;
	}

	public void setReplication(int replication) {
		this.replication = replication;
	}

	public int getReplicationCounter() {
		return replicationCounter;
	}

	public void setReplicationCounter(int replicationCounter) {
		this.replicationCounter = replicationCounter;
	}

	public Long getSeed() {
		return seed;
	}

	public void setSeed(Long seed) {
		this.seed = seed;
	}

	public String getSimulationConfigUrl() {
		return simulationConfigUrl;
	}

	public void setSimulationConfigUrl(String simulationConfigUrl) {
		this.simulationConfigUrl = simulationConfigUrl;
	}

	public FactoryBean<SimulationRun> getSimulationRunFactory() {
		return simulationRunFactory;
	}

	public void setSimulationRunFactory(
			FactoryBean<SimulationRun> simulationRunFactory) {
		this.simulationRunFactory = simulationRunFactory;
	}
}
