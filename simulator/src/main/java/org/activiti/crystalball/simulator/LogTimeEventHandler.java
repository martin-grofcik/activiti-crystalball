package org.activiti.crystalball.simulator;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.impl.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log time information. To see that something is happening during long simulation runs. 
 *
 */
public class LogTimeEventHandler implements SimulationEventHandler {

	private static Logger log = LoggerFactory.getLogger(LogTimeEventHandler.class);
	
	protected long simulationStart;
	protected String type;
	protected int logDelta;
	
	public LogTimeEventHandler(String type, int logDelta) {
		this.type = type;
		this.logDelta = logDelta;
	}
	
	@Override
	public void init(SimulationContext context) {
		simulationStart = System.currentTimeMillis();
		context.getEventCalendar().addEvent( new SimulationEvent( ClockUtil.getCurrentTime().getTime(), type, null));
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		Date simulationTime = new Date( event.getSimulationTime());
		log.info("SimExec {} sec, Simulation time [{}]", (System.currentTimeMillis() - simulationStart)/1000f, simulationTime);
		
		Calendar c = Calendar.getInstance();
		c.setTime(simulationTime);
		c.add( Calendar.MINUTE, logDelta);
		context.getEventCalendar().addEvent( new SimulationEvent( c.getTimeInMillis(), type, null));
	}

}
