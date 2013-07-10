package org.activiti.crystalball.simulator;

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


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.crystalball.simulator.impl.persistence.entity.ResultEntity;

/**
 * Post process simulation results
 * (prepare data for image generator) 
 *
 */
public class SimulationResultsPostProcessor {
	
	/**
	 * select sim events where type equals
	 * @param type
	 * @param resultList
	 * @return
	 */
	static public List<ResultEntity> getEventType( String type, List<ResultEntity> resultList  ) {
		List<ResultEntity> list = new ArrayList<ResultEntity>();
		if (type != null) {
			for( ResultEntity event : resultList ) {
				if (type.equals( event.getType()))  {
					list.add(event);
				}
			}
		}
		return list;
	}
	
	/**
	 * group sim results event based on process definitionKey
	 * 
	 * @param resultList
	 * @return
	 */
	static public Collection<List<ResultEntity>> groupProcessDefinitionKey( List<ResultEntity> resultList ) {
		Map<String, List<ResultEntity>> processMap = new HashMap<String, List<ResultEntity>>();
		for ( ResultEntity event : resultList) {
			if ( processMap.containsKey( (String) event.getVariable( "processDefinitionKey")) ) {
				// add event to the list
				processMap.get( (String) event.getVariable( "processDefinitionKey") ).add( event );
			} else {
				List<ResultEntity> list = new ArrayList<ResultEntity>();
				list.add(event);
 				processMap.put((String) event.getVariable( "processDefinitionKey"), list);
			}
		}

		return new ArrayList<List<ResultEntity>>(processMap.values());
	}
	
	/**
	 * returns DefinitionKeys for nodes to highlight
	 * @param resultList
	 * @return
	 */
	static public List<String> getTaskDefinitionKeys(List<ResultEntity> resultList) {
		List<String> keys = new ArrayList<String>();
		for (ResultEntity event : resultList) {
			if (!keys.contains( (String) event.getVariable( "taskDefinitionKey") )) {
				keys.add( (String) event.getVariable( "taskDefinitionKey") );
			}
		}
		return keys;
	}

	/**
	 * return node key and its description
	 * @param resultList
	 * @return
	 */
	static public Map<String, String> getNodeDescriptions(List<ResultEntity> resultList) {
		Map<String, String> desc = new HashMap<String,String>();
		for (ResultEntity event : resultList) {
			desc.put( (String) event.getVariable( "taskDefinitionKey"), (String) event.getVariable("description") );
		}
		return desc;
	}

}
