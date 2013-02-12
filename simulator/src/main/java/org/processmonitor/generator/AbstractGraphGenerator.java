package org.processmonitor.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.processmonitor.activiti.diagram.BasicProcessDiagramGenerator;
import org.processmonitor.activiti.diagram.HighlightNodeDiagramLayer;
import org.processmonitor.activiti.diagram.MergeLayersGenerator;
import org.processmonitor.activiti.diagram.WriteNodeDescriptionDiagramLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGraphGenerator {

	protected static Logger log = LoggerFactory
			.getLogger(AbstractGraphGenerator.class);

	RepositoryService repositoryService;
	/**
	 * limit for highlighting node - do not highlight counts below the limit
	 * default value is 0
	 */
	long limit = 0;

	public AbstractGraphGenerator() {
		super();
	}

	/**
	 * evaluate process data and prepare items for highlighting and displaying
	 * counts
	 * 
	 * @param processDefinitionId
	 * @param finishDate 
	 * @param startDate 
	 * @param highLightedActivities
	 * @param counts
	 * @return
	 */
	protected abstract ProcessDefinitionEntity getProcessData(
			String processDefinitionId, Date startDate, Date finishDate, List<String> highLightedActivities,
			Map<String, String> counts);

	/**
	 * Generate report about executions for the process instances driven by
	 * processDefinitionId. Report contains counts of tokens and node
	 * highlighting where tokens are located
	 * 
	 * @param processDefinitionId
	 * @param finishDate 
	 * @param startDate 
	 * @param fileName
	 * @throws IOException
	 */
	public void generateReport(String processDefinitionId, Date startDate, Date finishDate, String fileName)
			throws IOException {
		log.debug(" generating report");

		List<String> highLightedActivities = new ArrayList<String>();
		Map<String, String> counts = new HashMap<String, String>();

		ProcessDefinitionEntity pde = getProcessData(processDefinitionId, startDate, finishDate,
				highLightedActivities, counts);

		reportGraph(fileName, pde.getKey(), highLightedActivities, counts);
		log.debug(" generating report done");
	}

	private void reportGraph(String fileName, String processDefinitionKey,
			List<String> highLightedActivities, Map<String, String> counts)
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processDefinitionId", processDefinitionKey);

		Map<String, Object> highlightParams = new HashMap<String, Object>();
		highlightParams.put("processDefinitionId", processDefinitionKey);
		highlightParams.put("highLightedActivities", highLightedActivities);

		Map<String, Object> writeCountParams = new HashMap<String, Object>();
		writeCountParams.put("processDefinitionId", processDefinitionKey);

		writeCountParams.putAll(counts);
		BasicProcessDiagramGenerator basicGenerator = new BasicProcessDiagramGenerator(
				(RepositoryServiceImpl) repositoryService);
		HighlightNodeDiagramLayer highlightGenerator = new HighlightNodeDiagramLayer(
				(RepositoryServiceImpl) repositoryService);
		WriteNodeDescriptionDiagramLayer countGenerator = new WriteNodeDescriptionDiagramLayer(
				(RepositoryServiceImpl) repositoryService);
		MergeLayersGenerator mergeGenerator = new MergeLayersGenerator();

		Map<String, Object> mergeL = new HashMap<String, Object>();
		mergeL.put("1", basicGenerator.generateLayer("png", params));
		mergeL.put("2",
				highlightGenerator.generateLayer("png", highlightParams));
		mergeL.put("3", countGenerator.generateLayer("png", writeCountParams));

		mergeGenerator.generateLayer("png", mergeL);
		File generatedFile = new File(fileName);
		ImageIO.write(ImageIO.read(new ByteArrayInputStream(mergeGenerator
				.generateLayer("png", mergeL))), "png", generatedFile);
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

}