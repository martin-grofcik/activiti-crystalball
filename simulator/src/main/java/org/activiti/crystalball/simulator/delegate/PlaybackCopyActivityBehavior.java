package org.activiti.crystalball.simulator.delegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.crystalball.simulator.impl.PlaybackStartProcessEventHandler;

/**
 * Copy updated variables from the playback and take the first transition. 
 *
 */
public class PlaybackCopyActivityBehavior extends AbstractSimulationActivityBehavior {

	public static final String PLAYBACK_HISTORY_SERVICE = "_playback.historyService";
	
	public PlaybackCopyActivityBehavior(Element scriptTaskElement, ScopeImpl scope,
			ActivityImpl activity) {
		super(scriptTaskElement, scope, activity);
	}

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		String processInstanceId = (String) execution.getVariable(PlaybackStartProcessEventHandler.PROCESS_INSTANCE_ID);
	    HistoryService historyService = (HistoryService) Context.getProcessEngineConfiguration().getBeans().get(PLAYBACK_HISTORY_SERVICE);
		// get process variables for the current  activityId
		HistoricActivityInstance activityInstance = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.activityId( execution.getCurrentActivityId() )
				.singleResult();
		List<HistoricDetail> details = historyService.createHistoricDetailQuery()
				.processInstanceId( processInstanceId )
				.activityInstanceId( activityInstance.getId())
				.variableUpdates()
				.list();
		
		// fulfill variables
		Map<String, Object> variables = new HashMap<String,Object>();		
		for ( HistoricDetail detail : details) {
			variables.put( ((HistoricVariableUpdate) detail).getVariableName(), ((HistoricVariableUpdate) detail).getValue());			
		}
	    execution.setVariables(variables);

	    PvmTransition transition = execution.getActivity().getOutgoingTransitions().get(0);
	    execution.take(transition);
	}

}
