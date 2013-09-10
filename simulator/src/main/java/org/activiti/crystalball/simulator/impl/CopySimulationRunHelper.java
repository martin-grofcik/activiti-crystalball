package org.activiti.crystalball.simulator.impl;

import org.activiti.crystalball.simulator.SimulationRunHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * copy/delete DB file after or before simulation run
 * 
 */
public class CopySimulationRunHelper implements SimulationRunHelper {

	protected String liveDBFileName;
	protected String simulationRunDBFileName;

	public CopySimulationRunHelper(String liveDBFileName, String simulationRunDBFileName) {
		this.liveDBFileName = liveDBFileName;
		this.simulationRunDBFileName =simulationRunDBFileName;

	}
	@Override
	public void before(String configuration) throws IOException {
		FileUtils.copyFile(new File(liveDBFileName), new File( simulationRunDBFileName));
	}

	@Override
	public void after(String configuration) {
		File db = new File( simulationRunDBFileName);
		db.delete();
	}

}
