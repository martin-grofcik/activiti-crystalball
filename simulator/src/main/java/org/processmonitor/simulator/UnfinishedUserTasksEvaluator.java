package org.processmonitor.simulator;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;

public class UnfinishedUserTasksEvaluator implements HistoryEvaluator {

	public static String type = "unfinished_task";
	
	private RepositoryServiceImpl repositoryService;
	
	/* (non-Javadoc)
	 * @see org.processmonitor.simulator.HistoryEvaluator#evaluate(org.activiti.engine.HistoryService, java.util.List)
	 */
	@Override
	public void evaluate(HistoryService historyService,	List<SimulationResultEvent> resultList) {
		
		for (ProcessDefinition processDefinition : repositoryService.createProcessDefinitionQuery().active().list() ) {
			ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService.getDeployedProcessDefinition( processDefinition.getId());
			
			for (ActivityImpl activity : pde.getActivities()) {
				if ( activity.getProperty("type") != null && activity.getProperty("type") == "userTask" ) {
					
					long count = historyService.createHistoricTaskInstanceQuery()
									.processDefinitionKey( processDefinition.getKey())
									.taskDefinitionKey( activity.getId())
									.unfinished()
									.count();
					if ( count> 0) {
						resultList.add(new SimulationResultEvent( type, processDefinition.getKey(), activity.getId(), Long.toString( count)));
					}
				}
			}
		}
	}

	public RepositoryServiceImpl getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryServiceImpl repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Override
	public void setType(String type) {
		this.type = type;
		
	}

}
