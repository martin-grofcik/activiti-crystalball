package org.activiti.crystalball.simulator.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.crystalball.simulator.SimulationContext;
import org.activiti.crystalball.simulator.SimulationEvent;
import org.activiti.crystalball.simulator.SimulationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * event handler schedules processinstances to start according to history which is played back 
 * generates load which was the same as the already started process instances generated 
 * in the past.
 *
 */
public class PlaybackScheduleStartProcessEventHandler implements SimulationEventHandler {

	private static final String PROCESS_INSTANCE_ID = "_playback.processInstanceId";

	private static Logger log = LoggerFactory.getLogger(PlaybackScheduleStartProcessEventHandler.class);
	
	/** process to start key - this process will be play backed.*/
	private String processToPlaybackKey;

	/** event type on which Handler is listening to start new process */
	private String eventType;
	
	/** event type to schedule when new process should be started */
	private String eventTypeToSchedule;

	/** history from which process starts will be played */
	private HistoryService historyService;
	
	/** date from which playback will start */
	private Date playBackStart;
	
	/** date at which playback will end */
	private Date playBackEnd;

	/** when simulation starts*/
	private Date simulationRunStart;

	/** repeat playback forever flag */
	private boolean repeatPlayback = true; 
	
	/** delta in milisecond - limit bunch of processes to be scheduled in one schedule event*/  
	private int delta = 1000;
	
	@Override
	public void init(SimulationContext context) {
		// initialise simulation start
		simulationRunStart = ClockUtil.getCurrentTime();
		
		// determine when to start new process
		List<HistoricProcessInstance> processInstances = getPlaybackProcessInstances( playBackStart, context);
		
		if (!processInstances.isEmpty()) {		
			// schedule new process instance start now
			for (HistoricProcessInstance processInstance : processInstances )
				scheduleNextProcessStart(context, simulationRunStart.getTime() + getSimulationTimeDelta(processInstance.getStartTime()), processInstance.getId());
		}
		
	}


	/**
	 * provide simulation time delta at which process instance has to be started
	 * @param startTime - time when new process instance was started
	 * @return
	 */
	private long getSimulationTimeDelta(Date startTime) {
		long timeDelta = startTime.getTime() - playBackStart.getTime();
		long playBackTime = playBackEnd.getTime() - playBackStart.getTime();  
		long simulationTimeDelta = ClockUtil.getCurrentTime().getTime() - simulationRunStart.getTime();
		long playbackRepeated = 0;
		if ( repeatPlayback )
			playbackRepeated =  simulationTimeDelta/ playBackTime;
		
		return playbackRepeated * playBackTime + timeDelta;
	}

	/**
	 * schedule next process start - take history and choose next process from play back interval
	 * @param context
	 * @param simulationTime
	 * @param processInstanceId 
	 */
	private void scheduleNextProcessStart(SimulationContext context,
			long simulationTime, String processInstanceId) {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put(PROCESS_INSTANCE_ID, processInstanceId);
		
		SimulationEvent completeEvent = new SimulationEvent( simulationTime, eventTypeToSchedule, properties);
		// add start process event
		context.getEventCalendar().addEvent( completeEvent);
		log.debug("Scheduling new process start simtime [" + simulationTime + "] properties["+properties+"]");
	}

	@Override
	public void handle(SimulationEvent event, SimulationContext context) {
		//
		// determine next process instance start
		//
		
		// transform simulation time to the playback interval
		long playBackTime = playBackEnd.getTime() - playBackStart.getTime();  
		long simulationTimeDelta = ClockUtil.getCurrentTime().getTime() - simulationRunStart.getTime();
		long playbackRepeated = simulationTimeDelta/ playBackTime;
		// only for performance reasons
		if (!repeatPlayback && playbackRepeated > 0)
			return; 
		long timeDelta = simulationTimeDelta - playbackRepeated * playBackTime ;
		Date playbackPositionDate = new Date( playBackStart.getTime() + timeDelta ); 
		
		// determine new process instances to start from playback
		List<HistoricProcessInstance> processInstances = getPlaybackProcessInstances(playbackPositionDate, context);
					
		// schedule new process instance start now
		if (!processInstances.isEmpty())
			for (HistoricProcessInstance processInstance : processInstances)
				scheduleNextProcessStart(context, simulationRunStart.getTime() + playbackRepeated * playBackTime + (processInstance.getStartTime().getTime() - playBackStart.getTime() ), processInstance.getId() );

	}

	/**
	 * get process instances to schedule their start an schedule new schedule simulation event
	 * 
	 * @param playBackPosition
	 * @param context
	 * @return
	 */
	protected List<HistoricProcessInstance> getPlaybackProcessInstances( Date playBackPosition, SimulationContext context ) {
		// set end time for the query
		Date end = playBackEnd;
		Calendar c = Calendar.getInstance();
		c.setTime(playBackPosition);
		c.add(Calendar.MILLISECOND, delta);		
		if ( end.after( c.getTime() )) {
			end = c.getTime();
			// add schedule process event after selected process instances
			SimulationEvent scheduleEvent = new SimulationEvent( ClockUtil.getCurrentTime().getTime() + delta + 1, eventType, null);
			context.getEventCalendar().addEvent( scheduleEvent);
		} else {
			long playBackTime = playBackEnd.getTime() - playBackStart.getTime();  
			long simulationTimeDelta = ClockUtil.getCurrentTime().getTime() - simulationRunStart.getTime();
			long playbackRepeated = simulationTimeDelta/ playBackTime +1 ;
			
			SimulationEvent scheduleEvent = new SimulationEvent( simulationRunStart.getTime()+ playBackTime * playbackRepeated + 1, eventType, null);
			context.getEventCalendar().addEvent( scheduleEvent);			
		}
		
		// determine process instances to be played back
		List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processToPlaybackKey)
				.startedAfter( playBackPosition ).startedBefore(end )
				.orderByProcessInstanceStartTime().asc()
				.list();
		
		
		return processInstances;
	}
	public String getProcessToPlaybackKey() {
		return processToPlaybackKey;
	}

	public void setProcessToPlaybackKey(String processToStartKey) {
		this.processToPlaybackKey = processToStartKey;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String event_type) {
		this.eventType = event_type;
	}


	public HistoryService getHistoryService() {
		return historyService;
	}


	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}


	public Date getPlayBackStart() {
		return playBackStart;
	}


	public void setPlayBackStart(Date playBackStart) {
		this.playBackStart = playBackStart;
	}


	public Date getPlayBackEnd() {
		return playBackEnd;
	}


	public void setPlayBackEnd(Date playBackEnd) {
		this.playBackEnd = playBackEnd;
	}


	public boolean isRepeatPlayback() {
		return repeatPlayback;
	}


	public void setRepeatPlayback(boolean repeatPlayback) {
		this.repeatPlayback = repeatPlayback;
	}

	public int getDelta() {
		return delta;
	}


	public void setDelta(int delta) {
		this.delta = delta;
	}


	public String getEventTypeToSchedule() {
		return eventTypeToSchedule;
	}


	public void setEventTypeToSchedule(String eventTypeToSchedule) {
		this.eventTypeToSchedule = eventTypeToSchedule;
	}

}
