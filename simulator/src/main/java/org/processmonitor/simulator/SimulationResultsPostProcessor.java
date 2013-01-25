package org.processmonitor.simulator;

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
	static public List<SimulationResultEvent> getEventType( String type, List<SimulationResultEvent> resultList  ) {
		List<SimulationResultEvent> list = new ArrayList<SimulationResultEvent>();
		if (type != null) {
			for( SimulationResultEvent event : resultList ) {
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
	static public Collection<List<SimulationResultEvent>> groupProcessDefinitionKey( List<SimulationResultEvent> resultList ) {
		Map<String, List<SimulationResultEvent>> processMap = new HashMap<String, List<SimulationResultEvent>>();
		for ( SimulationResultEvent event : resultList) {
			if ( processMap.containsKey( event.getProcessDefinitionKey()) ) {
				// add event to the list
				processMap.get( event.getProcessDefinitionKey() ).add( event );
			} else {
				List<SimulationResultEvent> list = new ArrayList<SimulationResultEvent>();
				list.add(event);
 				processMap.put(event.getProcessDefinitionKey(), list);
			}
		}

		return new ArrayList<List<SimulationResultEvent>>(processMap.values());
	}
	
	/**
	 * returns DefinitionKeys for nodes to highlight
	 * @param resultList
	 * @return
	 */
	static public List<String> getTaskDefinitionKeys(List<SimulationResultEvent> resultList) {
		List<String> keys = new ArrayList<String>();
		for (SimulationResultEvent event : resultList) {
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
	static public Map<String, String> getNodeDescriptions(List<SimulationResultEvent> resultList) {
		Map<String, String> desc = new HashMap<String,String>();
		for (SimulationResultEvent event : resultList) {
			desc.put( event.getTaskDefinitionKey(), event.getDescription() );
		}
		return desc;
	}

}
