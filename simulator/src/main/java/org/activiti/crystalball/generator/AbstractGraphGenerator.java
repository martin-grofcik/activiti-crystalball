package org.activiti.crystalball.generator;

import org.activiti.crystalball.diagram.BasicProcessDiagramGenerator;
import org.activiti.crystalball.diagram.HighlightNodeDiagramLayer;
import org.activiti.crystalball.diagram.MergeLayersGenerator;
import org.activiti.crystalball.diagram.WriteNodeDescriptionDiagramLayer;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractGraphGenerator {

	protected RepositoryService repositoryService;
	
	/**
	 * limits for highlighting node - find interval into which count fits and highlight with color
	 */
	protected List<ColorInterval> highlightColorIntervalList;

	public AbstractGraphGenerator() {
		super();
	}
	protected void reportGraph(String fileName, String processDefinitionKey,
			Map<Color,List<String>> highLightedActivitiesMap, Map<String, String> counts)
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processDefinitionId", processDefinitionKey);

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
		mergeL.put("2", countGenerator.generateLayer("png", writeCountParams));
		int i = 3;
		for (Color c : highLightedActivitiesMap.keySet()) {
			highlightGenerator.setColor( c );

			Map<String, Object> highlightParams = new HashMap<String, Object>();
			highlightParams.put("processDefinitionId", processDefinitionKey);
			highlightParams.put("highLightedActivities", highLightedActivitiesMap.get( c ));

			mergeL.put( String.valueOf(i),
					highlightGenerator.generateLayer("png", highlightParams));
			i++;
		}

		File generatedFile = new File(fileName);
		ImageIO.write(ImageIO.read(mergeGenerator.generateLayer("png", mergeL)), "png", generatedFile);
	}
	
	protected static void addToHighlighted(	Map<Color, List<String>> highLightedActivitiesMap, Color color,	String id) {
		List<String> highLightedActivities = highLightedActivitiesMap.get( color );
		if (highLightedActivities == null) {
			highLightedActivities = new ArrayList<String>();
			highLightedActivitiesMap.put(color, highLightedActivities);			
		}
		highLightedActivities.add(id);		
	}
	


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public List<ColorInterval> getHighlightColorIntervalList() {
		return highlightColorIntervalList;
	}

	public void setHighlightColorIntervalList(
			List<ColorInterval> highlightColorIntervalList) {
		this.highlightColorIntervalList = highlightColorIntervalList;
	}
	
	protected Color getColorToHighlight(float number) {
		if (highlightColorIntervalList != null) {
			for ( ColorInterval colorInterval : highlightColorIntervalList) {
				if ( colorInterval.isInside( number ))
					return colorInterval.color;
			}
		}
		return Color.RED;
	}

}