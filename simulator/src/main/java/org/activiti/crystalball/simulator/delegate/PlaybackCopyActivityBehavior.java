package org.activiti.crystalball.simulator.delegate;

/*
 * #%L
 * simulator
 * %%
 * Copyright (C) 2012 - 2013 crystalball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.activiti.crystalball.simulator.impl.PlaybackStartProcessEventHandler;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copy updated variables from the playback and take the first transition. 
 *
 */
public class PlaybackCopyActivityBehavior extends AbstractSimulationActivityBehavior {

	public static final String PLAYBACK_HISTORY_SERVICE = "_playback.historyService";
	
	public PlaybackCopyActivityBehavior(ScopeImpl scope, ActivityImpl activity) {
		super(scope, activity);
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
