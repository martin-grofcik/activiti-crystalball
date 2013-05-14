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
	static public List<Result> getEventType( String type, List<Result> resultList  ) {
		List<Result> list = new ArrayList<Result>();
		if (type != null) {
			for( Result event : resultList ) {
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
	static public Collection<List<Result>> groupProcessDefinitionKey( List<Result> resultList ) {
		Map<String, List<Result>> processMap = new HashMap<String, List<Result>>();
		for ( Result event : resultList) {
			if ( processMap.containsKey( event.getProcessDefinitionKey()) ) {
				// add event to the list
				processMap.get( event.getProcessDefinitionKey() ).add( event );
			} else {
				List<Result> list = new ArrayList<Result>();
				list.add(event);
 				processMap.put(event.getProcessDefinitionKey(), list);
			}
		}

		return new ArrayList<List<Result>>(processMap.values());
	}
	
	/**
	 * returns DefinitionKeys for nodes to highlight
	 * @param resultList
	 * @return
	 */
	static public List<String> getTaskDefinitionKeys(List<Result> resultList) {
		List<String> keys = new ArrayList<String>();
		for (Result event : resultList) {
			if (!keys.contains( event.getTaskDefinitionKey())) {
				keys.add( event.getTaskDefinitionKey() );
			}
		}
		return keys;
	}

	/**
	 * return node key and its description
	 * @param resultList
	 * @return
	 */
	static public Map<String, String> getNodeDescriptions(List<Result> resultList) {
		Map<String, String> desc = new HashMap<String,String>();
		for (Result event : resultList) {
			desc.put( event.getTaskDefinitionKey(), event.getDescription() );
		}
		return desc;
	}

}
