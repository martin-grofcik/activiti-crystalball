package org.activiti.crystalball.diagram.generator;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.crystalball.simulator.RuntimeService;
import org.activiti.crystalball.simulator.SimulationRun;
import org.activiti.crystalball.simulator.SimulationRunHelper;
import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;
import org.activiti.crystalball.simulator.runtime.SimulationInstance;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProbabilityGraphGenerator extends SimulationResultsGraphGenerator {

	
	public void generateGraph( RuntimeService runtimeService, SimulationInstance simulationInstance, String processDefinitionId, String processDefinitionKey, String resultType, String fileName) throws Exception {
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				simulationInstance.getSimulationConfigurationId());
		PropertyPlaceholderConfigurer propConfig = new PropertyPlaceholderConfigurer();
		Properties properties = new Properties();
		String configuration = "report-" + Thread.currentThread().getId();
		properties.put("simulationRunId", configuration);
		propConfig.setProperties(properties);
		appContext.addBeanFactoryPostProcessor(propConfig);
		appContext.refresh();

		SimulationRunHelper runHelper = (SimulationRunHelper) appContext
				.getBean("simulationRunHelper");

		try {
			runHelper.before(configuration);

			SimulationRun simRun = (SimulationRun) appContext
					.getBean("simulationRun");

			ProcessEngine procEngine = simRun.getProcessEngine();

			this.setRepositoryService(procEngine.getRepositoryService());

			Map<Color, List<String>> highlighActivitiesMap = new HashMap<Color, List<String>>();
			Map<String, String> counts = new HashMap<String, String>();

			processResults(runtimeService, simulationInstance,
					processDefinitionId, resultType, highlighActivitiesMap,
					counts);

			reportGraph(fileName, processDefinitionKey, highlighActivitiesMap,
					counts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			runHelper.after(configuration);
			appContext.close();
		}

	}
	
	protected void processResults(RuntimeService runtimeService,
			SimulationInstance simulationInstance, String processDefinitionId,
			String resultType, Map<Color, List<String>> highlighActivitiesMap,
			Map<String, String> counts) {
		Long count = runtimeService.createResultQuery()
				.resultProcessDefinitionKey(processDefinitionId)
				.simulationInstanceId(simulationInstance.getId())
				.resultType(resultType)
				.count();
		List<ResultEntity> resultList =runtimeService.createResultQuery()
				.resultProcessDefinitionKey(processDefinitionId)
				.simulationInstanceId(simulationInstance.getId())
				.resultType(resultType)
				.list();

		if ( count != 0) {
			
			long percentage = (long) (((float)count / (float) simulationInstance.getReplication()) * 100);
			
			
			ResultEntity r = resultList.get(0);
			Color c = getColorToHighlight(Float.parseFloat((String)r.getVariable("description")));
			addToHighlighted(highlighActivitiesMap, c, (String) r.getVariable("taskDefinitionKey"));
			counts.put((String) r.getVariable("taskDefinitionKey"), percentage + "%");
		}
	}


}
