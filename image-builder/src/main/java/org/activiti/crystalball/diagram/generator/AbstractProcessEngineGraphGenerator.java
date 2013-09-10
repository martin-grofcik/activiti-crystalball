package org.activiti.crystalball.diagram.generator;

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


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProcessEngineGraphGenerator extends AbstractGraphGenerator {

	
	protected static Logger log = LoggerFactory
			.getLogger(AbstractProcessEngineGraphGenerator.class);


	public AbstractProcessEngineGraphGenerator() {
		super();
		highlightColorIntervalList = new ArrayList<ColorInterval>();
		highlightColorIntervalList.add( new ColorInterval(Color.red));		
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
			String processDefinitionId, Date startDate, Date finishDate, Map<Color,List<String>> highLightedActivitiesMap,
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

		Map<Color, List<String>> highLightedActivitiesMap = new HashMap<Color,List<String>>();
		Map<String, String> counts = new HashMap<String, String>();

		ProcessDefinitionEntity pde = getProcessData(processDefinitionId, startDate, finishDate,
				highLightedActivitiesMap, counts);

		reportGraph(fileName, pde.getKey(), highLightedActivitiesMap, counts);
		log.debug(" generating report done");
	}



}