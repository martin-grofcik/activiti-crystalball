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
package org.activiti.crystalball.simulator.evaluator;



import java.util.HashMap;
import java.util.Map;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationRunContext;
import org.activiti.crystalball.simulator.impl.context.SimulationContext;
import org.activiti.crystalball.simulator.impl.persistence.entity.SimulationRunEntity;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;

public class UnfinishedUserTasksEvaluator implements HistoryEvaluator {

	public String type = "unfinished_task";
		
	/* (non-Javadoc)
	 * @see org.activiti.crystalball.simulator.HistoryEvaluator#evaluate(org.activiti.engine.HistoryService, java.util.List)
	 */
	@Override
	public void evaluate(SimulationRunEntity simulationRun) {
		if (simulationRun != null) {
			for (ProcessDefinition processDefinition : SimulationRunContext.getRepositoryService().createProcessDefinitionQuery().active().list() ) {
				ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ((RepositoryServiceImpl)SimulationRunContext.getRepositoryService())
																		.getDeployedProcessDefinition( processDefinition.getId());
				
				for (ActivityImpl activity : pde.getActivities()) {
					if ( activity.getProperty("type") != null && activity.getProperty("type") == "userTask" ) {
						
						long count = SimulationRunContext.getHistoryService().createHistoricTaskInstanceQuery()
										.processDefinitionKey( processDefinition.getKey())
										.taskDefinitionKey( activity.getId())
										.unfinished()
										.count();
						if ( count> 0) {
							RuntimeService runtimeService = SimulationContext.getSimulationEngineConfiguration().getRuntimeService();
							Map<String, Object> resultVariables = new HashMap<String, Object>();
							resultVariables.put("processDefinitionKey", processDefinition.getKey());
							resultVariables.put("taskDefinitionKey", activity.getId());
							resultVariables.put( "description", Long.toString( count));
							runtimeService.saveResult(type, resultVariables);
						}
					}
				}
			}
		}
	}

	@Override
	public void setType(String type) {
		this.type = type;
		
	}

}
